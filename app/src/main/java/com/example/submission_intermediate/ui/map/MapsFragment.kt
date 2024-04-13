package com.example.submission_intermediate.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.CustomTooltipMapBinding
import com.example.submission_intermediate.databinding.FragmentMapsBinding
import com.example.submission_intermediate.service.response.ListStoryItem
import com.example.submission_intermediate.service.response.Story
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.detail.DetailActivity
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.Helper
import com.example.submission_intermediate.uitls.UserPreferences
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private lateinit var binding : FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var token: String
    private val mapViewModel : MapViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setViewModel()
    }

    private fun setViewModel() {
        val preferences = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getToken().observe(viewLifecycleOwner){
            token = it
            mapViewModel.getStoryWithLocation(token)
        }

        mapViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true


        val indonesia = LatLng(-0.7893, 113.9213)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 5f))

        mapViewModel.getStoriesWithLocData().observe(viewLifecycleOwner) { stories ->
            for (story in stories.listStory) {
                if (story != null) {
                    mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                story.lat,
                                story.lon
                            )
                        )
                    )?.tag = story
                }
            }
        }

        mMap.setInfoWindowAdapter(this)
        mMap.setOnInfoWindowClickListener { marker ->
            val data: Story = marker.tag as Story
            routeToDetailStory(data)
        }

//        setMapStyle()
    }

//
//    private fun setMapStyle() {
//        TODO("Not yet implemented")
//    }

    private fun routeToDetailStory(data: Story) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("UID", data.id)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(intent)
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_LONG).show()
    }
    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun getInfoContents(marker: Marker): View? {
      return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val bindingTool = CustomTooltipMapBinding.inflate(LayoutInflater.from(requireContext()))
        val data: ListStoryItem = marker.tag as ListStoryItem

        bindingTool.labelLocation.text = Helper.parseAddressLocation(requireContext(), marker.position.latitude, marker.position.longitude)
        bindingTool.name.text = data.name
        bindingTool.image.setImageBitmap(Helper.bitmapFromURL(requireContext(),data.photoUrl))
        bindingTool.storyDescription.text = data.description
        bindingTool.storyUploadTime.text = Helper.formatCreatedAt(data.createdAt)

        return bindingTool.root
    }
}
