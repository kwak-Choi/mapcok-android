package com.mapcok.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapcok.databinding.MyPagePhotoItemBinding
import timber.log.Timber

class MyPageAdapter(val context: Context, private val photo: MutableList<MyPagePhoto>) :
  RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    private  var onItemClick: ((MyPagePhoto) -> Unit)? = null
    inner class ViewHolder(private val binding:MyPagePhotoItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(photo: MyPagePhoto){
            binding.photoDto = photo
            binding.imageView.tag = photo
        }
    }

  private var onItemClick: ((MyPagePhoto) -> Unit)?= null


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


  override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
    holder.bind(photo[position])
    holder.itemView.setOnClickListener {
      Timber.d("클릭 리스너 확인")
      onItemClick?.let {
        it(photo[position])
      }
    }
  }

  override fun getItemCount(): Int {
    return photo.size
  }

  fun setOnItemClickListener(listener: (MyPagePhoto) -> Unit) {
    onItemClick = listener
  }


}