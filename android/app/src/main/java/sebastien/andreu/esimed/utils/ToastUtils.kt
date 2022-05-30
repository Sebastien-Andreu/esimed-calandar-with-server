package sebastien.andreu.esimed.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

object ToastUtils {
	fun error(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
		context?.let { Toasty.error(it, message?:"", duration, true).show()}
	}

	fun info(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
		context?.let { Toasty.info(context, message?:"", duration, true).show() }
	}

	fun success(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
		context?.let { Toasty.success(it, message?:"", duration, true).show()}
	}
}