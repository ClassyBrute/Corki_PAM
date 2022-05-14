package com.example.corki.models.post


import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("cities")
    val cities: List<String>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("dateFrom")
    val dateFrom: String,
    @SerializedName("dateTo")
    val dateTo: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("interestedIn")
    val interestedIn: List<Any>,
    @SerializedName("level")
    val level: List<String>,
    @SerializedName("ownerId")
    val ownerId: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("subjects")
    val subjects: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val v: Int
)