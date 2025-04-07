package com.vpn1.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

object PreferenceHelper {
    const val PREFS_NAME = "vpn_preferences"
    val gson = Gson()

    fun saveObject(context: Context, key: String, obj: Any) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(obj)
        prefs.edit { putString(key, json) }
    }

    inline fun <reified T> getObject(context: Context, key: String): T? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(key, null) ?: return null
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }
}