package com.mapcok.data.repository

import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.data.model.UserData
import com.mapcok.data.model.param.UserParam
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val userDataSource: UserDataSource
) : UserRepository {

  override suspend fun userSignUp(userParam: UserParam): Boolean {
    return userDataSource.signUp(userParam).data
  }

}