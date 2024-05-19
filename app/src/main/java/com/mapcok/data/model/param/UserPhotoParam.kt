package com.mapcok.data.model.param

import com.naver.maps.geometry.LatLng

data class UserPhotoParam (
    val userId: Int,
    val photoSrc : String,
    val position : LatLng
)