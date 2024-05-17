package com.mapcok.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapcok.ui.main.MainActivity
import com.mapcok.databinding.MyPagePhotoItemBinding

class MyPageAdapter(val context: Context,private val photo: MutableList<MyPagePhoto>):RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    private lateinit var onItemClick: (MyPagePhoto) -> Unit
    inner class ViewHolder(private val binding:MyPagePhotoItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(photo: MyPagePhoto){
            binding.photoDto = photo
            binding.imageView.tag = photo
            binding.activity = context as MainActivity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder {
        val listItemBinding = MyPagePhotoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
        holder.bind(photo[position])
    }

    override fun getItemCount(): Int {
        return photo.size
    }
    fun setOnItemClickListener(listener : (MyPagePhoto) -> Unit){
        onItemClick = listener
    }


}