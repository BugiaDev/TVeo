package com.bugiadev.presentation

import android.content.SharedPreferences
import com.bugiadev.di.ApplicationComponent
import com.bugiadev.di.annotations.ActivityScope
import com.bugiadev.presentation.fragment.DetailFragment
import com.bugiadev.presentation.fragment.ListFragment
import com.bugiadev.presentation.viewmodel.SplashViewModel
import com.bugiadev.presentation.viewmodel.TVeoDetailViewModel
import com.bugiadev.presentation.viewmodel.TVeoListViewModel
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class]
)
interface TVeoComponent {
    val splashViewModel: SplashViewModel
    val tVeoListViewModel: TVeoListViewModel
    val tVeoDetailViewModel: TVeoDetailViewModel

    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)

    @Component.Factory
    interface Factory {
        fun create(
            applicationComponent: ApplicationComponent
        ): TVeoComponent
    }
}

interface TVeoInjection {
    fun getTVeoComponent(): TVeoComponent
    fun getSharedPreferences(): SharedPreferences
}