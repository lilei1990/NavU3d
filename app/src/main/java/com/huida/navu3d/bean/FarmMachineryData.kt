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
@Entity(tableName = "farm_machinery")
class FarmMachineryData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Int = 0
    //轴距
    @ColumnInfo(name = "wheelbase")
    var wheelbase: Double? = 0.0
    //天线到前轴距离
    @ColumnInfo(name = "antennaToFront")
    var antennaToFront: Double? = 0.0
    //天线高度
    @ColumnInfo(name = "antennaHeight")
    var antennaHeight: Double? = 0.0
    //悬挂到后轴距离
    @ColumnInfo(name = "suspensionToBack")
    var suspensionToBack: Double? = 0.0
    //天线到中心距离
    @ColumnInfo(name = "antennaToCenter")
    var antennaToCenter: Double? = 0.0
}