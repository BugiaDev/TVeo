package com.bugiadev.tveo

import android.app.Application
import com.bugiadev.di.ApplicationComponent
import com.bugiadev.di.DaggerApplicationComponent
import com.bugiadev.presentation.ApplicationComponentProvider
import com.bugiadev.presentation.DaggerTVeoComponent
import com.bugiadev.presentation.TVeoComponent

class TVeoApp: Application(), ApplicationComponentProvider {
    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var tVeoComponent: TVeoComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
        tVeoComponent = DaggerTVeoComponent.factory().create(applicationComponent)
    }

    override fun provideApplicationComponent(): ApplicationComponent = applicationComponent
}