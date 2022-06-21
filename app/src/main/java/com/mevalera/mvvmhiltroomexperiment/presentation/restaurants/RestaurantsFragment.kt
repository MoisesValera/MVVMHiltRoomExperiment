package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.databinding.FragmentRestaurantBinding
import com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.filters_bottom_sheet.ConferencesFiltersBottomSheetFragment
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import com.mevalera.mvvmhiltroomexperiment.util.gone
import com.mevalera.mvvmhiltroomexperiment.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RestaurantsFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantBinding
    private val viewModel: RestaurantViewModel by activityViewModels()
    private lateinit var filtersBottomSheetFragment: ConferencesFiltersBottomSheetFragment
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

        Timber.d("Imprimiendo viewCreated data")
        setupViews()
        initObservers()
        initListeners()
    }

    private fun setupViews() = with(binding) {
        restaurantAdapter.itemClickListener = object : ClickEvent {
            override fun onItemClick(conference: Conference?) {
                conference?.let { conference ->
                    Timber.d(conference._id.toString())
                    findNavController().navigate(
                        RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantWebViewFragment(
                            conference._id!!
                        )
                    )
                }
            }
        }
        restaurantsList.adapter = restaurantAdapter
    }

    private fun initListeners() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                if (viewModel.yearFilter.value != null || viewModel.monthFilter.value != null) {
                    viewModel.getFilteredConferences(
                        viewModel.yearFilter.value.toString(),
                        viewModel.monthFilter.value.toString()
                    )
                } else {
                    viewModel.getConferences()
                }
            }
        }

        filtersOptionsFab.setOnClickListener {
            showBottomSheetArticles()
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
                    progressBar.gone()
                    swipeRefreshLayout.isRefreshing = false
                    restaurantAdapter.submitAndUpdateList(result.data!!)
                }
                is Resource.Error -> {
                    progressBar.gone()
                    textViewError.isVisible = result.data.isNullOrEmpty()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun showBottomSheetArticles() {
        filtersBottomSheetFragment =
            ConferencesFiltersBottomSheetFragment()
        filtersBottomSheetFragment.show(
            childFragmentManager,
            ConferencesFiltersBottomSheetFragment.TAG
        )
        filtersBottomSheetFragment.callbackChangeMade = {
            Timber.d("Imprimiendo filters callback data")
            viewModel.getFilteredConferences(it.year, it.getFormattedMonth())
        }
    }
}