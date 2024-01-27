package com.example.cryptoapp.market

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remote.model.SearchCoin
import com.example.remote.response.TrendingCoin
import com.example.repo.CryptoRepository
import com.example.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MarketScreenViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    var cryptoList = mutableStateOf(TrendingCoin())
    var searchedList = mutableStateOf(SearchCoin())
    var isSearching = mutableStateOf(false)
    var isLoading = mutableStateOf(false)
    init {
        getTrendingCoin()
    }

     fun getTrendingCoin(){
         isLoading.value = true
        viewModelScope.launch {
            val response = repository.getTrendsCoins()
            when(response){
                is Resource.Success -> {
                    cryptoList.value = response.data!!
                    isLoading.value = false
                }
                is Resource.Error -> {
                    println("viewmodellleld")
                    isLoading.value = false

                }
                else -> {}
            }

        }

    }

    fun getSearchedCoin(query : String) {
        if (query.isEmpty()) {
            isSearching.value = false
            return
        }
        isSearching.value = true
        viewModelScope.launch {
                val response = repository.getSearchCoin(query)
                when (response) {
                    is Resource.Success -> {
                       searchedList.value = response.data!!
                    }
                    is Resource.Error -> {
                        println("something went wrong")
                    }
                    else -> {}
                }
        }
    }
}