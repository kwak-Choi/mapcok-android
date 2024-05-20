package com.mapcok.data.datasource.remote

import com.mapcok.data.api.PostService
import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserData
import com.mapcok.data.model.PostData
import com.mapcok.data.model.param.PostParam
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postService: PostService
) : PostDataSource {

    override suspend fun registerPost(postParam: PostParam): ResponseData<PostData> {
        return postService.addPhoto(
            userId = postParam.userId,
            imageFile = postParam.imageFile,
            latitude = postParam.latitude,
            longitude = postParam.longitude
        )
    }

    override suspend fun deletePhoto(userId: Int, photoId: Int): ResponseData<Unit> {
        return postService.deletePhoto(userId, photoId)
    }

    override suspend fun getPhotoById(userId: Int, photoId: Int): ResponseData<PostData> {
        return postService.getPhotoById(userId, photoId)
    }

    override suspend fun getPhotolist(userId: Int): ResponseData<List<PostData>> {
        return postService.getPhotolist(userId)
    }
}
