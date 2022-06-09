package sebastien.andreu.esimed.local

import sebastien.andreu.esimed.utils.MONTH

enum class StorageEnum(val prefName: String, val defaultValue: Any){
    VIEW(sebastien.andreu.esimed.utils.VIEW, MONTH)
}