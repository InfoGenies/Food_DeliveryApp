package com.example.fooddelivery.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.models.HistoryOrders

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrders(orderModel: HistoryOrders)

    @Query("SELECT * FROM HistoryOrders")
    fun getAllOrders(): LiveData<List<HistoryOrders>>

    @Delete
    suspend fun removeOrder(historyOrders: HistoryOrders)
}