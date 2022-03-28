package apps.plinqdevelopers.evercast.ui.favorites.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import apps.plinqdevelopers.evercast.data.domain.DomainWeather
import apps.plinqdevelopers.evercast.databinding.FragmentFavoriteListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteListFragment : Fragment(), FavoritesAdapter.LocationClickListener {
    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val favoritesAdapter: FavoritesAdapter = FavoritesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSavedData()
        binding.apply {
            binding.favoriteList.apply {
                setHasFixedSize(true)
                adapter = favoritesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun loadSavedData() {
        binding.apply {
            val savedData = favoritesViewModel.savedWeather()
            savedData.observe(requireActivity(), Observer { weatherList ->
                favoritesAdapter.submitList(weatherList)
            })
        }
    }

    override fun onLocationClicked(weather: DomainWeather) {
        val locationID : String = weather.locationID.toString()
        val gotoExtraDetails =
            FavoriteListFragmentDirections.actionFavoriteListFragmentToFavoriteExtraFragment(savedPlaceID = locationID)
        findNavController().navigate(gotoExtraDetails)
    }

    private fun showErrorMessage(message: String) {
        val snackBar: Snackbar = Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_INDEFINITE
        )

        snackBar.setAction("OK") {
            snackBar.dismiss()
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}