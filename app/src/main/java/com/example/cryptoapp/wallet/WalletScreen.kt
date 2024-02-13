package com.example.cryptoapp.wallet

import android.media.Image
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cryptoapp.R
import com.example.cryptoapp.market.MarketScreenViewModel
import com.example.remote.UserWallet
import kotlin.random.Random


@Composable
fun WalletScreen(
    navController: NavController,
    viewModel: WalletScreenViewModel = hiltViewModel()
){

    Surface (
        color = Color(0xFF000000),
        modifier =  Modifier.fillMaxSize()
    ){
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBack, contentDescription = "backIcon",
                    tint = Color.White,
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                        .padding(start = 16.dp)
                        .size(28.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Wallet",
                    style = TextStyle(
                        fontSize = 28.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFEEEEEE),
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer( Modifier.weight(1f))
            }
            val userWallet by viewModel.userWallet.observeAsState(initial = emptyList())
            val data = userWallet.sortedBy {
                it.coinName?.length
            }.associate { Pair(it.coinName,it.coinCount) }
            println(userWallet)
            Spacer(modifier = Modifier.height(50.dp))
            PieChart(
                data = data,
            )
            WalletList()
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WalletItem(
    userWallet: UserWallet
){
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(5f)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(
                model = userWallet.coinImage, contentDescription ="", Modifier.clip(
                CircleShape).size(60.dp))
                Text(
                    text = userWallet.coinName.toString(),
                    fontWeight = FontWeight(500),
                    style = TextStyle(Color(0xFFEEEEEE), fontSize = 24.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = userWallet.coinCount.toString(),
                    style = TextStyle(Color(0xFFEEEEEE), fontSize = 24.sp),
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .align(Alignment.CenterVertically)
                )

        }
    }
}
@Composable
fun WalletList(
    viewModel: WalletScreenViewModel = hiltViewModel()
){
    val userWallet by viewModel.userWallet.observeAsState(initial = emptyList())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(contentPadding = PaddingValues(16.dp)){
            items(userWallet.size){
                WalletItem(userWallet = userWallet[it])
            }
        }
    }

}

@Composable
fun PieChart(
    data: Map<String?, Int>,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    // For a detailed explanation check out the Medium Article.
    // The link is in the about section and readme file of this GitHub Repository
    data.values.forEachIndexed { index, values ->
      floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    // add the colors as per the number of data(no. of pie chart entries)
    // so that each data will get a color
    val colors = List(data.size) {
        Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
    }

    var animationPlayed by remember {
        mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pie Chart using Canvas Arc
        Box(
            modifier =  Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
            DetailsPieChart(
                data = data,
                colors = colors
            )

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsPieChart(
    data: Map<String?, Int>,
    colors: List<Color>
) {
    FlowRow(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 3,


    ) {
        // create the data items
        data.values.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index],
            )
        }

    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String?, Int?>,
    color: Color
) {
    Surface(
        modifier =  Modifier
            .padding(horizontal = 8.dp),
        color = Color.Transparent
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(12.dp)
            )
                data.first?.let {
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = it,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                /*
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.second.toString(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Gray
                )

                 */


        }

    }

}
