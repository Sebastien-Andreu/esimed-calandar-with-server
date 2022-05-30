package sebastien.andreu.esimed.model.enum

enum class TaskEnum (val value: Int, val str: String) {
    NOT_START(0, sebastien.andreu.esimed.utils.NOT_START),
    IN_PROGRESS(1, sebastien.andreu.esimed.utils.IN_PROGRESS),
    FINISH(2, sebastien.andreu.esimed.utils.FINISH);

    companion object {
        fun getEnumByValue(value: Int): TaskEnum {
            values().forEach { enum ->
                if (enum.value == value) {
                    return enum
                }
            }

            return NOT_START
        }
    }
}