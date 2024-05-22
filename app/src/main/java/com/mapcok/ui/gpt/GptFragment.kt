package com.mapcok.ui.gpt

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mapcok.R
import com.mapcok.databinding.FragmentGptBinding
import com.mapcok.ui.base.BaseBottomSheetFragment
import com.mapcok.ui.gpt.viewmodel.GptViewModel
import com.mapcok.ui.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class GptFragment : BaseBottomSheetFragment<FragmentGptBinding>(R.layout.fragment_gpt, 100) {

  private val gptViewModel: GptViewModel by viewModels()

  private lateinit var gptAdapter: GptAdapter



  override fun initViewCreated() {
    initAdapter()
    sendMessage()
    observeMessage()
  }


  private fun initAdapter() {
    gptAdapter = GptAdapter()
    binding.rcChatContent.adapter = gptAdapter
  }

  private fun observeMessage() {
    gptViewModel.gptData.observe(viewLifecycleOwner){
      Timber.d("gpt 요오 ${it}")

      gptAdapter.submitList(it)
      lifecycleScope.launch {
        delay(500L)
        binding.loadingVisible = false
        binding.rcChatContent.scrollToPosition(0)
      }
    }
  }

  private fun sendMessage() {
    binding.imgSend.setOnClickListener {
      binding.loadingVisible = true
      requireActivity().hideKeyboard(binding.etChat)
      val value = binding.etChat.text.toString()
      gptViewModel.gptData.addAll(listOf(GptMessageItem("user",value )))
      gptViewModel.sendChat(value)
      binding.etChat.clearFocus()
      binding.etChat.text.clear()
    }
  }
}