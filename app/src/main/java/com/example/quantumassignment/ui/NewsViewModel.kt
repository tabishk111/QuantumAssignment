package com.example.quantumassignment.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quantumassignment.NewsApplication
import com.example.quantumassignment.models.NewsResponse
import com.example.quantumassignment.repository.NewsRepository
import com.example.quantumassignment.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app){

    val news: MutableLiveData<NewsResponse> = MutableLiveData()

    init {
        getNews()
    }

    fun getNews(){
        viewModelScope.launch {
            var res = newsRepository.getBreakingNews("us")
            news.value = res.body()
        }
    }

    fun serachNews(query: String){
        viewModelScope.launch {
            var res = newsRepository.searchNews(query)
            news.value=res.body()
        }
    }

    }