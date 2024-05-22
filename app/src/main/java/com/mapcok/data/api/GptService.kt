package com.mapcok.data.api

import com.mapcok.data.model.GptData
import com.mapcok.data.model.param.GptParam
import retrofit2.http.Body
import retrofit2.http.POST

interface GptService {


  @POST("chat/completions")
  suspend fun sendChat(
    @Body gptParam: GptParam
  ): GptData


}