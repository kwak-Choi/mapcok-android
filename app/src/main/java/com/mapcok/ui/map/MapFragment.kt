package com.mapcok.ui.map

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.inputmethodservice.Keyboard.Row
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "MapFragment_싸피"

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var mapView: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    lateinit var file: File

    private val builder = Clusterer.Builder<PhotoItem>()

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()

        binding.loadCamera.setOnClickListener {
         capture()
        }

    }

    private fun capture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = createImageFile()
        val photoUri = FileProvider.getUriForFile(requireContext(), "com.mapcok.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        requestCamera.launch(intent)
    }

    private val requestCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 이미지 캡쳐 성공
            // file 변수를 사용하여 이미지 처리
            // file 변수에는 이미지가 저장된 파일이 있습니다.
        } else {
            // 이미지 캡쳐 실패 또는 취소
            Log.d(TAG, "Image capture failed or cancelled")
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun initMapView() {
        mapView = childFragmentManager.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.map_view, it).commit()
            }

        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
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

        val keyTagMap = mapOf(
            PhotoItem(1, LatLng(37.372, 127.113)) to null,
            PhotoItem(2, LatLng(37.366, 127.106)) to null,
            PhotoItem(3, LatLng(37.365, 127.157)) to null,
            PhotoItem(4, LatLng(37.361, 127.105)) to null,
            PhotoItem(5, LatLng(37.368, 127.110)) to null,
            PhotoItem(6, LatLng(37.360, 127.106)) to null,
            PhotoItem(7, LatLng(37.363, 127.111)) to null
        )

        builder.minZoom(4).maxZoom(16)


        builder.clusterMarkerUpdater(object : DefaultClusterMarkerUpdater() {
            override fun updateClusterMarker(info: ClusterMarkerInfo, marker: Marker) {
                super.updateClusterMarker(info, marker)
                marker.icon = if (info.size < 3) {
                    MarkerIcons.CLUSTER_LOW_DENSITY
                } else {
                    MarkerIcons.CLUSTER_MEDIUM_DENSITY
                }
            }
        }).leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
            override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
                super.updateLeafMarker(info, marker)
                marker.icon = OverlayImage.fromResource(R.drawable.photomarker)
                marker.width = 220
                marker.height = 220
                marker.onClickListener = Overlay.OnClickListener {
                    if (it is Marker) {

                        val photoItem = keyTagMap.keys.find { item -> item.position == it.position }
                        val id = photoItem?.id
                        Log.d(TAG, "Clicked Marker ID: $id")


                        val clickedLatLng = it.position
                        Log.d(TAG, "Clicked Marker LatLng: $clickedLatLng")

                        val cameraUpdate = CameraUpdate.scrollAndZoomTo(clickedLatLng,18.0)
                            .animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                    }
                    true
                }


            }
        })

        val clusterer = builder.build()
        clusterer.addAll(keyTagMap)
        clusterer.map = naverMap

    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


}
