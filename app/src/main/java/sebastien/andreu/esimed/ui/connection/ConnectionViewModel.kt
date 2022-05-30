package sebastien.andreu.esimed.ui.connection

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sebastien.andreu.esimed.api.ApiInjector
import sebastien.andreu.esimed.api.interceptor.HostSelectionInterceptor
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel
@Inject constructor(
    private val apiInjector: ApiInjector,
    private val hostSelectionInterceptor: HostSelectionInterceptor
) : ViewModel() {
    private val TAG: String = "ConnectionActivityViewModel"
}