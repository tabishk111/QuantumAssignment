package com.example.quantumassignment.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quantumassignment.models.NewsResponse
import com.example.quantumassignment.repository.NewsRepository
import kotlinx.coroutines.launch


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