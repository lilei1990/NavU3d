package com.huida.navu3d.ui.activity.u3d

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.PointXYData
import com.huida.navu3d.ui.activity.DomeManager
import com.huida.navu3d.utils.GeoConvert
import java.util.*
import kotlin.math.roundToInt


class U3dViewModel : ViewModel() {
    //当前坐标
    var mCurrenLatLng = PointXYData()

    //A点坐标
    val DataMarkerA = MutableLiveData<MarkerOptions>()
    var A = PointXYData()

    //B点坐标
    val DataMarkerB = MutableLiveData<MarkerOptions>()
    var B = PointXYData()


    //平行线数据
    val DataParallelLine = MutableLiveData<MutableList<Polyline>>()
    val mParalleMaplLine: MutableList<Polyline> = ArrayList()


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

    //速度,距离数据
    private val pointAB by lazy {
    }

    /**
     * 设置A点
     */
    fun setPointA() {
        A = mCurrenLatLng
        DataMarkerA.postValue(markPoint(A))
        ToastUtils.showLong(A.toString())
    }

    private fun markPoint(point: PointXYData): MarkerOptions {
        val options = MarkerOptions()
        options.draggable(true)
            .snippet("DefaultMarker")
        options.position(LatLng(point.latGC102, point.lngGC102))
        options.isFlat = true
        return options
    }

    /**
     * 设置B点
     */
    fun setPointB() {
        B = mCurrenLatLng
        DataMarkerB.postValue(markPoint(B))
        ToastUtils.showLong(B.toString())

    }


    /**
     * 开始
     */
    fun start(u3dActivity: U3dActivity) {
        DomeManager.reset()
        DomeManager.start()
        DomeManager.setGGAListen {
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointXYData()
            val convertUTM = GeoConvert.INSTANCE.convertLatLonToUTM(latitude, longitude)
            val convertGaode =
                GeoConvert.INSTANCE.gaoDeConvert(LatLng(latitude, longitude), u3dActivity)
            pointXY.lat = latitude
            pointXY.lng = longitude
            pointXY.latGC102 = convertGaode.latitude
            pointXY.lngGC102 = convertGaode.longitude
            pointXY.X = convertUTM[0]
            pointXY.Y = convertUTM[1]
//            LogUtils.e(pointXY.toString())
            mPointXYData.add(pointXY)
            mCurrenLatLng = pointXY
            DataPointXY.postValue(mPointXYData)
            satelliteCount = "${it.satelliteCount}"
            DataSatelliteCount.postValue(satelliteCount)
            val p = Point(pointXY.X, pointXY.Y)
            //排序
            mParalleMaplLine.sortWith(
                Comparator { o1, o2 ->
                    -((ParallelLine.getPointToCurveDis(p, o2) - ParallelLine.getPointToCurveDis(
                        p,
                        o1
                    ))*100).roundToInt()
                }
            )
            if (mParalleMaplLine.size > 0) {
                offsetLineDistance = ParallelLine.getPointToCurveDis(p, mParalleMaplLine[0]).roundToInt()
            }
            DataOffsetLineDistance.postValue(offsetLineDistance)
        }
        DomeManager.setVTGListen {
            speed = "${(it.speedKmh * 100).roundToInt() / 100.00}Km/h"
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
    fun DrawMapParallelLine() {
        if (mParalleMaplLine.size > 0) {
            ToastUtils.showLong("导航线已经生成!")
            return
        }
        val pointData: MutableList<PointXYData> = ArrayList()
        //延长
        val length = 450
        //分别延长AB两点
        ParallelLine.extLine(A, B, length)
        ParallelLine.extLine(B, A, length)
        pointData.add(A)
        pointData.add(B)
        mParalleMaplLine.addAll(ParallelLine.budileUtmLine(pointData))
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

    /**
     * 查询上次设置的Ab点
     */
    fun findByPointAB() {
//        find<T>(PointXYData::class.java, id)
//
//        pointAB.findByType(CurrentWorkTask.task.sortId, 1)?.apply {
//            A = this
//            DataMarkerA.postValue(markPoint(A))
//            ToastUtils.showLong(A.toString())
//        }
//        pointAB.findByType(CurrentWorkTask.task.sortId, 2)?.apply {
//            B = this
//            DataMarkerA.postValue(markPoint(B))
//            ToastUtils.showLong(B.toString())
//        }

    }

    /**
     * 保存Ab点
     */
    fun insertPointAB() {
//        pointAB.insert()
    }
}