package com.bugiadev.presentation

import com.bugiadev.di.ApplicationComponent
import com.bugiadev.di.annotations.ActivityScope
import com.bugiadev.presentation.viewmodel.SplashViewModel
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class]
)
interface TVeoComponent {
    val splashViewModel: SplashViewModel
    // val marvelDetailViewModel: MarvelDetailViewModel

    // fun inject(fragment: ListFragment)
    // fun inject(fragment: DetailFragment)

    @Component.Factory
    interface Factory {
        fun create(
            applicationComponent: ApplicationComponent
        ): TVeoComponent
    }
}

interface TVeoInjection {
    fun getTVeoComponent(): TVeoComponent
}