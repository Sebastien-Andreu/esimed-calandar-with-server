package sebastien.andreu.esimed.api.interceptor

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Auto-Change the host address interceptor
 */
@Singleton
class HostSelectionInterceptor : Interceptor {
    private var host: String? = null
    private var scheme: String? = null
    private var port: Int? = -1
    private var connectTimeout: Int = 30
    private var timeoutTimeUnit: TimeUnit = TimeUnit.SECONDS

    fun setInterceptor(url: String) {
        val httpUrl = url.toHttpUrlOrNull()
        scheme = httpUrl?.scheme
        host = httpUrl?.host
        port = httpUrl?.port
    }

    fun getInterceptor(): String {
        return "$host:$port"
    }

    fun setConnectTimeout(timeout: Int, timeoutTimeUnit: TimeUnit) {
        this.connectTimeout = timeout
        this.timeoutTimeUnit = timeoutTimeUnit
    }

    fun resetConnectTimeout() {
        connectTimeout = 30
        timeoutTimeUnit = TimeUnit.SECONDS
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()

        if (scheme != null && host != null && port != -1) {
            val newUrl = original.url.newBuilder()
                .scheme(scheme!!)
                .host(host!!)
                .port(port!!)
                .build()
            original = original.newBuilder()
                .url(newUrl)
                .build()
        }

        return chain.withConnectTimeout(connectTimeout, timeoutTimeUnit).proceed(original)
    }

    companion object {
        private var sInstance: HostSelectionInterceptor? = null

        fun get(): HostSelectionInterceptor {
            if (sInstance == null) {
                sInstance = HostSelectionInterceptor()
            }
            return sInstance!!
        }
    }
}
