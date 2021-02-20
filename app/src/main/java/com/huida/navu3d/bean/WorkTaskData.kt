package com.huida.navu3d.bean

import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.constants.PointType
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport

/**
 * 作者 : lei
 * 时间 : 2021/01/15.
 * 邮箱 :416587959@qq.com
 * 描述 :作业任务数据模型
 */

class WorkTaskData : LitePalSupport() {

    var sortId: Int = 0

    //创建时间
    var date: Long = System.currentTimeMillis()

    //地块名称
    var name: String = ""

    //创建者
    var creator: String = ""

    //作业类型
    var area: Int = 0

    //农具
    var farmTools: Int = 0

    //ab点
    var pointAB: ArrayList<PointXYData>? = ArrayList()

    //平行线数据,包含AB点
    var navLineData: NavLineData? = NavLineData()
    //轨迹数据

    var LineXYDatas: ArrayList<LineXYData>? = null


    //参考线
    //历史轨迹
    //标记,引导标记


}


