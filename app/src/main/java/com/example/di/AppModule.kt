package com.example.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptoapp.local.UserWalletDao
import com.example.cryptoapp.local.UserWalletDatabase
import com.example.remote.CryptoApi
import com.example.repo.CryptoRepository
import com.example.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        api : CryptoApi,
        dao : UserWalletDao
    ) = CryptoRepository(api,dao)

    @Singleton
    @Provides
    fun provideCryptoApi() : CryptoApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(CryptoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) : UserWalletDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            UserWalletDatabase::class.java, "user_database"
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserWalletDao(userWalletDatabase : UserWalletDatabase) : UserWalletDao{
        return userWalletDatabase.userWalletDao()
    }

}