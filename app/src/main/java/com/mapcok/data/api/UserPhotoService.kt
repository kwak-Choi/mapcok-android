package com.mapcok.data.api

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserPhotoData
import com.mapcok.data.model.param.UserPhotoParam
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserPhotoService {

    // 특정 userId와 photoId로 사진 하나 가져오기
    @GET("{userId}/photo/{photoId}")
    suspend fun getPhotoById(
        @Path("userId") userId: Int,
        @Path("photoId") photoId: Int
    ): ResponseData<UserPhotoData>


    @GET("{userId}/photos")
    suspend fun getPhotolist(
        @Path("userId") userId: Int
    ): ResponseData<List<UserPhotoData>>


    @POST("{userId}/photo")
    suspend fun addPhoto(
        @Path("userId") userId: Int,
        @Body userPhotoParam: UserPhotoParam
    ): ResponseData<UserPhotoData>

    @DELETE("user/photo")
    suspend fun deletePhoto(
        @Query("userId") userId: Int,
        @Query("photoId") photoId: Int
    ): ResponseData<Unit>
}
