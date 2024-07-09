package com.arjun.samachar.di.component

import com.arjun.samachar.di.ActivityScope
import com.arjun.samachar.di.module.ActivityModule
import com.arjun.samachar.ui.MainActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MainActivity)

}