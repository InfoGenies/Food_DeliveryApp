package com.example.fooddelivery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.models.HistoryOrders
import com.example.fooddelivery.models.OrderModel
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.repository.FoodRepository
import com.example.fooddelivery.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val repository: FoodRepository) :
    ViewModel() {

    private val _orderProductsLiveData = MutableLiveData<Resource<OrderModel>>()
    val orderProductsLiveData: LiveData<Resource<OrderModel>> = _orderProductsLiveData

    private lateinit var _HistoryorderLiveData: LiveData<List<HistoryOrders>>
    val HistoryorderLiveData get() = _HistoryorderLiveData




    fun pushUserOrder(cartProductsList: Array<Carte>, userLocation: String, totalCost: Float) {
        _orderProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _orderProductsLiveData.postValue(
                repository.uploadProductsToOrders(cartProductsList, userLocation, totalCost)
            )
        }
    }

    fun saveOrders(orderModel: HistoryOrders) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrder(orderModel)
        }
    }

    fun getHistoryOrders(){
        viewModelScope.launch(Dispatchers.IO) {
            _HistoryorderLiveData = repository.getAllHistoryOrders()

        }
    }
    fun removeOrders(historyOrders: HistoryOrders){
        viewModelScope.launch(Dispatchers.IO) {
         repository.deleteOrders(historyOrders)

        }
    }
}