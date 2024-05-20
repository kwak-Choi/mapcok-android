package com.mapcok.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapcok.databinding.MyPagePhotoItemBinding

class MyPageAdapter() :
  RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

  private val photo = mutableListOf<MyPagePhoto>()

  private var onItemClick: ((MyPagePhoto) -> Unit)? = null

  inner class ViewHolder(private val binding: MyPagePhotoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: MyPagePhoto) {
      binding.photoDto = photo
      binding.imageView.tag = photo
    }
  }

  override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
    holder.bind(photo[position])
    holder.itemView.setOnClickListener {
      onItemClick?.let {
        it(photo[position])
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder {
    val listItemBinding =
      MyPagePhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(listItemBinding)
  }

  override fun getItemCount(): Int {
    return photo.size
  }

  fun setOnItemClickListener(listener: (MyPagePhoto) -> Unit) {
    onItemClick = listener
  }

  fun addPhotoData(value: List<MyPagePhoto>) {
    photo.addAll(value)
    notifyDataSetChanged()
  }
}