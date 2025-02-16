package com.mapcok.data.datasource.remote

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserData
import com.mapcok.data.model.PostData
import com.mapcok.data.model.SuccessData
import com.mapcok.data.model.param.PostParam

interface PostDataSource {

    suspend fun registerPost(postParam: PostParam, type : Boolean): ResponseData<SuccessData>

    suspend fun deletePhoto(userId: Int, photoId: Int): ResponseData<Unit>

    suspend fun getPhotoById(userId: Int, photoId: Int): ResponseData<PostData>

    suspend fun getUserPosts(userId: Int): ResponseData<List<PostData>>

    suspend fun updatePost(photoId: Int, post: PostData): ResponseData<PostData>


}
