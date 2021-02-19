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
import com.huida.navu3d.ui.activity.DomeManager
import com.huida.navu3d.ui.fragment.workTask.WorkTaskViewModel
import com.huida.navu3d.utils.GaoDeUtils
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
//        taskWorkby?.apply {
//            this.setPointA(A)
//        }
        DataMarkerA.postValue(markPoint(A))
        ToastUtils.showLong(A.toString())
    }

    /**
     * 设置B点
     */
    fun setPointB(workTaskViewModel: WorkTaskViewModel) {
        val B = mCurrenLatLng
//        taskWorkby?.apply {
//            this.setPointB(B)
//
//        }
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
        DomeManager.reset()
        DomeManager.start()
        DomeManager.setGGAListen {
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
        DomeManager.setVTGListen {
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
        DomeManager.stop()

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
        when (taskWorkby?.pointAB?.size!!) {
            0 -> {
                ToastUtils.showLong("请添加A点")
                return
            }
            1 -> {
                ToastUtils.showLong("请添加B点")
                return
            }
        }
        if (mParalleMaplLine.size > 0) {
            ToastUtils.showLong("导航线已经生成!")
            return
        }
        val A = taskWorkby?.pointAB?.get(0)!!
        val B = taskWorkby?.pointAB?.get(1)!!
        val pointData: MutableList<PointXYData> = ArrayList()
        //延长
        val length = EXTEND_LINE
        val distance = GeometryUtils.distanceOfTwoPoints(A, B)
        if (distance == 0.0) {
            ToastUtils.showLong("AB点重合,请重新设置AB点")
            return
        }
        //分别延长AB两点
        GeometryUtils.extLine(A, B, length)
        GeometryUtils.extLine(B, A, length)
        pointData.add(A)
        pointData.add(B)
        val navLineData = NavLineData()
        navLineData.startX = A.X
        navLineData.startY = A.Y
        navLineData.endX = B.X
        navLineData.endY = B.Y
        mParalleMaplLine.putAll(navLineData.budileUtmLine())
        DataParallelLine.postValue(mParalleMaplLine)
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