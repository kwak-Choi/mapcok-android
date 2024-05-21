package com.mapcok.ui.myphoto

import MyPhotoViewModel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPhotoBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.mypage.MyPageFragment
import com.mapcok.ui.mypage.MyPagePhoto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPhotoFragment : BaseFragment<FragmentMyPhotoBinding>(R.layout.fragment_my_photo) {

    private val photoViewModel: MyPhotoViewModel by activityViewModels()

    override fun initView() {
//        photoViewModel.selectedPhoto.observe(viewLifecycleOwner) { photo ->
//            binding.photoDto = photo
//        }
        hideBottomNavigation()
        binding.backmypage.setOnClickListener {
          it.findNavController().popBackStack()
        }
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


    companion object {
        private const val LOCATION = "location"
        private const val DATE = "date"
        private const val SRC = "src"

        @JvmStatic
        fun newInstance(photo: MyPagePhoto) =
            MyPhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(LOCATION, photo.location)
                    putLong(DATE, photo.date)
                    putString(SRC, photo.src)
                }
            }
    }
}
