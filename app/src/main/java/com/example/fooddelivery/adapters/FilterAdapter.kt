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

class FilterAdapter(private val context: Context, private val listener: MainFiilterListener) :
    RecyclerView.Adapter<FilterAdapter.FillterViewHolder>() {

    private val FillterList: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FillterViewHolder {
        return FillterViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_filter_food_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: FillterViewHolder, position: Int) {
        holder.bind(FillterList[position])
    }

    override fun getItemCount(): Int {
        return FillterList.size
    }
    fun addFilterListItems(list: ArrayList<Product>?) {
        if (list == null || list == FillterList) return
        FillterList.clear()
        FillterList.addAll(list)
        notifyDataSetChanged()
    }
    inner class FillterViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodModel: Product) = with(binding) {
            setVariable(BR.foodItem, foodModel)
            setVariable(BR.foodListener, listener)
        }



    }

    interface MainFiilterListener {
        fun onFillterClickedDetaille(fooditem : Product)
    }


}