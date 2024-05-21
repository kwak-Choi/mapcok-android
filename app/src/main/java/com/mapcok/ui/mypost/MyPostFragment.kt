package com.mapcok.ui.mypost

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPhotoBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyPostFragment : BaseFragment<FragmentMyPhotoBinding>(R.layout.fragment_my_photo) {

  private val myPostViewModel: MyPostViewModel by activityViewModels()

  override fun initView() {
    initData()
    clickBackBtn()
    hideBottomNavigation()
    createOption()
  }

  fun setupEditOption() {
    binding.textContent.visibility = View.GONE
    binding.option.visibility = View.GONE
    binding.etContent.visibility = View.VISIBLE
    binding.btnEditSave.visibility = View.VISIBLE

    binding.btnEditSave.setOnClickListener {
      val newContent = binding.etContent.text.toString()
      val updatedPost = binding.postData?.copy(content = newContent)
      updatedPost?.let {
        myPostViewModel.updatePost(it.id,it)
        binding.etContent.visibility = View.GONE
        binding.btnEditSave.visibility = View.GONE
        binding.textContent.apply {
          text = it.content
          visibility=View.VISIBLE
        }
        binding.option.visibility = View.VISIBLE
      }
    }

  }

  fun setupDeleteOption(){
    SingletonUtil.user?.let { myPostViewModel.deletePhoto(it.id,binding.postData!!.id) }
    findNavController().navigate(R.id.action_myPhotoFragment_to_myPageFragment)
  }

  private fun createOption() {
    binding.option.setOnClickListener {
      MyPostMenuFragment().show(childFragmentManager, "MyPostMenuFragment")
    }
  }


  private fun initData() {
    val safeArgs: MyPostFragmentArgs by navArgs()
    Timber.d("데이터 확인 ${safeArgs.postData}")
    binding.postData = safeArgs.postData
    binding.userData = SingletonUtil.user
  }

  private fun clickBackBtn() {
    binding.imgBack.setOnClickListener {
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


}
