package com.example.fooddelivery.utils.extention

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bumptech.glide.Glide




fun View.showWithAnimate(anim: Int){
    show()
    animation = AnimationUtils.loadAnimation(context, anim).apply {
        start()
    }
}
fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
fun ImageView.loadImage(link: String){
    Glide.with(context).load(link).into(this)
}
fun ImageView.loadGif(gifImage: Int){
    Glide.with(this.context).asGif().load(gifImage).into(this)
}
