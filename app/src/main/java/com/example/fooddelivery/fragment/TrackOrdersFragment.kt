package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentTrackOrdersBinding
import com.example.fooddelivery.utils.extention.closeFragment


class TrackOrdersFragment : Fragment() {
    private lateinit var binding: FragmentTrackOrdersBinding
    private val args by navArgs<TrackOrdersFragmentArgs>()
    private val orderModel by lazy { args.orderModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_track_orders, container, false)
        return binding.run {
            fragment = this@TrackOrdersFragment
            order = orderModel
            root
        }
    }
    fun backPressFragment() = closeFragment()

}