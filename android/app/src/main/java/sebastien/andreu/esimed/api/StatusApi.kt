package sebastien.andreu.esimed.api

data class StatusApi<T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object{

        fun <T> success(msg: String, data: T?): StatusApi<T> {
            return StatusApi(Status.SUCCESS,data, msg)
        }

        fun <T> error(msg: String, data: T?): StatusApi<T> {
            return StatusApi(Status.ERROR,data, msg)
        }
    }
}
