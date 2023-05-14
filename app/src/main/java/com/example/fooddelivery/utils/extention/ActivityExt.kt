package com.example.fooddelivery.utils.extention

import android.app.Activity
import android.os.Build
import android.view.View
import com.example.fooddelivery.views.MainActivity
import com.example.fooddelivery.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

fun MainActivity.showBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.nav_view)
    if(!navigation.isShown)
        navigation.show()
}

fun MainActivity.hideBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.nav_view)
    navigation.hide()
}