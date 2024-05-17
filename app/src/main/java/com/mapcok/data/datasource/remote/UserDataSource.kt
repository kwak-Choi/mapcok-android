package com.mapcok.data.datasource.remote

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.UserData
import com.mapcok.data.model.param.UserParam

interface UserDataSource {

  suspend fun signUp(userParam: UserParam): ResponseData<Boolean>
}