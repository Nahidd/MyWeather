package com.nahid.weather.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.weather.models.CurrentWeatherModel
import com.nahid.weather.models.ForecastWeatherModel
import com.nahid.weather.repos.WeatherRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LocationViewModel : ViewModel(){
    val repository = WeatherRepository()
    val locationLiveData: MutableLiveData<Location> = MutableLiveData()
    val currentModelLiveData: MutableLiveData<CurrentWeatherModel> = MutableLiveData()
    val forecastModelLiveData: MutableLiveData<ForecastWeatherModel> = MutableLiveData()


    fun setNewLocationLiveData(location: Location) {
        locationLiveData.value = location
    }

    fun fetchData(status: Boolean = false) {
       viewModelScope.launch {
           try {
               currentModelLiveData.value = repository.fetchCurrentWeatherData(locationLiveData.value!!, status = status)
               forecastModelLiveData.value = repository.fetchForecastWeatherData(locationLiveData.value!!, status = status)
           }catch (e: Exception){
               Log.d("LocationViewModel", e.localizedMessage)

           }
       }
    }
}







