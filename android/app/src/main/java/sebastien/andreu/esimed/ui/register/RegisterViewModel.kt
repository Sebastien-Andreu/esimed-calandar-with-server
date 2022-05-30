package sebastien.andreu.esimed.ui.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.launch
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.StatusApi
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.api.response.ResponseApi
import sebastien.andreu.esimed.extension.toMD5
import sebastien.andreu.esimed.utils.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "ConnectionActivityViewModel"

    val apiResponse: MutableLiveData<StatusApi<ResponseApi>> = MutableLiveData()

    fun createAccount(context: Context, pseudo: String, email: String, password: String) = viewModelScope.launch {
        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.signup(getBody(pseudo, email, password)).let {
                if (it.body()?.status?.equals(HttpStatusCode.OK.value) == true) {
                    apiResponse.postValue(StatusApi.success(it.message(), it.body()))
                } else {
                    apiResponse.postValue(StatusApi.error(it.body()?.message.toString(), it.body()))
                }
            }
        } catch (e: UnknownHostException) {
            apiResponse.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        } catch (e: SocketTimeoutException) {
            apiResponse.postValue(StatusApi.error(context.getString(R.string.SocketTimeoutException), null))
        } catch (e: ConnectException) {
            apiResponse.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        }
    }

    private fun getBody(pseudo: String, email: String, password: String): MutableMap<String, Any?> {
        val mutableMap: MutableMap<String, Any?> = mutableMapOf()
        mutableMap[PSEUDO] = pseudo
        mutableMap[EMAIL] = email
        mutableMap[PASSWORD] = password.toMD5()
        return mutableMap
    }
}