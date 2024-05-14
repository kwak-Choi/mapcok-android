package com.mapcok.ui.map

import android.content.Context.LOCATION_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable

import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

private const val TAG = "MapFragment_싸피"
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback,
    ClusteringKey {

    private lateinit var mapView: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()

    }

    private fun initMapView() {
        mapView = childFragmentManager.findFragmentById(R.id.map_view) as MapFragment? ?:
                MapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().replace(R.id.map_view,it).commit()
                }

        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun initView() {
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.minZoom = 6.0
        naverMap.maxZoom = 18.0

        naverMap.extent = LatLngBounds(LatLng(33.0041, 124.6094), LatLng(38.6140, 131.5928))

        setMarkers()

    }

    private fun setMarkers() {
        // Define marker positions
        val markerPositions = listOf(
            LatLng(37.5670135, 126.9783740),
            LatLng(37.566671, 126.977656),
            LatLng(37.566121, 126.977069)
            //사진의 위도 경도를 가져와 리스트로 표시
        )

        
        //마커로 표시
        for (position in markerPositions) {
            val marker = Marker()
            val resizedBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.photomarker), 200, 200, false)
            marker.position = position
            marker.icon = OverlayImage.fromBitmap(resizedBitmap)
            marker.map = naverMap
        }
    }
    override fun getPosition() = position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val itemKey = other as ItemKey
        return id == itemKey.id
    }

    override fun hashCode() = id


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


}
