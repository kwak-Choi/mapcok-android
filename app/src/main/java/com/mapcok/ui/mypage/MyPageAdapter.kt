package com.mapcok.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mapcok.data.model.PostData
import com.mapcok.databinding.MyPagePhotoItemBinding
import com.mapcok.ui.util.DiffUtilCallback
import com.mapcok.ui.util.SingletonUtil.photo

class MyPageAdapter() :
  ListAdapter<PostData, MyPageAdapter.MyPageViewHolder>(
    DiffUtilCallback<PostData>()
  ) {


  private var onItemClick: ((PostData) -> Unit)? = null

  class MyPageViewHolder(private val binding: MyPagePhotoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: PostData) {
      binding.photoDto = photo
      binding.imageView.tag = photo
    }
  }

  override fun onBindViewHolder(holder: MyPageAdapter.MyPageViewHolder, position: Int) {
    holder.bind(getItem(position))
    holder.itemView.setOnClickListener {
      onItemClick?.let {
        it(getItem(position))
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.MyPageViewHolder {
    val listItemBinding =
      MyPagePhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MyPageViewHolder(listItemBinding)
  }


  fun setOnItemClickListener(listener: (PostData) -> Unit) {
    onItemClick = listener
  }


}