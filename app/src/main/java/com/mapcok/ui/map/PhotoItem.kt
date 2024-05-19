package com.mapcok.ui.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

class PhotoItem(val id:Int,private var position: LatLng) : ClusteringKey {//TedClusterItem {

    fun getTedLatLng(): TedLatLng {
        return TedLatLng(position.latitude, position.longitude)
    }

    var title: String? = null

    var snippet: String? = null

    constructor(lat: Double, lng: Double) : this(1,LatLng(lat, lng)) {
        title = null
        snippet = null
    }

    constructor(lat: Double, lng: Double, title: String?, snippet: String?) : this(1,
        LatLng(lat, lng)
    ) {
        this.title = title
        this.snippet = snippet
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as PhotoItem
        return id == itemKey.id

    }

    override fun getPosition(): LatLng {
        return position
    }

    override fun hashCode() = id
}