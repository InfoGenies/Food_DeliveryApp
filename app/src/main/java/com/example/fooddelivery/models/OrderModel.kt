package com.example.fooddelivery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


enum class OrderEnums {

    PLACED, CONFIRMED, SHIPPED, DELIVERED
}

@Parcelize
data class OrderModel(
    val orderId: String = "",
    val userUid: String = "",
    val orderSubmittedTime: Long = 0,
    val orderLocation: String = "",
    val orderStatus: OrderEnums = OrderEnums.PLACED,
    val totalCost: Float = 0F,
    val productsList: List<Carte>? = null
) : Parcelable
