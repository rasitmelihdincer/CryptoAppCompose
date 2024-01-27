package com.example.remote.response

data class Category(
    val coins_count: Int,
    val `data`: Data,
    val id: Int,
    val market_cap_1h_change: Double,
    val name: String,
    val slug: String
)