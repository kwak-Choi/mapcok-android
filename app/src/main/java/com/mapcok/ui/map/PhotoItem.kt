package com.mapcok.ui.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

data class PhotoItem(
  val id: Int,
  private var position: LatLng
) : ClusteringKey {
  override fun getPosition(): LatLng {
    return LatLng(position.latitude, position.longitude)
  }
}