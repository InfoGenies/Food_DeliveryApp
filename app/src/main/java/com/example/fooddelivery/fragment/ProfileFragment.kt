package com.example.fooddelivery.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentProfileBinding
import com.example.fooddelivery.models.UserInfoModel
import com.example.fooddelivery.utils.Constante.Companion.DISPLAY_DIALOG
import com.example.fooddelivery.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.UserInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var userInfoModel: UserInfoModel? = null

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    @Inject
    @Named(DISPLAY_DIALOG)
    lateinit var displayAlert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
        return binding.run {
            binding.fragment = this@ProfileFragment
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeListener()
    }

    private fun observeListener() {
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    ShowInfo(userInfo.data)
                    loadingDialog.hide()
                    userInfoModel = userInfo.data
                    binding.userInfo = userInfo.data
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(userInfo.msg.toString())
                }
                is Resource.Loading -> loadingDialog.show()


            }

        })



        userInfoViewModel.userLocationLiveData.observe(viewLifecycleOwner, Observer {newLocation->
            if(newLocation != null){
                userInfoViewModel.changUserLocation(newLocation)
                showAlertDialog(getString(R.string.myLocation), getString(R.string.locationChanged))
            }

        })
    }
    private fun showAlertDialog(title: String, message: String) {
        displayAlert.apply {
            setTitle(title)
            setMessage(message)
            show()
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        navigateToAuthFragment()
        showToast(getString(R.string.logOutMessage))
    }
    fun openOrdersFragment() {
        val action = ProfileFragmentDirections.actionNavigationProfileToMyOrdersFragment()
        findNavController().navigate(action)
    }

    fun openHistoryOrdersFragment() {
        val action = ProfileFragmentDirections.actionNavigationProfileToHistoryOrdersFragment()
        findNavController().navigate(action)
    }

    fun openLocationsFragment() {
        val action = ProfileFragmentDirections.actionNavigationProfileToLocateUserLocationFragment()
        findNavController().navigate(action)
    }

    fun openUsersInfoFragment() {
        val action = ProfileFragmentDirections.actionNavigationProfileToChangeUserInfo2(userInfoModel)
        findNavController().navigate(action)
    }


    private fun navigateToAuthFragment() {
        val action = ProfileFragmentDirections.actionNavigationProfileToAuthFragment()
        findNavController().navigate(action)
    }
    private fun ShowInfo(data: UserInfoModel?) {
        println("the name is ${data!!.userName} from ProfileFragment ")
    }



}