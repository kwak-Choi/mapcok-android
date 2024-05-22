package com.mapcok.ui.upload

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.data.model.param.PostParam
import com.mapcok.databinding.FragmentUploadBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File

private const val TAG = "UploadFragment_싸피"

@AndroidEntryPoint
class UploadFragment : BaseFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

  private val args: UploadFragmentArgs by navArgs()
  private val uploadPhotoViewModel: UploadPhotoViewModel by activityViewModels()
  private lateinit var imageUri: Uri
  private var type = true
  private var file: File? = null
  override fun initView() {
    initData()
    clickBackBtn()
    clickSaveBtn()
    hideBottomNavigation()
  }

  private fun initData() {
    val imageUriString = args.imagePath
    type = args.type
    if (type) {
      file = uploadPhotoViewModel.imageFile.value
    }
    Timber.d("이미지 데이터확인 $imageUriString")
    imageUri = Uri.parse(imageUriString)
    binding.photoUrl = imageUri.toString()
  }

  //업로드 클릭
  private fun clickSaveBtn() {
    binding.saveBtn.setOnClickListener {
      Timber.d("게시글 업로드 클릭")
      val userId = SingletonUtil.user?.id

      uploadPhotoViewModel.location.value?.let { location ->
        val (latitude, longitude) = location
        val content = binding.etContent.text
        if (content.isNotEmpty()) {
          uploadPhotoViewModel.registerPost(
            PostParam(
              userId ?: 0,
              imageUri,
              content.toString(),
              latitude,
              longitude,
              file
            ),
            type
          )
          observeUploadSuccess()
        } else {
          Toast.makeText(requireActivity(), "게시글 내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
      }

    }
  }


  //뒤로가기
  private fun clickBackBtn() {
    binding.backMap.setOnClickListener {
      it.findNavController().popBackStack()
    }

  }

  //업로드 관찰
  private fun observeUploadSuccess() {
    uploadPhotoViewModel.registerPostSuccess.observe(viewLifecycleOwner) { success ->
      if (success) {
        Toast.makeText(requireContext(), "사진 업로드 성공", Toast.LENGTH_SHORT).show()
        this.findNavController().popBackStack()
      } else {
        Toast.makeText(requireContext(), "사진 업로드 실패", Toast.LENGTH_SHORT).show()
      }
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
}
