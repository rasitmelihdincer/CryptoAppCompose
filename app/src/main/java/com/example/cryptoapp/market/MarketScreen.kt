package com.example.cryptoapp.market

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cryptoapp.R
import com.example.remote.UserWallet
import com.example.remote.model.Coin2
import com.example.remote.response.Coin

@Composable
fun MarketScreen(
    navController: NavController,
    viewModel: MarketScreenViewModel = hiltViewModel()
){
    var isSearching by remember { mutableStateOf(false) }
    Surface(
        color = Color(0xFF000000),
        modifier = Modifier.fillMaxSize()
    ) {
        Column{
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

                Icon(
                    painter = painterResource(id = R.drawable.baseline_wallet_24),
                    contentDescription = "walletIcon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            navController.navigate("wallet_screen")
                        }
                    )
            }

            SearchBar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    ,
                hint = "Search coins",
            ){
                viewModel.getSearchedCoin(it)
                isSearching = it.isNotEmpty()
            }
            if (!isSearching){
                Test()
            }
            CryptoList()

        }

    }

}
@Composable
fun Test(){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = "Trending Coins",
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp),
            textDecoration = TextDecoration.Underline
        )
        Text(
            text = "Price(USD)",
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(end = 16.dp),
            textDecoration = TextDecoration.Underline
        )
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
                     onSearch("")
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

    val showDialog =  remember { mutableStateOf(false) }
    var glideImage by remember {
        mutableStateOf("")
    }
    var coinName by remember {
        mutableStateOf("")
    }
    if(showDialog.value)
        CustomDialog(value = "", image = glideImage, coinName = coinName, setShowDialog = {
            showDialog.value = it
        },) {
            Log.i("MarketScreen","MarketScreen : $it")
        }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(4f)
            .fillMaxWidth()
            .clickable {
                showDialog.value = true
            }

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            glideImage = coin.item.large
            coinName = coin.item.name
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CustomDialog(
    value: String,
    image : String,
    coinName : String,
    viewModel: MarketScreenViewModel = hiltViewModel(),
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit,
) {
    val txtFieldError by remember { mutableStateOf("") }
    var txtField by remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Black,
            modifier = Modifier.padding(16.dp),
            shadowElevation = 8.dp,
            tonalElevation = 8.dp
            ) {
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    GlideImage(model = image , contentDescription = "" ,Modifier.clip(CircleShape))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = coinName, color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = txtField, onValueChange ={
                            txtField = it
                        },
                        placeholder = { Text(text = "Enter value") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .aspectRatio(4f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateOrAddWallet(coinName,txtField.toInt(),image)
                                setShowDialog(false)
                                      },
                            colors = ButtonDefaults.buttonColors(Color.Green),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Buy",color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                viewModel.deleteWallet(coinName,txtField.toInt(),image)
                                setShowDialog(false)
                                      } ,
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Sell",color = Color.White)
                        }
                    }

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


