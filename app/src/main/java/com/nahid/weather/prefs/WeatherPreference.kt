package com.nahid.weather.prefs

import android.content.Context
import android.content.SharedPreferences

class WeatherPreference(context: Context) {
    private lateinit var preference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val tempStatus = "status"
    init {
        preference = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        editor = preference.edit()
    }
    fun setTempUnitStatus(status: Boolean){
        editor.putBoolean(tempStatus, status)
        editor.commit()
    }
    fun getTempUnitStatus() : Boolean{
        return preference.getBoolean(tempStatus, false)
    }
}