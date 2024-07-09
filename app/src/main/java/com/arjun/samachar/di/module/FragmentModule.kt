package com.arjun.samachar.di.module

import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.di.FragmentScope
import com.arjun.samachar.ui.base.ViewModelProviderFactory
import com.arjun.samachar.ui.country.CountriesAdapter
import com.arjun.samachar.ui.country.CountriesBottomSheet
import com.arjun.samachar.ui.country.CountriesViewModel
import com.arjun.samachar.ui.home.HeadlinesAdapter
import com.arjun.samachar.ui.home.HomeViewModel
import com.arjun.samachar.ui.language.LanguageViewModel
import com.arjun.samachar.ui.language.LanguagesAdapter
import com.arjun.samachar.ui.language.LanguagesBottomSheet
import com.arjun.samachar.ui.search.SearchViewModel
import com.arjun.samachar.ui.source.SourcesAdapter
import com.arjun.samachar.ui.source.SourcesBottomSheet
import com.arjun.samachar.ui.source.SourcesViewModel
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
    fun provideSourcesViewModel(mainRepository: MainRepository): SourcesViewModel {
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(SourcesViewModel::class) {
                SourcesViewModel(mainRepository)
            })[SourcesViewModel::class.java]
    }

    @FragmentScope
    @Provides
    fun provideLanguagesViewModel(mainRepository: MainRepository): LanguageViewModel {
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(LanguageViewModel::class) {
                LanguageViewModel(mainRepository)
            })[LanguageViewModel::class.java]
    }

    @FragmentScope
    @Provides
    fun provideCountriesViewModel(mainRepository: MainRepository): CountriesViewModel {
        return ViewModelProvider(fragment,
            ViewModelProviderFactory(CountriesViewModel::class) {
                CountriesViewModel(mainRepository)
            })[CountriesViewModel::class.java]
    }

    @FragmentScope
    @Provides
    fun providesHeadlinesAdapter(): HeadlinesAdapter {
        return HeadlinesAdapter(headlineList = ArrayList(), onHeadlineClicked = {})
    }

    @FragmentScope
    @Provides
    fun providesSourcesAdapter(): SourcesAdapter {
        return SourcesAdapter(sources = ArrayList(), onSourceSelected = {})
    }

    @FragmentScope
    @Provides
    fun providesLanguagesAdapter(): LanguagesAdapter {
        return LanguagesAdapter(
            languages = ArrayList(),
            selectedLanguageCode = "",
            onLanguageSelected = {})
    }

    @FragmentScope
    @Provides
    fun providesCountriesAdapter(): CountriesAdapter {
        return CountriesAdapter(countries = ArrayList(), onCountrySelected = {})
    }

    @FragmentScope
    @Provides
    fun providesSourcesBottomSheet(): SourcesBottomSheet {
        return SourcesBottomSheet()
    }

    @FragmentScope
    @Provides
    fun providesLanguagesBottomSheet(): LanguagesBottomSheet {
        return LanguagesBottomSheet()
    }

    @FragmentScope
    @Provides
    fun providesCountriesBottomSheet(): CountriesBottomSheet {
        return CountriesBottomSheet()
    }

}