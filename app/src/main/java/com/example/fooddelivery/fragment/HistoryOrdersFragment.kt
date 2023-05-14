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
import com.example.fooddelivery.adapters.HistoryOrdersAdapter
import com.example.fooddelivery.databinding.FragmentHistoryOrdersBinding
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.models.HistoryOrders
import com.example.fooddelivery.models.OrderModel
import com.example.fooddelivery.utils.Constante
import com.example.fooddelivery.utils.extention.closeFragment
import com.example.fooddelivery.utils.extention.hide
import com.example.fooddelivery.utils.extention.show
import com.example.fooddelivery.viewmodels.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HistoryOrdersFragment : Fragment(), HistoryOrdersAdapter.OrderListener {
    private lateinit var binding: FragmentHistoryOrdersBinding
    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private lateinit var historyAdapter: HistoryOrdersAdapter

    @Inject
    @Named(Constante.LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutViewModel.getHistoryOrders()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history_orders, container, false
        )
        return binding.run {
            fragment = this@HistoryOrdersFragment
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        checkoutViewModel.HistoryorderLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                showRecyclerView(it)

            } else {
                hideRecyclerView()
            }
            
        })    }

    private fun showRecyclerView(data: List<HistoryOrders>) {
        historyAdapter =  HistoryOrdersAdapter(data,this@HistoryOrdersFragment)
        binding.apply {
            println("the size of history is ${data.size}")
            historyOrderFragmentRecycler.adapter = historyAdapter
            emptyView.hide()
        }
    }
    private fun hideRecyclerView() {
        binding.let {
            it.historyOrderFragmentRecycler.hide()
            it.emptyView.show()
        }
    }
    fun backPressFragment() = closeFragment()

    override fun onOrderClicked(orderModel: HistoryOrders) {
        navigateToSpecificHistoryOrderFragment(orderModel)
    }

    override fun onDeleteOrder(orderModel: HistoryOrders) {
        checkoutViewModel.removeOrders(orderModel)
    }

    private fun navigateToSpecificHistoryOrderFragment(orderModel: HistoryOrders) {
        val action = HistoryOrdersFragmentDirections.actionHistoryOrdersFragmentToSpecificHistoryOrderFragment(orderModel)
        findNavController().navigate(action)
    }
}