package com.example.fooddelivery.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class HistoryOrders (
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0 ,
    val userUid: String = "",
    val orderSubmittedTime: Long = 0,
    val orderLocation: String = "",
    val totalCost: Float = 0F,
    val productsList: List<Carte>? = null
) : Parcelable