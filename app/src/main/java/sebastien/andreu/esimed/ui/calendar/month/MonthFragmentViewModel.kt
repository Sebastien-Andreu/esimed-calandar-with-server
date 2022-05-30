package sebastien.andreu.esimed.ui.calendar.month

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.CalendarUtils
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MonthFragmentViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "MonthActivityViewModel"

    val listOfTask: MutableLiveData<List<Task>> = MutableLiveData()



    fun getListOfTask() {
        val list = ArrayList<Task>()
        list.add(Task("1", LocalDate.now(), LocalTime.now()))
        list.add(Task("2", LocalDate.now(), LocalTime.now()))
        list.add(Task("3", LocalDate.now(), LocalTime.now()))
        list.add(Task("4", LocalDate.now(), LocalTime.now()))

        listOfTask.postValue(daysInMonthArray(list))
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
        run breaking@ {
            listTaskApi.forEachIndexed { index, task ->
                if (task.date == date) {
                    daysInMonthArray.add(task)
                    return@breaking
                } else {
                    if (index == listTaskApi.size-1) {
                        daysInMonthArray.add(Task(date = date))
                        return@breaking
                    }
                }
            }
        }
    }
}