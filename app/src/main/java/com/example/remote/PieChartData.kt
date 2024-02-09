package com.example.remote

import androidx.compose.ui.graphics.Color
import com.example.remote.response.Coin
import com.example.remote.response.İtem

data class PieChartData(
    val data:List<Coin>? = null,
    val color: Color? = null,
    val partName:String? = null
)
