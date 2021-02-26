package com.huida.navu3d.common

import kotlinx.coroutines.delay
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
    var mNmeaBuilder = NmeaBuilder.INSTANCE
    val sf = SentenceFactory.getInstance()
    var mCallBackGGAs = ConcurrentHashMap<String, (GGASentence) -> Unit>()
    var mCallBackVTGs = ConcurrentHashMap<String, (VTGSentence) -> Unit>()
    val freq: Long = (1000 / mNmeaBuilder.nudHz).toLong()
    val isDome = true
    var isRun = false
    suspend fun start() {
        isRun = true
        if (isDome) {
            while (isRun) {
                loop()
                delay(freq)
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
        isRun = false
    }


    fun registGGAListen(registrants: String, callBackGGA: (GGASentence) -> Unit) {
        mCallBackGGAs.put(registrants, callBackGGA)
    }

    fun registVTGListen(registrants: String, callBackVTG: (VTGSentence) -> Unit) {
        mCallBackVTGs.put(registrants, callBackVTG)
    }

    fun clearAllRegist() {
        mCallBackGGAs.clear()
        mCallBackVTGs.clear()
    }

    fun reset() {
        mNmeaBuilder.speed = 0.0
        mNmeaBuilder.stepDistance = 0.0
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

    fun setAngle(vlue: Double) {
        if (-1 > mNmeaBuilder.steerAngle) {
            mNmeaBuilder.steerAngle = -1.0
            return
        }
        if (mNmeaBuilder.steerAngle > 1) {
            mNmeaBuilder.steerAngle = 1.0
            return
        }

        mNmeaBuilder.steerAngle += vlue

    }
}