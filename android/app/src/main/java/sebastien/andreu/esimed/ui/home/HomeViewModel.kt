package sebastien.andreu.esimed.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.launch
import sebastien.andreu.esimed.R
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.StatusApi
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.api.response.ResponseApi
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.extension.toTime
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "HomeViewModel"

    val apiResponse: MutableLiveData<StatusApi<ResponseApi>> = MutableLiveData()

    fun addTask(context: Context, task: Task) = viewModelScope.launch {
        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.sendTask(getHeader(context), getBody(task)).let {
                if (it.body()?.status?.equals(HttpStatusCode.OK.value) == true) {
                    apiResponse.postValue(StatusApi.success(it.body()!!.message, it.body()))
                } else {
                    Gson().fromJson(it.errorBody()!!.charStream(), ResponseApi::class.java)?.let { errorResponse ->
                        apiResponse.postValue(StatusApi.error(errorResponse.message, null))
                    }
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

    private fun getBody(task: Task): MutableMap<String, Any?> {
        val mutableMap: MutableMap<String, Any?> = mutableMapOf()
        mutableMap[NAME] = task.name
        mutableMap[DATE] = task.date
        mutableMap[TIME] = task.time
        mutableMap[PROGRESS] = task.progress
        mutableMap[IDUSER] = JWT(Token.value.toString()).getClaim("id").asString().toString()
        return mutableMap
    }

    private fun getHeader(context: Context): Map<String, String> {
        val mutableMap = HashMap<String, String>()
        mutableMap[TOKEN] = context.getString(R.string.send_token, Token.value.toString())
        return mutableMap
    }
}