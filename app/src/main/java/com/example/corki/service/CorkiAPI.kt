package com.example.corki.service

import com.example.corki.models.account.Account
import com.example.corki.models.post.Post
import com.example.corki.models.post.Posts
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface CorkiAPI {
    //POSTS
    @GET("post/{id}")
    fun getPost(@Path("id") id: String): Single<Post>

    @GET("posts")
    fun getPostsWithQuery(@QueryMap(encoded=true) map: Map<String, String>): Single<Posts>

    @POST("posts")
    fun postPost(@Body map: Map<String, String>, @Header("Authorization") token: String): Single<String>

    //ACCOUNT
    @POST("login")
    fun postLogin(@Body map: Map<String, String>): Single<String>

    @POST("register")
    fun postRegister(@Body map: Map<String, String>): Single<String>

    @GET("profile")
    fun getLoggedUser(@Header("Authorization") token: String): Single<Account>

    @GET("account/{id}")
    fun getAccount(@Path("id") id: String): Single<Account>
}