package sebastien.andreu.esimed.ui.calendar.week

import android.content.Context
import android.util.Log
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
import sebastien.andreu.esimed.api.Status
import sebastien.andreu.esimed.api.StatusApi
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.api.moduleApi.LOGIN
import sebastien.andreu.esimed.api.response.ResponseApi
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeekFragmentViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "WeekActivityViewModel"

    val apiResponse: MutableLiveData<Triple<Status, String?, Pair<ArrayList<Task>?, ArrayList<Task>>?>> = MutableLiveData()
    val apiResponseDelete: MutableLiveData<StatusApi<ResponseApi>> = MutableLiveData()
    val apiResponseUpdate: MutableLiveData<StatusApi<ResponseApi>> = MutableLiveData()

    fun getTask(context: Context, date: LocalDate?) = viewModelScope.launch {
        val date = CalendarUtils.selectedDate ?: LocalDate.now()

        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.getTask(getHeader(context)).let {
                if (it.isSuccessful) {
                    val list = arrayListOf<Task>()
                    (it.body()!! as ArrayList<Task>).forEach { task ->
                        if (Task.isLocalDateInTheSameWeek(task.date.toDate(), date)) {
                            list.add(task)
                        }
                    }
                    apiResponse.postValue(Triple(Status.SUCCESS,null, Pair(daysInWeekArray(it.body()!! as ArrayList<Task>), list)))
                } else {
                    Gson().fromJson(it.errorBody()!!.charStream(), ResponseApi::class.java)?.let { errorResponse ->
                        apiResponse.postValue(Triple(Status.ERROR, errorResponse.message, Pair(arrayListOf(), arrayListOf())))
                    }
                }
            }
        } catch (e: UnknownHostException) {
            apiResponse.postValue(Triple(Status.ERROR, context.getString(R.string.UnknownHostException, IP, PORT), null))
        } catch (e: SocketTimeoutException) {
            apiResponse.postValue(Triple(Status.ERROR, context.getString(R.string.SocketTimeoutException), null))
        } catch (e: ConnectException) {
            apiResponse.postValue(Triple(Status.ERROR, context.getString(R.string.UnknownHostException, IP, PORT), null))
        }
    }

    fun deleteTask(context: Context, task: Task) = viewModelScope.launch {
        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.deleteTask(getHeader(context, task)).let {
                if (it.body()?.status?.equals(HttpStatusCode.OK.value) == true) {
                    apiResponseDelete.postValue(StatusApi.success(it.body()!!.message, it.body()))
                } else {
                    Gson().fromJson(it.errorBody()!!.charStream(), ResponseApi::class.java)?.let { errorResponse ->
                        apiResponseDelete.postValue(StatusApi.error(errorResponse.message, null))
                    }
                }
            }
        } catch (e: UnknownHostException) {
            apiResponseDelete.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        } catch (e: SocketTimeoutException) {
            apiResponseDelete.postValue(StatusApi.error(context.getString(R.string.SocketTimeoutException), null))
        } catch (e: ConnectException) {
            apiResponseDelete.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        }
    }

    fun updateTask(context: Context, task: Task) = viewModelScope.launch {
        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.updateTask(getHeader(context, task), getBodyUpdate(task)).let {
                if (it.body()?.status?.equals(HttpStatusCode.OK.value) == true) {
                    apiResponseUpdate.postValue(StatusApi.success(it.body()!!.message, it.body()))
                } else {
                    Gson().fromJson(it.errorBody()!!.charStream(), ResponseApi::class.java)?.let { errorResponse ->
                        apiResponseUpdate.postValue(StatusApi.error(errorResponse.message, null))
                    }
                }
            }
        } catch (e: UnknownHostException) {
            apiResponseUpdate.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        } catch (e: SocketTimeoutException) {
            apiResponseUpdate.postValue(StatusApi.error(context.getString(R.string.SocketTimeoutException), null))
        } catch (e: ConnectException) {
            apiResponseUpdate.postValue(StatusApi.error(context.getString(R.string.UnknownHostException, IP, PORT), null))
        }
    }

    private fun daysInWeekArray(listOfTaskApi: ArrayList<Task>): ArrayList<Task> {
        val date = CalendarUtils.selectedDate ?: LocalDate.now()

        val daysInArray = ArrayList<Task>()
        var current = CalendarUtils.sundayForDate(date)
        val endDate = current!!.plusWeeks(1)
        while (current!!.isBefore(endDate)) {
            addInArray(daysInArray, listOfTaskApi, current)
            current = current.plusDays(1)
        }

        return daysInArray
    }

    private fun addInArray(daysInArray: ArrayList<Task>, listTaskApi: ArrayList<Task>, date: LocalDate) {
        if (listTaskApi.size == 0) {
            daysInArray.add(Task(date = CalendarUtils.formattedDate(date)))
        } else {
            run breaking@ {
                listTaskApi.forEachIndexed { index, task ->
                    if (task.date.toDate() == date) {
                        daysInArray.add(task)
                        return@breaking
                    } else {
                        if (index == listTaskApi.size-1) {
                            daysInArray.add(Task(date = CalendarUtils.formattedDate(date)))
                            return@breaking
                        }
                    }
                }
            }
        }

    }

    private fun getHeader(context: Context, task: Task? = null): Map<String, String> {
        val mutableMap = HashMap<String, String>()
        mutableMap[TOKEN] = context.getString(R.string.send_token, Token.value.toString())
        mutableMap[IDUSER] = JWT(Token.value.toString()).getClaim("id").asString().toString()
        mutableMap[ID] = task?.id.toString()
        return mutableMap
    }

    private fun getBodyUpdate(task: Task): MutableMap<String, Any?> {
        val mutableMap: MutableMap<String, Any?> = mutableMapOf()
        mutableMap[ID] = task.id
        mutableMap[NAME] = task.name
        mutableMap[DATE] = task.date
        mutableMap[TIME] = task.time
        mutableMap[PROGRESS] = task.progress
        mutableMap[IDUSER] = JWT(Token.value.toString()).getClaim("id").asString().toString()
        return mutableMap
    }
}