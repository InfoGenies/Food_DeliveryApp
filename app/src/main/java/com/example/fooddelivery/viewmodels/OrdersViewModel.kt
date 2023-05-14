package com.example.fooddelivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.models.OrderModel
import com.example.fooddelivery.repository.FoodRepository
import com.example.fooddelivery.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _userOrdersLiveData = MutableLiveData<Resource<ArrayList<OrderModel>>>()
    val userOrdersLiveData: LiveData<Resource<ArrayList<OrderModel>>> = _userOrdersLiveData

    init {
        getUserOrders()
    }

    private fun getUserOrders() {
        _userOrdersLiveData.value = Resource.Loading()
        // here we are juste define our coroutine
        viewModelScope.launch(Dispatchers.IO) {
            _userOrdersLiveData.postValue(
                repository.getUserOrders()
            )
        }
    }

}