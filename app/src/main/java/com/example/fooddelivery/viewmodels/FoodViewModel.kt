package com.example.fooddelivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.repository.FoodRepository
import com.example.fooddelivery.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val repository: FoodRepository) :
    ViewModel() {


    private val _ProductListLiveData =
        MutableLiveData<Resource<ArrayList<Product>>>(Resource.Loading())
    val ProductList: LiveData<Resource<ArrayList<Product>>> get() = _ProductListLiveData


    private lateinit var _favoriteFoodsLiveData: LiveData<List<Product>>
    val favoriteFoodsLiveData get() = _favoriteFoodsLiveData

    private lateinit var _FoodsLiveDataCarts: LiveData<List<Carte>>
    val FoodsLiveDataCarts get() = _FoodsLiveDataCarts

    private val _FillteListLiveData =
        MutableLiveData<Resource<ArrayList<Product>>>(Resource.Loading())
    val FillteListLiveData: LiveData<Resource<ArrayList<Product>>> get() = _FillteListLiveData


    fun getPopularFoodList() {
        // we have lifecycleScope can be used inside the activity or fragment and it related to the lifecycle of the Frg of Act so when the Frg or the Act is destroyed the coroutine is also destroyed
        // and viewModelScope can be only in viewModels and it related to the lifecycle of the viewModel  so when the vieModel  is destroyed the coroutine is also destroyed
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _ProductListLiveData.postValue(repository.getFoodList())
        }
    }

    fun getFillterFoodList(stars: Int, typefood: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _FillteListLiveData.postValue(repository.getFillterList(stars, typefood))
        }
    }

    fun saveFoodInFavorites(food: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrRemoveFoodFromFavorite(food)
        }
    }

    fun favoriteLiveData(id: String) = repository.getFoodFromFavoriteLiveData(id)

    fun getAllFavoriteFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteFoodsLiveData = repository.getAllFavoriteFoods()
        }
    }

    fun getAllFoodsCarts() {
        viewModelScope.launch(Dispatchers.IO) {
            _FoodsLiveDataCarts = repository.getAllFoodCarte()
        }
    }

    fun saveCarte(carteItem: Carte) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveCarte(carteItem)
        }
    }

    fun updateCarte(carteItem: Carte) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCarte(carteItem)
        }
    }


    fun deleteCarte(carteItem: Carte) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCarte(carteItem)
        }
    }


}