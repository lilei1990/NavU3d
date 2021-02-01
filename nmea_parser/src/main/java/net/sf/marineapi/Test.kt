package net.sf.marineapi

import java.util.regex.Pattern

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
fun main() {
    var nmea = "\$GPVTG,0.0,T,034.4,M,0.0,N,0.0,K*4A"
    val reChecksum = Pattern.compile(
        "^[$|!]{1}[A-Z0-9]{3,10}[,][\\x20-\\x7F]*[*][A-Fa-f0-9]{2}(\\r|\\n|\\r\\n|\\n\\r){0,1}$"
    )

    println( reChecksum.matcher(nmea).matches())
}

class Test {

}