package com.bugiadev.di.modules

import com.bugiadev.data.repository.TVeoRepositoryImpl
import com.bugiadev.di.annotations.ApplicationScope
import com.bugiadev.domain.repository.TVeoRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @ApplicationScope
    @Binds
    abstract fun bindsTVeoRepository(implementation: TVeoRepositoryImpl): TVeoRepository
}