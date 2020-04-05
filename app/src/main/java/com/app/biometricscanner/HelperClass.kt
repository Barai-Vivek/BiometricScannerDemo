package com.app.biometricscanner

import android.content.Context

class HelperClass(private val context: Context) {

    lateinit var IS_APP_LOCK_ACTIVE: String
    lateinit var LOCK_PAUSE_TIME: String
    lateinit var MINUTES_TO_LOCK: String
    private val APP_MAIN_PREF = "app_pref"
    init {
        IS_APP_LOCK_ACTIVE = "IS_APP_LOCK_ACTIVE"
        LOCK_PAUSE_TIME = "LOCK_PAUSE_TIME"
        MINUTES_TO_LOCK = "MINUTES_TO_LOCK"
    }


    fun savePreferences(key: String?, value: String?) {
        val preferences = context.getSharedPreferences(
                APP_MAIN_PREF, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * Method To Load Preferences
     *
     * @param key The string value of the preference to load
     * @returns value The string value of the key passed
     */
    fun loadPreferences(key: String?): String {
        var strValue: String = ""
        val preferences = context.getSharedPreferences(APP_MAIN_PREF, Context.MODE_PRIVATE)
        strValue = preferences.getString(key, "").toString()
        return strValue
    }

    fun saveBoolPreferences(key: String?, value: Boolean) {
        val preferences = context.getSharedPreferences(
                APP_MAIN_PREF, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun clearPreferences() {
        val preferences = context.getSharedPreferences(
                APP_MAIN_PREF, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

    fun loadBoolPreferences(key: String?): Boolean {
        var value = false
        val preferences = context.getSharedPreferences(
                APP_MAIN_PREF, Context.MODE_PRIVATE)
        value = preferences.getBoolean(key, false)
        return value
    }
}