package com.mapcok.ui.photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.PostDataSource
import com.mapcok.data.model.PostData
import com.mapcok.data.model.param.PostParam
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadPhotoViewModel @Inject constructor(
    private val postDataSource: PostDataSource
) : ViewModel() {


    private val _location = MutableLiveData<Pair<Double, Double>>()
    val location: LiveData<Pair<Double, Double>> get() = _location

    fun setLocation(lat: Double, lon: Double) {
        _location.value = Pair(lat, lon)
    }

    private val _postList = MutableLiveData<List<PostData>>()
    val postList: LiveData<List<PostData>> get() = _postList

    private val _postListSize = MutableLiveData<Int>()
    val postListSize: LiveData<Int> get() = _postListSize

    var currentUserId: Int = 0


    fun getUserPosts(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                postDataSource.getUserPosts(userId)
            }) {
                is ResultWrapper.Success -> {
                    _postList.value = response.data.data?.let {
                        if (it != _postList.value) it else _postList.value
                    }
                    _postListSize.value = response.data.data.size
                    Timber.d("게시물 목록 불러오기 성공")
                }
                is ResultWrapper.GenericError -> {
                    Timber.d("게시물 목록 불러오기 에러 ${response.message}")
                }
                is ResultWrapper.NetworkError -> {
                    Timber.d("게시물 목록 불러오기 네트워크 에러")
                }
            }
        }
    }

    private val _selectedPost = MutableLiveData<PostData?>()
    val selectedPost: MutableLiveData<PostData?> get() = _selectedPost
    fun getPhotoById(userId: Int, photoId: Int) {
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                Timber.d("${userId}, ${photoId} ???")
                postDataSource.getPhotoById(userId, photoId)
            }) {
                is ResultWrapper.Success -> {
                    _selectedPost.value = response.data.data
                    Timber.d("게시물 불러오기 성공")

                }
                is ResultWrapper.GenericError -> {
                    Timber.d("게시물 불러오기 에러 ${response.message}")
                }
                is ResultWrapper.NetworkError -> {
                    Timber.d("게시물 불러오기 네트워크 에러")
                }
            }
        }
    }


    private val _registerPostSuccess = MutableLiveData<Boolean>()
    val registerPostSuccess: LiveData<Boolean> get() = _registerPostSuccess

    fun setRegisterPostSuccess(value: Boolean) {
        _registerPostSuccess.value = value
    }



    fun registerPost(postParam: PostParam, type : Boolean) {
        viewModelScope.launch {
            Timber.d("확인 파람 $postParam")
            when (val response = safeApiCall(Dispatchers.IO) {
                postDataSource.registerPost(postParam, type)
            }) {
                is ResultWrapper.Success -> {
                    setRegisterPostSuccess(response.data.data.success)
                    Timber.d("게시글 추가 성공")
                }

                is ResultWrapper.GenericError -> {
                    Timber.d("게시글 추가 에러 ${response.message}")
                }

                is ResultWrapper.NetworkError -> {
                    Timber.d("게시글 추가 네트워크 에러")
                }
            }
        }
    }

    //촬영된 이미지 파일
    private val _imageFile = MutableLiveData<File>()
    val imageFile : LiveData<File> get() = _imageFile

    fun setImageFile(value : File){
        _imageFile.value = value
    }

    //마커 클릭이벤트
    private val _markerClick = MutableLiveData<Boolean>()
    val markerClick : LiveData<Boolean> get() = _markerClick

    fun setMarkerClick(value : Boolean){
        _markerClick.value = value
    }

}
