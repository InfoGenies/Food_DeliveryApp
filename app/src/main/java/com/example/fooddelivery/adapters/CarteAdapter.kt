package com.example.fooddelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.BR
import com.example.fooddelivery.R
import com.example.fooddelivery.models.Carte

class CarteAdapter(
    private val context: Context, private val listener: MainCarteListener
) :
    RecyclerView.Adapter<CarteAdapter.CarteViewHolder>() {
    private val cartelist: ArrayList<Carte> = ArrayList()

    inner class CarteViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(carteModel: Carte) = with(binding) {
            setVariable(BR.carteItem, carteModel)
            setVariable(BR.carteListener, listener)
            setVariable(BR.adapter, this@CarteAdapter)


        }
    }
    fun onQuantityTextChanged(
        text: CharSequence,
        productModel: Carte,
        priceTextView: TextView
    ) {
        val quantity = text.toString()
        if (quantity.isNotEmpty()) {
            val quantityNumber = quantity.toDouble().toInt()
            if (quantityNumber > 0) {
                productModel.productQuantite = quantityNumber
                priceTextView.text =  (productModel.productPrice * quantityNumber).toString()

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarteViewHolder {
        return CarteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_cart, parent, false
            )

        )

    }

    fun addCarteListItems(list: ArrayList<Carte>?) {
        if (list == null || list == cartelist) return
        cartelist.clear()
        cartelist.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CarteViewHolder, position: Int) {
        holder.bind(cartelist[position])
    }

    override fun getItemCount(): Int = cartelist.size

    interface MainCarteListener {
        fun onItemClickPlus(quantity :Int , item :Carte,editText :EditText)
        fun onItemClickMinus(quantity :Int , item :Carte,editText :EditText)
        fun onItemClickDelete(item :Carte)


    }

}