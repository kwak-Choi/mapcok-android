package com.mapcok.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val userDataSource: UserDataSource
)  : ViewModel() {

  private val _loginSuccess = MutableLiveData<Boolean>()
  val loginSuccess : LiveData<Boolean> get() = _loginSuccess

  fun setLoginSuccess(value : Boolean){
    _loginSuccess.value = value
  }


  fun getUserInfo(userEmail : String){
    viewModelScope.launch {
      when(val response = safeApiCall(Dispatchers.IO){
        userDataSource.getUserInfo(userEmail)
      }){
        is ResultWrapper.Success ->{
          Timber.d("로그인 파람 ${userEmail}")
          val value = response.data.data
          Timber.d("로그인 성공 ${response.data.data}")
          if(value != null){
            SingletonUtil.user = response.data.data
            setLoginSuccess(true)
          }else{
            setLoginSuccess(false)
          }
        }
        is ResultWrapper.GenericError -> {
          setLoginSuccess(false)
          Timber.d("로그인 에러 ${response.message}")
        }

        is ResultWrapper.NetworkError -> {
          setLoginSuccess(false)
          Timber.d("로그인 네트워크 에러")
        }
      }
    }
  }

}