package com.huida.navu3d.common

import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.LiveEventBusCore
import com.jeremyliao.liveeventbus.core.Observable

/**
 * 作者 : lei
 * 时间 : 2021/02/26.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
public inline fun <T> liveEvenBus(
    key: String,
    type: Class<T>
): Observable<T> {
    return LiveEventBus.get(key,type)
}
public inline fun  liveEvenBus(
    key: String
): Observable<Any> {
    return LiveEventBus.get(key)
}