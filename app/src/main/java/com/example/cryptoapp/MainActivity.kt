package com.example.cryptoapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptoapp.market.MarketScreen
import com.example.cryptoapp.market.MarketScreenViewModel
import com.example.cryptoapp.ui.theme.CryptoAppTheme
import com.example.cryptoapp.wallet.WalletScreen
import com.example.remote.UserWallet
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "market_screen",
                    ){
                    composable("market_screen"){
                        MarketScreen(navController = navController)
                    }
                    composable("wallet_screen",){
                        WalletScreen(navController)
                    }
                }

            }
        }
    }
}


