package com.example.corki.service

import com.example.corki.models.account.Account
import com.example.corki.models.post.Post
import com.example.corki.models.post.Posts
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CorkiAPIService {
    private val BASE_URL = "http://panoramx.ift.uni.wroc.pl:8888/v1/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(CorkiAPI::class.java)

    //POSTS
    fun getPostsDataWithQuery(map : Map<String, String>): Single<Posts> {
        return api.getPostsWithQuery(map)
    }

    fun getPostData(id: String): Single<Post> {
        return api.getPost(id)
    }

    //ACCOUNT
    fun postLoginData(map: Map<String, String>): Single<String> {
        return api.postLogin(map)
    }

    fun postRegisterData(map: Map<String, String>): Single<String> {
        return api.postRegister(map)
    }

    fun getLoggedUserData(token: String): Single<Account> {
        return api.getLoggedUser(token)
    }

    fun getAccountData(id: String): Single<Account> {
        return api.getAccount(id)
    }
}