package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.Point
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.NavLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.utils.GeometryUtils
import com.huida.navu3d.utils.PointConvert
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import java.util.ArrayList


/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :HomeFragmentBean
 */
class HomeFragmentBean {


    //与最近导航线偏移距离
    var offsetLineDistance = MutableLiveData<Int>()

    //卫星数
    var satelliteCount = MutableLiveData<Int>()

    //角度
    var steerAngle = MutableLiveData<Double>()

    //速度
    var speed = MutableLiveData<Double>()

    //当前坐标点
    var currenLatLng = MutableLiveData<PointData>()

    //A点
    var pointA = MutableLiveData<PointData>()

    //B点
    var pointB = MutableLiveData<PointData>()

    //平行线数据
    val DataParallelLine = MutableLiveData<MutableMap<Int, Polyline>>()
    val mParalleMaplLine: MutableMap<Int, Polyline> = mutableMapOf<Int, Polyline>()
    /**
     * 计算gga数据
     */
    fun putGGA(gga: GGASentence) {
        val position = gga.position
        val latitude = position.latitude
        val longitude = position.longitude
        val pointXY = PointConvert.convertPoint(latitude, longitude)
        currenLatLng.postValue(pointXY)
        satelliteCount.postValue(gga.satelliteCount)
        val p = Point(pointXY.X, pointXY.Y)
        //计算偏移的距离
//        val polyline = mParalleMaplLine.get(3)
    }

    /**
     * 计算vtg数据
     */
    fun putVTG(vtg: VTGSentence) {
        speed.postValue(vtg.speedKmh)
        steerAngle.postValue(vtg.trueCourse)
    }

    /**
     * 记录a b点
     */
    fun creatPointA() {
        pointA.postValue(currenLatLng.value)
    }

    /**
     * 记录a b点
     */
    fun creatPointB() {
        pointB.postValue(currenLatLng.value)
    }

    /**
     * 创建到导航线
     */
    fun creatGuideLine(mA:PointData,mB:PointData) {
        //没有任何意义,仅仅是历史数据加载时更新zoom,确定utm区域
        PointConvert.convertPoint(mA.lat, mA.lng)
        val pointData: MutableList<PointData> = ArrayList()
        //延长
        val length = Constants.EXTEND_LINE
        val distance = GeometryUtils.distanceOfTwoPoints(mA, mB)
        if (distance == 0.0) {
            ToastUtils.showLong("AB点重合,请重新设置AB点")
            return
        }
        //分别延长AB两点
        GeometryUtils.extLine(mA, mB, length)
        GeometryUtils.extLine(mB, mA, length)
        pointData.add(mA)
        pointData.add(mB)
        val navLineData = NavLineData()
        navLineData.startX = mA.X
        navLineData.startY = mA.Y
        navLineData.endX = mB.X
        navLineData.endY = mB.Y
        mParalleMaplLine.putAll(navLineData.budileUtmLine())
        DataParallelLine.postValue(mParalleMaplLine)

    }

}