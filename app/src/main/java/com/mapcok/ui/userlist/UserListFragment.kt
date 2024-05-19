package com.mapcok.ui.userlist

import com.mapcok.R
import com.mapcok.databinding.FragmentUserListBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.userlist.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>(R.layout.fragment_user_list) {

    private lateinit var userListAdapter: UserListAdapter
    override fun initView() {
        initAdapter()
    }

    private fun initAdapter() {
        userListAdapter = UserListAdapter()
        binding.rcUserList.adapter = userListAdapter
    }

}
