package com.mapcok.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostData(
  val id: Int,
  val userId: Int,
  val photoUrl: String,
  val content: String,
  val latitude: Double,
  val longitude: Double
) : Parcelable