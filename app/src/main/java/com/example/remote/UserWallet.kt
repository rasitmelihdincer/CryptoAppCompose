package com.example.remote

import android.os.Parcelable
import androidx.navigation.NavType
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize
import java.io.Serializable
data class UserWallet(
    var coinName : String? = null,
    val coinCount : String? = null,
)
