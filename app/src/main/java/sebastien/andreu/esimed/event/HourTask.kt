package sebastien.andreu.esimed.event

import sebastien.andreu.esimed.model.Task
import java.time.LocalTime
import java.util.ArrayList

class HourTask(var time: LocalTime, tasks: ArrayList<Task>) {
    var listEvents: ArrayList<Task> = tasks

    fun getTasks(): ArrayList<Task> {
        return listEvents
    }
}