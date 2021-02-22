package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.LineData
import com.huida.navu3d.bean.NavLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.constants.Constants.EXTEND_LINE
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.ui.fragment.workTask.WorkTaskViewModel
import com.huida.navu3d.utils.GeometryUtils
import com.huida.navu3d.utils.PointConvert
import org.litepal.LitePal
import java.util.*
import kotlin.math.roundToInt


class HomeViewModel : ViewModel() {
    //当前坐标
    val DataCurrenLatLng = MutableLiveData<PointData>()
    var mCurrenLatLng = PointData()

    //平行线数据
    val DataParallelLine = MutableLiveData<MutableMap<Int, Polyline>>()
    val mParalleMaplLine: MutableMap<Int, Polyline> = mutableMapOf<Int, Polyline>()


    //经纬度数据,轨迹
    val DataPointXY = MutableLiveData<MutableList<LineData>>()
    val mLinesData: MutableList<LineData> = ArrayList()

    //速度
    val DataSpeed = MutableLiveData<Double>()
    var speed = 0.0

    //角度
    val DataSteerAngle = MutableLiveData<Double>()
    var steerAngle = 0.0

    //卫星数
    val DataSatelliteCount = MutableLiveData<String>()
    var satelliteCount = ""

    //与最近导航线偏移距离
    val DataOffsetLineDistance = MutableLiveData<Int>()
    var offsetLineDistance = 0
    var taskWorkby: WorkTaskData? = null

    //A点
    val DataPointA = MutableLiveData<PointData>()
    private var pointA: PointData?= null

    //B点
    val DataPointB = MutableLiveData<PointData>()
    private var pointB: PointData?= null

    /**
     * 设置A点
     */
    fun setPointA() {
        pointA = mCurrenLatLng
        taskWorkby?.navLineData?.apply {
            setStart(pointA!!)
            save()
        }
        taskWorkby?.save()
        DataPointA.postValue(pointA)

    }

    /**
     * 设置B点
     */

    fun setPointB() {
        pointB = mCurrenLatLng
        taskWorkby?.navLineData?.apply {
            setEnd(pointB!!)
            save()
        }
        taskWorkby?.save()
        DataPointB.postValue(pointB)
    }



    /**
     * 开始
     */
    fun start() {

        NameProviderManager.reset()
        NameProviderManager.start()
        val lineDbManage = LineDbManage()
        lineDbManage.build(taskWorkby)

//        //每次点start的时候就代表一个新的线段
//        val lineXYData = LineXYData()
//        taskWorkby?.LineXYDatas?.add(lineXYData)
//        lineXYData.save()
//        taskWorkby?.save()
////        val concurrentHashMap = ConcurrentHashMap<String, PointXYData>()
//        mLinesData.add(lineXYData)
        NameProviderManager.setGGAListen {
            val position = it.position
            val latitude = position.latitude
            val longitude = position.longitude
            val pointXY = PointConvert.convertPoint(latitude, longitude)
            //存储点
            lineDbManage.savePoint(pointXY)
            mCurrenLatLng = pointXY
            DataCurrenLatLng.postValue(mCurrenLatLng)
//            DataPointXY.postValue(mLinesData)
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
            speed =it.speedKmh
            steerAngle =it.trueCourse
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
        //没有任何意义,仅仅是历史数据加载时更新zoom,确定utm区域
        PointConvert.convertPoint(mA.lat, mA.lng)
        taskWorkby?.navLineData?.apply {
            val pointData: MutableList<PointData> = ArrayList()
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
    fun DrawTrack() {
//        aMap.addPolyline(
//            PolylineOptions().addAll(latLngs).width(10f).color(Color.argb(255, 1, 255, 1))
//        )
    }

}