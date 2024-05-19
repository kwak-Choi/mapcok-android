package com.mapcok.data.model

import com.naver.maps.geometry.LatLng

data class UserPhotoData(
    val photoId: Int,
    val userId: Int,
    val photoSrc : String,
    val position : LatLng
)