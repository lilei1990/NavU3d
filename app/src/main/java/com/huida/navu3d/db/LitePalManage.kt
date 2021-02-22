package com.huida.navu3d.db

import com.huida.navu3d.bean.LineData
import org.litepal.LitePal

/**
 * 作者 : lei
 * 时间 : 2021/02/22.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object LitePalManage {
    fun findLineDataTable(){
        LitePal.deleteAll(LineData::class.java,"worktaskdata_id=?","")
    }
}