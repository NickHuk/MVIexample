package com.huchihaItachi.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.huchihaItachi.database.entity.PageAndAnime
import com.huchihaItachi.database.entity.PageEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PageDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(page: PageEntity): Completable

  @Transaction
  @Query("""SELECT * FROM PageEntity WHERE currentPage=:pageNum""")
  fun loadPage(pageNum: Int): Single<PageAndAnime?>

  @Query("""DELETE FROM PageEntity""")
  fun deleteAll(): Completable
}