package com.example.fooddelivery.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentSuccessDialogBinding
import com.example.fooddelivery.utils.extention.closeFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SuccessDialogFragment : BottomSheetDialogFragment() {
    private  var _binding: FragmentSuccessDialogBinding? = null
    private val binding get()  = _binding!!
    private val args by navArgs<OrderStatusFragmentArgs>()
    private val mIsOrderSubmitted by lazy { args.isOrderSubmitted }
    private val orderModel by lazy { args.orderModel }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentSuccessDialogBinding.inflate(LayoutInflater.from(requireContext()))
        super.onCreateDialog(savedInstanceState)

        return MaterialAlertDialogBuilder(requireContext())
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .setView(binding.root)
            .setCancelable(true)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

      return binding.run {
            binding.fragment = this@SuccessDialogFragment
            binding.root
        }
    }

     fun navigateToSuccessDialogue() {

    }


}