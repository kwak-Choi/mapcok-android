package com.mapcok.ui.map

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapcok.R
import com.mapcok.databinding.FragmentMapBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.map.MapFragmentDirections
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.properties.Delegates


private const val TAG = "MapFragment_싸피"

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()
    private lateinit var mapView: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap
    private var lat by Delegates.notNull<Double>()
    private var lon by Delegates.notNull<Double>()
    lateinit var file: File

    private val builder = Clusterer.Builder<PhotoItem>()


    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        checkLocationPermissionAndFetchLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.loadMenu.setOnClickListener {
            if (binding.loadCamera.visibility == View.VISIBLE) {
                hideFab()
            } else {
                showFab()
            }
        }
        binding.loadCamera.setOnClickListener {
            capture()
        }
        binding.loadGallery.setOnClickListener {
            getPicture()
        }

    }
    private fun checkLocationPermissionAndFetchLocation() {
        // 위치 권한 체크
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            // 위치 정보 가져오기
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    uploadPhotoViewModel.setLocation(lat, lon)
                    Log.d(TAG, "checkLocationPermissionAndFetchLocation: $lat,$lon")
                } else {
                    Log.e(TAG, "Failed to get current location")
                }
            }
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
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.loadCamera.visibility = View.INVISIBLE
                binding.loadGallery.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }

    private fun capture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = createImageFile()
        val photoUri = FileProvider.getUriForFile(requireContext(), "com.mapcok.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        requestCamera.launch(intent)
    }



    lateinit var currentPhotoPath: String

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        galleryResult.launch(intent)
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val action = MapFragmentDirections.actionMapFragmentToUpLoadFragment(currentPhotoPath)
                findNavController().navigate(action)
            } else {
                Log.d(TAG, "Image capture failed or cancelled")
            }
        }
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val imageUri = it.data?.data
                imageUri?.let { uri ->
                    val uriString = uri.toString()
                    val action = MapFragmentDirections.actionMapFragmentToUpLoadFragment(uriString)
                    findNavController().navigate(action)
                }
            }
        }

    private fun initMapView() {
        mapView = childFragmentManager.findFragmentById(com.mapcok.R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(com.mapcok.R.id.map_view, it)
                    .commit()
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
                marker.icon = OverlayImage.fromResource(com.mapcok.R.drawable.photomarker)
                marker.width = 220
                marker.height = 220
                marker.onClickListener = Overlay.OnClickListener {
                    if (it is Marker) {

                        val photoItem = keyTagMap.keys.find { item -> item.position == it.position }
                        val id = photoItem?.id
                        Log.d(TAG, "Clicked Marker ID: $id")


                        val clickedLatLng = it.position
                        Log.d(TAG, "Clicked Marker LatLng: $clickedLatLng")

                        val cameraUpdate = CameraUpdate.scrollAndZoomTo(clickedLatLng, 18.0)
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
