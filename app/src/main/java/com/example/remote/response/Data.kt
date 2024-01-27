package com.example.remote.response

data class Data(
    val market_cap: Double,
    val market_cap_btc: Double,
    val market_cap_change_percentage_24h: MarketCapChangePercentage24h,
    val sparkline: String,
    val total_volume: Double,
    val total_volume_btc: Double
)