package com.huida.navu3d.ui.activity

import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.VTGSentence
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 : name数据源提供者
 */
object NameProviderManager {
    var mNmeaBuilder = NmeaBuilder.INSTANCE
    val sf = SentenceFactory.getInstance()
    lateinit var mCallBackGGA: (GGASentence) -> Unit
    lateinit var mCallBackVTG: (VTGSentence) -> Unit
    lateinit var timer: Timer
    val isDome = true
    fun start() {
        if (isDome) {
            runDome()
        }

    }

    private fun runDome() {
        timer = fixedRateTimer("", false, 0, 1000) {

            var nmeaStr = mNmeaBuilder.doTick()
            val lines = nmeaStr.lines()
            for (line in lines) {
                try {
                    //必须字符都大写,
                    val createParser = sf.createParser(line.toUpperCase())
                    //卫星信息的解析类
                    if (createParser is GGASentence) {
                        mCallBackGGA.apply {
                            mCallBackGGA(createParser)
                        }

                    }
                    //地面速度信息
                    if (createParser is VTGSentence) {
                        mCallBackVTG.apply {
                            mCallBackVTG(createParser)
                        }
                    }
                } catch (e: Exception) {
                    //数据格式不对,或者解析类没有注册进去,数据将会被丢弃
                    //LogUtils.e("数据格式不对")
                }

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
        timer.apply {
            this.cancel()
        }
    }


    fun setGGAListen(callBackGGA: (GGASentence) -> Unit) {
        mCallBackGGA = callBackGGA
    }

    fun setVTGListen(callBackVTG: (VTGSentence) -> Unit) {
        mCallBackVTG = callBackVTG
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