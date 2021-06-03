package com.huchihaitachi.domain

fun Dirtyable.isDirty(currentTime: Long): Boolean {
  val timeDifference = currentTime - timeOfBirth
  return timeDifference < 0 || timeDifference > timeToStale
}

fun Dirtyable.isEmptyOrDirty(currentTime: Long): Boolean {
  return this is EmptyDirtyable || isDirty(currentTime)
}