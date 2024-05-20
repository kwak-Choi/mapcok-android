package com.mapcok.data.model.param

import com.naver.maps.geometry.LatLng
import okhttp3.MultipartBody

data class UserPhotoParam (
    val userId: Int,
    val imageFile: MultipartBody.Part,
//    val content : String,
    val latitude : Double,
    val longitude : Double
)