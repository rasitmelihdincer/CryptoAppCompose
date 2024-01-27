package com.example.di

import com.example.remote.CryptoApi
import com.example.repo.CryptoRepository
import com.example.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCryptoRepo(
        api : CryptoApi
    ) = CryptoRepository(api)

    @Singleton
    @Provides
    fun provideCryptoApi() : CryptoApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(CryptoApi::class.java)
    }

}