package com.mapcok.data.api

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.PostData
import com.mapcok.data.model.SuccessData
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {

    // 특정 userId와 photoId로 사진 하나 가져오기
    @GET("post/{userId}/{photoId}")
    suspend fun getPhotoById(
        @Path("userId") userId: Int,
        @Path("photoId") photoId: Int
    ): ResponseData<PostData>



    @GET("post/{userId}")
    suspend fun getUserPosts(
        @Path("userId") userId: Int
    ): ResponseData<List<PostData>>


    //게시물 업로드
    @Multipart
    @POST("post/{userId}")
    suspend fun registerPost(
      @Path("userId") userId: Int,
      @Part image: MultipartBody.Part?,
      @Query("latitude") latitude: Double,
      @Query("longitude") longitude: Double,
      @Query ("content") content: String
    ): ResponseData<SuccessData>

    @DELETE("user/photo")
    suspend fun deletePhoto(
        @Query("userId") userId: Int,
        @Query("photoId") photoId: Int
    ): ResponseData<Unit>
}
