package com.example.repo

import androidx.lifecycle.LiveData
import com.example.cryptoapp.local.UserWalletDao
import com.example.cryptoapp.local.UserWalletDatabase
import com.example.remote.CryptoApi
import com.example.remote.UserWallet
import com.example.remote.model.SearchCoin
import com.example.remote.response.TrendingCoin
import com.example.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CryptoRepository @Inject constructor(
   private val api : CryptoApi,
    private val dao : UserWalletDao
) {

        suspend fun getTrendsCoins() : Resource<TrendingCoin>{
            val response =
                try {
                    api.getTrendsCoin()
                } catch (e : Exception){
                    return Resource.Error("errorr")
                }
            return Resource.Success(response)
        }

    suspend fun getSearchCoin (query : String) : Resource<SearchCoin>{
            val response = try {
                api.getSearchCoin(query)
            } catch (e : Exception){
                return Resource.Error("error something")
            }
        return Resource.Success(response)
    }

     fun getUserWallet() : LiveData<List<UserWallet>>{
       return dao.getAllWallets()
    }
    suspend fun updateOrInsertWallet(coinName: String,coinCountChange: Int,coinImage : String){
        val wallet = dao.findWalletByCoinNameAndCount(coinName)
        if (wallet != null){
            val newCount = wallet.coinCount + coinCountChange
            if (newCount > 0){
                dao.insertWallet(wallet.copy(coinCount = newCount))
            } else {
                dao.deleteWallet(wallet)
            }
        } else if (coinCountChange > 0){
                dao.insertWallet(UserWallet(coinName = coinName, coinCount = coinCountChange, coinImage = coinImage))
        }
    }
}