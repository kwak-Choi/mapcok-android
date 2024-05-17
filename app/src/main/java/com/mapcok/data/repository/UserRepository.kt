package com.mapcok.data.repository

import com.mapcok.data.model.UserData
import com.mapcok.data.model.param.UserParam

interface UserRepository {

  suspend fun userSignUp(userParam: UserParam) : Boolean
}