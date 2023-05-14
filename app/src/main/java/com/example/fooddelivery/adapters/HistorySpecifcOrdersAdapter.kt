package com.example.fooddelivery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.models.Carte

class HistorySpecifcOrdersAdapter (
    private val productsList: List<Carte>
) : RecyclerView.Adapter<HistorySpecifcOrdersAdapter.HistorySpecifcOrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorySpecifcOrdersViewHolder =
        HistorySpecifcOrdersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.order_products_items_rv, parent, false
            )
        )


    override fun onBindViewHolder(holder: HistorySpecifcOrdersViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size


    inner class HistorySpecifcOrdersViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: Carte) = with(binding) {
            setVariable(BR.food, productModel)
        }
    }


}