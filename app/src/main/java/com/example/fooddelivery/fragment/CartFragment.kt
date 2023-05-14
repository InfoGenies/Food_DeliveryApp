package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.CarteAdapter
import com.example.fooddelivery.databinding.FragmentCartBinding
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.utils.extention.hide
import com.example.fooddelivery.utils.extention.show
import com.example.fooddelivery.viewmodels.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(), CarteAdapter.MainCarteListener {
    private lateinit var binding: FragmentCartBinding
    private val foodViewModel by activityViewModels<FoodViewModel>()
    private val carteAdapter by lazy {
        CarteAdapter(requireContext(), this)
    }
    private var cartProductsList = ArrayList<Carte>()
    var sum: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel.getAllFoodsCarts()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cart, container, false
        )
        return binding.run {
            fragment = this@CartFragment
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
        initViews()
    }

    private fun observeListener() {
        foodViewModel.FoodsLiveDataCarts.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                println("its notify adapter")
                cartProductsList = it as ArrayList<Carte>
                calculTotal(it)
                carteAdapter.addCarteListItems(it as ArrayList<Carte>?)
            } else {
                noDataFound()
            }
        })
    }

    private fun initViews() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            HideShimmer()
            binding.cartsRecyclerview.apply {
                visibility = View.VISIBLE
                hasFixedSize()
                isNestedScrollingEnabled = true
                adapter = carteAdapter
            }
        }

    }

    private fun noDataFound() {
        binding.apply {
            shimmer.hide()
            bottomCartLayout.hide()
            cartsRecyclerview.hide()
            emptyView.show()
        }
    }

    private fun HideShimmer() {
        binding.apply {
            shimmer.stopShimmer()
            shimmer.hide()
        }
    }

    private fun calculTotal(items: List<Carte>?) {
        sum = 0
        items?.forEach {
            var price = it.productQuantite * it.productPrice
            sum += price
        }
        binding.totalPriceBagFrag.text = sum.toString() + " $"
    }

    fun changeProductQuantity(
        increasePrice: Boolean,
        quantity: Int,
        item: Carte,
        editText: EditText
    ) {
        // increase or decrease quantity value .
        var quantity = quantity
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        item.productQuantite = quantity
        editText.setText(quantity.toString())
        foodViewModel.updateCarte(item)
        // we can use .notifyDataSetChanged() insthead of using editText to notify Ui that data is changed on DB
        // carteAdapter.notifyDataSetChanged()


    }

     fun navigateTocheckOutBottomSheetDialog() {
        val action = CartFragmentDirections.actionNavigationCartToCheckOutBottomSheetDialog(sum.toFloat(),cartProductsList.toTypedArray())
        findNavController().navigate(action)
    }

    override fun onItemClickPlus(quantity: Int, item: Carte, editText: EditText) {
        changeProductQuantity(true, quantity, item, editText)
    }

    override fun onItemClickMinus(quantity: Int, item: Carte, editText: EditText) {
        changeProductQuantity(false, quantity, item, editText)
    }

    override fun onItemClickDelete(item: Carte) {
        foodViewModel.deleteCarte(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println(" cart is onDestroyView is running")
    }

    override fun onDestroy() {
        super.onDestroy()
        println(" cart is onDestroy is running")
    }
}