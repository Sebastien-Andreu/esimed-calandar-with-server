package sebastien.andreu.esimed.api.moduleApi

import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import retrofit2.http.*
import sebastien.andreu.esimed.api.response.ResponseApiTask
import sebastien.andreu.esimed.model.Task

interface ApiService {

    @Headers(SEND_TYPE_JSON)
    @POST(SIGNUP)
    suspend fun signup(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @POST(LOGIN)
    suspend fun login(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @POST(SEND_TASK)
    suspend fun sendTask(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @POST(UPDATE_TASK)
    suspend fun updateTask(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @DELETE(DELETE_TASK)
    suspend fun deleteTask(@HeaderMap headers: Map<String, String>): Response<ResponseApi>

    @Headers(SEND_TYPE_JSON)
    @GET(GET_TASK)
    suspend fun getTask(@HeaderMap headers: Map<String, String>): Response<List<Task>>
}