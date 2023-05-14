package com.example.fooddelivery.utils

import androidx.room.TypeConverter
import com.example.fooddelivery.models.Carte
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MyTypeConverters {
    /*@TypeConverter
    fun fromDateToLong(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun fromLongToDate(date: Long): Date {
        return Date(date)
    }*/
    @TypeConverter
    fun fromCarteToJSON(carte: List<Carte>): String {
        return Gson().toJson(carte)
    }
    @TypeConverter
    fun fromJSONToCarte(json: String): List<Carte> {
        val listOfVisits = object : TypeToken<ArrayList<Carte>>() {}.type
        return Gson().fromJson(json,listOfVisits)
    }
}