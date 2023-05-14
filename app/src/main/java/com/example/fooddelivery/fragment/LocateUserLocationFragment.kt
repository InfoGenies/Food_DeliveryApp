package com.example.fooddelivery.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentLocateUserLocationBinding
import com.example.fooddelivery.utils.Constante.Companion.BASE_LATITUDE
import com.example.fooddelivery.utils.Constante.Companion.BASE_LONGITUDE
import com.example.fooddelivery.utils.Constante.Companion.LOCATION_ZOOM
import com.example.fooddelivery.utils.Constante.Companion.PERMISSION_ANNOTATION
import com.example.fooddelivery.utils.extention.*
import com.example.fooddelivery.utils.helpers.GoogleMapMarkerHelper
import com.example.fooddelivery.viewmodels.UserInfoViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LocateUserLocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener {
    private lateinit var binding: FragmentLocateUserLocationBinding
    private lateinit var mapView: MapView
    private var mLocationPermissionGranted: Boolean = false
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private var mGoogleMap: GoogleMap? = null
    private val googleMapHelper by lazy { GoogleMapMarkerHelper() }
    private var locationLatLng = LatLng(BASE_LATITUDE, BASE_LONGITUDE)
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }

    @Inject
    @Named(PERMISSION_ANNOTATION)
    lateinit var permissionDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_locate_user_location, container, false
        )
        binding.fragment = this
        initGoogleMaps(savedInstanceState)
        return binding.root
    }

    fun confirmLocation() {
        if (checkLocationPermissions())
            userInfoViewModel.setUserLocation(getCityNameFromLocation(locationLatLng))
        println("la location est ${getCityNameFromLocation(locationLatLng)}")
        backPressFragment()
    }

    fun backPressFragment() {
        closeFragment()
    }

    private fun initGoogleMaps(savedInstanceState: Bundle?) {
        mapView = binding.userLocationMap
        mapView.onCreate(savedInstanceState)
        resumeMapView()
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermissions() && !mLocationPermissionGranted) {
            mLocationPermissionGranted = true
            mapView.getMapAsync(this)
            permissionDialog.hide()
        }
        mapView.onResume()
    }

    private fun resumeMapView() {
        if (checkMapServices() && !checkLocationPermissions()) {
            requestLocationPermission()
        }
    }

    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            if (isMapsEnabled())
                return true
        }
        return false
    }



    private fun requestLocationPermission() {
        // Request location permission, so that we can get the location of the device.
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    // handle user permission interaction
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (!isGranted) {
                permissionDialog.show()
            } else {
                mLocationPermissionGranted = true
                if (permissionDialog.isShowing)
                    permissionDialog.hide()
            }
        }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onMapReady(map: GoogleMap) {
        if (checkLocationPermissions()) {
            // Move to user location in mapView.
            mGoogleMap = map
            map.setOnCameraIdleListener(this)
            map.setOnCameraMoveListener(this)
//            moveToDeviceLocation()
            binding.getDeviceLocation.showWithAnimate(R.anim.slide_down)
            return
        }
        requestLocationPermission()
    }


    @SuppressLint("MissingPermission")
    fun moveToDeviceLocation() {
        /*
          Get the best and most recent location of the device, which may be null in rare
          cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        locationLatLng = LatLng(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude
                        )
                        googleMapHelper.addNewLocationMarker(
                            bitmapDescriptorFromVector(R.drawable.ic_baseline_location_on_24),
                            mGoogleMap
                        )
                        mGoogleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                locationLatLng,
                                LOCATION_ZOOM
                            )
                        )
                    }
                } else {
                    mGoogleMap?.moveCameraToDefault()
                    mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: Exception) {
            Log.d("TAG>>>>>>>>>>", "getDeviceLocation: ${e.message}")
        }
    }


    override fun onCameraIdle() {
        locationLatLng = mGoogleMap?.cameraPosition?.target!!
        googleMapHelper.addNewLocationMarker(
            bitmapDescriptorFromVector(R.drawable.ic_baseline_location_on_24),
            mGoogleMap
        )
    }

    override fun onCameraMove() {
        googleMapHelper.addNewLocationMarker(
            bitmapDescriptorFromVector(R.drawable.ic_baseline_point),
            mGoogleMap
        )
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    fun closeFragment() {
        findNavController().popBackStack()
    }


}