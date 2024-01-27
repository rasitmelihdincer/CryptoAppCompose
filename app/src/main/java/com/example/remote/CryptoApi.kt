package com.example.remote

import com.example.remote.model.SearchCoin
import com.example.remote.response.TrendingCoin
import retrofit2.http.GET
import retrofit2.http.Query


interface CryptoApi {
    @GET("search/trending")
    suspend fun getTrendsCoin() : TrendingCoin

    @GET("search")
    suspend fun getSearchCoin(@Query("query") query : String) : SearchCoin
}