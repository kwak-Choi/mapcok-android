package com.mapcok.ui.mypage


import MyPhotoViewModel
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.mypage.viewmodel.MyPageViewModel
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MyPageFragment_싸피"

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val photoViewModel: MyPhotoViewModel by activityViewModels()
    private val myPageViewModel : MyPageViewModel by viewModels()
    private lateinit var myPageAdapter: MyPageAdapter

    override fun initView() {
        initData()
        initAdapter()
        observePostData()
    }
    private fun initData(){
        binding.userData = SingletonUtil.user
        myPageViewModel.getPosts(SingletonUtil.user?.id ?: 0)
    }


    private fun observePostData(){
        myPageViewModel.userPost.observe(viewLifecycleOwner){
            myPageAdapter.submitList(it)
        }
    }

    private fun initAdapter(){
        myPageAdapter = MyPageAdapter()
        binding.rcMyPagePhoto.adapter = myPageAdapter


        myPageAdapter.setOnItemClickListener { myPagePhoto ->

            this.findNavController().navigate(R.id.action_myPageFragment_to_myPhotoFragment)
        }
    }
}
