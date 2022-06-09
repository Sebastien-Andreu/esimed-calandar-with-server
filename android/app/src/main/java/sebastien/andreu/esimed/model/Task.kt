package sebastien.andreu.esimed.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sebastien.andreu.esimed.extension.toDate
import sebastien.andreu.esimed.extension.toTime
import sebastien.andreu.esimed.utils.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.ArrayList

@JsonClass(generateAdapter = true)
data class Task(

    @field:Json(name = ID)
    val id: Long = 0,

    @field:Json(name = NAME)
    val name: String? = null,

    @field:Json(name = DATE)
    val date: String,

    @field:Json(name = TIME)
    val time: String? = null,

    @field:Json(name = PROGRESS)
    var progress: Int = 0
) {
    companion object {

        var taskList = ArrayList<Task>()

        fun eventsForDateInWeek(date: LocalDate?): ArrayList<Task> {
            val tasks = ArrayList<Task>()
            if (date != null) {
                for (task in taskList) {
                    if (isLocalDateInTheSameWeek(task.date.toDate(), date)) tasks.add(task)
                }
            }
            return tasks
        }

        fun eventsForDateAndTime(date: LocalDate?, time: LocalTime): ArrayList<Task> {
            val tasks = ArrayList<Task>()
            if (date != null) {
                for (task in taskList) {
                    val eventHour = task.time?.toTime()?.hour
                    val cellHour = time.hour
                    if (task.date.toDate() == date && eventHour == cellHour) {
                        tasks.add(task)
                    }
                }
            }
            return tasks
        }

        fun isLocalDateInTheSameWeek(date1: LocalDate, date2: LocalDate): Boolean {
            val sundayBeforeDate1 = date1.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            val saturdayAfterDate1 = date1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
            return ((date2.isEqual(sundayBeforeDate1) || date2.isAfter(sundayBeforeDate1))
                    && (date2.isEqual(saturdayAfterDate1) || date2.isBefore(saturdayAfterDate1)))
        }
    }

    override fun toString(): String {
        return name.toString()
    }
}
