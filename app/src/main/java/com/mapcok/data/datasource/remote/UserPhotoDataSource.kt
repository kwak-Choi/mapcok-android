package com.mapcok.data.datasource.remote

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserPhotoData
import com.mapcok.data.model.param.UserPhotoParam

interface UserPhotoDataSource {

    suspend fun addPhoto(userPhotoParam: UserPhotoParam): ResponseData<UserPhotoData>

    suspend fun deletePhoto(userId: Int, photoId: Int): ResponseData<Unit>

    suspend fun getPhotoById(userId: Int, photoId: Int): ResponseData<UserPhotoData>

    suspend fun getPhotolist(userId: Int): ResponseData<List<UserPhotoData>>
}
