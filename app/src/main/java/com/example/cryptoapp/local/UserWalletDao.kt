package com.example.cryptoapp.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.remote.UserWallet


@Dao
interface UserWalletDao {

    @Query("SELECT * FROM UserWallet")
    fun getAllWallets() : LiveData<List<UserWallet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(userWallet: UserWallet)

    @Query("SELECT * FROM UserWallet WHERE coinName = :coinName LIMIT 1")
    suspend fun findWalletByCoinNameAndCount(coinName: String): UserWallet?

    @Delete
    suspend fun deleteWallet(userWallet: UserWallet)

}