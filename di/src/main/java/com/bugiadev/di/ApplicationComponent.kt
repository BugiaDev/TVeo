package com.bugiadev.di

import android.content.Context
import com.bugiadev.di.annotations.ApplicationScope
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.di.modules.TVeoModule
import com.bugiadev.di.modules.NetworkModule
import com.bugiadev.di.modules.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named

@ApplicationScope
@Component(modules = [NetworkModule::class, RepositoryModule::class, TVeoModule::class])
interface ApplicationComponent {
    val okHttpClient: OkHttpClient
    val converter: Converter.Factory
    val adapter: CallAdapter.Factory
    val context: Context
    val tVeoRepository: TVeoRepository

    @Named("commonRetrofit")
    fun commonRetrofit(): Retrofit

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): ApplicationComponent
    }
}