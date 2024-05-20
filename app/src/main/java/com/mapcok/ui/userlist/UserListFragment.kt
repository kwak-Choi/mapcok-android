package com.mapcok.ui.userlist

import androidx.fragment.app.viewModels
import com.mapcok.R
import com.mapcok.databinding.FragmentUserListBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.userlist.adapter.UserListAdapter
import com.mapcok.ui.userlist.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>(R.layout.fragment_user_list) {

    private val userListViewModel : UserListViewModel by viewModels()

    private lateinit var userListAdapter: UserListAdapter
    override fun initView() {
        initAdapter()
        observeData()
    }

    private fun observeData(){
        userListViewModel.getUserList()
        userListViewModel.userList.observe(viewLifecycleOwner){
            userListAdapter.submitList(it)
        }
    }

    private fun initAdapter() {
        userListAdapter = UserListAdapter()
        binding.rcUserList.adapter = userListAdapter
    }

}
