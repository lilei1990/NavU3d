package com.huida.navu3d.common

import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.fixedRateTimer

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 : name数据源提供者
 */
object NmeaProviderManager {
    var mNmeaBuilder = NmeaBuilder()
    val sf = SentenceFactory.getInstance()
    var mCallBackGGAs = ConcurrentHashMap<String, (GGASentence) -> Unit>()
    var mCallBackVTGs = ConcurrentHashMap<String, (VTGSentence) -> Unit>()

    val isDome = true
    var timer: Timer? = null
    fun start() {
        if (isDome) {
            //数据刷新频率
            val freq: Long = (1000 / mNmeaBuilder.nudHz).toLong()
            timer?.cancel()
            timer = fixedRateTimer("", false, 0, freq) {
                loop()
            }
        }

    }

    private fun loop() {
        var nmeaStr = mNmeaBuilder.doTick()
        val lines = nmeaStr.lines()
        for (line in lines) {
            try {
                //必须字符都大写,
                val createParser = sf.createParser(line.toUpperCase())
                //卫星信息的解析类
                if (createParser is GGASentence) {
                    for ((key, mCallBackGGA) in mCallBackGGAs) {
                        mCallBackGGA(createParser)
                    }

                }
                //地面速度信息
                if (createParser is VTGSentence) {

                    for ((key, mCallBackVTG) in mCallBackVTGs) {
                        mCallBackVTG(createParser)
                    }
                }
            } catch (e: Exception) {
                //数据格式不对,或者解析类没有注册进去,数据将会被丢弃
                //LogUtils.e("数据格式不对")
            }

        }
    }


    /**
     * 计算方向角度
     */
    fun calculate() {

//            if (abFixHeadingDelta > fix_heading) abFixHeadingDelta = fix_heading;


    }

    fun stop() {
        timer?.cancel()
    }


    fun registGGAListen(registrants: String, callBackGGA: (GGASentence) -> Unit) {
        mCallBackGGAs.put(registrants, callBackGGA)
    }

    fun registVTGListen(registrants: String, callBackVTG: (VTGSentence) -> Unit) {
        mCallBackVTGs.put(registrants, callBackVTG)
    }

    fun removeRegist(str: String) {
        mCallBackGGAs.remove(str)
        mCallBackVTGs.remove(str)
    }

    fun clearAllRegist() {
        mCallBackGGAs.clear()
        mCallBackVTGs.clear()
    }

    fun reset() {
        mNmeaBuilder = NmeaBuilder()
    }

    fun setSpeedDistance(vlue: Double) {
        if (0.0 > mNmeaBuilder.stepDistance) {
            mNmeaBuilder.stepDistance = 0.0
            return
        }
        if (mNmeaBuilder.stepDistance > 310) {
            mNmeaBuilder.stepDistance = 310.0
            return
        }

        mNmeaBuilder.stepDistance += vlue
    }

    fun left() {
        mNmeaBuilder.steerAngle = -1.5
    }
    fun right() {
        mNmeaBuilder.steerAngle =1.5
    }
    fun center() {
        mNmeaBuilder.steerAngle =0.0
    }
}