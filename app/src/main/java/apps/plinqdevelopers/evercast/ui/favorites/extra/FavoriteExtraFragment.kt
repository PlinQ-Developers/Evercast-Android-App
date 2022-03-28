package apps.plinqdevelopers.evercast.ui.favorites.extra


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import apps.plinqdevelopers.evercast.R
import apps.plinqdevelopers.evercast.databinding.FragmentFavoriteExtraBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.snackbar.Snackbar
import java.util.*


class FavoriteExtraFragment : Fragment() {
    private var _binding: FragmentFavoriteExtraBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteExtraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extraArguments = navArgs<FavoriteExtraFragmentArgs>()
        val locationID: String = extraArguments.value.savedPlaceID
        loadLocationExtraDetails(placeID = locationID)
    }

    private fun loadLocationExtraDetails(placeID: String) {
        // Specify the fields to return.
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.ICON_URL,
            Place.Field.OPENING_HOURS,
            Place.Field.PHONE_NUMBER
        )

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.PLACES_API_KEY), Locale.US);
        } else {
            val request = FetchPlaceRequest.newInstance(placeID, placeFields)
            val placesClient: PlacesClient = Places.createClient(requireContext())
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place
                    showErrorMessage(message = "Place found: ${place.name}")
                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        showErrorMessage(message = "Place not found: ${exception.message}")
                        val statusCode = exception.statusCode
                    }
                }
        }
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