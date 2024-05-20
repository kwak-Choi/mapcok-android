package com.mapcok.data.model

import com.naver.maps.geometry.LatLng
import okhttp3.MultipartBody

data class UserPhotoData(
    val photoId: Int,
    val userId: Int,
    val imageFile: MultipartBody.Part,
//    val content : String,
    val latitude : Double,
    val longitude : Double
)