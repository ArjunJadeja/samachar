package com.arjun.samachar.di.module

import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.arjun.samachar.di.FragmentScope
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

}