package sebastien.andreu.esimed.local

import android.content.Context
import android.content.SharedPreferences
import sebastien.andreu.esimed.utils.CALENDAR
import java.lang.Exception

class Storage(private val context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences(CALENDAR, Context.MODE_PRIVATE)

    fun getString(enum: StorageEnum): String? {
        return try {
            preferences.getString(enum.prefName, enum.defaultValue.toString())
        } catch (e: Exception) {
            null
        }
    }

    fun getInt(enum: StorageEnum): Int? {
        return try {
            preferences.getInt(enum.prefName, enum.defaultValue.toString().toInt())
        } catch (e: Exception) {
            null
        }
    }

    fun getBoolean(enum: StorageEnum): Boolean? {
        return try {
            preferences.getBoolean(enum.prefName, enum.defaultValue.toString().toBoolean())
        } catch (e: Exception) {
            null
        }
    }

    fun setString(enum: StorageEnum, value: String) {
        preferences.edit().putString(enum.prefName, value).apply()
    }

    fun setInt(enum: StorageEnum, value: Int) {
        preferences.edit().putInt(enum.prefName, value).apply()
    }

    fun setBoolean(enum: StorageEnum, value: Boolean) {
        preferences.edit().putBoolean(enum.prefName, value).apply()
    }
}