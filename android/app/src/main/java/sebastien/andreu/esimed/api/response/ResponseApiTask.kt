package sebastien.andreu.esimed.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sebastien.andreu.esimed.model.Task
import sebastien.andreu.esimed.utils.MESSAGE
import sebastien.andreu.esimed.utils.STATUS
import sebastien.andreu.esimed.utils.TASK

@JsonClass(generateAdapter = true)
data class ResponseApiTask (
    @field:Json(name = STATUS)
    val status: Int,

    @field:Json(name = TASK)
    val tasks: ArrayList<Task>
)