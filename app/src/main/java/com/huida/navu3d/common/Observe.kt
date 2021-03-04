package com.huida.navu3d.common

import android.app.Application
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.TrackLineData
import com.huida.navu3d.bean.WorkTaskData
import org.litepal.LitePal

/**
 * 作者 : lei
 * 时间 : 2021/03/04.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class Observe {

}

private fun initLitpal(application: Application?) {
    LitePal.initialize(application!!.applicationContext);
    //LitePal.deleteAll(WorkTaskData::class.java)
//LitePal.deleteAll(PointData::class.java)
//LitePal.deleteAll(GuideLineData::class.java)
//LitePal.deleteAll(TrackLineData::class.java)
    liveEvenBus(BusEnum.DB_TRACK_LINE, TrackLineData::class.java)
        .observeForever {
            it.save()
        }
    liveEvenBus(BusEnum.DB_POINT, PointData::class.java)
        .observeForever {
            it.save()
        }
    liveEvenBus(BusEnum.DB_WORK_TASK_DATA, WorkTaskData::class.java)
        .observeForever {
            it.save()
        }
}


