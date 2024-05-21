package com.mapcok.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.PostDataSource
import com.mapcok.data.model.PostData
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
  private val postDataSource: PostDataSource
) : ViewModel() {


  //게시물
  private val _userPost = MutableLiveData<List<PostData>>()
  val userPost: LiveData<List<PostData>> get() = _userPost

  fun setUserPost(value: List<PostData>?) {
    value?.let {
      _userPost.value = it
    }
  }


  fun getPosts(userId: Int) {
    viewModelScope.launch {
      when (val response = safeApiCall(Dispatchers.IO) {
        postDataSource.getUserPosts(userId)
      }) {
        is ResultWrapper.Success -> {
          setUserPost(response.data.data)
          Timber.d("게시물 조회 성공 ${response.data.data}")
        }

        is ResultWrapper.GenericError -> {
          Timber.d("게시물 조회 에러 ${response.message}")
        }

        is ResultWrapper.NetworkError -> {
          Timber.d("게시물 조회 네트워크 에러")
        }
      }
    }
  }
}