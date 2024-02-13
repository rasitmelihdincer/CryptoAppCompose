package com.example.cryptoapp.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.remote.UserWallet
import com.example.repo.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletScreenViewModel @Inject constructor(
    repository: CryptoRepository
) : ViewModel() {

    val userWallet : LiveData<List<UserWallet>> = repository.getUserWallet()
}