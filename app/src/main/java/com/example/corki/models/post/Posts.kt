package com.example.corki.models.post


import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("posts")
    val posts: List<Post>
)