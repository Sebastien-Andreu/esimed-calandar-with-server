package sebastien.andreu.esimed.extension

import android.util.Log
import sebastien.andreu.esimed.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

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

val String.sha1: String
    get() {
        val bytes = MessageDigest.getInstance("SHA-1").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
