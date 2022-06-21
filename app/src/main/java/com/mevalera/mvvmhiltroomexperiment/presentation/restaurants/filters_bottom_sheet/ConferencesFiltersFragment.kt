package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.filters_bottom_sheet

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mevalera.mvvmhiltroomexperiment.R
import com.mevalera.mvvmhiltroomexperiment.data.model.ConferencesFilter
import com.mevalera.mvvmhiltroomexperiment.databinding.FragmentConferencesFiltersBinding
import com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.RestaurantViewModel
import com.mevalera.mvvmhiltroomexperiment.util.px
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConferencesFiltersBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentConferencesFiltersBinding
    private lateinit var _bottomSheetBehavior: BottomSheetBehavior<*>
    private val viewModel: ConferencesFiltersViewModel by activityViewModels()
    var callbackChangeMade: ((filter: ConferencesFilter) -> Unit)? = null
    private var yearFilter: String? = null
    private var monthFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConferencesFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bottomSheetBehavior = BottomSheetBehavior.from(requireView().parent as View)
        afterOnViewCreated()
        setStateExpanded()
        binding.contentScroll.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        initViews()
    }

    private fun initViews() = with(binding) {
        yearItemsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), YEAR_ROWS, GridLayoutManager.HORIZONTAL, false)
            adapter = YearFilterAdapter(requireContext()).apply {
                setDataList(viewModel.getYearsFilters().reversed())
                initYearFilterRecyclerViewListener()
            }
        }
        monthItemsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), MONTH_ROWS, GridLayoutManager.HORIZONTAL, false)
            adapter = MonthsFilterAdapter(requireContext()).apply {
                setDataList(viewModel.getMonthsFilters())
                initMonthFilterRecyclerViewListener()
            }
        }
    }

    private fun YearFilterAdapter.initYearFilterRecyclerViewListener() {
        yearFilterAction = { year ->
            yearFilter = year
        }
    }

    private fun MonthsFilterAdapter.initMonthFilterRecyclerViewListener() {
        monthFilterAction = { month ->
            monthFilter = month
        }
    }

    private fun setStateExpanded() {
        dialog?.apply {
            if (!bottomSheetIsExpanded(_bottomSheetBehavior)) {
                _bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        configToolbarNotExpanded()
    }

    private fun bottomSheetIsExpanded(behavior: BottomSheetBehavior<*>) =
        behavior.state == BottomSheetBehavior.STATE_EXPANDED

    private fun afterOnViewCreated() {
        listenerState()
    }

    private fun listenerState() {
        _bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            configToolbarExpanded()
                        }
                        else -> {
                            configToolbarNotExpanded()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // NO-OP
                }
            }
        )

        binding.iconClose.setOnClickListener {
            binding.contentScroll.scrollTo(0, 0)
            _bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setHideableDraggable(flag: Boolean) {
        _bottomSheetBehavior.apply {
            isDraggable = flag
            isHideable = flag
        }
    }

    private fun configToolbarNotExpanded() {
        binding.iconClose.isGone = true
        binding.cardView.elevation = 0f
        binding.lineSeparatorHeader.isGone = false

        setHideableDraggable(true)
    }

    private fun configToolbarExpanded() {
        binding.iconClose.isGone = false
        binding.cardView.elevation = ELEVATION.px.toFloat()
        binding.lineSeparatorHeader.isGone = true

        setHideableDraggable(false)
    }

    override fun onDestroy() {
        if (yearFilter != null && monthFilter != null) {
            callbackChangeMade?.invoke(ConferencesFilter(yearFilter!!, monthFilter!!))
        }
        super.onDestroy()
    }

    companion object {
        const val TAG = "ConferencesFiltersBottomSheet"
        private const val ELEVATION = 4
        private const val YEAR_ROWS = 5
        private const val MONTH_ROWS = 3
    }
}