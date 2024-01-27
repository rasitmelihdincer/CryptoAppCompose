package com.example.remote.response

data class DataX(
    val content: Content,
    val market_cap: String,
    val market_cap_btc: String,
    val price: String,
    val price_btc: String,
    val price_change_percentage_24h: PriceChangePercentage24h,
    val sparkline: String,
    val total_volume: String,
    val total_volume_btc: String
)