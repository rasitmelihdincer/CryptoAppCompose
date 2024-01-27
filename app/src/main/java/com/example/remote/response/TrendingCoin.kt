package com.example.remote.response

data class TrendingCoin(
    val categories: List<Category>? = null,
    var coins: List<Coin>? = null,
    val nfts: List<Nft>? = null
)