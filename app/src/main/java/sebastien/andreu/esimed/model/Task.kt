package sebastien.andreu.esimed.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

data class Task(
    val name: String? = null,
    val date: LocalDate,
    val time: LocalTime? = null,
    var progress: Int = 0
) {
    companion object {

        var taskList = ArrayList<Task>()

        fun eventsForDate(date: LocalDate?): ArrayList<Task> {
            val tasks = ArrayList<Task>()
            if (date != null) {
                for (task in taskList) {
                    if (task.date == date) tasks.add(task)
                }
            }
            return tasks
        }

        fun eventsForDateAndTime(date: LocalDate?, time: LocalTime): ArrayList<Task> {
            val tasks = ArrayList<Task>()
            if (date != null) {
                for (task in taskList) {
                    val eventHour = task.time?.hour
                    val cellHour = time.hour
                    if (task.date == date && eventHour == cellHour) tasks.add(task)
                }
            }
            return tasks
        }
    }

    override fun toString(): String {
        return name.toString()
    }
}
