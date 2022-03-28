package apps.plinqdevelopers.evercast.ui.weather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.data.domain.DomainWeatherList
import apps.plinqdevelopers.evercast.databinding.ItemForecastBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlin.math.roundToInt

class WeatherForecastAdapter() :
    ListAdapter<DomainWeatherList, WeatherForecastAdapter.ForecastViewHolder>(ForecastComparator()) {

    inner class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        @SuppressLint("SetTextI18n")
        fun bind(forecast: DomainWeatherList) {
            binding.apply {
                when (forecast.weatherName) {
                    "Rain" -> {
                        Glide.with(forecastIcon)
                            .load(R.drawable.ic_rain)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_image_error)
                            .into(forecastIcon)
                    }
                    "Clouds" -> {
                        Glide.with(forecastIcon)
                            .load(R.drawable.ic_cloud)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_image_error)
                            .into(forecastIcon)
                    }
                    "Sun" -> {
                        Glide.with(forecastIcon)
                            .load(R.drawable.ic_sun)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_image_error)
                            .into(forecastIcon)
                    }
                }

                forecastDay.text = forecast.dt
                forecastValue.text = forecast.weatherValue.toString() + "\u00B0"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast: DomainWeatherList = getItem(position)
        holder.bind(forecast)
    }

    class ForecastComparator() : DiffUtil.ItemCallback<DomainWeatherList>() {
        override fun areItemsTheSame(
            oldItem: DomainWeatherList,
            newItem: DomainWeatherList
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: DomainWeatherList,
            newItem: DomainWeatherList
        ): Boolean {
            return oldItem == newItem
        }
    }
}