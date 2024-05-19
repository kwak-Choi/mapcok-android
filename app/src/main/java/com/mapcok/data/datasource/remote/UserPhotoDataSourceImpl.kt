package com.mapcok.data.datasource.remote

import com.mapcok.data.api.UserPhotoService
import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserPhotoData
import com.mapcok.data.model.param.UserPhotoParam
import javax.inject.Inject

class UserPhotoDataSourceImpl @Inject constructor(
    private val userPhotoService: UserPhotoService
) : UserPhotoDataSource {

    override suspend fun addPhoto(userPhotoParam: UserPhotoParam): ResponseData<UserPhotoData> {
        return userPhotoService.addPhoto(userPhotoParam.userId, userPhotoParam)
    }

    override suspend fun deletePhoto(userId: Int, photoId: Int): ResponseData<Unit> {
        return userPhotoService.deletePhoto(userId, photoId)
    }

    override suspend fun getPhotoById(userId: Int, photoId: Int): ResponseData<UserPhotoData> {
        return userPhotoService.getPhotoById(userId, photoId)
    }

    override suspend fun getPhotolist(userId: Int): ResponseData<List<UserPhotoData>> {
        return userPhotoService.getPhotolist(userId)
    }
}
