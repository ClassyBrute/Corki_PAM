package com.example.corki.models.post


import com.google.gson.annotations.SerializedName

data class PageInfo(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("lastPage")
    val lastPage: Int,
    @SerializedName("perPage")
    val perPage: Int,
    @SerializedName("total")
    val total: Int
)