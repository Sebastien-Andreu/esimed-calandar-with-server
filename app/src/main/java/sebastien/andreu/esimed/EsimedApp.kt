package sebastien.andreu.esimed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import sebastien.andreu.esimed.utils.IP
import sebastien.andreu.esimed.utils.PORT
import javax.inject.Inject

@HiltAndroidApp
class EsimedApp : Application() {

    @Inject lateinit var hostSelectionInterceptor: HostSelectionInterceptor

    override fun onCreate() {
        super.onCreate()

        hostSelectionInterceptor.setInterceptor(getString(R.string.addrIp, IP, PORT))
    }
}