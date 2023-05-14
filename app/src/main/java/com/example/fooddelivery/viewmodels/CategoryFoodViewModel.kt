package com.example.fooddelivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.models.Category
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
class CategoryFoodViewModel @Inject constructor(private val repository: FoodRepository) :
    ViewModel() {

    private val _CategoryListLiveData =
        MutableLiveData<Resource<ArrayList<Category>>>(Resource.Loading())
    val CategoryListLiveData: LiveData<Resource<ArrayList<Category>>> get() = _CategoryListLiveData

    private val _foodStatus =
        MutableLiveData<Resource<ArrayList<Product>>>(Resource.Idle())
    val foodStatus: LiveData<Resource<ArrayList<Product>>> get() = _foodStatus


    fun getCategoryList(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _CategoryListLiveData.postValue(repository.getCategoryList(restaurantId))
        }
    }

    fun getFoodOfCategory(restaurantId: String, categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _foodStatus.postValue(Resource.Loading())
            val result = if (categoryId > 0) {
                repository.getFoodOfCategory(restaurantId, categoryId)
            } else { // load for you
                repository.getProductForYou(restaurantId)
            }
            _foodStatus.postValue(result)
        }
    }

    fun favoriteLiveData(id: String) = repository.getSpecificFavoriteRestaurantLiveData(id)


    fun saveOrRemoveRestaurantFromFavorite(restaurant: Restaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrRemoveRestaurantFromFavorite(restaurant)
        }

    }

}