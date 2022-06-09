package sebastien.andreu.esimed.ui.calendar.month

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
import sebastien.andreu.esimed.api.response.ResponseApi
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MonthFragmentViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "MonthActivityViewModel"

    val apiResponse: MutableLiveData<Triple<Status, String?, ArrayList<Task>?>> = MutableLiveData()

    fun getTask(context: Context) = viewModelScope.launch {
        try {
            hostSelectionInterceptor.setConnectTimeout(15, TimeUnit.SECONDS)
            apiInjector.getTask(getHeader(context)).let {
                if (it.isSuccessful) {
                    apiResponse.postValue(Triple(Status.SUCCESS,null, daysInMonthArray(it.body()!! as ArrayList<Task>)))
                } else {
                    Gson().fromJson(it.errorBody()!!.charStream(), ResponseApi::class.java)?.let { errorResponse ->
                        apiResponse.postValue(Triple(Status.ERROR, errorResponse.message, arrayListOf()))
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

    private fun daysInMonthArray(listTaskApi: ArrayList<Task>): ArrayList<Task> {
        val daysInMonthArray = ArrayList<Task>()
        val yearMonth = YearMonth.from(CalendarUtils.selectedDate)
        val daysInMonth = yearMonth.lengthOfMonth()
        val prevMonth = CalendarUtils.selectedDate!!.minusMonths(1)
        val nextMonth = CalendarUtils.selectedDate!!.plusMonths(1)
        val prevYearMonth = YearMonth.from(prevMonth)
        val prevDaysInMonth = prevYearMonth.lengthOfMonth()
        val firstOfMonth = CalendarUtils.selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            when {
                i <= dayOfWeek -> {
                    LocalDate.of(prevMonth.year, prevMonth.month, prevDaysInMonth + i - dayOfWeek).let { date ->
                        addInArray(daysInMonthArray, listTaskApi, date)
                    }
                }
                i > daysInMonth + dayOfWeek -> {
                    LocalDate.of(nextMonth.year, nextMonth.month, i - dayOfWeek - daysInMonth).let { date ->
                        addInArray(daysInMonthArray, listTaskApi, date)
                    }
                }
                else -> {
                    LocalDate.of(CalendarUtils.selectedDate!!.year, CalendarUtils.selectedDate!!.month, i - dayOfWeek).let { date ->
                        addInArray(daysInMonthArray, listTaskApi, date)
                    }
                }
            }
        }
        return daysInMonthArray
    }

    private fun addInArray(daysInMonthArray: ArrayList<Task>, listTaskApi: ArrayList<Task>, date: LocalDate) {
        if (listTaskApi.size == 0) {
            daysInMonthArray.add(Task(date = CalendarUtils.formattedDate(date)))
        } else {
            run breaking@ {
                listTaskApi.forEachIndexed { index, task ->
                    if (task.date.toDate() == date) {
                        daysInMonthArray.add(task)
                        return@breaking
                    } else {
                        if (index == listTaskApi.size-1) {
                            daysInMonthArray.add(Task(date = CalendarUtils.formattedDate(date)))
                            return@breaking
                        }
                    }
                }
            }
        }
    }

    private fun getHeader(context: Context): Map<String, String> {
        val mutableMap = HashMap<String, String>()
        mutableMap[TOKEN] = context.getString(R.string.send_token, Token.value.toString())
        mutableMap[IDUSER] = JWT(Token.value.toString()).getClaim("id").asString().toString()
        return mutableMap
    }
}