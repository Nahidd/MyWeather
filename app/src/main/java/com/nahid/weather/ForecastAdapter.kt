package com.nahid.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nahid.weather.databinding.ForecastItemRowBinding
import com.nahid.weather.models.ForecastWeatherModel

class ForecastAdapter : ListAdapter<ForecastWeatherModel.ForecastList, ForecastAdapter.ForecastViewHolder>(
    ForecastDiffUtil()
) {

    class ForecastViewHolder(private val binding: ForecastItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ForecastWeatherModel.ForecastList) {
            binding.forecast = item
        }
    }


    class ForecastDiffUtil: DiffUtil.ItemCallback<ForecastWeatherModel.ForecastList>() {
        override fun areItemsTheSame(
            oldItem: ForecastWeatherModel.ForecastList,
            newItem: ForecastWeatherModel.ForecastList
        ): Boolean {
             return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastWeatherModel.ForecastList,
            newItem: ForecastWeatherModel.ForecastList
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ForecastItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}