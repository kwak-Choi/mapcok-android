package com.mapcok.ui.mypage

import MyPhotoViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import com.google.android.play.integrity.internal.m
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.map.MapFragmentDirections
import com.mapcok.ui.mypage.myphoto.MyPhotoFragment
import kotlinx.coroutines.launch

private const val TAG = "MyPageFragment_싸피"
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val photoViewModel: MyPhotoViewModel by activityViewModels()
    private lateinit var listAdapter: MyPageAdapter
    private lateinit var dao: MyPageDao
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun initView() {

    }
}
