package sebastien.andreu.esimed.api.moduleApi

import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

//    @Headers(SEND_TYPE_JSON)
//    @POST(SEND_INVENTORY)
//    suspend fun sendInventory(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>

//    @Headers(SEND_TYPE_JSON)
//    @POST(SEND_COMMAND)
//    suspend fun sendCommand(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>
//
//    @Headers(SEND_TYPE_JSON)
//    @POST(SEND_RECEPTION)
//    suspend fun sendReception(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>
//
//    @Headers(SEND_TYPE_JSON)
//    @POST(SEND_BLBILL)
//    suspend fun sendBlBill(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>
//
//    @Headers(SEND_TYPE_JSON)
//    @POST(SEND_PRICE)
//    suspend fun sendPrice(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>
//
    @GET(SEND_CALL_SERVER)
    suspend fun callServer(): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @POST(SEND_CALL_SERVER)
    suspend fun signup(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>
}