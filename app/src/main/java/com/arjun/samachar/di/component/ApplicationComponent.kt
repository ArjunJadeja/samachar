package com.arjun.samachar.di.component

import com.arjun.samachar.App
import com.arjun.samachar.data.api.NetworkService
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.di.module.ApplicationModule
import com.arjun.samachar.utils.network.NetworkConnected
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: App)

    fun getNetworkService(): NetworkService

    fun getMainRepository(): MainRepository

    fun getNetworkConnected(): NetworkConnected

}