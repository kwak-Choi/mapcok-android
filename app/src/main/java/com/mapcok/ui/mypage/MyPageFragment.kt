package com.mapcok.ui.mypage

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import com.mapcok.R
import com.mapcok.databinding.FragmentMyPageBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.mypage.myphoto.MyPhotoFragment
import kotlinx.coroutines.launch

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

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
            val photoFragment = MyPhotoFragment.newInstance(myPagePhoto)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_main, photoFragment)
                .addToBackStack(null)
                .commit()
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
