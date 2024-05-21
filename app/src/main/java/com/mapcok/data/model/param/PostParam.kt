package com.mapcok.data.model.param

import android.net.Uri
import java.io.File

data class PostParam(
  val userId: Int,
  val photoUrl: Uri,
  val content: String,
  val latitude: Double,
  val longitude: Double,
  val photoFile : File?
)