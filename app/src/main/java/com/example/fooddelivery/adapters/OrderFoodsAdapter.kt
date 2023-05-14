package com.example.fooddelivery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.R
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.BR

class OrderFoodsAdapter(
    private val productsList: List<Carte>
) : RecyclerView.Adapter<OrderFoodsAdapter.OrderFoodsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderFoodsViewHolder =
        OrderFoodsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.order_products_items_rv, parent, false
            )
        )


    override fun onBindViewHolder(holder: OrderFoodsViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size


    inner class OrderFoodsViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: Carte) = with(binding) {
            setVariable(BR.food, productModel)
        }
    }

    interface ProductListener {
        fun onProductClick(productModel: Product, transitionImageView: ImageView)
    }
}