package com.mapcok.data.datasource.remote

import com.mapcok.data.api.GptService
import com.mapcok.data.model.GptData
import com.mapcok.data.model.param.GptParam
import javax.inject.Inject

class GptDataSourceImpl @Inject constructor(
  private val gptService: GptService
) : GptDataSource {

  override suspend fun sendChat(gptParam: GptParam): GptData {
    return gptService.sendChat(gptParam)
  }
}