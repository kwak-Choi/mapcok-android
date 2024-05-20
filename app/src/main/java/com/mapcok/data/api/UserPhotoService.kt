package com.mapcok.data.api

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserPhotoData
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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



    @Multipart
    @POST("{userId}/photo")
    suspend fun addPhoto(
        @Path("userId") userId: Int,
        @Part imageFile: MultipartBody.Part,
        @Part latitude: Double,
        @Part longitude: Double
    ): ResponseData<UserPhotoData>

    @DELETE("user/photo")
    suspend fun deletePhoto(
        @Query("userId") userId: Int,
        @Query("photoId") photoId: Int
    ): ResponseData<Unit>
}
