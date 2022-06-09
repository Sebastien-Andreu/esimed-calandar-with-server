package sebastien.andreu.esimed.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sebastien.andreu.esimed.utils.MESSAGE
import sebastien.andreu.esimed.utils.STATUS

@JsonClass(generateAdapter = true)
data class ResponseApi (
    @field:Json(name = STATUS)
    val status: Int,

    @field:Json(name = MESSAGE)
    val message: String
)