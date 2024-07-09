package com.arjun.samachar.di.component

import com.arjun.samachar.di.FragmentScope
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.home.HomeFragment
import com.arjun.samachar.ui.search.SearchFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: SearchFragment)

}