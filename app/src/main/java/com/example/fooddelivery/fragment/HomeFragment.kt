package com.example.fooddelivery.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fooddelivery.views.MainActivity
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.PopularAdapter
import com.example.fooddelivery.adapters.ViewPagerFragmentTabsAdapter
import com.example.fooddelivery.databinding.FragmentHomeBinding
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.UserInfoModel
import com.example.fooddelivery.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.showBottomNav
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.FoodViewModel
import com.example.fooddelivery.viewmodels.UserInfoViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), PopularAdapter.MainProductListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // le signification de cette inst popularAdapter = PopularAdapter(requireContext(), this@HomeFragment)
    private val popularAdapter by lazy { PopularAdapter(requireContext(), this@HomeFragment) }


    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val foodViewModel: FoodViewModel by viewModels()

    // field injection
    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return binding.run {
            binding.fragment = this@HomeFragment
            binding.breakfast = "breakfast"
            binding.burgers = "burger"
            binding.pizzaa = "pizza"
            binding.coffee = "coffee"
            binding.drink = "drink"
            binding.root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomNav()
        observeListener()
    }

    private fun observeListener() {
        // check if user have data into firebase firestore
        userInfoViewModel.userInformationLiveData.observe(
            viewLifecycleOwner, Observer { userInfo ->
                when (userInfo) {
                    is Resource.Success -> {
                        showInfo(userInfo.data)
                        initViews()
                        foodViewModel.getPopularFoodList()
                        setTabs()
                    }
                    is Resource.Error -> {
                        loadingDialog.hide()
                        navigateToCreateUserInfoFragment()
                    }
                    is Resource.Loading -> {
                        println("Data is checking ")
                        loadingDialog.show()
                    }
                }

            })

        foodViewModel.ProductList.observe(viewLifecycleOwner, Observer { productliste ->
            when (productliste) {
                is Resource.Success -> {
                    popularAdapter.addMainPopularListItems(productliste.data)
                    hideShimmer()
                }
                is Resource.Error -> {
                    showToast(productliste.msg.toString())
                }
                is Resource.Loading -> {
                    showShimmer()

                }
            }
        })

    }

    private fun showInfo(data: UserInfoModel?) {
        println("the name is ${data!!.userName} from HomeFragment ")
    }

    private fun showShimmer() {
        binding.shimmer.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }

    private fun initViews() {
        binding.homeFragmentRecyclerPopular.apply {
            hasFixedSize()
            isNestedScrollingEnabled = true
            adapter = popularAdapter
        }

    }


    private fun navigateToCreateUserInfoFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToCreateUserInfoFragment()
        findNavController().navigate(action)
    }

    private fun navigateTofoodDetailsFragment(food: Product) {
        val action = HomeFragmentDirections.actionHomeFragmentToFoodDetailsFragment(food)
        findNavController().navigate(action)
    }

    private fun setTabs() {

        binding.tabs.addTab(binding.tabs.newTab().setText("Recent"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Favourite"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Rating"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Popular"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Notification"))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager2.currentItem = tab!!.position
                println(" la position de talbe est ${tab!!.position}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        setupViewPager(binding.viewPager2)

    }


    private fun setupViewPager(viewPager2: ViewPager2) {

        val adapter = ViewPagerFragmentTabsAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position))
            }

            override fun onPageScrollStateChanged(state: Int) {
                /*empty*/
            }
        })
    }

    fun startToFilterFragment(filterName: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToFilterFragment(filterName)
        findNavController().navigate(action)
    }

    override fun onProductClicked(productModel: Product) {
        navigateTofoodDetailsFragment(productModel)
    }

    override fun onResume() {
        super.onResume()
        userInfoViewModel.getUserInformation()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        println(" Home is onDestroyView is running")
        _binding = null
    }

    override fun onDestroy() {
        println(" Home is onDestroy is running")

        super.onDestroy()
    }
}