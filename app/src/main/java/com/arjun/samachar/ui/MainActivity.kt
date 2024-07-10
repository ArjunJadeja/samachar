package com.arjun.samachar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arjun.samachar.App
import com.arjun.samachar.di.component.DaggerActivityComponent
import com.arjun.samachar.di.module.ActivityModule
import com.arjun.samachar.ui.base.AppNavHost
import com.arjun.samachar.ui.filters.country.CountriesViewModel
import com.arjun.samachar.ui.headlines.home.HomeViewModel
import com.arjun.samachar.ui.filters.language.LanguageViewModel
import com.arjun.samachar.ui.headlines.search.SearchViewModel
import com.arjun.samachar.ui.filters.source.SourcesViewModel
import com.arjun.samachar.ui.theme.AppTheme
import com.arjun.samachar.utils.network.NetworkConnected
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var languageViewModel: LanguageViewModel

    @Inject
    lateinit var countriesViewModel: CountriesViewModel

    @Inject
    lateinit var sourcesViewModel: SourcesViewModel

    @Inject
    lateinit var networkConnected: NetworkConnected

    @Inject
    lateinit var customTabsIntent: CustomTabsIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        installSplashScreen()
        observeNetworkChanges()
        setContent {
            AppTheme {
                AppNavHost(
                    mainViewModel = mainViewModel,
                    homeViewModel = homeViewModel,
                    searchViewModel = searchViewModel,
                    languageViewModel = languageViewModel,
                    countriesViewModel = countriesViewModel,
                    sourcesViewModel = sourcesViewModel,
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

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as App).applicationComponent)
            .activityModule(ActivityModule(this@MainActivity))
            .build()
            .inject(this@MainActivity)
    }

}