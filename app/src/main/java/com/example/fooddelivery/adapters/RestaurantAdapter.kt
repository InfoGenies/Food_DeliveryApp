package com.example.fooddelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.ItemRecentLayoutBinding
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.models.Restaurant
import com.example.fooddelivery.utils.Utils.calculationByDistance
import com.google.android.gms.maps.model.LatLng

class RestaurantAdapter(
    private val context: Context,
    private val restaurantlist: ArrayList<Restaurant>, private val listener: MainRestaurantListener
) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recent_layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurantlist[position],holder.binding.itemRecentSave)

    }

    override fun getItemCount(): Int {
        return restaurantlist.size
    }


    inner class RestaurantViewHolder(val binding: ItemRecentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(RestaurantModel: Restaurant , image :ImageView) = with(binding) {
            setVariable(BR.restaurantItem, RestaurantModel)
            setVariable(BR.restaurantListener, listener)
            setVariable(BR.image, image)
        }


    }

    interface MainRestaurantListener {
        fun onCallClicked(number: String)
        fun onFavClicked(item: Restaurant, imgFav :ImageView)
        fun onItemClicked(item: Restaurant)

    }


}