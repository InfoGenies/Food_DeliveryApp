package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentOrderStatusBinding
import com.example.fooddelivery.utils.extention.closeFragment


class OrderStatusFragment : Fragment() {
    private lateinit var binding: FragmentOrderStatusBinding
    private val args by navArgs<OrderStatusFragmentArgs>()
    private val mIsOrderSubmitted by lazy { args.isOrderSubmitted }
    private val orderModel by lazy { args.orderModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_status, container, false)
        return binding.run {
            fragment = this@OrderStatusFragment
            isOrderSubmitted = mIsOrderSubmitted
            root
        }
    }
    fun trackOrTryAgain(){
        if(mIsOrderSubmitted){
            navigateToTrackOrderFragment()
        }else{
            closeFragment()
        }
    }

    private fun navigateToTrackOrderFragment() {
        val action = OrderStatusFragmentDirections.actionOrderStatusFragmentToTrackOrdersFragment(orderModel)
        findNavController().navigate(action)
    }

    fun backToHome(){
        val action = OrderStatusFragmentDirections.actionOrderStatusFragmentToNavigationHome()
        findNavController().navigate(action)
    }


}