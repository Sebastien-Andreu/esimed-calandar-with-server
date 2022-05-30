package sebastien.andreu.esimed.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

object CalendarUtils {
    var selectedDate: LocalDate? = null

    fun formattedDate(date: LocalDate?): String {
        if (date != null) {
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }
        return ""
    }

    fun formattedTime(time: LocalTime?): String {
        if (time != null) {
            val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }
        return ""
    }

    fun formattedShortTime(time: LocalTime?): String {
        if (time != null) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return time.format(formatter)
        }
        return ""
    }

    fun monthYearFromDate(date: LocalDate?): String {
        if (date != null) {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }
        return ""
    }

    fun monthDayFromDate(date: LocalDate?): String {
        if (date != null) {
            val formatter = DateTimeFormatter.ofPattern("MMMM d")
            return date.format(formatter)
        }
        return ""
    }

    fun sundayForDate(current: LocalDate): LocalDate? {
        var myCurrent = current
        val oneWeekAgo = myCurrent.minusWeeks(1)
        while (myCurrent.isAfter(oneWeekAgo)) {
            if (myCurrent.dayOfWeek == DayOfWeek.SUNDAY) return myCurrent
            myCurrent = myCurrent.minusDays(1)
        }
        return null
    }
}