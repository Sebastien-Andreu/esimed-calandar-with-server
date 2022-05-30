package sebastien.andreu.esimed.api

import sebastien.andreu.esimed.api.moduleApi.ApiHelper
import sebastien.andreu.esimed.api.response.ResponseApi
import retrofit2.Response
import javax.inject.Inject

class ApiInjector
@Inject constructor(private val apiHelper: ApiHelper){
    suspend fun callServer(): Response<ResponseApi> = apiHelper.callServer()
    suspend fun signup(message: MutableMap<String, Any?>): Response<ResponseApi> = apiHelper.signup(message)
}