package com.huida.navu3d.common

import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable

/**
 * 作者 : lei
 * 时间 : 2021/02/26.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
fun <T> liveEvenBus(
    key: Enum<BusEnum>,
    type: Class<T>
): Observable<T> {
    return LiveEventBus.get(key.name, type)
}

 fun liveEvenBus(
    key: Enum<BusEnum>
): Observable<Any> {
    return LiveEventBus.get(key.name)
}

enum class BusEnum {
    SELECT_WORK_TASK_DATA,
    DB_WORK_TASK_DATA,
    DB_TRACK_LINE,
    DB_POINT,

    //viewPage 切换
    TO_PAGE_HOME,
    TO_PAGE_MAIN,
    TO_PAGE_MAIN_MENU,
    TO_PAGE_FARM_MACHINERY,
    TO_PAGE_FARM_TOOLS,
    TO_PAGE_WORK_TASK,
    TO_PAGE_SETTING,
    TO_EXIT,
    //Nmea
    NMEA_GGA,
    NMEA_VTG,
}