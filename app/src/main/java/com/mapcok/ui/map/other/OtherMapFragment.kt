package com.mapcok.ui.map.other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.annotations.concurrent.UiThread
import com.mapcok.R
import com.mapcok.databinding.FragmentOtherMapBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.base.BaseMapFragment
import com.mapcok.ui.map.MapFragment
import com.mapcok.ui.map.PhotoItem
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.upload.UploadFragmentArgs
import com.mapcok.ui.util.SingletonUtil
import com.mapcok.ui.util.checkLocationPermission
import com.mapcok.ui.util.requestMapPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
import ted.gun0912.clustering.naver.TedNaverClustering
import timber.log.Timber
import kotlin.properties.Delegates

private const val TAG = "OtherMapFragment_싸피"
@AndroidEntryPoint
class OtherMapFragment() : BaseMapFragment<FragmentOtherMapBinding>(R.layout.fragment_other_map),OnMapReadyCallback {

    private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()
    private val args: OtherMapFragmentArgs by navArgs()
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var otherMapView: MapView
    override var mapView: MapView? = null

    override fun initOnCreateView() {
        initMapView()
    }
    override fun initViewCreated() {
        hideBottomNavigation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        clickEventListener()
        binding.dialogVisibility = false
    }

    private fun initMapView() {
        otherMapView = binding.mapOther
        otherMapView?.getMapAsync(this)
        locationSource = FusedLocationSource(this, OtherMapFragment.LOCATION_PERMISSION_REQUEST_CODE)
        binding.tvOtherName.text = args.userName + " 님의 맵 콕!"
    }

    override fun onStart() {
        super.onStart()
        otherMapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        otherMapView?.onResume()
    }

    override fun initOnResume() {

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        otherMapView?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        otherMapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        otherMapView?.onLowMemory()
    }


    override fun onPause() {
        super.onPause()
        otherMapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation2()
        otherMapView?.onDestroy()
    }

    private fun hideBottomNavigation() {
        Log.d(TAG, "hideBottomNavigation: 나옴??")
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.GONE
    }

    private fun showBottomNavigation1() {
        Log.d(TAG, "showBottomNavigation: 나옴????쇼쇼111")
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.VISIBLE
    }
    private fun showBottomNavigation2() {
        Log.d(TAG, "showBottomNavigation: 나옴????쇼쇼222")
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.VISIBLE
    }


    @UiThread
    override fun initOnMapReady(naverMap: NaverMap) {
       initNaverMap(naverMap)
        setMarkers()
        observeSelectMarker()
    }

    private fun clickEventListener() {
        binding.imgOtherCancelPreview.setOnClickListener {
            animateView(binding.layoutOtherPreview, 0f, -binding.layoutOtherPreview.width.toFloat())
        }
        binding.btnBackUserlist.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setMarkers() {
        uploadPhotoViewModel.postList.observe(viewLifecycleOwner) { postList ->
            // Convert postList to PhotoItem list
            val photoItems = postList.map { post ->
                PhotoItem(post.id, LatLng(post.latitude, post.longitude))
            }

            Timber.d("마커 개수는 ${photoItems.size}")

            context?.let { context ->
                TedNaverClustering.with<PhotoItem>(context, naverMap)
                    .customMarker { // Customize the marker icon
                        Marker().apply {
                            icon = OverlayImage.fromResource(R.drawable.photomarker)
                            width = 220
                            height = 220
                        }
                    }
                    .markerClickListener { markerItem -> // Handle marker click
                        if (markerItem is PhotoItem) {
                            val clickedLatLng = LatLng(markerItem.getTedLatLng().latitude, markerItem.getTedLatLng().longitude)
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(clickedLatLng, 18.0).animate(CameraAnimation.Easing)
                            naverMap.moveCamera(cameraUpdate)

                            Timber.d("마커 확인 클릭")
                            uploadPhotoViewModel.setMarkerClick(true)
                            uploadPhotoViewModel.getPhotoById(args.userId, markerItem.id)

                        }
                        true
                    }
                    .minClusterSize(2) // Minimum cluster size
                    .clickToCenter(true) // Center map on marker click
                    .items(photoItems) // Set the items for clustering
                    .make() // Make the clustering
            }
        }
    }



    private fun observeSelectMarker() { //마커 클릭시 이미지랑 content
        uploadPhotoViewModel.selectedPost.observe(viewLifecycleOwner) {
            if(uploadPhotoViewModel.markerClick.value == true){
                binding.layoutOtherPreview.bringToFront()
                animateView(binding.layoutOtherPreview, -binding.layoutOtherPreview.width.toFloat(), 0f)
                binding.dialogVisibility = true
                binding.postData = it
                uploadPhotoViewModel.setMarkerClick(false)
            }
        }
    }

    private fun animateView(view: View, startTranslationX: Float, endTranslationX: Float) {
        view.translationX = startTranslationX
        view.isVisible = true
        view.animate()
            .translationX(endTranslationX)
            .setDuration(OtherMapFragment.ANIMATION_DURATION)
            .setListener(null)
            .start()
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

        naverMap.extent = LatLngBounds(LatLng(33.0041, 124.6094), LatLng(38.6140, 131.5928))
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val ANIMATION_DURATION = 300L
    }
}