package com.mapcok.ui.mypage


import MyPhotoViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.util.SingletonUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MyPageFragment_싸피"

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val photoViewModel: MyPhotoViewModel by activityViewModels()
    private lateinit var myPageAdapter: MyPageAdapter

    override fun initView() {
        initData()
        initAdapter()
    }
    private fun initData(){
        binding.userData = SingletonUtil.user
    }


    private fun initAdapter(){
        myPageAdapter = MyPageAdapter()
        binding.rcMyPagePhoto.adapter = myPageAdapter


        myPageAdapter.setOnItemClickListener { myPagePhoto ->
            photoViewModel.setSelectedPhoto(myPagePhoto)
            this.findNavController().navigate(R.id.action_myPageFragment_to_myPhotoFragment)
        }
    }
}
