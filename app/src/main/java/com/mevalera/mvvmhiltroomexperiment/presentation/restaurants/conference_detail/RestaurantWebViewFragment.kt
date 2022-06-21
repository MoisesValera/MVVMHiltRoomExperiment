package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.conference_detail

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mevalera.mvvmhiltroomexperiment.R
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.databinding.FragmentRestaurantWebviewBinding
import com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.RestaurantViewModel
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import com.mevalera.mvvmhiltroomexperiment.util.gone
import com.mevalera.mvvmhiltroomexperiment.util.loadImageFromURL
import com.mevalera.mvvmhiltroomexperiment.util.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RestaurantWebViewFragment : Fragment(R.layout.fragment_restaurant_webview) {

    private val viewModel by viewModels<RestaurantViewModel>()
    private lateinit var binding: FragmentRestaurantWebviewBinding
    private val arguments: RestaurantWebViewFragmentArgs by navArgs()
    private var bookmarkStatus: Boolean = false
    private var scrollingStatus: Boolean = false
    private var darkModeEnabled: Boolean? = null
    private var isFABOpen: Boolean = false
    private var scrollValue = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantWebviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visible()

        viewModel.getConference(arguments.id)
        setupViews()

        initListeners()
        initObservers()
    }

    private fun setupViews() {
        binding.toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        viewModel.setLetterSize(DEFAULT_LETTER_SIZE)
        //binding.bookmarkArticle.setImageResource(getBookmarkDrawable(bookmarkStatus))
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            val backStack = findNavController().navigateUp()
            if (backStack.not()) {
                activity?.finish()
            }
        }

        binding.conferenceOptionsFab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        binding.fabOption3.setOnClickListener {
            when (getDarkModeEnabled()) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

        binding.fabOption2.setOnClickListener {
            if (viewModel.letterSize.value!! in MINIMAL_LETTER_SIZE..PRE_MAXIMAL_LETTER_SIZE) {
                viewModel.setLetterSize(viewModel.letterSize.value!! + LETTER_UNIT)
            }
        }

        binding.fabOption1.setOnClickListener {
            if (viewModel.letterSize.value!! in PRE_MINIMAL_LETTER_SIZE..MAXIMAL_LETTER_SIZE ) {
                viewModel.setLetterSize(viewModel.letterSize.value!! - LETTER_UNIT)
            }
        }
    }

    private fun initObservers() = with(binding) {
        viewModel.conference.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    progressBar.isVisible = true
                }
                is Resource.Success -> {
                    val conference = result.data

                    conference?.let {
                        titleTxt.text = conference.title
                        dateTxt.text = conference.nicedate
                        locationTxt.text = conference.location
                        extrasTxt.text = getString(R.string.extras, conference.activity, conference.duration)
                        thumbnailImg.loadImageFromURL(conference.thumbnail)

                        if (!conference.body.isNullOrEmpty()) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                bodyTxt.text = Html.fromHtml(conference.body, Html.FROM_HTML_MODE_LEGACY)
                            } else {
                                bodyTxt.text = Html.fromHtml(conference.body)
                            }
                        }
                        initScrollListeners(conference)
                    }

                    progressBar.gone()
                }
                is Resource.Error -> {
                    progressBar.gone()
                }
            }
        }
        viewModel.letterSize.observe(viewLifecycleOwner) {
            when (it) {
                1 -> binding.bodyTxt.textSize = pxFromDp(resources.getDimension(R.dimen._8ssp))
                2 -> binding.bodyTxt.textSize = pxFromDp(resources.getDimension(R.dimen._12ssp))
                3 -> binding.bodyTxt.textSize = pxFromDp(resources.getDimension(R.dimen._16ssp))
                4 -> binding.bodyTxt.textSize = pxFromDp(resources.getDimension(R.dimen._20ssp))
                5 -> binding.bodyTxt.textSize = pxFromDp(resources.getDimension(R.dimen._24ssp))
            }
        }
    }

    private fun initScrollListeners(conference: Conference) {
        val nestedScrollView: NestedScrollView =
            this.view?.findViewById(R.id.conferenceFragmentNestedScroll) as NestedScrollView

        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY && scrollY >= binding.thumbnailImg.height + OFFSET) {
                    scrollValue = scrollY
                    scrollingStatus = true
                    if(isFABOpen){
                        binding.fabOption4.apply {
                            animate().translationY(0F).alpha(1.0f).duration = 300
                        }
                    }
                    binding.toolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.toolbarTitleTxt.visibility = View.VISIBLE
                    binding.toolbarTitleTxt.text = conference.title
                }
                if (scrollY <= binding.thumbnailImg.height + OFFSET) {
                    scrollValue = scrollY
                    scrollingStatus = false
                    if(isFABOpen){
                        binding.fabOption4.apply {
                            animate().translationY(this.height.toFloat()).alpha(0.0f).duration = 300
                        }
                    }
                    val params = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    params.addRule(RelativeLayout.CENTER_VERTICAL)
                    nestedScrollView.layoutParams = params
                    binding.toolbarTitleTxt.visibility = View.GONE
                }
            }
        )

        binding.fabOption4.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }
    }

    private fun showFABMenu() = with(binding) {
        isFABOpen = true
        if (scrollValue > 0) {
            fabOption4.animate().translationY(0F).alpha(1.0f).duration = 300
        }
        fabOption1.animate().translationY(0F).alpha(1.0f).duration = 300
        fabOption2.animate().translationY(0F).alpha(1.0f).duration = 300
        fabOption3.animate().translationY(0F).alpha(1.0f).duration = 300
    }

    private fun closeFABMenu() = with(binding) {
        isFABOpen = false
        if (scrollValue > 0) {
            fabOption4.animate().translationY(fabOption4.height.toFloat()).alpha(0.0f).duration = 300
        }
        fabOption1.animate().translationY(fabOption1.height.toFloat()).alpha(0.0f).duration = 300
        fabOption2.animate().translationY(fabOption2.height.toFloat()).alpha(0.0f).duration = 300
        fabOption3.animate().translationY(fabOption3.height.toFloat()).alpha(0.0f).duration = 300
    }

    private fun getDarkModeEnabled(): Boolean {
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> darkModeEnabled = true
            Configuration.UI_MODE_NIGHT_NO -> darkModeEnabled = false
        }

        return darkModeEnabled!!
    }

    private fun openPDF(url: String) = with(binding) {
        val intent = Intent()
        intent.setDataAndType(Uri.parse(url), "application/pdf")
        startActivity(intent)
    }

    private fun pxFromDp(px: Float): Float {
        val scaledDensity: Float = requireContext().resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }

    companion object {
        private const val OFFSET = 25
        private const val LETTER_UNIT = 1
        private const val DEFAULT_LETTER_SIZE = 3
        private const val MINIMAL_LETTER_SIZE = 1
        private const val PRE_MINIMAL_LETTER_SIZE = 2
        private const val PRE_MAXIMAL_LETTER_SIZE = 4
        private const val MAXIMAL_LETTER_SIZE = 5
    }
}