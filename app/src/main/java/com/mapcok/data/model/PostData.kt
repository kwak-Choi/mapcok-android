package com.mapcok.data.model

import okhttp3.MultipartBody

data class PostData(
    val id: Int,
    val userId: Int,
    val photoUrl: String,
    val content : String,
    val latitude : Double,
    val longitude : Double
)