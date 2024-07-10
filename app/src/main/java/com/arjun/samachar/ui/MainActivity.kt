package com.arjun.samachar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arjun.samachar.ui.base.AppNavHost
import com.arjun.samachar.ui.filters.country.CountriesViewModel
import com.arjun.samachar.ui.headlines.home.HomeViewModel
import com.arjun.samachar.ui.filters.language.LanguageViewModel
import com.arjun.samachar.ui.headlines.search.SearchViewModel
import com.arjun.samachar.ui.filters.source.SourcesViewModel
import com.arjun.samachar.ui.theme.AppTheme
import com.arjun.samachar.utils.network.NetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkConnected: NetworkConnected

    @Inject
    lateinit var customTabsIntent: CustomTabsIntent

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        observeNetworkChanges()
        setContent {
            AppTheme {
                AppNavHost(
                    mainViewModel = mainViewModel,
                    customTabsIntent = customTabsIntent
                )
            }
        }
    }

    private fun observeNetworkChanges() {
        networkConnected.observe(this@MainActivity) {
            mainViewModel.updateNetworkStatus(it)
        }
    }

}