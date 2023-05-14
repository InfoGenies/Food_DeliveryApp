package com.example.fooddelivery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Product(
    @PrimaryKey
    val productId: String = "",
    val restaurantId: String = "",
    val categoryId: Int? = -1,
    val productName: String = "",
    val productType: String = "",
    val productPrice: Int = -1,
    val freeDelivery: Boolean? = false,
    var inFav:Boolean?=false,
    val productDescription: String = "",
    val image: String = "",
    val rating: Int? = -1,
) : Parcelable
