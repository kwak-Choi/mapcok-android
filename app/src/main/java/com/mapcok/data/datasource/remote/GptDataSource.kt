package com.mapcok.data.datasource.remote

import com.mapcok.data.model.GptData
import com.mapcok.data.model.param.GptParam

interface GptDataSource {

  suspend fun sendChat(gptParam: GptParam) : GptData

}