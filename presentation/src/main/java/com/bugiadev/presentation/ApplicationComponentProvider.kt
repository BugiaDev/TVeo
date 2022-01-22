package com.bugiadev.presentation

import com.bugiadev.di.ApplicationComponent

interface ApplicationComponentProvider {
    fun provideApplicationComponent(): ApplicationComponent
}