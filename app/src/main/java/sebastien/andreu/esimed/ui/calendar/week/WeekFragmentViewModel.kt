package sebastien.andreu.esimed.ui.calendar.week

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.CalendarUtils
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WeekFragmentViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "WeekActivityViewModel"

    val listOfTask: MutableLiveData<Pair<ArrayList<Task>, ArrayList<Task>>> = MutableLiveData()

    fun getListOfTask() {
        val list = ArrayList<Task>()
        list.add(Task("1", LocalDate.now(), LocalTime.now()))
        list.add(Task("2", LocalDate.now(), LocalTime.now()))
        list.add(Task("3", LocalDate.now(), LocalTime.now()))
        list.add(Task("4", LocalDate.now(), LocalTime.now()))

        listOfTask.postValue(Pair(daysInWeekArray(list),list))
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
        run breaking@ {
            listTaskApi.forEachIndexed { index, task ->
                if (task.date == date) {
                    daysInArray.add(task)
                    return@breaking
                } else {
                    if (index == listTaskApi.size-1) {
                        daysInArray.add(Task(date = date))
                        return@breaking
                    }
                }
            }
        }
    }
}