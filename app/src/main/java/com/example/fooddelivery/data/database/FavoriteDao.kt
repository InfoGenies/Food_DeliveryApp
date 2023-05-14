package com.example.fooddelivery.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant

@Dao
interface FavoriteDao {

    // we use LiveData when the responce return Multi value
    // we use suspend when the responce return single value


    @Insert(onConflict = REPLACE)
    fun saveRestaurant(restaurantModel: Restaurant)

    @Query("SELECT * FROM Restaurant")
    fun getAllFavoriteRestaurant(): LiveData<List<Restaurant>>

    @Query("SELECT * FROM Restaurant WHERE restaurantId =:id")
    suspend fun getSpecificFavoriteRestaurant(id: String): Restaurant?

    @Query("SELECT * FROM restaurant WHERE restaurantId =:id")
    fun getSpecificFavoriteRestaurantLiveData(id: String): LiveData<Restaurant?>

    @Delete
    suspend fun removeRestaurantFromFavorites(restaurantModel: Restaurant)

    @Query("DELETE FROM Restaurant")
    suspend fun deleteAllRestaurants()


    @Insert(onConflict = REPLACE)
    fun saveFood(foodModel: Product)

    @Query("SELECT * FROM Product")
    fun getAllFavoriteFoods(): LiveData<List<Product>>

    @Query("SELECT * FROM Product WHERE productId =:id")
    suspend fun getSpecificFavoriteFood(id: String): Product?

    @Query("SELECT * FROM Product WHERE productId =:id")
    fun getSpecificFavoriteFoodLiveData(id: String): LiveData<Product?>

    @Delete
    suspend fun removeFoodFromFavorites(foodModel: Product)

    @Query("DELETE FROM Product")
    suspend fun deleteAllFoods()




}