package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.GuideLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.TrackLineData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.utils.GeometryUtils
import com.huida.navu3d.utils.PointConvert
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.concurrent.fixedRateTimer


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

    //历史数据
    val workTaskData = MutableLiveData<WorkTaskData>()

    //轨迹线
    var trackLineData = MutableLiveData<TrackLineData>()

    //是否录制轨迹
    var isRecord = false

    //点的队列
    val mPointQueue = ConcurrentLinkedDeque<PointData>()

    //抽稀操作
    val generalizer =
        OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.Generalize) as OperatorGeneralize


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
        //如果开始录制
        if (isRecord) {
            mPointQueue.addFirst(pointXY)
        }
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
     * 创建到引导线
     */
    fun creatGuideLine(mA: PointData, mB: PointData) {
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
        val navLineData = GuideLineData()
        navLineData.startX = mA.X
        navLineData.startY = mA.Y
        navLineData.endX = mB.X
        navLineData.endY = mB.Y
        mParalleMaplLine.putAll(navLineData.budileUtmLine())
        DataParallelLine.postValue(mParalleMaplLine)

    }

    /**
     * 添加历史数据
     */
    fun setWorkTaskData(taskData: WorkTaskData) {
        workTaskData.postValue(taskData)
    }

    /**
     * 创建导航线
     */
    fun creatNavLine() {
        workTaskData.value?.apply {
            trackLineData.postValue(TrackLineData())
            lines?.add(trackLineData.value!!)
        }
    }

    /**
     * 是否录制轨迹
     */

    fun isRecord(b: Boolean) {
        isRecord = b
        loop()
    }

    /**
     * 循环进行数据处理
     */
    private fun loop() {
        if (isRecord && mPointQueue.size > 100) {
            mPointQueue.first.apply {
                //将原始离散点转换成折线
                val outpm = Polyline()
                outpm.startPath(this.X, this.Y)
                var len = mPointQueue.pollFirst()
                while (len != null) {
                    outpm.lineTo(len.X, len.Y)
                    len = mPointQueue.pollFirst()
                }
                //1 传入点抽稀
                var outputGeom = generalizer.execute(
                    outpm,
                    0.3,
                    true,
                    null
                ) as Polyline
            }

        }
    }

}