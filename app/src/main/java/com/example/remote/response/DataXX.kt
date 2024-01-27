package com.example.remote.response

data class DataXX(
    val content: Any,
    val floor_price: String,
    val floor_price_in_usd_24h_percentage_change: String,
    val h24_average_sale_price: String,
    val h24_volume: String,
    val sparkline: String
)