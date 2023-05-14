package com.example.fooddelivery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.models.HistoryOrders
import com.example.fooddelivery.models.OrderModel

class HistoryOrdersAdapter (
    private val orderList: List<HistoryOrders>,
    private val listener: OrderListener
) : RecyclerView.Adapter<HistoryOrdersAdapter.HistoryOrdersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrdersViewHolder =
        HistoryOrdersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.historyorder_item_rv_layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HistoryOrdersViewHolder, position: Int) {
        println("onBindViewHolder ${orderList[position].toString()}")
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    inner class HistoryOrdersViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderModel: HistoryOrders) {
            binding.setVariable(BR.order, orderModel)
            binding.setVariable(BR.orderListener, listener)
        }
    }

    interface OrderListener{
        fun onOrderClicked(orderModel: HistoryOrders)
        fun onDeleteOrder(orderModel: HistoryOrders)
    }
}