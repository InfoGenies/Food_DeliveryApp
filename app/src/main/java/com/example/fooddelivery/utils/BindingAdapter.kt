package com.example.fooddelivery.utils

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.fooddelivery.R
import com.example.fooddelivery.utils.extention.loadGif
import com.example.fooddelivery.utils.extention.loadImage
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, link: String) {
    imageView.loadGif(R.drawable.spinner)
    if (link.isNotEmpty())
        imageView.loadImage(link)
}
@BindingAdapter("quantityEditText", "increasePrice")
fun changeProductQuantityValue(
    imageView: ImageView,
    quantityEditText: EditText,
    increasePrice: Boolean
) {
    imageView.setOnClickListener {
        var quantity = quantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        quantityEditText.setText(quantity.toString())
    }
}
@BindingAdapter("formatDate")
fun formatMilliSecondsDate(textView: TextView, milliseconds: Long) {
    val df = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    df.format(Date(milliseconds)).let {
        textView.text = it
    }

}
@BindingAdapter("loadGif")
fun loadGifIntoImageView(imageView: ImageView, imageGif: Drawable) {
    imageView.loadGif(R.drawable.food_empty_gif)
}