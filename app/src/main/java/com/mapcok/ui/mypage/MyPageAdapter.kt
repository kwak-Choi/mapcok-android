package com.mapcok.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mapcok.data.model.PostData
import com.mapcok.databinding.MyPagePhotoItemBinding
import com.mapcok.ui.util.DiffUtilCallback

class MyPageAdapter() :
  ListAdapter<PostData, MyPageAdapter.ViewHolder>(
    DiffUtilCallback<PostData>()
  ) {


  private var onItemClick: ((PostData) -> Unit)? = null

  class ViewHolder(private val binding: MyPagePhotoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(postData: PostData) {
      binding.postData = postData
    }
  }

  override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.setOnClickListener {
      onItemClick?.let {
        it(getItem(position))
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder {
    val listItemBinding =
      MyPagePhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(listItemBinding)
  }


  fun setOnItemClickListener(listener: (PostData) -> Unit) {
    onItemClick = listener
  }

}