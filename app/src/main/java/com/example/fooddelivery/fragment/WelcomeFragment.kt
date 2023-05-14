package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentWelcomeBinding
import com.example.fooddelivery.viewmodels.PhoneAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private val authViewModel: PhoneAuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* check if it the first time for the user open the app
           so if it the first time Welcome fragment still shown but if its not will run
           a function that check if user logged in with firebase authentication if he has logged in
           will open MainFragment if not will open Authentication Fragment.
        */
        val isFirstLogIn = authViewModel.checkIfFirstAppOpened()
        if(!isFirstLogIn){
            checkIfUserLoggedIn()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome, container, false
        )
        binding.fragment = this
        return binding.root
    }
    // check if user logged in and open main fragment if user logged in
    // or authentication fragment if not.
    private fun checkIfUserLoggedIn() {
        val isLoggedIn = authViewModel.checkIfUserLoggedIn()
        if(isLoggedIn){
            navigateToMainFragment()
        }else{
            navigateToAuthenticationFragment()
        }
    }


    private fun navigateToAuthenticationFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToAuthFragment2()
        findNavController().navigate(action)
    }

    private fun navigateToMainFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
        findNavController().navigate(action)
    }


    fun openSignupFragment() {
        navigateToAuthenticationFragment()
    }

}