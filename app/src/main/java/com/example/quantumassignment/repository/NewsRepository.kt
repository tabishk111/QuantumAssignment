package com.example.quantumassignment.repository


import com.example.quantumassignment.api.RetrofitInstance

class NewsRepository() {

    suspend fun getBreakingNews(countryCode: String)=
        RetrofitInstance.api.getBreakingNews(countryCode)

    suspend fun searchNews(searchQuery: String) =
        RetrofitInstance.api.searchForNews(searchQuery)

}