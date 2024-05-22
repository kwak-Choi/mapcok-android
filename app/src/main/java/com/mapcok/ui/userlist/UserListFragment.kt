package com.mapcok.ui.userlist

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mapcok.R
import com.mapcok.databinding.FragmentUserListBinding
import com.mapcok.ui.base.BaseFragment
import com.mapcok.ui.photo.viewmodel.UploadPhotoViewModel
import com.mapcok.ui.userlist.adapter.UserListAdapter
import com.mapcok.ui.userlist.viewmodel.UserListViewModel
import com.mapcok.ui.util.SingletonUtil.user
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

private const val TAG = "UserListFragment_싸피"
@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>(R.layout.fragment_user_list) {

    private val userListViewModel : UserListViewModel by viewModels()
    private val uploadPhotoViewModel : UploadPhotoViewModel by viewModels()

    private lateinit var userListAdapter: UserListAdapter
    override fun initView() {
        initAdapter()
        observeData()
    }

    private fun observeData() {
        userListViewModel.getUserList()
        userListViewModel.userList.observe(viewLifecycleOwner) { userList ->
            userList.forEach { user ->
                uploadPhotoViewModel.getUserPosts(user.id)
                uploadPhotoViewModel.postListSize.observe(viewLifecycleOwner){ postCount ->
                    user.copy(userPostCount = postCount)
                }
            }
            userListAdapter.submitList(userList)
        }

//        uploadPhotoViewModel.postListSize.observe(viewLifecycleOwner) { postCount ->
//            val currentUserId = uploadPhotoViewModel.currentUserId
//            Log.d(TAG, "observeData: $currentUserId")
//            val updatedList = userListAdapter.currentList.map { user ->
//                if (user.id == currentUserId) {
//                    user.copy(userPostCnt = postCount)
//                } else {
//                    user
//                }
//            }
//            userListAdapter.submitList(updatedList)
//        }
    }


    private fun initAdapter() {
        userListAdapter = UserListAdapter { userId, userName ->
           findNavController().navigate(R.id.action_userListFragment_to_otherMapFragment,
               bundleOf(
                   "userId" to userId,
                   "userName" to userName
               )
           )
        }
        binding.rcUserList.adapter = userListAdapter
    }

}
