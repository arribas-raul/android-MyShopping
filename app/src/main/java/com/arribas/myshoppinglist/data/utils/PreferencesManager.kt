package com.arribas.myshoppinglist.data.utils

import android.content.Context
import android.content.SharedPreferences


enum class PreferencesEnum {
    MAIN_LIST, MAIN_LIST_NAME
}

class PreferencesManager(context: Context){
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Config", Context.MODE_PRIVATE)

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun deleteData(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}