package com.huida.navu3d.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blankj.utilcode.util.FileUtils
import com.huida.navu3d.bean.*
import com.huida.navu3d.common.Config
import com.huida.navu3d.common.Config.dataBasePath
import com.lei.base_core.BaseApp


/**
 * 数据库基本类
 */
@Database(
    entities = [WorkTaskData::class, FarmToolsData::class, FarmMachineryData::class],
    version = 5,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
//    /**
//     * 获取HistoryAudioDao
//     */
//    abstract fun historyDao(): HistoryAudioDao
//
    /**
     * 获取CollectAudioBean
     */
    abstract fun collectWorkTaskDao(): WorkTaskDao
    abstract fun collectFarmToolsDao(): FarmToolsDao
    abstract fun collectFarmMachineryDao(): FarmMachineryDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(BaseApp.getContext())
                    .also {
                        instance = it
                    }
            }
        }


        private fun buildDataBase(context: Context): AppDataBase {
            return Room
                .databaseBuilder(context, AppDataBase::class.java, dataBasePath)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                //如果版本重建后,将会破坏性的重建表
                .fallbackToDestructiveMigration()
                .build()
        }

    }

    /**
     * 删除数据库文件
     */
    fun deleteDataBase() {
        FileUtils.delete(Config.dataBasePath)
    }
}