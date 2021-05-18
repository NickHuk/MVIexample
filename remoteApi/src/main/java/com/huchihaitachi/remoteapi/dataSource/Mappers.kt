package com.huchihaitachi.remoteapi.dataSource

import com.huchihaitachi.domain.Type
import com.huchihaitachi.remoteapi.type.MediaType

fun MediaType.toDomain() = when(this) {
    MediaType.MANGA -> Type.MANGA
    else -> Type.ANIME
}