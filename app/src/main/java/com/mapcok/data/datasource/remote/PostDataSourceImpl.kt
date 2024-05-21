package com.mapcok.data.datasource.remote

import android.content.Context
import com.mapcok.data.api.PostService
import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.PostData
import com.mapcok.data.model.SuccessData
import com.mapcok.data.model.param.PostParam
import com.mapcok.data.util.toMultipartBodyBasic
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postService: PostService,
    @ApplicationContext private val context: Context
) : PostDataSource {
    //type : true -> 파일, false -> uri
    override suspend fun registerPost(postParam: PostParam, type : Boolean): ResponseData<SuccessData> {
        val imageValue = if(type){
            postParam.photoFile?.toMultipartBodyBasic( "image")
        }else{
            postParam.photoUrl.toMultipartBodyBasic(context, "image")
        }

        Timber.d("데이터 확인  ${postParam.userId} $imageValue ${postParam.latitude} ${postParam.longitude} ${postParam.content} ")
        return postService.registerPost(
            userId = postParam.userId,
            image = imageValue,
            latitude = postParam.latitude,
            longitude = postParam.longitude,
            content = postParam.content,
        )
    }

    override suspend fun deletePhoto(userId: Int, photoId: Int): ResponseData<Unit> {
        return postService.deletePhoto(userId, photoId)
    }

    override suspend fun getPhotoById(userId: Int, photoId: Int): ResponseData<PostData> {
        return postService.getPhotoById(userId, photoId)
    }

    override suspend fun getUserPosts(userId: Int): ResponseData<List<PostData>> {
        return postService.getUserPosts(userId)
    }

    override suspend fun updatePost(photoId: Int, post: PostData): ResponseData<PostData> {
        return postService.updatePost(photoId, post)
    }


}
