package com.mapcok.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.data.model.param.UserParam
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userDataSource: UserDataSource
) : ViewModel() {

  private val _userSignUpSuccess = MutableLiveData<Boolean>()
  val userSignUpSuccess: LiveData<Boolean> get() = _userSignUpSuccess

  fun setUserSignUpSuccess(value: Boolean) {
    _userSignUpSuccess.value = value
  }


  fun signUp(userParam: UserParam) {
    viewModelScope.launch {
      when (val response = safeApiCall(Dispatchers.IO) {
        userDataSource.signUp(userParam)
      }) {
        is ResultWrapper.Success -> {
          Timber.d("회원 가입 파람 $userParam")
          SingletonUtil.user = response.data.data
          setUserSignUpSuccess(true)
          Timber.d("회원 가입 성공 ${response.data.data}")
        }

        is ResultWrapper.GenericError -> {
          setUserSignUpSuccess(false)
          Timber.d("회원 가입 에러 ${response.message}")
        }

        is ResultWrapper.NetworkError -> {
          setUserSignUpSuccess(false)
          Timber.d("회원 가입 네트워크 에러")
        }
      }
    }

  }


}