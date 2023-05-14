package com.example.fooddelivery.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.ActivityMainBinding
import com.example.fooddelivery.utils.ConnectionLiveData
import com.example.fooddelivery.utils.extention.hideBottomNav
import com.example.fooddelivery.utils.extention.hideSystemUI
import com.example.fooddelivery.utils.extention.showBottomNav
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var firstCheckInternetConnection = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        hideSystemUI()
        initBottomNavigationView()
        observeNetworkConnection()

    }

    private fun observeNetworkConnection() {
        connectionLiveData.observe(this, Observer {isInternetAvailable->
            if(isInternetAvailable && !firstCheckInternetConnection){
                Snackbar.make(binding.parentLayout, getString(R.string.backOnline), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getColor(R.color.green))
                    .show()
            }else if(!isInternetAvailable){
                Snackbar.make(binding.parentLayout, getString(R.string.connectionLost), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getColor(R.color.red))
                    .show()
            }
            firstCheckInternetConnection = false
        })
    }



    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener(this)
        binding.navView.setupWithNavController(navController)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_home, R.id.navigation_cart,
            R.id.navigation_saves, R.id.navigation_profile -> showBottomNav()
            else -> hideBottomNav()
        }
    }

    override fun onPause() {
        super.onPause()
        firstCheckInternetConnection = true
    }


}