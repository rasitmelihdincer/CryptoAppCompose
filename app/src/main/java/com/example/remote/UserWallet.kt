package com.example.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity
data class UserWallet(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var coinName : String? = null,
    val coinCount : Int = 0,
    val coinImage : String? = null
)
