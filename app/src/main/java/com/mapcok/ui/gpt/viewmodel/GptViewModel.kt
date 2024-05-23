package com.mapcok.ui.gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapcok.data.datasource.remote.GptDataSource
import com.mapcok.data.model.param.GptParam
import com.mapcok.ui.gpt.GptMessageItem
import com.mapcok.ui.gpt.toGptMessageItem
import com.mapcok.ui.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import hustle.com.util.custom.ListLiveData
import hustle.com.util.server.ResultWrapper
import hustle.com.util.server.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GptViewModel @Inject constructor(
  private val gptDataSource: GptDataSource
) : ViewModel() {


  val gptData = ListLiveData<GptMessageItem>()


  val gptDataSuccess = SingleLiveEvent<Boolean>()

  fun sendChat(answer: String) {
    viewModelScope.launch {
      when (val response = safeApiCall(Dispatchers.IO) {
        gptDataSource.sendChat(
          GptParam(
            messages = listOf(
              GptParam.Message(
                role = "system",
                content = "당신은 여행을 어디로 갈지 고민하고 있는 사람들에게" +
                    "도움을 주는 능력 있는 여행 가이드입니다."
              ),
              GptParam.Message(
                role = "user",
                content = answer
              )
            ),
            model = "gpt-4o"
          )
        )
      }) {
        is ResultWrapper.Success -> {
          gptData.addAll(response.data.choices.map {
            it.toGptMessageItem()
          })
          gptDataSuccess.value = true
          Timber.d("gpt 전송 성공! ${response.data}")
        }

        is ResultWrapper.GenericError -> {

          Timber.d("gpt 에러 ${response.message}")
        }

        is ResultWrapper.NetworkError -> {
          Timber.d("gpt 네트워크 에러")
        }
      }
    }
  }

}