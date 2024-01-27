package com.example.cryptoapp.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cryptoapp.R
import com.example.remote.model.Coin2
import com.example.remote.response.Coin

@Composable
fun MarketScreen(
    viewModel: MarketScreenViewModel = hiltViewModel()
){
    Surface(
        color = Color(0xFF000000),
        modifier = Modifier.fillMaxSize()
    ) {
        Column{
            Spacer(modifier = Modifier.height(50.dp))
             Text(
              text = "Markets",
               style = TextStyle(
                 fontSize = 28.sp,
                 lineHeight = 36.sp,
                 fontWeight = FontWeight(500),
                 color = Color(0xFFEEEEEE),
               ),
                 modifier = Modifier.padding(start = 16.dp)
              )
            SearchBar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    ,
                hint = "Search coins",
            ){
                viewModel.getSearchedCoin(it)
            }
            CryptoList()

        }

    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: MarketScreenViewModel = hiltViewModel(),
    hint : String = "",
    onSearch : (String) -> Unit = {},

){
     Box(
         modifier = modifier
     ) {
         var text by remember {
             mutableStateOf("")
         }
         var isFocused by remember {
             mutableStateOf(false)
         }

         BasicTextField(
             value = text,
             onValueChange = {
                 text = it
                 onSearch(it)
                 if (it.isNotEmpty()){
                   //  viewModel.isSearching.value = true
                 }
             },

             modifier = Modifier
                 .fillMaxWidth()
                 .background(color = Color(0xFF141414), RoundedCornerShape(8.dp))
                 .padding(16.dp)
                 .onFocusChanged { focusState ->
                     isFocused = focusState.isFocused
                     if (!isFocused && text.isEmpty()) {
                       //  viewModel.isSearching.value = true
                     }
                 }
                 ,
             textStyle = TextStyle(color = Color.White, fontSize = 22.sp),


         )
         if (isFocused || text.isNotEmpty()) {
             IconButton(
                 onClick = {
                     text = ""
                     isFocused = false
                     viewModel.isSearching.value = false
                     onSearch("") // Arama sonuçlarını temizle
                 },
                 modifier = Modifier.align(Alignment.CenterEnd)
             ) {
                 Icon(Icons.Default.Close, contentDescription = "Clear")
             }
         }
         if (!isFocused && text.isEmpty()){
             Icon(
                 painter = painterResource(id = R.drawable.search),
                 contentDescription = "Search",
                 modifier = Modifier
                     .align(Alignment.CenterStart)
                     .padding(start = 8.dp)
                     .size(32.dp)
             )
             Text(
                 text = hint,
                 color = Color(0xFF545454),
                 modifier = Modifier
                     .align(Alignment.CenterStart)
                     .padding(start = 50.dp)

             )
         }
     }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CryptoItem(
    coin: Coin,

) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(4f)

            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(model = coin.item.large, contentDescription = coin.item?.name , Modifier.clip(CircleShape))
            Column(
            modifier = Modifier.weight(2f)
            ){
                Text(
                    text = coin.item.symbol,
                    fontWeight = FontWeight(500),
                    style = TextStyle( Color(0xFFEEEEEE), fontSize = 24.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                    ,
                    )
                Text(
                    text = coin.item.name  ,
                    style = TextStyle(Color(0xFFAFAFAF)),
                    modifier = Modifier
                        .padding(start = 15.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val priceChange = coin.item.data.price_change_percentage_24h.usd.toString()
                val color = if (priceChange.startsWith("-")) Color.Red else Color.Green
                val formattedValue = priceChange.substringBefore('.') +
                        "." +
                        priceChange.substringAfter('.').first()

                Text(
                    text = coin.item.data.price ?: "",
                    style = TextStyle(color = Color.White, fontSize = 17.sp),
                )
                Text(
                    text = "${formattedValue} %" ?: "",
                    color = color,
                    fontSize = 17.sp,
                    maxLines = 1,
                )
            }

        }

    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchedItem(
    searchedList : Coin2
){
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(4f)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(
                model = searchedList.large, contentDescription = searchedList.name ,
                Modifier.clip(CircleShape))
            Column(
                modifier = Modifier.weight(2f)
            ){
                Text(
                    text = searchedList.symbol,
                    fontWeight = FontWeight(500),
                    style = TextStyle( Color(0xFFEEEEEE), fontSize = 24.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                    ,
                )
                Text(
                    text = searchedList.name  ,
                    style = TextStyle(Color(0xFFAFAFAF)),
                    modifier = Modifier
                        .padding(start = 15.dp)
                )
            }
        }
    }

}

@Composable
fun CryptoList(
    viewModel: MarketScreenViewModel = hiltViewModel()
) {
    val cryptoList by remember {
        viewModel.cryptoList
    }
    val searchedList  by remember {
        viewModel.searchedList
    }
    val isSearching by remember {
        viewModel.isSearching
    }
    val isLoading by remember {
        viewModel.isLoading
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            if (!isSearching && !isLoading) {
                items(cryptoList.coins?.size ?: 0) { index ->
                    cryptoList.coins?.get(index)?.let { coin ->
                        CryptoItem(coin = coin)
                    }
                }
            } else if (isSearching) {
                items(searchedList.coins?.size ?: 0) { index ->
                    searchedList.coins?.get(index)?.let { coin ->
                        SearchedItem(searchedList = coin)
                    }
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}


