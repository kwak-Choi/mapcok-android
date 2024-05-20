package com.mapcok.ui.photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.PostDataSource
import com.mapcok.data.model.UserData
import com.mapcok.data.model.param.PostParam
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
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


    private val _registerPostSuccess = MutableLiveData<Boolean>()
    val registerPostSuccess: LiveData<Boolean> get() = _registerPostSuccess

    fun setRegisterPostSuccess(value: Boolean) {
        _registerPostSuccess.value = value
    }



    fun registerPost(postParam: PostParam) {
        viewModelScope.launch {
            Timber.d("확인 파람 $postParam")
            when (val response = safeApiCall(Dispatchers.IO) {
                postDataSource.registerPost(postParam)
            }) {
                is ResultWrapper.Success -> {
                    setRegisterPostSuccess(response.data.data.success)
                    Timber.d("사진 추가 성공")
                }

                is ResultWrapper.GenericError -> {
                    Timber.d("사진 추가 에러 ${response.message}")
                }

                is ResultWrapper.NetworkError -> {
                    Timber.d("사진 추가 네트워크 에러")
                }
            }
        }
    }

}
