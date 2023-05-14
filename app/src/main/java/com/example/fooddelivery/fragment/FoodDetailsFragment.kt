package com.example.fooddelivery.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.FragmentFoodDetailsBinding
import com.example.fooddelivery.models.Carte
import com.example.fooddelivery.utils.extention.closeFragment
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.FoodViewModel

class FoodDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailsBinding
    private val args: FoodDetailsFragmentArgs by navArgs()
    private val foodModel by lazy { args.foodModel }
    private val foodViewModel by activityViewModels<FoodViewModel>()
    private var productQuantity = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_details, container, false
        )
        return binding.run {
            binding.fragment = this@FoodDetailsFragment
            binding.food = foodModel
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeListener()
    }

    private fun initViews() {
        binding.apply {
            // change total price by quantity value in edit text.
            productQuantityEditText.addTextChangedListener {
                val quantity = productQuantityEditText.text.toString().trim()
                if (quantity.isNotEmpty() && TextUtils.isDigitsOnly(quantity)) {
                    productQuantity = quantity.toInt()
                    if (productQuantity > 0) {
                        countPrice.text = (foodModel.productPrice * productQuantity).toString()+" $"
                    }
                }
            }
        }
    }

    private fun observeListener() {
        foodViewModel.favoriteLiveData(foodModel.productId).observe(viewLifecycleOwner, Observer { food ->
            if (food != null) {
                binding.btnMark.setImageResource(R.drawable.saved)
            } else {
                binding.btnMark.setImageResource(R.drawable.not_save)            }
        })

    }
    fun saveFoodInFavorite() {
        foodModel.inFav = true
        foodViewModel.saveFoodInFavorites(foodModel)
    }
    fun changeProductQuantity(increasePrice: Boolean) {
        // increase or decrease quantity value .
        var quantity = binding.productQuantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        binding.productQuantityEditText.setText(quantity.toString())
    }
    fun saveFoodInCarte() {
        var carte = Carte(foodModel.productId,
            foodModel.restaurantId,
        foodModel.productName,
        foodModel.productPrice,
        foodModel.image,
            productQuantity)
        foodViewModel.saveCarte(carte)
        showToast(getString(R.string.InsertSucces))

    }


    fun backPressFragment() {
        closeFragment()
    }


}