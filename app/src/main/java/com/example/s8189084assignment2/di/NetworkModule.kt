package com.example.s8189084assignment2.di

import com.example.s8189084assignment2.data.remote.ApiService
import com.example.s8189084assignment2.data.repository.SportsRepository
import com.example.s8189084assignment2.data.repository.SportsRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://nit3213apinew.onrender.com/"

// Provides Moshi, Retrofit, and ApiService, and binds SportsRepository to its implementation.
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    // Whenever something asks for a SportsRepository, give it a SportsRepositoryImpl.
    @Binds
    @Singleton
    abstract fun bindSportsRepository(
        impl: SportsRepositoryImpl
    ): SportsRepository

    companion object {

        // Converts JSON to Kotlin objects and back, using reflection.
        @Provides
        @Singleton
        fun provideMoshi(): Moshi {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        // Retrofit is the HTTP client, it uses Moshi to convert every request and response.
        @Provides
        @Singleton
        fun provideRetrofit(moshi: Moshi): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        // Retrofit reads the ApiService interface and builds a real implementation of it.
        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
