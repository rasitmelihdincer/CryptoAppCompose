package com.example.cryptoapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.remote.UserWallet


@Database(
    entities = [UserWallet::class],
    version = 3
)

 abstract class UserWalletDatabase : RoomDatabase() {
    abstract fun userWalletDao() : UserWalletDao
}