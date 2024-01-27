package com.example.remote.response

data class Nft(
    val `data`: DataXX,
    val floor_price_24h_percentage_change: Double,
    val floor_price_in_native_currency: Double,
    val id: String,
    val name: String,
    val native_currency_symbol: String,
    val nft_contract_id: Int,
    val symbol: String,
    val thumb: String
)