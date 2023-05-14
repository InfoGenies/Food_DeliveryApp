package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.HistorySpecifcOrdersAdapter
import com.example.fooddelivery.adapters.OrderFoodsAdapter
import com.example.fooddelivery.databinding.FragmentSpecificHistoryOrderBinding
import com.example.fooddelivery.utils.extention.closeFragment

class SpecificHistoryOrderFragment : Fragment() {
    private lateinit var binding: FragmentSpecificHistoryOrderBinding
    private val args by navArgs<SpecificHistoryOrderFragmentArgs>()
    private val orderModel by lazy { args.order }
    private val specificHistoryOrdersAdapter by lazy { HistorySpecifcOrdersAdapter(orderModel.productsList!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_specific_history_order, container, false)
        return binding.run {
            fragment = this@SpecificHistoryOrderFragment
            order = orderModel
            adapter = specificHistoryOrdersAdapter
            root
        }
    }
    fun backPressFragment() = closeFragment()

}