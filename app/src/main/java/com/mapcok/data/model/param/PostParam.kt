package com.mapcok.data.model.param

import okhttp3.MultipartBody

data class PostParam (
    val userId: Int,
    val imageFile: MultipartBody.Part,
    val content : String,
    val latitude : Double,
    val longitude : Double
)