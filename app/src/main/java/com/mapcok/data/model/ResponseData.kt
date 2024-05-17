package com.mapcok.data.model

data class ResponseData<T>(
  val statusCode : Int,
  val msg : String,
  val data : T
)
