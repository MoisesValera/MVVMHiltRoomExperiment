package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import com.mevalera.mvvmhiltroomexperiment.databinding.FragmentRestaurantBinding
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import com.mevalera.mvvmhiltroomexperiment.util.SwipeToDeleteCallback
import com.mevalera.mvvmhiltroomexperiment.util.gone
import com.mevalera.mvvmhiltroomexperiment.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber



@AndroidEntryPoint
class RestaurantsFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantBinding

    private val viewModel by viewModels<RestaurantViewModel>()
    private val restaurantAdapter by lazy {
        RestaurantAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRestaurants()
        setupViews()
        initListeners()
        initObservers()
    }

    private fun setupViews() = with(binding) {
        restaurantAdapter.itemClickListener = object : ClickEvent {
            override fun onItemClick(restaurant: Restaurant?) {
                restaurant?.let {
                    Timber.d("${it.story_url}")
                    if (!it.story_url.isNullOrEmpty()) {
                        findNavController().navigate(
                            RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantWebViewFragment(
                                it.story_url
                            )
                        )
                    } else {
                        Snackbar.make(
                            restaurantsList,
                            "This record doesn't have any URL",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        restaurantsList.adapter = restaurantAdapter
    }

    private fun initListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getRestaurants()
            }
        }
    }

    private fun initObservers() = with(binding) {
        viewModel.restaurants.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    if (!swipeRefreshLayout.isRefreshing) {
                        progressBar.visible()
                    }
                    progressBar.isVisible = result.data.isNullOrEmpty()
                }
                is Resource.Success -> {
                    val data = result.data

                    data?.let {
                        restaurantAdapter.submitList(it)
                    }

                    progressBar.gone()
                    swipeRefreshLayout.isRefreshing = false

                    val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                        override fun onSwiped(
                            viewHolder: RecyclerView.ViewHolder,
                            direction: Int
                        ) {
                            val position = viewHolder.layoutPosition
                            viewModel.restaurants.value!!.data?.let { restaurantList ->
                                viewModel.deleteRestaurant(restaurantList[position].story_id!!)
                                val newList = restaurantList.toMutableList()
                                newList.removeAt(position)
                                restaurantAdapter.updateItemRemoved(viewHolder.adapterPosition)
                            }
                        }
                    }

                    val itemTouchHelper = ItemTouchHelper(swipeHandler)
                    itemTouchHelper.attachToRecyclerView(restaurantsList)
                }
                is Resource.Error -> {
                    progressBar.gone()
                    textViewError.isVisible = result.data.isNullOrEmpty()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}