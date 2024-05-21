package com.mapcok.ui.mypost

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.PostDataSource
import com.mapcok.data.model.PostData
import com.mapcok.data.model.ResponseData
import com.mapcok.ui.mypage.MyPagePhoto
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import kotlin.math.log

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val postDataSource: PostDataSource
) : ViewModel() {
    private val _selectedPhoto = MutableLiveData<MyPagePhoto>()
    val selectedPhoto: LiveData<MyPagePhoto>
        get() = _selectedPhoto

    private val _selectedPost = MutableLiveData<PostData>()
    val selectedPost: LiveData<PostData>
        get() = _selectedPost

    fun setSelectedPhoto(photo: MyPagePhoto) {
        _selectedPhoto.value = photo
    }

    fun updatePost(photoId: Int, post: PostData) {
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                Log.d(TAG, "updatePost: $photoId, $post")
                postDataSource.updatePost(photoId, post)
            }) {
                is ResultWrapper.Success -> {
                    Timber.d("게시물 업데이트 성공")
                }
                is ResultWrapper.GenericError -> {
                    Timber.d("게시물 업데이트 에러 ${response.message}")
                }
                is ResultWrapper.NetworkError -> {
                    Timber.d("게시물 업데이트 네트워크 에러")
                }
            }
        }
    }

    private val _deleteResult = MutableLiveData<Response<ResponseData<Unit>>>()
    val deleteResult: LiveData<Response<ResponseData<Unit>>> = _deleteResult

    fun deletePhoto(userId: Int, photoId: Int) {
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                postDataSource.deletePhoto(userId, photoId)
            }) {
                is ResultWrapper.Success -> {
                    Timber.d("게시물 삭제 성공")
                }
                is ResultWrapper.GenericError -> {
                    Timber.d("게시물 삭제 에러 ${response.message}")
                }
                is ResultWrapper.NetworkError -> {
                    Timber.d("게시물 삭제 네트워크 에러")
                }
            }
        }
    }



}
