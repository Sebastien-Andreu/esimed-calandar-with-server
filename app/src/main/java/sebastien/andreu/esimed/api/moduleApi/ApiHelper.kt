package sebastien.andreu.esimed.api.moduleApi

import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response


interface ApiHelper {
//    suspend fun sendInventory(message: MutableMap<String, Any?>): Response<ResponseApi>
//    suspend fun sendCommand(message: MutableMap<String, Any?>): Response<ResponseApi>
//    suspend fun sendReception(message: MutableMap<String, Any?>): Response<ResponseApi>
//    suspend fun sendBlBill(message: MutableMap<String, Any?>): Response<ResponseApi>
//    suspend fun sendPrice(message: MutableMap<String, Any?>): Response<ResponseApi>
    suspend fun callServer(): Response<ResponseApi>
//    suspend fun requestApkVersion(): Response<ApkVersion>

}