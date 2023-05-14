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
import com.example.fooddelivery.adapters.OrderFoodsAdapter
import com.example.fooddelivery.databinding.FragmentSpecificOrderBinding
import com.example.fooddelivery.utils.extention.closeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificOrderFragment : Fragment() {
    private lateinit var binding: FragmentSpecificOrderBinding
    private val args by navArgs<SpecificOrderFragmentArgs>()
    private val orderModel by lazy { args.order }
    private val specificOrdersAdapter by lazy { OrderFoodsAdapter(orderModel.productsList!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_specific_order, container, false)
        return binding.run {
            fragment = this@SpecificOrderFragment
            order = orderModel
            adapter = specificOrdersAdapter
            root
        }
    }


    fun backPressFragment() = closeFragment()

    fun trackOrder() {
        val action =
            SpecificOrderFragmentDirections.actionSpecificOrderFragmentToTrackOrdersFragment(
                orderModel
            )
        findNavController().navigate(action)
    }


}