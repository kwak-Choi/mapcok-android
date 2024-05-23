package com.mapcok.ui.map

import com.naver.maps.geometry.LatLng
import org.jetbrains.annotations.NotNull
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng


data class PhotoItem(
  val id: Int,
  private var position: LatLng
) : TedClusterItem {

  @NotNull
  @Override
  override fun getTedLatLng(): TedLatLng {
    return TedLatLng(position.latitude, position.longitude)
  }
}