package com.mapcok.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
  timeMillis: Long = 200L,
  coroutineScope: CoroutineScope,
  block: (T) -> Unit
): (T) -> Unit {
  var debounceJob: Job? = null
  return {
    debounceJob?.cancel()
    debounceJob = coroutineScope.launch {
      delay(timeMillis)
      block(it)
    }
  }
}

fun debounceAction(
  timeMillis: Long = 200L,
  coroutineScope: CoroutineScope,
  block: () -> Unit
): () -> Unit {
  var debounceJob: Job? = null
  return {
    debounceJob?.cancel()
    debounceJob = coroutineScope.launch {
      delay(timeMillis)
      block.invoke()
    }
  }
}