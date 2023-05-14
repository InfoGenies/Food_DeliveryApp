package com.example.fooddelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.models.Product


class PopularAdapter(private val context: Context, private val listener: MainProductListener) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    private val PopularList: ArrayList<Product> = ArrayList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularViewHolder {
        return PopularViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_popular_food, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(PopularList[position])
    }

    override fun getItemCount(): Int {
        return PopularList.size
    }


    inner class PopularViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productModel: Product) = with(binding) {
            setVariable(BR.productItem, productModel)
            setVariable(BR.productListener, listener)
        }

    }


    fun addMainPopularListItems(list: ArrayList<Product>?) {
        if (list == null || list == PopularList) return
        PopularList.clear()
        PopularList.addAll(list)
        notifyDataSetChanged()
    }

    interface MainProductListener {
        fun onProductClicked(productModel: Product)
    }
}

