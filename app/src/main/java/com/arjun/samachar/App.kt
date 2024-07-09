package com.arjun.samachar

import android.app.Application
import com.arjun.samachar.di.component.ApplicationComponent
import com.arjun.samachar.di.component.DaggerApplicationComponent
import com.arjun.samachar.di.module.ApplicationModule

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this@App))
            .build()
        applicationComponent.inject(this@App)
    }

}