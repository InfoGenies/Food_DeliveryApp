package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_auth, container, false
        )
        binding.fragment = this
        return binding.root
    }
    fun navigateToPhoneAuthFragment() {
        val action = AuthFragmentDirections.actionAuthFragment2ToPhoneNumberAuthFragment()
        findNavController().navigate(action)
    }
    fun navigateToGoogleAuthFragment() {
        val action = AuthFragmentDirections.actionAuthFragment2ToGoogleAuthFragment()
        findNavController().navigate(action)
    }



}