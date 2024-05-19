package com.mapcok.ui.upload

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.data.model.param.UserPhotoParam
import com.mapcok.databinding.FragmentUploadBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.util.SingletonUtil
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment : BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val args: UploadFragmentArgs by navArgs()
    private val uploadPhotoViewModel: UploadPhotoViewModel by viewModels()

    override fun initView() {
        val currentLatLng = arguments?.getParcelable<LatLng>("current_latlng")

        hideBottomNavigation()
        binding.backMap.setOnClickListener {
            findNavController().navigate(R.id.action_uploadFragment_to_mapFragment)
        }

        val imageUriString = args.imagePath
        val imageUri = Uri.parse(imageUriString)
        binding.setmyimage.setImageURI(imageUri)

        binding.saveBtn.setOnClickListener {
            val userId = SingletonUtil.user?.id ?: return@setOnClickListener
            val photoParam = currentLatLng?.let { it1 ->
                UserPhotoParam(userId, imageUriString,
                    it1
                )
            }
            if (photoParam != null) {
                uploadPhotoViewModel.addPhoto(photoParam)
            }
        }

        Log.d(TAG, "initView: ${currentLatLng}")
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    private fun hideBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.GONE
    }

    private fun showBottomNavigation() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_main)?.visibility =
            View.VISIBLE
    }
}
