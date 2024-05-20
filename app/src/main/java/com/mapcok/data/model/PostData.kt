package com.mapcok.data.model

import okhttp3.MultipartBody

data class PostData(
    val id: Int,
    val userId: Int,
    val content : String,
    val photoUrl : String,
    val latitude : Double,
    val longitude : Double
)