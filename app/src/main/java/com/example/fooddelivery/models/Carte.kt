package com.example.fooddelivery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Carte(
    @PrimaryKey
    val productId: String = "",
    val restaurantId: String = "",
    val productName: String = "",
    val productPrice: Int = -1,
    val image: String = "",
    var productQuantite: Int = -1
    ) : Parcelable
