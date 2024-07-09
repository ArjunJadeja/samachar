package com.arjun.samachar.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arjun.samachar.di.ActivityScope
import com.arjun.samachar.ui.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope
    @Provides
    fun provideMainViewModel(): MainViewModel {
        return ViewModelProvider(activity)[MainViewModel::class.java]
    }

}