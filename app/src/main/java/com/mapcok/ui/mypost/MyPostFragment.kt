package com.mapcok.ui.mypost

import android.view.View
import androidx.navigation.findNavController
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


  override fun initView() {
    initData()
    clickBackBtn()
    hideBottomNavigation()
    createOption()
  }

  private fun createOption() {
    binding.option.setOnClickListener {

    }
  }

  private fun initData() {
    val safeArgs: MyPostFragmentArgs by navArgs()
    Timber.d("데이ㅓㅌ 확인 ${safeArgs.postData}")
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
