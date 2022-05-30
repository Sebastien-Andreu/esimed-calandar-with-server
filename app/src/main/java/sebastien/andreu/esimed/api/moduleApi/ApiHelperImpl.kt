package sebastien.andreu.esimed.api.moduleApi

import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl
@Inject constructor( private val apiService: ApiService): ApiHelper {
//    override suspend fun sendInventory(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
//        return apiService.sendInventory(getHeader(message), message)
//    }

//    override suspend fun sendCommand(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
//        return apiService.sendCommand(getHeader(message), message)
//    }
//
//    override suspend fun sendReception(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
//        return apiService.sendReception(getHeader(message), message)
//    }
//
//    override suspend fun sendBlBill(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
//        return apiService.sendBlBill(getHeader(message), message)
//    }
//
//    override suspend fun sendPrice(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
//        return apiService.sendPrice(getHeader(message), message)
//    }
//
    override suspend fun callServer(): Response<ResponseApi> = apiService.callServer()
//
//    override suspend fun requestApkVersion(): Response<ApkVersion> = apiService.requestApkVersion()
//
//    private fun getHeader(message: MutableMap<String, Any?>): Map<String, String> {
//        val map = HashMap<String, String>()
//        map[SHA1] = Gson().toJson(message).sha1
//        return map
//    }
}