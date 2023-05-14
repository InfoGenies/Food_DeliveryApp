package com.example.fooddelivery.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.R
import com.example.fooddelivery.adapters.FilterAdapter
import com.example.fooddelivery.databinding.FragmentFilterBinding
import com.example.fooddelivery.models.Product
import com.example.fooddelivery.utils.Resource
import com.example.fooddelivery.utils.extention.closeFragment
import com.example.fooddelivery.utils.extention.hide
import com.example.fooddelivery.utils.extention.show
import com.example.fooddelivery.utils.extention.showToast
import com.example.fooddelivery.viewmodels.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFragment : Fragment(), FilterAdapter.MainFiilterListener {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val filterAdapter by lazy { FilterAdapter(requireContext(), this@FilterFragment) }
    private val foodViewModel by activityViewModels<FoodViewModel>()
    val args: FilterFragmentArgs by navArgs<FilterFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_filter, container, false
        )

        return binding.run {
            fragment = this@FilterFragment
            filterbyName = args.filterName
            initViews()
            binding.root
        }

    }

    private fun initViews() {
        binding.productRecyclerView.apply {
            hasFixedSize()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = true
            adapter = filterAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            foodViewModel.FillteListLiveData.observe(viewLifecycleOwner, Observer { filterlist ->
                when (filterlist) {
                    is Resource.Success -> {
                        if (!filterlist.data!!.isEmpty()) {
                            filterAdapter.addFilterListItems(filterlist.data)
                            HideShimmer()
                        } else {
                            HideShimmer()
                            noDataFound()

                        }

                    }
                    is Resource.Error -> {
                        showToast(filterlist.msg.toString())
                    }
                    is Resource.Loading -> {
                        ShowShimmer()

                    }

                }


            })
        }


    }

    override fun onResume() {
        super.onResume()
        foodViewModel.getFillterFoodList(5, args.filterName)
    }

    private fun ShowShimmer() {
        binding.shimmer.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun HideShimmer() {
        binding.shimmer.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }

    private fun noDataFound() {
        binding.apply {
            productRecyclerView.hide()
            emptyView.show()
        }
    }

    fun DeleteStarsFilitring() {
        println("DeleteStarsFilitring was clicked")
        foodViewModel.getFillterFoodList(0, args.filterName)
        observeListener()

    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

    fun backPressFragment() {
        closeFragment()
    }

    override fun onFillterClickedDetaille(fooditem: Product) {
        TODO("Not yet implemented")
    }

}