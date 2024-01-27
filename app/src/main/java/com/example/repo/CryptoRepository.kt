package com.example.repo

import com.example.remote.CryptoApi
import com.example.remote.model.SearchCoin
import com.example.remote.response.TrendingCoin
import com.example.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CryptoRepository @Inject constructor(
   private val api : CryptoApi
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
}