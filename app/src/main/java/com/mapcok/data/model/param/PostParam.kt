package com.mapcok.data.model.param

import android.net.Uri

data class PostParam(
  val userId: Int,
  val imageFile: Uri,
  val content: String,
  val latitude: Double,
  val longitude: Double
)