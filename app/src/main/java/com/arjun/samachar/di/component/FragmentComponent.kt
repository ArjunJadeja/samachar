package com.arjun.samachar.di.component

import com.arjun.samachar.di.FragmentScope
import com.arjun.samachar.di.module.FragmentModule
import com.arjun.samachar.ui.home.HomeFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

}