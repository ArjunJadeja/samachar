package com.arjun.samachar.di.module

import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModelProvider
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.di.ActivityScope
import com.arjun.samachar.ui.MainViewModel
import com.arjun.samachar.ui.base.ViewModelProviderFactory
import com.arjun.samachar.ui.filters.country.CountriesViewModel
import com.arjun.samachar.ui.headlines.home.HomeViewModel
import com.arjun.samachar.ui.filters.language.LanguageViewModel
import com.arjun.samachar.ui.headlines.search.SearchViewModel
import com.arjun.samachar.ui.filters.source.SourcesViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: ComponentActivity) {

    @ActivityScope
    @Provides
    fun provideMainViewModel(): MainViewModel {
        return ViewModelProvider(activity)[MainViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideHomeViewModel(mainRepository: MainRepository): HomeViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(mainRepository)
            })[HomeViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideSearchViewModel(mainRepository: MainRepository): SearchViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(SearchViewModel::class) {
                SearchViewModel(mainRepository)
            })[SearchViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideSourcesViewModel(mainRepository: MainRepository): SourcesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(SourcesViewModel::class) {
                SourcesViewModel(mainRepository)
            })[SourcesViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideLanguagesViewModel(mainRepository: MainRepository): LanguageViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(LanguageViewModel::class) {
                LanguageViewModel(mainRepository)
            })[LanguageViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun provideCountriesViewModel(mainRepository: MainRepository): CountriesViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(CountriesViewModel::class) {
                CountriesViewModel(mainRepository)
            })[CountriesViewModel::class.java]
    }

    @ActivityScope
    @Provides
    fun providesCustomChromeTabsIntent(): CustomTabsIntent {
        val builder = CustomTabsIntent.Builder()
        return builder.build()
    }

}