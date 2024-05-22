package com.mapcok.ui.map.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.annotations.concurrent.UiThread
import com.mapcok.R
import com.mapcok.databinding.FragmentOtherMapBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.map.MapFragment
import com.mapcok.ui.map.PhotoItem
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.upload.UploadFragmentArgs
import com.mapcok.ui.util.SingletonUtil
import com.mapcok.ui.util.checkLocationPermission
import com.mapcok.ui.util.requestMapPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OtherMapFragment : BaseFragment<FragmentOtherMapBinding>(R.layout.fragment_other_map),OnMapReadyCallback {

    private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()
    private val args: OtherMapFragmentArgs by navArgs()
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapView: MapView
    private val builder = Clusterer.Builder<PhotoItem>()
    override fun initView() {
        hideBottomNavigation()
        initMapView()
    }

    private fun initMapView() {
        mapView = binding.mapOther
        mapView?.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


    override fun onPause() {
        super.onPause()
        showBottomNavigation()
        mapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
        mapView?.onDestroy()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.VISIBLE
    }

    companion object {

    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
       initNaverMap(naverMap)
        setMarkers()
    }

    private fun setMarkers() {
        uploadPhotoViewModel.postList.observe(viewLifecycleOwner){
            val keyTagMap = it.associate { post ->
                PhotoItem(post.id, LatLng(post.latitude,post.longitude)) to null
            }

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
                    marker.icon = OverlayImage.fromResource(com.mapcok.R.drawable.photomarker)
                    marker.width = 220
                    marker.height = 220
                    marker.onClickListener = Overlay.OnClickListener {
                        if (it is Marker) {
                            val photoItem = keyTagMap.keys.find { item -> item.position == it.position }
                            val id = photoItem?.id
                            if (photoItem != null) {
                                Timber.d("마커 확인 클릭")
                                uploadPhotoViewModel.setMarkerClick(true)
                                uploadPhotoViewModel.getPhotoById(args.userId,photoItem.id)
                            }
                            val clickedLatLng = it.position
                            val cameraUpdate =
                                CameraUpdate.scrollAndZoomTo(clickedLatLng, 18.0).animate(
                                    CameraAnimation.Easing)
                            naverMap.moveCamera(cameraUpdate)
                        }
                        true
                    }
                }
            })

            val clusterer: Clusterer<PhotoItem> = builder.build()
            clusterer.addAll(keyTagMap)
            clusterer.map = naverMap
        }
    }

    private fun initNaverMap(naverMap: NaverMap) { // 위치 및 naverMap 세팅
        this.naverMap = naverMap
        this.naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        this.naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        this.naverMap.locationTrackingMode = LocationTrackingMode.Follow
        this.naverMap.minZoom = 6.0
        this.naverMap.maxZoom = 18.0
        getLastLocation(naverMap)
        uploadPhotoViewModel.getUserPosts(args.userId)
    }

    private fun getLastLocation(map: NaverMap) { // 마지막 위치 가져오기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission(requireActivity())
        requireContext().requestMapPermission {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val loc = if (location == null) {
                    LatLng(MapFragment.DEFAULT_LATITUDE, MapFragment.DEFAULT_LONGITUDE)
                } else {
                    LatLng(location.latitude, location.longitude)
                }
                map.cameraPosition = CameraPosition(loc, MapFragment.DEFAULT_ZOOM)
                map.locationTrackingMode = LocationTrackingMode.Follow

                uploadPhotoViewModel.setLocation(location.latitude, location.longitude)
            }
        }
    }
}