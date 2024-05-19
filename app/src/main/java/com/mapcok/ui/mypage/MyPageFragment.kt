package com.mapcok.ui.mypage

import MyPhotoViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment

import kotlinx.coroutines.launch


class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val photoViewModel: MyPhotoViewModel by activityViewModels()
    private lateinit var listAdapter: MyPageAdapter
    private lateinit var dao: MyPageDao


    override fun initView() {


        dao = MyPageDao(requireContext()).apply {
            this.dbOpenHelper(requireContext())
            this.open()
            //지금은 db에서 가져왔지만 나중에는 server에서 가져오도록
        }

        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                dao.selectAllPhotos()
            }.onSuccess {
                listAdapter = MyPageAdapter(requireContext(), it.toMutableList())
                binding.mypagePhotoRecycler.apply {
                    adapter = listAdapter
                }
                clickPhoto()
            }.onFailure {

            }
        }

        listAdapter.setOnItemClickListener { myPagePhoto ->
            photoViewModel.setSelectedPhoto(myPagePhoto)
            this.findNavController().navigate(R.id.action_myPageFragment_to_myPhotoFragment)
        }

        binding.photoCnt.text = listAdapter.itemCount.toString()

    }


    override fun onDestroy() {
        super.onDestroy()
        dao.close()
    }


    //사진 클릭
    private fun clickPhoto() {
        listAdapter.setOnItemClickListener {
            this.findNavController().navigate(R.id.action_myPageFragment_to_myPhotoFragment)
        }
    }
}
