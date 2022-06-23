package com.nahid.weather

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nahid.weather.databinding.FragmentWeatherBinding
import com.nahid.weather.models.CurrentWeatherModel
import com.nahid.weather.network.detectUserLocation
import com.nahid.weather.network.getFormattedDate
import com.nahid.weather.network.icon_prefix
import com.nahid.weather.network.icon_suffix
import com.nahid.weather.prefs.WeatherPreference
import com.nahid.weather.viewmodel.LocationViewModel
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {

    private val TAG = "WeatherFragment"
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var preference: WeatherPreference
    private val locationViewModel: LocationViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu, menu)
        val searchView = menu.findItem(R.id.item_search).actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search any City"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    convertQueryToLatLng(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_location){
            detectUserLocation(requireContext()) {
                locationViewModel.setNewLocationLiveData(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun convertQueryToLatLng(query: String) {
        val geocoder = Geocoder(requireActivity())
        val addressList = geocoder.getFromLocationName(query, 1)
        if (addressList.isNotEmpty()){
            val lat = addressList[0].latitude
            val lng = addressList[0].longitude
            Log.e(TAG, "lat: $lat, lng: $lng")
            val location = Location("").apply {
                latitude = lat
                longitude = lng
            }
            locationViewModel.setNewLocationLiveData(location)
        }else{
            Toast.makeText(requireActivity(), "Invalid city name", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preference = WeatherPreference(requireContext())
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.tempSwitch.isChecked = preference.getTempUnitStatus()
        val adapter = ForecastAdapter()
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.forecastRV.layoutManager = linearLayoutManager
        binding.forecastRV.adapter = adapter
        locationViewModel.locationLiveData.observe(viewLifecycleOwner){
           locationViewModel.fetchData(status = preference.getTempUnitStatus())


            /*Toast.makeText(
               requireActivity(),
               "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT
           ).show()*/
       }
        locationViewModel.currentModelLiveData.observe(viewLifecycleOwner){
             setCurrentData(it)
        }
        locationViewModel.forecastModelLiveData.observe(viewLifecycleOwner){
            Log.d(TAG, "${it.list.size}" )
            adapter.submitList(it.list)
        }

        binding.tempSwitch.setOnCheckedChangeListener { compoundButton, status ->
            preference.setTempUnitStatus(status)
            locationViewModel.fetchData(status)
        }

        return binding.root
    }

    private fun setCurrentData(it: CurrentWeatherModel) {
        binding.dateTV.text = getFormattedDate(it.dt, "MMM dd, yyyy HH:mm")
        binding.addressTV.text = "${it.name}, ${it.sys.country}"
        val iconUrl = "$icon_prefix${it.weather[0].icon}${icon_suffix}"
        Glide.with(requireActivity()).load(iconUrl).into(binding.iconIV)
        binding.tempTV.text = it.main.temp.roundToInt().toString()
        binding.feelsLikeTV.text = "feels like: ${it.main.feelsLike.roundToInt()}"
        binding.conditionTV.text = it.weather[0].description
        binding.humidityTV.text = it.main.humidity.toString()
        binding.pressureTV.text = it.main.pressure.toString()

    }


}