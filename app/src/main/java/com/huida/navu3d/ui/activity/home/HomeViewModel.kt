package com.huida.navu3d.ui.activity.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.NavLineData
import com.huida.navu3d.bean.PointXYData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.constants.Constants.EXTEND_LINE
import com.huida.navu3d.ui.activity.NameProviderManager
import com.huida.navu3d.ui.fragment.workTask.WorkTaskViewModel
import com.huida.navu3d.utils.GaoDeUtils
import com.huida.navu3d.utils.GeoConvert
import com.huida.navu3d.utils.GeometryUtils
import com.huida.navu3d.utils.PointConvert
import java.util.*
import kotlin.math.roundToInt


class HomeViewModel : ViewModel() {
    //当前坐标
    var mCurrenLatLng = PointXYData()

    //A点坐标
    val DataMarkerA = MutableLiveData<MarkerOptions>()

    //B点坐标
    val DataMarkerB = MutableLiveData<MarkerOptions>()


    //平行线数据
    val DataParallelLine = MutableLiveData<MutableMap<Int, Polyline>>()
    val mParalleMaplLine: MutableMap<Int, Polyline> = mutableMapOf<Int, Polyline>()


    //经纬度数据,轨迹
    val DataPointXY = MutableLiveData<MutableList<PointXYData>>()
    val mPointXYData: MutableList<PointXYData> = ArrayList()

    //速度
    val DataSpeed = MutableLiveData<String>()
    var speed = ""

    //角度
    val DataSteerAngle = MutableLiveData<String>()
    var steerAngle = ""

    //卫星数
    val DataSatelliteCount = MutableLiveData<String>()
    var satelliteCount = ""

    //与最近导航线偏移距离
    val DataOffsetLineDistance = MutableLiveData<Int>()
    var offsetLineDistance = 0
    var taskWorkby: WorkTaskData? = null


    //速度,距离数据
    private val pointAB by lazy {
    }

    /**
     * 设置A点
     */
    fun setPointA(workTaskViewModel: WorkTaskViewModel) {
        val A = mCurrenLatLng
        taskWorkby?.navLineData?.apply {
            setStart(A)
            save()
        }
        taskWorkby?.save()
        DataMarkerA.postValue(markPoint(A))
        ToastUtils.showLong(A.toString())
    }

    /**
     * 设置B点
     */

    fun setPointB(workTaskViewModel: WorkTaskViewModel) {
        val B = mCurrenLatLng
        taskWorkby?.navLineData?.apply {
            setEnd(B)
            save()
        }
        taskWorkby?.save()
        DataMarkerB.postValue(markPoint(B))
        ToastUtils.showLong(B.toString())
    }

    private fun markPoint(point: PointXYData): MarkerOptions {
        val options = MarkerOptions()
        options.draggable(true)
                .snippet("DefaultMarker")
        options.position(GaoDeUtils.convertGPS(LatLng(point.lat, point.lng)))
        options.isFlat = true
        return options
    }


    /**
     * 开始
     */
    fun start() {
        NameProviderManager.reset()
        NameProviderManager.start()
        NameProviderManager.setGGAListen {
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointConvert.convertPoint(latitude, longitude)
            mPointXYData.add(pointXY)
            mCurrenLatLng = pointXY
            DataPointXY.postValue(mPointXYData)
            satelliteCount = "${it.satelliteCount}"
            DataSatelliteCount.postValue(satelliteCount)
            val p = Point(pointXY.X, pointXY.Y)
            //计算偏移的距离
            val polyline = mParalleMaplLine.get(3)
            polyline?.apply {
                //计算偏移的距离
                offsetLineDistance = (GeometryUtils.getPointToCurveDis(p, this) * 100).roundToInt()
                Log.d("TAG_lilei", "偏移距离: " + offsetLineDistance)
            }


            DataOffsetLineDistance.postValue(offsetLineDistance)
        }
        NameProviderManager.setVTGListen {
            speed = "${(it.speedKmh * 100).roundToInt() / 1000.00}Km/h"
            steerAngle = "${it.trueCourse}°"
            DataSpeed.postValue(speed)
            DataSteerAngle.postValue(steerAngle)
        }

    }


    /**
     * 停止
     */
    fun stop() {
        NameProviderManager.stop()

    }

    /**
     * 暂停
     */
    fun pause() {


    }

    /**
     * 录制
     */
    fun saveRecord() {

    }


    //补点操作
    var densifier = OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.DensifyByLength) as OperatorDensifyByLength

    /**
     * 画平行线
     */
    fun DrawMapParallelLine(workTaskViewModel: WorkTaskViewModel) {
//        A ?: return ToastUtils.showLong("请添加A点")
//        B ?: return ToastUtils.showLong("请添加B点")
        val mA =  taskWorkby?.navLineData!!.getStart()
        val mB =  taskWorkby?.navLineData!!.getEnd()
        taskWorkby?.navLineData?.apply {
            setStart(mA)
            setEnd(mB)
            save()
            taskWorkby?.save()
        }
        mParalleMaplLine.clear()
        PointConvert.convertPoint(mA.lat, mA.lng)
        taskWorkby?.navLineData?.apply {
            val pointData: MutableList<PointXYData> = ArrayList()
            //延长
            val length = EXTEND_LINE
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

    /**
     * 轨迹
     */
    fun DrawTrack(aMap: AMap) {
//        aMap.addPolyline(
//            PolylineOptions().addAll(latLngs).width(10f).color(Color.argb(255, 1, 255, 1))
//        )
    }

}