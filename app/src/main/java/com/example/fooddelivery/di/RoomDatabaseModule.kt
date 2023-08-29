package com.example.fooddelivery.di

import android.content.Context
import androidx.room.Room
import com.example.fooddelivery.data.database.FavoriteDatabase
import com.example.fooddelivery.utils.Constante.Companion.FAVORITE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// we are juste install this dependency in all the app so when the the app finished the dependency is also finished
@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            FAVORITE_DATABASE
        ).build()


    @Singleton
    @Provides
    fun provideYourDao(db: FavoriteDatabase) = db.getFavoriteDao()

    @Singleton
    @Provides
    fun provideCarteDao(db: FavoriteDatabase) = db.getCarteDao()

    /* fun provideCarteDao(db: FavoriteDatabase) : CarteDao {
         return  db.getCarteDao()
     }*/



    @Singleton
    @Provides
    fun provideOrdersDao(db: FavoriteDatabase) = db.getOrderDao()
}