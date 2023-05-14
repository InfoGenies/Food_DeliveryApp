package com.example.fooddelivery.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.views.MainActivity
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentCreateUserInfoBinding
import com.example.fooddelivery.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.closeFragment
import com.example.fooddelivery.utils.extention.hideBottomNav
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.PhoneAuthViewModel
import com.example.fooddelivery.viewmodels.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CreateUserInfoFragment : Fragment() {
    private lateinit var binding: FragmentCreateUserInfoBinding

    private val authViewModel by activityViewModels<PhoneAuthViewModel>()
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()

    private var mImageUri: Uri? = null



    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNav()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_user_info, container, false
        )
        return binding.run {
            fragment = this@CreateUserInfoFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }


    private fun observeListener() {
        userInfoViewModel.userLocationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.apply {
                    println("user located ${it.toString()}")
                    selectLocationEditText.setText(it)
                    selectLocationEditText.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.green)
                    )
                }
            }
        })
        // Observation the LiveData
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer { info ->
            when (info) {
                is Resource.Success -> {
                    loadingDialog.hide()
                    showToast(info.data!!)
                    authViewModel.setUserInformationValue()
                    closeFragment()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(info.msg!!)
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showUserImage()
    }

    fun submitUserInfo() = with(binding) {
        val userName = userNameEditText.text.toString().trim()
        val userLocation = selectLocationEditText.text.toString().trim()
        if (userName.isEmpty()) {
            showToast(getString(R.string.addUsearName))
            return@with
        }
        if (mImageUri == null) {
            showToast(getString(R.string.addUserImage))
            return@with
        }
        if (userLocation.isEmpty()) {
            showToast(getString(R.string.addUserLocation))
            return@with
        }
        authViewModel.uploadUserInformation(
            userName,
            mImageUri,
            userLocation
        )

    }

    fun selectUserLocation() {
        val action =
            CreateUserInfoFragmentDirections.actionCreateUserInfoFragmentToLocateUserLocationFragment()
        findNavController().navigate(action)
    }

    fun changeUserProfileImage() {
        selectImageFromGallery()
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            mImageUri = result.data?.data
            showUserImage()
        }

    private fun showUserImage() {
        if (mImageUri != null)
            binding.userProfileImage.setImageURI(mImageUri)
    }

}