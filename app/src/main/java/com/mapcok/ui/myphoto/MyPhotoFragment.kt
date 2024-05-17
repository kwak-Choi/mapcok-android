package com.mapcok.ui.myphoto

import MyPhotoViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPhotoBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.mypage.MyPagePhoto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPhotoFragment : BaseFragment<FragmentMyPhotoBinding>(R.layout.fragment_my_photo) {

    private val photoViewModel :MyPhotoViewModel by activityViewModels()


    override fun initView() {
        photoViewModel.selectedPhoto.observe(viewLifecycleOwner){photo ->
            binding.photoDto = photo
        }
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