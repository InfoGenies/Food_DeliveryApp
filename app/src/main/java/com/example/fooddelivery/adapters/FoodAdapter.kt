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

class FoodAdapter (private val context: Context, private val listener: MainFoodListener) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private val FoodList: ArrayList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_food_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(FoodList[position])
    }

    override fun getItemCount(): Int {
        return FoodList.size
    }
    fun addFilterListItems(list: ArrayList<Product>?) {
        if (list == null || list == FoodList) return
        FoodList.clear()
        FoodList.addAll(list)
        notifyDataSetChanged()
    }
    inner class FoodViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodModel: Product) = with(binding) {
            setVariable(BR.foodItem, foodModel)
            setVariable(BR.foodListener, listener)
        }

    }

    interface MainFoodListener {
        fun onClickedFoodDetaille(fooditem : Product)
    }


}