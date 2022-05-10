package com.example.corki.service

import com.example.corki.models.post.Posts
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CorkiAPI {
    @GET("posts")
    fun getPosts(): Single<Posts>
}