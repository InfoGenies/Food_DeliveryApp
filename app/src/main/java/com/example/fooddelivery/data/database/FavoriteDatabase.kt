package com.example.fooddelivery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fooddelivery.models.*
import com.example.fooddelivery.utils.MyTypeConverters

@TypeConverters(value = [MyTypeConverters::class])
@Database(entities = [Restaurant::class,Product::class, Carte::class, HistoryOrders::class], version = 1, exportSchema = false)
abstract  class FavoriteDatabase  : RoomDatabase()  {
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getCarteDao(): CarteDao
    abstract fun getOrderDao(): OrderDao
}