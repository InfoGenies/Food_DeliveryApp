package com.example.fooddelivery.fragment.tabs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.RestaurantAdapter
import com.example.fooddelivery.databinding.FragmentRecentBinding
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.utils.Constante
import com.example.fooddelivery.utils.Constante.Companion.CURRENT_RESTAURANT
import com.example.fooddelivery.utils.PermissionsUtility
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.Utils
import com.example.fooddelivery.utils.Utils.calculationByDistance
import com.example.fooddelivery.utils.extention.*
import com.example.fooddelivery.viewmodels.RestaurantViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RecentFragment : Fragment(), RestaurantAdapter.MainRestaurantListener,
    EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentRecentBinding? = null
    private val binding get() = _binding!!
    private val restaurantViewModel :RestaurantViewModel by viewModels()
    val navController by lazy {
        findNavController()
    }
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var location: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recent, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }


    private fun observeListener() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (view != null)
                restaurantViewModel.restaurantStatus.observe(
                    viewLifecycleOwner,
                    Observer { RestaurantStatus ->
                        when (RestaurantStatus) {
                            is Resource.Success -> {
                                if (!RestaurantStatus.data!!.isEmpty()) {
                                    HideShimmer()
                                    initViews(RestaurantStatus.data)
                                } else {
                                    HideShimmer()
                                    noDataFound()
                                }
                            }
                            is Resource.Error -> {
                                showToast(RestaurantStatus.msg.toString())
                            }
                            is Resource.Loading -> {
                                ShowShimmer()
                            }
                        }


                    })

        }
    }

    private fun noDataFound() {
        binding.apply {
            recentFragmentRecycler.hide()
            emptyView.show()
        }
    }

    private fun initViews(data: ArrayList<Restaurant>?) {
        binding.recentFragmentRecycler.apply {

            hasFixedSize()
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            for (position in 0..data!!.size - 1) {
                data[position].distance = calculationByDistance(
                    LatLng(location!!.latitude, location!!.longitude),
                    LatLng(data[position].latitude, data[position].longitude)
                ).toString() + " KM"
            }
            adapter = RestaurantAdapter(requireContext(), data!!, this@RecentFragment)


        }
    }

    private fun ShowShimmer() {
        binding.shimmer.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun HideShimmer() {
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        restaurantViewModel.getRestaurantList()

    }


    override fun onCallClicked(number: String) {
        restaurantViewModel.callPhone.postValue(number)
        requestPermissionsCall()
    }

    private fun requestPermissionsCall() {

        if (PermissionsUtility.hasCallPhonePermissions(requireContext())) {
            callPhone()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "you need to accept Call Phone permissions to use this Option.",
                Constante.REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.CALL_PHONE
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "you need to accept Call Phone permissions to use this Option.",
                Constante.REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.CALL_PHONE
            )
        }
    }

    private fun callPhone() {
        try {
            Utils.startCallIntent(requireContext(), restaurantViewModel.callPhone.value.toString())
        } catch (e: Exception) {
            showToast("Phone Not Found")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        callPhone()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onFavClicked(item: Restaurant, imgFav: ImageView) {
        GlobalScope.launch(Dispatchers.Main) {
            val isSavedBefore =
                restaurantViewModel.getRemoveRestaurantFromFavorite(item.restaurantId)
            if (isSavedBefore) {
                // delete
                imgFav.setImageResource(R.drawable.not_save)
                restaurantViewModel.saveProductInFavorites(item)
                showToast("Restaurant Existe Before , Try Again Please")

            } else {
                // ajouter
                item.inFav = true
                imgFav.setImageResource(R.drawable.saved)
                restaurantViewModel.saveProductInFavorites(item)
            }

        }

    }

    override fun onItemClicked(item: Restaurant) {
        val bundle = bundleOf(CURRENT_RESTAURANT to item)
        navController.navigate(R.id.restaurantDetailsFragment, bundle)
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isMapsEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity(), { task ->
                    location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                    }
                })
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation!!

        }
    }


}