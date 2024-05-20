package com.mapcok.data.model

import okhttp3.MultipartBody

data class PostData(
    val photoId: Int,
    val userId: Int,
    val imageFile: String,
    val content : String,
    val latitude : Double,
    val longitude : Double
)