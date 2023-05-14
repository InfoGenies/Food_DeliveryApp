package com.example.fooddelivery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.ItemFavLayoutBinding
import com.example.fooddelivery.models.Restaurant

class FavoriteAdapter (
    private val restaurantlist: ArrayList<Restaurant>, private val listener: MainFavoriteListener
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fav_layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(restaurantlist[position],holder.binding.itemRecentSave)

    }

    override fun getItemCount(): Int {
        return restaurantlist.size
    }


    inner class FavoriteViewHolder(val binding: ItemFavLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(RestaurantModel: Restaurant, image : ImageView) = with(binding) {
            setVariable(BR.restaurantItem, RestaurantModel)
            setVariable(BR.restaurantListener, listener)
            setVariable(BR.image, image)
        }


    }

    interface MainFavoriteListener {
        fun onCallClicked(number: String)
        fun onFavClicked(item: Restaurant, imgFav : ImageView)
    }


}