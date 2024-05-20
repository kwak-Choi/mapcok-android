package com.mapcok.ui.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.databinding.FragmentUploadBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.util.SingletonUtil
import timber.log.Timber
import java.io.File

private const val TAG = "UploadFragment_싸피"

class UploadFragment : BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val args: UploadFragmentArgs by navArgs()

    override fun initView() {
        hideBottomNavigation()
        binding.backMap.setOnClickListener {
            findNavController().navigate(R.id.action_uploadFragment_to_mapFragment)
        }
        Timber.d("확인 ${SingletonUtil.user}")
        val imageUriString = args.imagePath
        val imageUri = Uri.parse(imageUriString)
        binding.setmyimage.setImageURI(imageUri)
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
