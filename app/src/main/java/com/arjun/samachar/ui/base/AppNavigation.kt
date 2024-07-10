package com.arjun.samachar.ui.base

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.filters.country.CountriesViewModel
import com.arjun.samachar.ui.headlines.home.HomeScreen
import com.arjun.samachar.ui.headlines.home.HomeViewModel
import com.arjun.samachar.ui.filters.language.LanguageViewModel
import com.arjun.samachar.ui.headlines.search.SearchScreen
import com.arjun.samachar.ui.headlines.search.SearchViewModel
import com.arjun.samachar.ui.filters.source.SourcesViewModel

sealed class Route(val name: String) {
    data object HomeScreen : Route(name = "home_screen")
    data object SearchScreen : Route(name = "search_screen")
}

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    languageViewModel: LanguageViewModel,
    countriesViewModel: CountriesViewModel,
    sourcesViewModel: SourcesViewModel,
    customTabsIntent: CustomTabsIntent
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen.name
    ) {
        composable(route = Route.HomeScreen.name) {
            HomeScreen(
                context = context,
                navController = navController,
                mainViewModel = mainViewModel,
                homeViewModel = homeViewModel,
                languageViewModel = languageViewModel,
                countriesViewModel = countriesViewModel,
                sourcesViewModel = sourcesViewModel
            ) {
                openCustomChromeTab(
                    customTabsIntent = customTabsIntent,
                    context = context,
                    url = it
                )
            }
        }
        composable(route = Route.SearchScreen.name) {
            SearchScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                searchViewModel = searchViewModel
            ) {
                openCustomChromeTab(
                    customTabsIntent = customTabsIntent,
                    context = context,
                    url = it
                )
            }
        }
    }
}

fun openCustomChromeTab(customTabsIntent: CustomTabsIntent, context: Context, url: String) {
    customTabsIntent.launchUrl(context, Uri.parse(url))
}