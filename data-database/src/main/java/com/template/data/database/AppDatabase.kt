package com.template.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.template.data.database.dao.PostDao
import com.template.data.database.entity.PostEntity

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = true // 推荐开启，用于数据库迁移
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}