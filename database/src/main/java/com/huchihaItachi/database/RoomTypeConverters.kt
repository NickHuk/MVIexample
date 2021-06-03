package com.huchihaItachi.database

import androidx.room.TypeConverter
import com.huchihaitachi.domain.Season
import com.huchihaitachi.domain.Type

class RoomTypeConverters {

  val Type?.dbFormat: String?
    @TypeConverter get() = this?.name

  val String?.type: Type?
    @TypeConverter get() = this?.let(Type::valueOf)

  val Season?.dbFormat: String?
    @TypeConverter get() = this?.name

  val String?.season: Season?
    @TypeConverter get() = this?.let(Season::valueOf)
}