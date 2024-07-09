package com.arjun.samachar.di.module

import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.di.FragmentScope
import com.arjun.samachar.ui.base.ViewModelProviderFactory
import com.arjun.samachar.ui.home.HeadlinesAdapter
import com.arjun.samachar.ui.home.HomeViewModel
import com.arjun.samachar.ui.search.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @FragmentScope
    @Provides
    fun providesCustomChromeTabsIntent(): CustomTabsIntent {
        val builder = CustomTabsIntent.Builder()
        return builder.build()
    }

    @FragmentScope
    @Provides
    fun provideHomeViewModel(mainRepository: MainRepository): HomeViewModel {
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(mainRepository)
            })[HomeViewModel::class.java]
    }

    @FragmentScope
    @Provides
    fun provideSearchViewModel(mainRepository: MainRepository): SearchViewModel {
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(SearchViewModel::class) {
                SearchViewModel(mainRepository)
            })[SearchViewModel::class.java]
    }

    @FragmentScope
    @Provides
    fun providesHeadlinesAdapter(): HeadlinesAdapter {
        return HeadlinesAdapter(headlineList = ArrayList(), onHeadlineClicked = {})
    }

}