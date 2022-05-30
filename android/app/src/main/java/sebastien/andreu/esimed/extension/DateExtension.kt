package sebastien.andreu.esimed.extension

import sebastien.andreu.esimed.utils.DATE_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.dateToString(): String {
    return this.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
}