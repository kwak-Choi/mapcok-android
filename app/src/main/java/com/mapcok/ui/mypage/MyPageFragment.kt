package com.mapcok.ui.mypage

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.myphoto.MyPhotoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

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

        binding.photoCnt.text = listAdapter.itemCount.toString()

    }


    override fun onDestroy() {
        super.onDestroy()
        dao.close()
    }



    //사진 클릭
    private fun clickPhoto(){
        listAdapter.setOnItemClickListener {
            this.findNavController().navigate(R.id.action_myPageFragment_to_myPhotoFragment)
        }
    }
}
