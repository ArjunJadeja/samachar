package com.arjun.samachar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arjun.samachar.App
import com.arjun.samachar.databinding.ActivityMainBinding
import com.arjun.samachar.di.component.DaggerActivityComponent
import com.arjun.samachar.di.module.ActivityModule
import com.arjun.samachar.utils.network.NetworkConnected
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var networkConnected: NetworkConnected

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeNetworkChanges()
    }

    private fun observeNetworkChanges() {
        networkConnected.observe(this@MainActivity) {
            viewModel.updateNetworkStatus(it)
        }
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as App).applicationComponent)
            .activityModule(ActivityModule(this@MainActivity))
            .build()
            .inject(this@MainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}