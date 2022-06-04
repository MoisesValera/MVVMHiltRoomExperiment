package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mevalera.mvvmhiltroomexperiment.R
import com.mevalera.mvvmhiltroomexperiment.databinding.FragmentRestaurantWebviewBinding
import com.mevalera.mvvmhiltroomexperiment.util.gone
import com.mevalera.mvvmhiltroomexperiment.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantWebViewFragment : Fragment(R.layout.fragment_restaurant_webview) {

    private lateinit var binding: FragmentRestaurantWebviewBinding
    private val arguments: RestaurantWebViewFragmentArgs by navArgs()

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

        initListeners()
        initWebView()
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    }

    private fun initWebView() = with(binding) {
        progressBar.visible()
        val webView = iapWebView
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(arguments.url)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.gone()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                progressBar.gone()
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            val backStack = findNavController().navigateUp()
            if (backStack.not()) {
                activity?.finish()
            }
        }
    }
}