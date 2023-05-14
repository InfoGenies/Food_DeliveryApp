package com.example.fooddelivery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Restaurant(
    @PrimaryKey
    val restaurantId :String="",
    val restaurantName:String = "",
    val imageRestaurant :String ="",
    val contact :String = "",
    val latitude :Double = 0.0,
    val longitude :Double = 0.0,
    var distance: String ="",
    val restaurantType:String ="",
    val freeDelivery:Boolean =false,
    var inFav:Boolean?=false,
    var rateCount:Double = 0.0
): Parcelable {}