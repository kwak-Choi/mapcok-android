package com.mapcok.data.model.param

import android.net.Uri

data class PostParam(
  val userId: Int,
  val photoUrl: Uri,
  val content: String,
  val latitude: Double,
  val longitude: Double
)