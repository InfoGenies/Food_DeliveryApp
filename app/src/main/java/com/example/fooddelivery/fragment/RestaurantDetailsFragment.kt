package com.example.fooddelivery.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.FoodAdapter
import com.example.fooddelivery.adapters.PopularAdapter
import com.example.fooddelivery.databinding.FragmentRestaurantDetailsBinding
import com.example.fooddelivery.models.Category
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.utils.Constante
import com.example.fooddelivery.utils.Constante.Companion.CURRENT_RESTAURANT
import com.example.fooddelivery.utils.PermissionsUtility
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.Utils
import com.example.fooddelivery.utils.extention.*
import com.example.fooddelivery.viewmodels.CategoryFoodViewModel
import com.example.fooddelivery.viewmodels.RestaurantViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RestaurantDetailsFragment : Fragment(), FoodAdapter.MainFoodListener,
    EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentRestaurantDetailsBinding
    private lateinit var currentRestaurant: Restaurant
    private val restaurantViewModel by activityViewModels<RestaurantViewModel>()
    private val categoryProductViewModel: CategoryFoodViewModel by viewModels()
    lateinit var animationView: LottieAnimationView
    private val foodAdapter by lazy {
        FoodAdapter(
            requireContext(),
            this@RestaurantDetailsFragment
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_restaurant_details, container, false
        )
        return binding.run {

            fragment = this@RestaurantDetailsFragment
            this@RestaurantDetailsFragment.animationView = binding.animationView
            initViews()
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentRestaurant = requireArguments().getParcelable(CURRENT_RESTAURANT)!!
        currentRestaurant?.let { restaurant ->
            bindRestaurantData(restaurant)
        }

        observeListener()

    }

    private fun initViews() {
        binding.productRecyclerView.apply {
            hasFixedSize()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = true
            adapter = foodAdapter
        }
    }

    private fun observeListener() {
        categoryProductViewModel.CategoryListLiveData.observe(
            viewLifecycleOwner,
            Observer { categoryStatus ->
                when (categoryStatus) {
                    is Resource.Success -> {
                        if (!categoryStatus.data!!.isEmpty()) {
                            binding.tabs.visibility = View.VISIBLE
                            ShowAnimation()
                            setupActionTabsCategory(categoryStatus.data!!)
                        } else {
                            noDataFound()
                        }

                    }
                    is Resource.Error -> {
                        showToast(categoryStatus.msg.toString())
                    }

                }
            })
        categoryProductViewModel.foodStatus.observe(viewLifecycleOwner, Observer { foodlist ->

            when (foodlist) {
                is Resource.Success -> {
                    if (!foodlist.data!!.isEmpty()) {
                        HideShimmer()

                        foodAdapter.addFilterListItems(foodlist.data)
                    } else {
                        HideShimmer()
                        noDataFound()
                    }
                }
                is Resource.Error -> {
                    showToast(foodlist.msg.toString())
                }
                is Resource.Loading -> {
                    HideAnimation()
                    ShowShimmer()
                }
            }
        })

        categoryProductViewModel.favoriteLiveData(currentRestaurant.restaurantId)
            .observe(viewLifecycleOwner, Observer { food ->
                if (food != null) {
                    binding.imageSave.setImageResource(R.drawable.saved)
                } else {
                    binding.imageSave.setImageResource(R.drawable.not_save)
                }
            })


    }

    fun saveRestaurantInFavorite() {
        categoryProductViewModel.saveOrRemoveRestaurantFromFavorite(currentRestaurant)
    }

    private fun HideAnimation() {
        animationView.visibility = View.GONE
        animationView.pauseAnimation()
    }

    private fun ShowAnimation() {
        animationView.visibility = View.VISIBLE
        animationView.playAnimation()

    }

    private fun noDataFound() {
        binding.apply {
            productRecyclerView.hide()
            emptyView.show()
        }
    }


    private fun setupActionTabsCategory(categories: List<Category>) {
        binding.tabs.removeAllTabs()
        binding.tabs.addTab(binding.tabs.newTab().setText("For You").setId(0))
        for (i in categories.indices) {
            binding.tabs.addTab(
                binding.tabs.newTab().setText(categories[i].categoryName)
                    .setId(categories[i].categoryId!!)
            )
        }
        categoryProductViewModel.getFoodOfCategory(currentRestaurant.restaurantId, 0)
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val categoryId = tab.id
                    categoryProductViewModel.getFoodOfCategory(
                        currentRestaurant.restaurantId,
                        categoryId
                    )
                    //    productAdapter.products = arrayListOf()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }


    private fun bindRestaurantData(restaurant: Restaurant) {
        binding.apply {
            restaurantItem = restaurant
        }

    }

    private fun ShowShimmer() {
        if (binding.emptyView.isVisible) {
            binding.emptyView.visibility = View.GONE
        }
        binding.shimmer.apply {
            visibility = View.VISIBLE
            startShimmer()
        }

        binding.productRecyclerView.visibility = View.GONE
    }

    private fun HideShimmer() {
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.productRecyclerView.visibility = View.VISIBLE

    }

    fun onCallClicked(number: String) {
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

    override fun onResume() {
        super.onResume()
        categoryProductViewModel.getCategoryList(currentRestaurant.restaurantId)
    }

    override fun onClickedFoodDetaille(fooditem: Product) {
        startToDetqilFoodFragment(fooditem)
    }
    fun startToDetqilFoodFragment(food :Product) {
        val action = RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToFoodDetailsFragment(food)
        findNavController().navigate(action)
    }
    fun backPressFragment() {
        closeFragment()
    }


}