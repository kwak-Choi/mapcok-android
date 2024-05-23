package com.mapcok.ui.util

import com.mapcok.data.model.UserData
import com.mapcok.data.model.PostData

object SingletonUtil {

  var user : UserData?=null
  var photo : PostData?=null


  val baseUrl = "http://192.168.0.6:8080/"
  val gptUrl = " https://api.openai.com/v1/"

  val chatGptApi = "sk-proj-aKurzhjxFAHM3X4c2b4aT3BlbkFJYmvUVRqAZSRrvEc99E93"
}