package apps.plinqdevelopers.evercast.ui.favorites.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.databinding.ItemFavoriteBinding
import kotlin.math.roundToInt

class FavoritesAdapter(private val listener:LocationClickListener) : ListAdapter<DomainWeather, FavoritesAdapter.FavoritesViewHolder>(FavoritesComparator()) {
    inner class FavoritesViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val clickedLocation : DomainWeather = getItem(adapterPosition)
                        listener.onLocationClicked(clickedLocation)
                    }
                }
            }
        }

        fun bind(weather: DomainWeather) {
            binding.apply {
                val name: String =
                    "${weather.locationName} - ${weather.weatherMain}"

                weatherCountryName.text = name
                weatherValue.text = weather.mainTemp.toString()
                weatherMinValue.text = weather.mainTempMin.toString()
                weatherCurrentValue.text = weather.mainTemp.toString()
                weatherMaxValue.text = weather.mainTempMax.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface LocationClickListener {
        fun onLocationClicked(weather: DomainWeather)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(weather = getItem(position))
    }

    class FavoritesComparator : DiffUtil.ItemCallback<DomainWeather>() {
        override fun areItemsTheSame(oldItem: DomainWeather, newItem: DomainWeather): Boolean {
            return oldItem.locationID == newItem.locationID
        }

        override fun areContentsTheSame(oldItem: DomainWeather, newItem: DomainWeather): Boolean {
            return oldItem == newItem
        }
    }
}