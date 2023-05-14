package com.example.fooddelivery.models

data class Category(
    val categoryId :Int?=-1,
    var restaurantId :String="",
    val categoryName: String = ""
)