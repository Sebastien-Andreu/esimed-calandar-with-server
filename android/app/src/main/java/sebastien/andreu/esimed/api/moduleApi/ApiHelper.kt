package sebastien.andreu.esimed.api.moduleApi

import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import retrofit2.http.HeaderMap
import sebastien.andreu.esimed.api.response.ResponseApiTask
import sebastien.andreu.esimed.model.Task


interface ApiHelper {
    suspend fun signup(message: MutableMap<String, Any?>): Response<ResponseApi>
    suspend fun login(message: MutableMap<String, Any?>): Response<ResponseApi>
    suspend fun sendTask(headers: Map<String, String>, message: MutableMap<String, Any?>): Response<ResponseApi>
    suspend fun getTask(headers: Map<String, String>): Response<List<Task>>
    suspend fun deleteTask(headers: Map<String, String>): Response<ResponseApi>
    suspend fun updateTask(headers: Map<String, String>, message: MutableMap<String, Any?>): Response<ResponseApi>

}