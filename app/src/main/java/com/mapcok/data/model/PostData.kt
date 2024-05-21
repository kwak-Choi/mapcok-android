package com.mapcok.data.model


data class PostData(
    val id: Int,
    val userId: Int,
    val photoUrl: String,
    val content : String,
    val latitude : Double,
    val longitude : Double
)