package com.example.fooddelivery.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.AllOrdersAdapter
import com.example.fooddelivery.databinding.FragmentMyOrdersBinding
import com.example.fooddelivery.models.OrderModel
import com.example.fooddelivery.utils.Constante.Companion.LOADING_ANNOTATION
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.closeFragment
import com.example.fooddelivery.utils.extention.hide
import com.example.fooddelivery.utils.extention.show
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MyOrdersFragment : Fragment() , AllOrdersAdapter.OrderListener {
    private lateinit var binding: FragmentMyOrdersBinding
    private lateinit var allOrdersAdapter: AllOrdersAdapter
    private val ordersViewModel by viewModels<OrdersViewModel>()


    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false)
        return binding.run {
            fragment = this@MyOrdersFragment
            root
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }
    private fun observeListener() {
        ordersViewModel.userOrdersLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    if(it.data!=null && it.data.isNotEmpty()){
                        showRecyclerView(it.data as List<OrderModel>)
                    }else{
                        hideRecyclerView()
                    }
                    loadingDialog.hide()
                }
                is Resource.Error -> {
                    showToast(it.msg!!)
                    hideRecyclerView()
                    loadingDialog.hide()
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })
    }
    private fun hideRecyclerView() {
        binding.let {
            it.allOrdersRV.hide()
            it.emptyProducts.show()
        }
    }
    private fun showRecyclerView(data: List<OrderModel>) {
        allOrdersAdapter = AllOrdersAdapter(data.sortedBy { it.orderSubmittedTime }, this)
        binding.let {
            it.allOrdersRV.adapter = allOrdersAdapter
            it.emptyProducts.hide()
            it.allOrdersRV.show()
        }
    }


    fun backPress() = closeFragment()
    override fun onOrderClicked(orderModel: OrderModel) {
        navigateToSpecificOrderFragment(orderModel)
    }
    private fun navigateToSpecificOrderFragment(orderModel: OrderModel) {
        val action = MyOrdersFragmentDirections.actionMyOrdersFragmentToSpecificOrderFragment(orderModel)
        findNavController().navigate(action)
    }

}