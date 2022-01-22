package com.bugiadev.di.modules

import com.bugiadev.data.network.TVeoRXCallAdapterFactory
import com.bugiadev.di.BuildConfig
import com.bugiadev.di.annotations.ApplicationScope
import com.bugiadev.di.utils.delegatingCallFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {
    @JvmStatic
    @Provides
    @Named("baseCommonUrl")
    fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @ApplicationScope
    @JvmStatic
    @Provides
    fun providesHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(30L, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val newUrl = chain.request().url
                    .newBuilder()
                    .build()

                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }

        }.build()
    }

    @ApplicationScope
    @JvmStatic
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return MoshiConverterFactory.create()
    }

    @ApplicationScope
    @JvmStatic
    @Provides
    fun providesCallAdapterFactory(): CallAdapter.Factory {
        return TVeoRXCallAdapterFactory.create()
    }

    @ApplicationScope
    @JvmStatic
    @Provides
    @Named("commonRetrofit")
    fun providesRetrofit(
        @Named("baseCommonUrl") baseUrl: String,
        httpClient: dagger.Lazy<OkHttpClient>,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .delegatingCallFactory(httpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()
    }
}