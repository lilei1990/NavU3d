package com.huida.navu3d

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
fun main() {
    var a=null
    1.takeIf { it==null }.apply { this }
    a?: print("判断为空")
}

