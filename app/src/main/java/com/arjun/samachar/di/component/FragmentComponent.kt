package com.arjun.samachar.di.component

import com.arjun.samachar.di.FragmentScope
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.country.CountriesBottomSheet
import com.arjun.samachar.ui.home.HomeFragment
import com.arjun.samachar.ui.language.LanguagesBottomSheet
import com.arjun.samachar.ui.search.SearchFragment
import com.arjun.samachar.ui.source.SourcesBottomSheet
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: SearchFragment)

    fun inject(bottomSheet: LanguagesBottomSheet)

    fun inject(bottomSheet: CountriesBottomSheet)

    fun inject(bottomSheet: SourcesBottomSheet)

}