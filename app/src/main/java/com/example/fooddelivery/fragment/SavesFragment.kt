package com.example.fooddelivery.fragment

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.FoodAdapter
import com.example.fooddelivery.adapters.RestaurantAdapter
import com.example.fooddelivery.databinding.FragmentFilterBinding
import com.example.fooddelivery.databinding.FragmentSavesBinding
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.utils.Constante
import com.example.fooddelivery.utils.PermissionsUtility
import com.example.fooddelivery.utils.Utils
import com.example.fooddelivery.utils.extention.*
import com.example.fooddelivery.viewmodels.FoodViewModel
import com.example.fooddelivery.viewmodels.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class SavesFragment : Fragment(), FoodAdapter.MainFoodListener,
    RestaurantAdapter.MainRestaurantListener, EasyPermissions.PermissionCallbacks {
    lateinit var binding: FragmentSavesBinding
    private val restaurantViewModel by activityViewModels<RestaurantViewModel>()
    private val foodViewModel by activityViewModels<FoodViewModel>()
    lateinit var restaurantAdapter: RestaurantAdapter
    private val foodAdapter by lazy {
        FoodAdapter(requireContext(), this@SavesFragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel.getAllFavoriteFoods()
        restaurantViewModel.getAllFavoriteRestaurant()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saves, container, false
        )
        return binding.run {
            fragment = this@SavesFragment
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        initViews(true)

    }

    private fun observeListener() {
        foodViewModel.favoriteFoodsLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                println("la taille de la liste est ${it.size}")
                foodAdapter.addFilterListItems(it as ArrayList<Product>?)
            } else {
                noDataFound()
            }
        })
        restaurantViewModel.favoriteRestaurantsLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                restaurantAdapter =
                    RestaurantAdapter(requireContext(), it as ArrayList<Restaurant>, this)
            } else {
                noDataFound()
            }

        })
    }

    fun buttonsTopClick(foods: Boolean) {
        initViews(foods)

        if (foods) {
            binding.foodsButton.setTextColor(Color.WHITE)

            binding.foodsButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)

            binding.restaurantButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
            binding.restaurantButton.setTextColor(Color.BLACK)
        } else {
            binding.restaurantButton.setTextColor(Color.WHITE)
            binding.restaurantButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)

            binding.foodsButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorAccent)
            binding.foodsButton.setTextColor(Color.BLACK)
        }
        binding.emptyView.isVisible = false
    }

    fun backPressFragment() {
        closeFragment()
    }

    private fun initViews(food: Boolean) {
        binding.favRecyclerview.apply {
            hasFixedSize()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = true
            adapter = if (food) foodAdapter else restaurantAdapter
        }
    }


    private fun noDataFound() {
        binding.apply {
            favRecyclerview.hide()
            emptyView.show()
        }
    }

    private fun navigateToDetailFoodFragment(food: Product) {
        val action = SavesFragmentDirections.actionNavigationSavesToFoodDetailsFragment(food)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onClickedFoodDetaille(fooditem: Product) {
        navigateToDetailFoodFragment(fooditem)
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
        TODO("Not yet implemented")
    }

    override fun onItemClicked(item: Restaurant) {
        val bundle = bundleOf(Constante.CURRENT_RESTAURANT to item)
        findNavController().navigate(R.id.restaurantDetailsFragment, bundle)

    }


}