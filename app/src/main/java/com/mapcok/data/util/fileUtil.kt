package com.mapcok.data.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import java.io.File


fun Uri.toMultipartBodyBasic(context: Context, parameter: String): MultipartBody.Part {

  val uri = this
  var fileName: String = System.currentTimeMillis().toString()

  context.contentResolver.query(
    this,
    arrayOf(MediaStore.Images.Media.DISPLAY_NAME),
    null,
    null,
    null
  )?.use { cursor ->
    if (cursor.moveToFirst()) {
      fileName = cursor.getString(
        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
      )
    }
  }

  val requestBody = object : RequestBody() {
    override fun contentType(): MediaType = "image/jpeg".toMediaType()


    override fun writeTo(sink: BufferedSink) {
      context.contentResolver.openInputStream(uri)?.source()?.use { source ->
        sink.writeAll(source)
      }
    }

    override fun contentLength(): Long {
      return context.contentResolver.openInputStream(uri)?.available()?.toLong() ?: -1
    }

  }


  return MultipartBody.Part.createFormData(parameter, fileName, requestBody)
}


fun File.toMultipartBodyBasic(parameter: String): MultipartBody.Part {


  var fileName: String = System.currentTimeMillis().toString()


  val requestBody = this.asRequestBody("image/jpeg".toMediaTypeOrNull())

  return MultipartBody.Part.createFormData(parameter, fileName, requestBody)
}