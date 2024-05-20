package com.mapcok.ui.userlist.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.data.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
  private val userDataSource: UserDataSource
) : ViewModel() {

  private val _userList = MutableLiveData<List<UserData>>()
  val userList: LiveData<List<UserData>> get() = _userList

  fun setUserList(value: List<UserData>) {
    _userList.value = value
  }


  fun getUserList() {
    viewModelScope.launch {
      when (val response = safeApiCall(Dispatchers.IO) {
        userDataSource.getUsers()
      }) {
        is ResultWrapper.Success -> {
          setUserList(response.data.data)
          Timber.d("유저 리스트 조회 성공")
        }

        is ResultWrapper.GenericError -> {
          Timber.d("유저 리스트 조회 에러 ${response.message}")
        }

        is ResultWrapper.NetworkError -> {
          Timber.d("유저 리스트 조회 네트워크 에러")
        }
      }
    }

  }

}