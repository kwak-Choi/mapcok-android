package com.mapcok.data.api

import com.mapcok.data.model.ResponseData
import com.mapcok.data.model.param.UserParam
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

  @POST("user")
  suspend fun userSignUp(
    @Body userParam: UserParam
  ): ResponseData<Boolean>



}