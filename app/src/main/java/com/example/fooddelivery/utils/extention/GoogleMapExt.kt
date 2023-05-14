package com.example.fooddelivery.utils.extention

import com.example.fooddelivery.utils.Constante.Companion.BASE_LATITUDE
import com.example.fooddelivery.utils.Constante.Companion.BASE_LONGITUDE
import com.example.fooddelivery.utils.Constante.Companion.LOCATION_ZOOM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun createMarkerOption(
    latLag: LatLng, icon: BitmapDescriptor
): MarkerOptions {
    return MarkerOptions().position(
        latLag
    ).title("My Location")
        .icon(icon)

}

fun GoogleMap.moveCameraToDefault() {
    animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            LatLng(
                BASE_LATITUDE,
                BASE_LONGITUDE
            ), LOCATION_ZOOM
        )
    )
}