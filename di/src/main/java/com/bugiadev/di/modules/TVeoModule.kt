package com.bugiadev.di.modules

import com.bugiadev.data.datasource.TVeoDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
object TVeoModule {
    @JvmStatic
    @Provides
    fun providesTVeoDataSource(@Named("commonRetrofit") retrofit: Retrofit): TVeoDataSource =
        retrofit.create(TVeoDataSource::class.java)
}