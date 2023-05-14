package com.example.fooddelivery.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fooddelivery.models.Carte

@Dao
interface CarteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun saveCarte(carteModel: Carte)

    @Query("SELECT * FROM Carte")
    fun getAllCarte(): LiveData<List<Carte>>

    @Query("SELECT * FROM Carte WHERE productId =:id")
    suspend fun getSpecificCarte(id: String): Carte?

    @Query("SELECT * FROM Carte WHERE productId =:id")
    fun getSpecificCarteLiveData(id: String): LiveData<Carte?>

    @Delete
    suspend fun removeCarte(carteModel: Carte)

    @Query("DELETE FROM Carte")
    suspend fun deleteAllCarte()

    @Update()
    fun updateCarte(carte: Carte)

}