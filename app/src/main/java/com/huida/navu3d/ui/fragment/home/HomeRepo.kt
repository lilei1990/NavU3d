package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.esri.core.geometry.*
import com.huida.navu3d.bean.GuideLineData
import com.huida.navu3d.bean.PointData
import com.huida.navu3d.bean.TrackLineData
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.constants.Constants
import com.huida.navu3d.utils.GeometryUtils
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import uk.me.jstott.jcoord.LatLng
import uk.me.jstott.jcoord.UTMRef
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :具体实现类,主要数据的处理
 */
class HomeRepo(
    val viewModelScope: CoroutineScope,
    val errorLiveData: MutableLiveData<ApiException>,
    homeVM: HomeVM
) {
    //监听回调标识
    val REGIST_FLAG = "HomeViewModel"

    var status = MutableLiveData<Status>()

    //与最近导航线偏移距离
    var offsetLineDistance = MutableLiveData<Double>()

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
    val mParallelLine: MutableMap<Int, Polyline> = mutableMapOf<Int, Polyline>()


    //是否录制轨迹
    var isRecord = MutableLiveData<Boolean>(Constants.isRecord)

    //作业里程
    var workLength = MutableLiveData<Double>(0.0)

    //点的队列
    val mPointQueue = ConcurrentLinkedDeque<PointData>()


    //抽稀操作
    val generalizer =
        OperatorFactoryLocal
            .getInstance()
            .getOperator(Operator.Type.Generalize) as OperatorGeneralize


    //当前记录点的线段
    var trackLine = MutableLiveData<TrackLineData>()

    //轨迹的历史数据
    var trackLineHistory = MutableLiveData<ArrayList<TrackLineData>>()

    var workTaskData: WorkTaskData? = null


    /**
     * 开始
     */
    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(Status.START)
            NmeaProviderManager.start()
            NmeaProviderManager.registGGAListen(REGIST_FLAG) {
                receive(it)
            }
            NmeaProviderManager.registVTGListen(REGIST_FLAG) {
                receive(it)
            }
        }
    }

    /**
     * 停止
     */
    fun stop() {
        status.postValue(Status.STOP)
        NmeaProviderManager.removeRegist(REGIST_FLAG)
        NmeaProviderManager.reset()
        NmeaProviderManager.stop()
    }

    /**
     * 暂停
     */
    fun pause() {
        status.postValue(Status.PAUSE)
        NmeaProviderManager.removeRegist(REGIST_FLAG)
        NmeaProviderManager.stop()
    }

    /**
     * 设置A点
     */
    fun setPointA() {
        creatPointA()
    }

    /**
     * 设置B点
     */

    fun setPointB() {
        creatPointB()
    }


    /**
     * 录制
     */
    fun openRecord() {

        isRecord(true)
    }


    /**
     * 画引导线
     */
    fun drawGuideLine() {
        creatGuideLine()
    }

    /**
     * 接收GGA
     */
    fun receive(gga: GGASentence) {
        putGGA(gga)
    }

    /**
     * 接收VTG
     */
    fun receive(vtg: VTGSentence) {
        putVTG(vtg)
    }


    /**
     * 计算gga数据
     */
    fun putGGA(gga: GGASentence) {
        val position = gga.position
        val latitude = position.latitude
        val longitude = position.longitude
        val point = PointData(latitude, longitude)

        currenLatLng.postValue(point)
        satelliteCount.postValue(gga.satelliteCount)
        //如果开始录制
        if (isRecord.value!!) {
            mPointQueue.addFirst(point)
        }
        //计算偏移的距离
        computeDistance()
        //计算工作长度,计算面积
        computerWorkLength()

        loop()
    }

    /**
     *    //计算偏移的距离
     */
    fun computeDistance() {
        val data = currenLatLng.value!!
        val toUTMRef = LatLng(data.lat, data.lng).toUTMRef()
        val p = Point(toUTMRef.easting, toUTMRef.northing)
        val value = DataParallelLine.value
        val len = 0
        value?.forEach { t, u ->
            val a = GeometryUtils.getPointToCurveDis(p, u).toInt()
            if (t == 0) {
                offsetLineDistance.postValue(GeometryUtils.getPointToCurveDis(p, u))
            }
        }
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
        val value = currenLatLng.value
        pointA.postValue(value)
        workTaskData?.guideLineData?.apply {
            setStart(value!!)
            save()
        }
        workTaskData?.save()
    }

    /**
     * 记录a b点
     */
    fun creatPointB() {
        val value = currenLatLng.value
        pointB.postValue(value)
        workTaskData?.guideLineData?.apply {
            setEnd(value!!)
            save()
        }
        workTaskData?.save()
    }

    /**
     * 创建到引导线
     */
    fun creatGuideLine() {
        if (workTaskData?.guideLineData?.getStart() == null) {
            ToastUtils.showLong("请设置A点")
            return
        }
        if (workTaskData?.guideLineData?.getEnd() == null) {
            ToastUtils.showLong("请设置B点")
            return
        }
        val mA = workTaskData?.guideLineData?.getStart()!!.toUtm()
        val mB = workTaskData?.guideLineData?.getEnd()!!.toUtm()
        if (mA.easting <= 0 || mA.northing <= 0) {
            ToastUtils.showLong("A点的值不正确:${mA.easting}--${mA.northing}")
            return
        }
        if (mB.easting <= 0 || mB.northing <= 0) {
            ToastUtils.showLong("B点的值不正确:${mB.easting}--${mB.northing}")
            return
        }
        val pointData: MutableList<UTMRef> = ArrayList()
        //延长
        val length = Constants.EXTEND_LINE
        //计算两点之间的距离
        val distance = GeometryUtils.distanceOfTwoPoints(mA, mB)
        if (distance == 0.0) {
            ToastUtils.showLong("AB点重合,请重新设置AB点")
            return
        }
//        //分别延长AB两点
        GeometryUtils.extLine(mA, mB, length)
        GeometryUtils.extLine(mB, mA, length)
        pointData.add(mA)
        pointData.add(mB)
        val navLineData = GuideLineData()
        navLineData.setStart(mA)
        navLineData.setEnd(mB)
        DataParallelLine.postValue(navLineData.budileUtmLine())


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
                val toUtm = this.toUtm()
                //将原始离散点转换成折线
                val outpm = Polyline()
                outpm.startPath(toUtm.easting, toUtm.northing)
                var len = mPointQueue.pollFirst()
                while (len != null) {
                    val utm = len.toUtm()
                    outpm.lineTo(utm.easting, utm.northing)
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
                    val utm = currenLatLng.value?.toUtm()!!
                    val d = UTMRef(utm.lngZone, utm.latZone, point.x, point.y).toLatLng()
                    savePoint(PointData(d.latitude, d.longitude))

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

    /**
     * 计算作业历程
     */
    fun computerWorkLength() {
        var length = workLength.value
        viewModelScope.launch(Dispatchers.IO) {
            val trackLineData1 = workTaskData?.trackLineData
            trackLineData1?.forEachIndexed { index, trackLineData ->
                length = length?.plus(
                    GeometryUtils.distanceOfTwoPoints(
                        trackLineData.findFirst().toUtm(),
                        trackLineData.findLast().toUtm()
                    )
                )

            }
            workLength.postValue(
                length
            )
        }

    }

    enum class Status {
        START, PAUSE, STOP
    }


}
