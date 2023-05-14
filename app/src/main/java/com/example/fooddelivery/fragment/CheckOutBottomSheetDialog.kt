package com.example.fooddelivery.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentCheckOutBottomSheetDialogBinding
import com.example.fooddelivery.models.HistoryOrders
import com.example.fooddelivery.models.OrderEnums
import com.example.fooddelivery.models.OrderModel
import com.example.fooddelivery.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.CheckoutViewModel
import com.example.fooddelivery.viewmodels.UserInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CheckOutBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCheckOutBottomSheetDialogBinding
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    // we used viewModels instead of activityViewModels cause
    // 1 viewmodel related to fragment , when fragment is destroyed the viewmodel will destroyed
    // 2 activityViewModels is related to the activity ,whe the act destroyed the viewmodel will destroyed
    private val checkoutViewModel :  CheckoutViewModel by viewModels()


    private val args: CheckOutBottomSheetDialogArgs by navArgs()
    private val total by lazy { args.totalPrice }
    private val cartProductsList by lazy { args.productList }
    private lateinit var orderModel: OrderModel
    private val userUid by lazy { firebaseAuth.uid!! }

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    private var userLocation = ""

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_check_out_bottom_sheet_dialog, container, false
        )
        return binding.run {
            binding.fragment = this@CheckOutBottomSheetDialog
            binding.totalePrice = total
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        // gt user information from firebase to get user location and save in userLocation object to pass with user order .
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    userLocation = userInfo.data?.userLocationName!!
                }
                is Resource.Error -> {
                    showToast(userInfo.msg!!)
                }
            }
        })

        // observe if payment process successfully after order uploaded and the money sent successfully.
        println("la valeur de livedata is ${checkoutViewModel.orderProductsLiveData.toString()}")
        checkoutViewModel.orderProductsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    orderModel = it.data!!
                    loadingDialog.hide()
                    navigateToSuccessDialogue(true)
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    navigateToSuccessDialogue(false)
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })
    }

    private fun navigateToSuccessDialogue(status: Boolean) {
        val action =
            CheckOutBottomSheetDialogDirections.actionCheckOutBottomSheetDialogToOrderStatusFragment(
                orderModel,
                status
            )
        findNavController().navigate(action)

    }

    fun CheckOut() {
        checkoutViewModel.pushUserOrder(cartProductsList, userLocation, total)
        val orderModel = HistoryOrders(0,
            userUid,
            System.currentTimeMillis(),
            userLocation,
            total,
            cartProductsList.toList()
        )
        checkoutViewModel.saveOrders(orderModel)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("CheckOutBottomSheetDialog onDestroyView is running")
        //   checkoutViewModel.orderProductsLiveData =
    }

    override fun onDestroy() {
        super.onDestroy()
        println("CheckOutBottomSheetDialog onDestroy is running")

    }
}
