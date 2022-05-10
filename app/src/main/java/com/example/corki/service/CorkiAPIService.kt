package com.example.corki.service

import android.util.Log
import com.example.corki.models.post.Posts
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CorkiAPIService {
    //https://panoramx.ift.uni.wroc.pl:8888/v1/

    private val BASE_URL = "http://panoramx.ift.uni.wroc.pl:8888/v1/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(CorkiAPI::class.java)

    fun getPostsData(): Single<Posts> {
        return api.getPosts()
    }
}