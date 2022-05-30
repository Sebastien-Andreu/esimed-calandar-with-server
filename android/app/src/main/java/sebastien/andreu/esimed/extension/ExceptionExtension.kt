package sebastien.andreu.esimed.extension

import android.util.Log
import sebastien.andreu.esimed.BuildConfig.DEBUG

fun Exception.toTreatFor(containerTag: String) {
//     sometimes localized message is null :/
    this.localizedMessage?.toDebug()

    if (DEBUG) {
        Log.e("toTreatFor", "An exception ${this.message} occured with $containerTag: $this \n ${this.stackTrace}")
        this.printStackTrace()
    }
}

fun Throwable.toTreatFor(containerTag: String) {
    if (DEBUG) {
        this.printStackTrace()
    }
}
