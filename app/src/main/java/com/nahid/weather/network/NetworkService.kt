package com.nahid.weather.network

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.nahid.weather.models.CurrentWeatherModel
import com.nahid.weather.models.ForecastWeatherModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.*

const val weather_api_key = "505fc7105787369974fcd5c2f77d092f"
const val base_url = "https://api.openweathermap.org/data/2.5/"
const val icon_prefix = "https://openweathermap.org/img/wn/"
const val icon_suffix = "@2x.png"

fun getFormattedDate(dt: Long, pattern: String) =
    SimpleDateFormat(pattern).format(Date(dt * 1000))

@SuppressLint("MissingPermission")
fun detectUserLocation(context: Context, callback: (Location) -> Unit){
    val provider = FusedLocationProviderClient(context)
    provider.lastLocation.addOnSuccessListener {
        Log.e("MainActivity", "detectUserLocation: $it")
        it?.let {
            callback(it)
        }
    }
}

val retrofit = Retrofit.Builder()
    .baseUrl(base_url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherServiceApi{
    @GET()
    suspend fun getCurrentWeatherData(@Url endUrl: String) : CurrentWeatherModel

    @GET()
    suspend fun getforecastWeatherData(@Url endUrl: String) : ForecastWeatherModel
}

object NetworkService {
    val weatherServiceApi: WeatherServiceApi by lazy {
        retrofit.create(WeatherServiceApi::class.java)

    }
}