package sebastien.andreu.esimed.ui.calendar.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.event.HourTask
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.CalendarUtils
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class DailyFragmentViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "DailyActivityViewModel"

    val listOfTask: MutableLiveData<List<HourTask>> = MutableLiveData()

    fun getListOfTask() {
        val list = ArrayList<Task>()
        list.add(Task("1", LocalDate.now(), LocalTime.now()))
        list.add(Task("2", LocalDate.now(), LocalTime.now(), progress = 2))
        list.add(Task("3", LocalDate.now(), LocalTime.now(), progress = 1))

        list.add(Task("4", LocalDate.now(), LocalTime.now().minusHours(2)))
        list.add(Task("5", LocalDate.now(), LocalTime.now().minusHours(1)))

        listOfTask.postValue(hourEventList(list))
    }

    private fun hourEventList(listOfTaskApi: ArrayList<Task>): ArrayList<HourTask> {
        val date = CalendarUtils.selectedDate ?: LocalDate.now()
        val list = ArrayList<HourTask>()
        Task.taskList = listOfTaskApi
        for (hour in 0..23) {
            val time = LocalTime.of(hour, 0)
            val task: ArrayList<Task> = Task.eventsForDateAndTime(date, time)
            val hourTask = HourTask(time, task)
            list.add(hourTask)
        }
        return list
    }

}