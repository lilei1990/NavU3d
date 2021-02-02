package com.huida.navu3d.bean

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.litepal.crud.LitePalSupport
import java.sql.Time

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :农机参数
 */
class FarmToolsData: LitePalSupport() {

    //农具名称
    var name: String? = ""

    //宽幅
    var width: Double? = 0.0

    //偏移
    var offset: Double? = 0.0

    //农具中心到悬挂点
    var centerToSuspension: Double? = 0.0

    //重叠/遗漏
    var overlapping: Double? = 0.0

    override fun toString(): String {
        return "FarmToolsData( name=$name, width=$width, offset=$offset, centerToSuspension=$centerToSuspension, overlapping=$overlapping)"
    }

}