package com.huida.navu3d.constants

/**
 * 作者 : lei
 * 时间 : 2021/01/21.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object Constants {
//    var pathRoot="${SDCardUtils.getSDCardPathByEnvironment()}/NavU3d/"
//    //数据库路径
//    var dataBasePath ="$pathRoot/dataBase.db"


    /**
     * Sp主题key false 白天模式, true 夜间模式
     */
    var SP_THEME_KEY = "sp_theme_key"

    //线的延长长度,单位米
    var EXTEND_LINE = 10000

    //平行线的间隔
    var lineOffset = 10

    //是否录制
    var isRecord = false

    //犁具宽度
    var interval: Float = 1.5f

    // 重叠/遗漏,重叠：负数,遗漏：正数
    var overlapskip: Float = 0.3f
}