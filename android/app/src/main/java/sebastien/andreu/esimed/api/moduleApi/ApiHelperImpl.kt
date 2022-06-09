package sebastien.andreu.esimed.api.moduleApi

import com.google.gson.Gson
import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import sebastien.andreu.esimed.api.response.ResponseApiTask
import sebastien.andreu.esimed.extension.sha1
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.SHA1
import javax.inject.Inject

class ApiHelperImpl
@Inject constructor( private val apiService: ApiService): ApiHelper {

    override suspend fun signup(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
        return apiService.signup(getHeader(message), message)
    }

    override suspend fun login(@Body message: MutableMap<String, Any?>): Response<ResponseApi> {
        return apiService.login(getHeader(message), message)
    }

    override suspend fun sendTask(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi> {
        return apiService.sendTask(headers, message)
    }

    override suspend fun updateTask(@HeaderMap headers: Map<String, String>, @Body message: MutableMap<String, Any?>): Response<ResponseApi> {
        return apiService.updateTask(headers, message)
    }

    override suspend fun getTask(@HeaderMap headers: Map<String, String>): Response<List<Task>> {
        return apiService.getTask(headers)
    }

    override suspend fun deleteTask(@HeaderMap headers: Map<String, String>): Response<ResponseApi> {
        return apiService.deleteTask(headers)
    }

    private fun getHeader(message: MutableMap<String, Any?>): Map<String, String> {
        val map = HashMap<String, String>()
        map[SHA1] = Gson().toJson(message).sha1
        return map
    }
}