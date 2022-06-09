package sebastien.andreu.esimed.extension

import android.util.Log
import sebastien.andreu.esimed.BuildConfig
import sebastien.andreu.esimed.utils.DATE_FORMAT
import sebastien.andreu.esimed.utils.Time_FORMAT
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val TAG = "StringExtension"

fun String.toDebug(contextTag: String? = null) {
    try {
        if (BuildConfig.DEBUG) {
            if (null == contextTag) Log.w("DEBUG", this)
            else Log.w("DEBUG $contextTag", this)
        }
    } catch (exception: Exception) {
        exception.toTreatFor(TAG)
    }
}

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

fun String.toDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_FORMAT))
}

fun String.toTime(): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(Time_FORMAT))
}

val String.sha1: String
    get() {
        val bytes = MessageDigest.getInstance("SHA-1").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
