package com.example.remote.model

import com.example.remote.response.Coin
import com.example.remote.response.İtem

data class SearchCoin(
    val categories: List<Category>? = null,
    val coins: List<Coin2>? = null,
    val exchanges: List<Exchange>? = null,
    val icos: List<Any>? = null,
    val nfts: List<Nft>? = null,
)