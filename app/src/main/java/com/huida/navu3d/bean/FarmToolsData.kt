package com.huida.navu3d.bean

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :农机参数
 */
@Entity(tableName = "farm_tools")
class FarmToolsData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Int = 0

    //农具名称
    @ColumnInfo(name = "name")
    var name: String? = ""

    //宽幅
    @ColumnInfo(name = "width")
    var width: Double? = 0.0

    //偏移
    @ColumnInfo(name = "offset")
    var offset: Double? = 0.0

    //农具中心到悬挂点
    @ColumnInfo(name = "centerToSuspension")
    var centerToSuspension: Double? = 0.0

    //重叠/遗漏
    @ColumnInfo(name = "overlapping")
    var overlapping: Double? = 0.0

    override fun toString(): String {
        return "FarmToolsData(sortId=$sortId, name=$name, width=$width, offset=$offset, centerToSuspension=$centerToSuspension, overlapping=$overlapping)"
    }

}