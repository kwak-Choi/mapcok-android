package com.mapcok.ui.map

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseMapFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
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
import ted.gun0912.clustering.naver.TedNaverClustering
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "MapFragment"

@AndroidEntryPoint
class MapFragment : BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap

    lateinit var file: File

    override var mapView: MapView? = null

    lateinit var currentPhotoPath: String


    override fun initOnCreateView() {
        initMapView()
    }

    override fun initOnMapReady(naverMap: NaverMap) {
        initNaverMap(naverMap)
        setMarkers()  // Pass clusterer to setMarkers
        observeSelectMarker()
    }

    override fun initViewCreated() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        clickEventListener()
        binding.dialogVisibility = false


    }

    private fun initMapView() {
        mapView = binding.mapView
        mapView?.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun initOnResume() {}

    private fun initNaverMap(naverMap: NaverMap) { // 위치 및 naverMap 세팅
        this.naverMap = naverMap
        this.naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        this.naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        this.naverMap.locationTrackingMode = LocationTrackingMode.Follow
        this.naverMap.minZoom = 6.0
        this.naverMap.maxZoom = 21.0
        getLastLocation(naverMap)
        uploadPhotoViewModel.getUserPosts(SingletonUtil.user?.id ?: 0)
    }

    private fun getLastLocation(map: NaverMap) { // 마지막 위치 가져오기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission(requireActivity())
        requireContext().requestMapPermission {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val loc = if (location == null) {
                    LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                } else {
                    LatLng(location.latitude, location.longitude)
                }
                map.cameraPosition = CameraPosition(loc, DEFAULT_ZOOM)
                map.locationTrackingMode = LocationTrackingMode.Follow

                uploadPhotoViewModel.setLocation(location.latitude, location.longitude)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: 내 클러스터 ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "pause: 내 클러스터 ")
    }

    override fun onStop() {
        super.onStop()
//    clusterer.clear()
        Log.d(TAG, "stop: 내 클러스터 ")
    }

    //클릭 이벤트
    private fun clickEventListener() {
        binding.loadMenu.setOnClickListener {
            if (binding.loadCamera.visibility == View.VISIBLE) {
                hideFab()
            } else {
                showFab()
            }
            setMarkers()
        }

        binding.loadCamera.setOnClickListener {
            capture()
        }
        binding.loadGallery.setOnClickListener {
            getPicture()
        }

        binding.imgCancelPreview.setOnClickListener {
            animateView(binding.layoutPreview, 0f, -binding.layoutPreview.width.toFloat())
            binding.dialogVisibility = false
        }

        binding.imgGptSearch.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_gptFragment)
        }

    }

    private fun showFab() {
        binding.loadCamera.visibility = View.VISIBLE
        binding.loadGallery.visibility = View.VISIBLE
        val showAnimation =
            AnimationUtils.loadAnimation(requireActivity(), com.mapcok.R.anim.fab_show)
        binding.loadCamera.startAnimation(showAnimation)
        binding.loadGallery.startAnimation(showAnimation)
    }

    private fun hideFab() {
        val hideAnimation =
            AnimationUtils.loadAnimation(requireActivity(), com.mapcok.R.anim.fab_hide)
        binding.loadCamera.startAnimation(hideAnimation)
        binding.loadGallery.startAnimation(hideAnimation)
        hideAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.loadCamera.visibility = View.INVISIBLE
                binding.loadGallery.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun capture() { //카메라 요청
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = createImageFile()
        uploadPhotoViewModel.setImageFile(file)
        val photoUri = FileProvider.getUriForFile(requireContext(), "com.mapcok.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        requestCamera.launch(intent)
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                this@MapFragment.findNavController().navigate(
                    R.id.action_mapFragment_to_upLoadFragment,
                    bundleOf("imagePath" to currentPhotoPath)
                )
            } else {
                Timber.d("이미지 캡처 실패")
            }
        }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun getPicture() { //갤러리
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        galleryResult.launch(intent)
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val imageUri = it.data?.data
                imageUri?.let { uri ->
                    val uriString = uri.toString()
                    this@MapFragment.findNavController().navigate(
                        R.id.action_mapFragment_to_upLoadFragment,
                        bundleOf("imagePath" to uriString, "type" to false)
                    )
                }
            }
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

    private fun observeSelectMarker() { //마커 클릭시 이미지랑 content
        uploadPhotoViewModel.selectedPost.observe(viewLifecycleOwner) {
            if (uploadPhotoViewModel.markerClick.value == true) {
                binding.layoutPreview.bringToFront()
                animateView(binding.layoutPreview, -binding.layoutPreview.width.toFloat(), 0f)
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
            .setDuration(Companion.ANIMATION_DURATION)
            .setListener(null)
            .start()
    }

    private fun setMarkers() {
        uploadPhotoViewModel.postList.observe(viewLifecycleOwner) { postList ->
            val photoItems = postList.map { post ->
                PhotoItem(post.id, LatLng(post.latitude, post.longitude))
            }

            Timber.d("마커 개수는 ${photoItems.size}")

            context?.let { context ->
                TedNaverClustering.with<PhotoItem>(context, naverMap)
                    .customMarker {
                        Marker().apply {
                            icon = OverlayImage.fromResource(R.drawable.photomarker)
                            width = 220
                            height = 220
                        }
                    }
                    .clusterClickListener { clusterItem ->
                        val clusterLatLng = LatLng(clusterItem.position.latitude, clusterItem.position.longitude)
                        val cameraUpdate = CameraUpdate.scrollAndZoomTo(clusterLatLng, naverMap.maxZoom).animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)

                        // 동적으로 minClusterSize 설정
                        val minClusterSize = if (naverMap.cameraPosition.zoom <= 10.0) 1 else 20
                        applyClustering(photoItems, minClusterSize)
                    }
                    .markerClickListener { markerItem ->
                        if (markerItem is PhotoItem) {
                            val clickedLatLng = LatLng(markerItem.getTedLatLng().latitude, markerItem.getTedLatLng().longitude)
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(clickedLatLng, naverMap.maxZoom).animate(CameraAnimation.Easing)
                            naverMap.moveCamera(cameraUpdate)
                            Timber.d("마커 확인 클릭")
                            uploadPhotoViewModel.setMarkerClick(true)
                            SingletonUtil.user?.let { user ->
                                uploadPhotoViewModel.getPhotoById(user.id, markerItem.id)
                            }
                        }
                        true
                    }
                    .minClusterSize(1) // 기본 minClusterSize 설정
                    .clickToCenter(true)
                    .items(photoItems)
                    .make()
            }
        }
    }


    private fun applyClustering(photoItems: List<PhotoItem>, minClusterSize: Int) {
        context?.let { context ->
            TedNaverClustering.with<PhotoItem>(context, naverMap)
                .customMarker {
                    Marker().apply {
                        icon = OverlayImage.fromResource(R.drawable.photomarker)
                        width = 220
                        height = 220
                    }
                }
                .clusterClickListener { clusterItem ->
                    val clusterLatLng = LatLng(clusterItem.position.latitude, clusterItem.position.longitude)
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(clusterLatLng, naverMap.maxZoom).animate(CameraAnimation.Easing)
                    naverMap.moveCamera(cameraUpdate)
                    applyClustering(photoItems, 1)


                }
                .markerClickListener { markerItem ->
                    if (markerItem is PhotoItem) {
                        val clickedLatLng = LatLng(markerItem.getTedLatLng().latitude, markerItem.getTedLatLng().longitude)
                        val cameraUpdate = CameraUpdate.scrollAndZoomTo(clickedLatLng, naverMap.maxZoom).animate(CameraAnimation.Easing)
                        naverMap.moveCamera(cameraUpdate)
                        Timber.d("마커 확인 클릭")
                        uploadPhotoViewModel.setMarkerClick(true)
                        SingletonUtil.user?.let { user ->
                            uploadPhotoViewModel.getPhotoById(user.id, markerItem.id)
                        }
                    }
                    true
                }
                .minClusterSize(minClusterSize) // 변경된 minClusterSize 설정
                .clickToCenter(true)
                .items(photoItems)
                .make()
        }
    }




    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val ANIMATION_DURATION = 300L
        const val DEFAULT_LATITUDE = 37.563242272383114
        const val DEFAULT_LONGITUDE = 126.92566852521531
        const val DEFAULT_ZOOM = 15.0
    }
}
