package com.mevalera.mvvmhiltroomexperiment.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mevalera.mvvmhiltroomexperiment.R
import com.mevalera.mvvmhiltroomexperiment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav(binding.toolbarLayout)
    }

    private fun setupNav(toolbar: Toolbar) = with(binding) {
        val config = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, config)
    }

    override fun onSupportNavigateUp(): Boolean {
        val backStack = navController.popBackStack()
        if (backStack.not()) {
            finish()
        }
        return backStack
    }

    override fun onBackPressed() {
        if ((navController.navigateUp() || super.onSupportNavigateUp()).not()) {
            super.onBackPressed()
        }
    }
}