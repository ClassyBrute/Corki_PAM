package com.example.corki.models.account


import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("accountName")
    val accountName: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val v: Int
)