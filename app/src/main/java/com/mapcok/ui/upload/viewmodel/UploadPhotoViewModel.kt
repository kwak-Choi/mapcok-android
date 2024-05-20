package com.mapcok.ui.photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.UserPhotoDataSource
import com.mapcok.data.model.param.UserPhotoParam
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
    private val userPhotoDataSource: UserPhotoDataSource
) : ViewModel() {


    private val _location = MutableLiveData<Pair<Double, Double>>()
    val location: LiveData<Pair<Double, Double>> get() = _location

    fun setLocation(lat: Double, lon: Double) {
        _location.value = Pair(lat, lon)
    }


    private val _photoAddedSuccess = MutableLiveData<Boolean>()
    val photoAddedSuccess: LiveData<Boolean> get() = _photoAddedSuccess

    fun setPhotoAddedSuccess(value: Boolean) {
        _photoAddedSuccess.value = value
    }

    private val _photoDeletedSuccess = MutableLiveData<Boolean>()
    val photoDeletedSuccess: LiveData<Boolean> get() = _photoDeletedSuccess

    fun setPhotoDeletedSuccess(value: Boolean) {
        _photoDeletedSuccess.value = value
    }

    fun addPhoto(userPhotoParam: UserPhotoParam) {
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                userPhotoDataSource.addPhoto(userPhotoParam)
            }) {
                is ResultWrapper.Success -> {
                    SingletonUtil.photo = response.data.data
                    setPhotoAddedSuccess(true)
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

    fun deletePhoto(userId: Int, photoId: Int) {
        viewModelScope.launch {
            when (val response = safeApiCall(Dispatchers.IO) {
                userPhotoDataSource.deletePhoto(userId, photoId)
            }) {
                is ResultWrapper.Success -> {
                    setPhotoDeletedSuccess(true)
                    Timber.d("사진 삭제 성공")
                }

                is ResultWrapper.GenericError -> {
                    Timber.d("사진 삭제 에러 ${response.message}")
                }

                is ResultWrapper.NetworkError -> {
                    Timber.d("사진 삭제 네트워크 에러")
                }
            }
        }
    }
}
