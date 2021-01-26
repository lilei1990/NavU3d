package com.huida.navu3d.common

import com.blankj.utilcode.util.SDCardUtils

/**
 * 作者 : lei
 * 时间 : 2021/01/21.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object Config {
   var pathRoot="${SDCardUtils.getSDCardPathByEnvironment()}/NavU3d/"
    //数据库路径
    var dataBasePath ="$pathRoot/dataBase.db"
}