package sebastien.andreu.esimed.api

import sebastien.andreu.esimed.api.moduleApi.ApiHelper
import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import sebastien.andreu.esimed.api.response.ResponseApiTask
import sebastien.andreu.esimed.model.Task
import javax.inject.Inject

class ApiInjector
@Inject constructor(private val apiHelper: ApiHelper){
    suspend fun signup(message: MutableMap<String, Any?>): Response<ResponseApi> = apiHelper.signup(message)
    suspend fun login(message: MutableMap<String, Any?>): Response<ResponseApi> = apiHelper.login(message)
    suspend fun sendTask(header: Map<String, String>, message: MutableMap<String, Any?>): Response<ResponseApi> = apiHelper.sendTask(header, message)
    suspend fun getTask(header: Map<String, String>): Response<List<Task>> = apiHelper.getTask(header)
    suspend fun deleteTask(header: Map<String, String>): Response<ResponseApi> = apiHelper.deleteTask(header)
    suspend fun updateTask(header: Map<String, String>, message: MutableMap<String, Any?>): Response<ResponseApi> = apiHelper.updateTask(header, message)
}