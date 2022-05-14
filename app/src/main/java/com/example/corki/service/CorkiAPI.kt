package com.example.corki.service

import com.example.corki.models.post.Post
import com.example.corki.models.post.Posts
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface CorkiAPI {
    //POSTS
    @GET("post/{id}")
    fun getPost(@Path("id") id: String): Single<Post>

    @GET("posts")
    fun getPostsWithQuery(@QueryMap(encoded=true) map: Map<String, String>): Single<Posts>

    //ACCOUNT
}