package com.example.fooddelivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.repository.FoodRepository
import com.example.fooddelivery.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(private val repository: FoodRepository) :
    ViewModel() {


    val callPhone = MutableLiveData<String>()

    private lateinit var _favoriteRestaurantsLiveData: LiveData<List<Restaurant>>
    val favoriteRestaurantsLiveData get() = _favoriteRestaurantsLiveData

    // getting Restaurant liste from firestore
    private val _restaurantStatus =
        MutableLiveData<Resource<ArrayList<Restaurant>>>(Resource.Idle())
    val restaurantStatus: LiveData<Resource<ArrayList<Restaurant>>> get() = _restaurantStatus

    // getting FavoriteRestaurant from Room DB
    private lateinit var _setFavRestaurantStatus: LiveData<List<Restaurant>>
    val FavRestaurantStatus get() = _setFavRestaurantStatus


    fun getRestaurantList() {
        _restaurantStatus.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _restaurantStatus.postValue(repository.getRestaurantList())
        }
    }

    fun getFavRestaurantList() {
        viewModelScope.launch(Dispatchers.IO) {
            _setFavRestaurantStatus = repository.getFavoriteResaurantLiveData()
        }
    }


    fun saveProductInFavorites(restaurant: Restaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrRemoveRestaurantFromFavorite(restaurant)
        }
    }

    suspend fun getRemoveRestaurantFromFavorite(id: String): Boolean {
        val productModel = repository.getRemoveRestaurantFromFavorite(id)
        println("the value of db is ${productModel} ")
        return productModel
    }

    fun getAllFavoriteRestaurant() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteRestaurantsLiveData = repository.getAllFavoriteRestaurant()
        }
    }

}