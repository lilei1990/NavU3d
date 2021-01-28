package com.huida.navu3d.common

import com.blankj.utilcode.util.SDCardUtils

/**
 * 作者 : lei
 * 时间 : 2021/01/21.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object Constants {
    var pathRoot="${SDCardUtils.getSDCardPathByEnvironment()}/NavU3d/"
    //数据库路径
    var dataBasePath ="$pathRoot/dataBase.db"


    /**
     * Sp主题key false 白天模式, true 夜间模式
     */
    const val SP_THEME_KEY = "sp_theme_key"
}