package com.huida.navu3d.bean

import androidx.room.*
import java.sql.Time

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :作业任务数据模型
 */
@Entity(tableName = "work_task")
class WorkTaskData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Int = 0

    //创建时间
    @ColumnInfo(name = "date")
    var date: Long = System.currentTimeMillis()

    //地块名称
    @ColumnInfo(name = "name")
    var name: String = ""

    //创建者
    @ColumnInfo(name = "creator")
    var creator: String = ""

    //作业类型
    @ColumnInfo(name = "area")
    var area: Int = 0

    //农具
    @ColumnInfo(name = "farmTools")
    var farmTools: Int = 0

    //参考线
    //历史轨迹
    //标记,引导标记
}