package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.*
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.utils.GeometryUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.collections.ArrayList


/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :HomeFragmentBean
 */
class HomeFragmentBean {


    var status = MutableLiveData<Status>()

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


    //是否录制轨迹
    var isRecord = MutableLiveData<Boolean>(Constants.isRecord)

    //点的队列
    val mPointQueue = ConcurrentLinkedDeque<PointData>()


    //抽稀操作
    val generalizer =
        OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.Generalize) as OperatorGeneralize

    //当前所在的utm区域编号
    var lngZone: Int? = null
    var latZone: Char? = null

    //当前记录点的线段
    var trackLine = MutableLiveData<TrackLineData>()

    //轨迹的历史数据
    var trackLineHistory = MutableLiveData<ArrayList<TrackLineData>>()

    var workTaskData: WorkTaskData? = null

    /**
     * 计算gga数据
     */
    fun putGGA(gga: GGASentence) {
        val position = gga.position
        val latitude = position.latitude
        val longitude = position.longitude
        val point = PointData.build(latitude, longitude)
        lngZone = point.lngZone
        latZone = point.latZone
        currenLatLng.postValue(point)
        satelliteCount.postValue(gga.satelliteCount)
        //如果开始录制
        if (isRecord.value!!) {
            mPointQueue.addFirst(point)
        }
        val p = Point(point.x, point.y)
        //计算偏移的距离
//        val polyline = mParalleMaplLine.get(3)

        loop()
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
    fun creatGuideLine() {
        if (pointA.value == null) {
            ToastUtils.showLong("请设置A点")
            return
        }
        if (pointB.value == null) {
            ToastUtils.showLong("请设置B点")
            return
        }
        val mA = pointA.value!!.copy()
        val mB = pointB.value!!.copy()
        if (mA.x <= 0 || mA.y <= 0) {
            ToastUtils.showLong("A点的值不正确:${mA.x}--${mA.y}")
            return
        }
        if (mB.x <= 0 || mB.y <= 0) {
            ToastUtils.showLong("B点的值不正确:${mB.x}--${mB.y}")
            return
        }
        val pointData: MutableList<PointData> = ArrayList()
        //延长
        val length = Constants.EXTEND_LINE
        //计算两点之间的距离
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
        navLineData.startX = mA.x
        navLineData.startY = mA.y
        navLineData.endX = mB.x
        navLineData.endY = mB.y
        mParalleMaplLine.putAll(navLineData.budileUtmLine())
        DataParallelLine.postValue(mParalleMaplLine)

    }


    /**
     * 是否录制轨迹
     */

    fun isRecord(b: Boolean) {

        //每次重新录制就重新初始化一条线
        GlobalScope.launch(Dispatchers.IO) {

            newLine()
        }
        isRecord.postValue(b)

    }

    /**
     * //每次重新录制就重新初始化一条线
     */
    private fun newLine() {
        val trackLine = TrackLineData()
        trackLine.workTaskId = workTaskData?.getId()!!
        trackLine.save()
        this.trackLine.postValue(trackLine)
    }

    /**
     * 循环进行数据处理,对数据进行抽稀
     */
    private fun loop() {
        if (isRecord.value!! && mPointQueue.size > 100) {
            mPointQueue.first.apply {
                //将原始离散点转换成折线
                val outpm = Polyline()
                outpm.startPath(this.x, this.y)
                var len = mPointQueue.pollFirst()
                while (len != null) {
                    outpm.lineTo(len.x, len.y)
                    len = mPointQueue.pollFirst()
                }

                //1 传入点抽稀
                var outputGeom = generalizer.execute(
                    outpm,
                    0.03,
                    true,
                    null
                ) as Polyline

                val pathSize = outputGeom.getPathSize(0)
                for (i in (0 until pathSize)) {
                    val point = outputGeom.getPoint(i)
                    savePoint(PointData.build(lngZone!!, latZone!!, point.x, point.y))

                }
            }

        }
    }

    /**
     * 存储数据
     */
    fun savePoint(pointXY: PointData) {
        pointXY.trackLineId = trackLine.value?.getId()!!
        pointXY.save()
    }


    enum class Status {
        START, PAUSE, STOP
    }
}