package com.huchihaItachi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.huchihaItachi.database.dao.AnimeDao
import com.huchihaItachi.database.dao.PageDao
import com.huchihaItachi.database.entity.AnimeEntity
import com.huchihaItachi.database.entity.PageEntity

@Database(
  entities = [
    AnimeEntity::class,
    PageEntity::class
  ],
  version = BuildConfig.DB_VERSION,
  exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class AnimeDatabase : RoomDatabase() {

  abstract fun pageDao(): PageDao
  abstract fun animeDao(): AnimeDao
}