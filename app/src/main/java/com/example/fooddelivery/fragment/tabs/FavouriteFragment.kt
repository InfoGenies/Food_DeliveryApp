package com.example.fooddelivery.fragment.tabs

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.FavoriteAdapter
import com.example.fooddelivery.adapters.RestaurantAdapter
import com.example.fooddelivery.databinding.FragmentFavouriteBinding
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.Utils
import com.example.fooddelivery.utils.extention.*
import com.example.fooddelivery.viewmodels.RestaurantViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : Fragment(), FavoriteAdapter.MainFavoriteListener {

    private lateinit var binding: FragmentFavouriteBinding
    private val restaurantViewModel by activityViewModels<RestaurantViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurantViewModel.getFavRestaurantList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favourite, container, false
        )

        ShowShimmer()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated FavouriteFragment is runnin")

        observeListener()
    }

    private fun observeListener() {
            if (view != null)
                restaurantViewModel.FavRestaurantStatus.observe(viewLifecycleOwner, Observer {
                    if (it.isEmpty()) {
                        HideShimmer()
                        noDataFound()
                    } else {
                        initViews(it)
                        HideShimmer()
                    }
                })



    }

    private fun noDataFound() {
        binding.apply {
            recentFragmentRecycler.hide()
            emptyView.show()
        }
    }

    private fun initViews(data: List<Restaurant>?) {
        binding.recentFragmentRecycler.apply {

            hasFixedSize()
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            adapter = FavoriteAdapter(data!! as ArrayList<Restaurant>, this@FavouriteFragment)
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


    override fun onCallClicked(number: String) {
        TODO("Not yet implemented")
    }

    override fun onFavClicked(item: Restaurant, imgFav: ImageView) {
        GlobalScope.launch(Dispatchers.Main) {
            val isSavedBefore =
                restaurantViewModel.getRemoveRestaurantFromFavorite(item.restaurantId)
            println("restauarant exist before ${isSavedBefore}")
            if (isSavedBefore) {
                // delete
                imgFav.setImageResource(R.drawable.not_save)
                restaurantViewModel.saveProductInFavorites(item)
                showToast("Restaurant Existe Before , Try Again Please")

            } else {
                // ajouter
                imgFav.setImageResource(R.drawable.saved)
                restaurantViewModel.saveProductInFavorites(item)
            }

        }

    }



}