package com.mapcok.ui.userlist.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mapcok.data.model.UserData
import com.mapcok.databinding.UserListItemBinding
import com.mapcok.ui.util.DiffUtilCallback

class UserListAdapter(private val onOtherBtnClick: (Int) -> Unit) : ListAdapter<UserData, UserListAdapter.UserListViewHolder>(
    DiffUtilCallback<UserData>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding, onOtherBtnClick)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserListViewHolder(
        val binding: UserListItemBinding,
        private val onOtherBtnClick : (Int) -> Unit
    ) : ViewHolder(binding.root) {

        fun bind(userData: UserData) {
            binding.apply {
                val localUserData = userData
                this.userData = localUserData

                binding.btnOtherMap.setOnClickListener {
                    onOtherBtnClick(localUserData.id)
                }
            }
        }
    }
}