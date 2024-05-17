package com.mapcok.data.datasource.remote

import com.mapcok.data.api.UserService
import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserData
import com.mapcok.data.model.param.UserParam
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
  private val userService: UserService
) : UserDataSource {

  override suspend fun signUp(userParam: UserParam): ResponseData<Boolean> {
    return userService.userSignUp(userParam)
  }
}