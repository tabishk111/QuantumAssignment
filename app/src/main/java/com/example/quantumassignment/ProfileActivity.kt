package com.example.quantumassignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quantumassignment.adapter.NewsAdapter
import com.example.quantumassignment.databinding.ActivityProfileBinding
import com.example.quantumassignment.repository.NewsRepository
import com.example.quantumassignment.ui.NewsViewModel
import com.example.quantumassignment.ui.NewsViewModelProviderFactory
import com.example.quantumassignment.util.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("Profile","onCreate")

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        actionBar = supportActionBar!!
        actionBar.title = "News"

        val newsRepository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        setUpRecyclerView()
        viewModel.getNews()

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                it?.let {
                    if(it.toString().isNotEmpty())
                        viewModel.serachNews(it.toString())
                }
            }
        }

        viewModel.news.observe(this, Observer {
            it.let {
                newsAdapter.differ.submitList(it.articles.toList())
            }
        })

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun checkUser() {
        val user = firebaseAuth.currentUser
        if(user!=null){
            val email = user.email
        }
        else{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

}