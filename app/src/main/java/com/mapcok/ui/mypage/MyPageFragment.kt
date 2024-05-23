package com.mapcok.ui.mypage


import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.mypage.viewmodel.MyPageViewModel
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val TAG = "MyPageFragment_싸피"

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
  private val myPageViewModel: MyPageViewModel by viewModels()
  private lateinit var myPageAdapter: MyPageAdapter


  override fun onResume() {
    super.onResume()
    initData()
    initAdapter()
  }
  override fun initView() {
    initData()
    initAdapter()
    observePostData()
  }


  private fun initData() {
    binding.userData = SingletonUtil.user
    myPageViewModel.getPosts(SingletonUtil.user?.id ?: 0)

  }

  private fun observePostData() {
    myPageViewModel.userPost.observe(viewLifecycleOwner) {
      binding.textPhotoCnt.text = it.size.toString()
      if(binding.textPhotoCnt.text =="0"){
        binding.tvFirstPost.visibility = View.VISIBLE
      }else{
        binding.tvFirstPost.visibility = View.GONE
      }
      myPageAdapter.submitList(it)
    }
  }


  private fun initAdapter() {
    myPageAdapter = MyPageAdapter()
    binding.rcMyPagePhoto.adapter = myPageAdapter


    myPageAdapter.setOnItemClickListener { postData ->

      this@MyPageFragment.findNavController().navigate(
        R.id.action_myPageFragment_to_myPostFragment,
        bundleOf("postData" to postData)
      )
    }
  }
}
