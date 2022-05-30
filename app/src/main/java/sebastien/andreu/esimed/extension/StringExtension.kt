package sebastien.andreu.esimed.extension

import android.util.Log
import sebastien.andreu.esimed.BuildConfig

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
