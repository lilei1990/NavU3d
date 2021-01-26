package com.huida.navu3d.ui.activity

import android.os.Bundle
import android.widget.SeekBar
import com.amap.api.location.CoordinateConverter
import com.amap.api.location.DPoint
import com.amap.api.maps.AMap
import com.huida.navu3d.common.BindingView
import com.huida.navu3d.databinding.ActivityMainBinding
import com.lei.base_core.base.BaseVmActivity
import com.lei.base_core.common.binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 入口activity
 */
class MainActivity : BaseVmActivity<ActivityMainBinding>(ActivityMainBinding::inflate)
     {
//    private val mStartDistance = -1 //初始小车位置(米)
//    var angle = 30.0
//
//
//    //平行线的间隔宽
//    val interval = 250
//    val lineLengt = 3000
    override fun init(savedInstanceState: Bundle?) {


//        binding.glsv.init()
//        var converter = CoordinateConverter(this)
//        // CoordType.GPS 待转换坐标类型
//        converter.from(CoordinateConverter.CoordType.GPS)
//// sourceLatLng待转换坐标点 DPoint类型  116.397564,39.90694
//        val dPoint = DPoint()
//        dPoint.latitude=39.90694
//        dPoint.longitude=116.397564
//        converter.coord(dPoint)
//// 执行转换操作
//        val desLatLng: DPoint = converter.convert()
//
//        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                angle = progress.toDouble()
//                binding.tvProgress.text = "$progress"
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
//            }
//
//        })
//        launch {
//            delay(2000)
//            val width = binding.glsv.width
//            val height = binding.glsv.height
//            while (true) {
//                delay(1000)
//                binding.glsv.doDrawLine()
//            }

//            while (true) {
//                delay(200)
//                //x轴间隔
//                val a = interval / Math.sin(Math.toRadians(angle))
//                //y轴的高度
//                val b = lineLengt * Math.sin(Math.toRadians(angle))
//                //ex轴偏移量
//                val bx = lineLengt * Math.cos(Math.toRadians(angle)) - a
//                Log.d("TAG---", "a:$a b:$b bx:$bx ")
//                var xies = ArrayList<XY>()
//                for (i in 1..10) {
//                    var xy = XY()
//                    xy.x = (i * a).toFloat()
//                    xy.y = 0.0F
//                    xy.ex = (i * a + bx).toFloat()
//                    xy.ey = b.toFloat()
//                    xies.add(xy)
//                }
//
//
//                binding.glsv.doDrawLine(xies as ArrayList<XY>?)
//            }
//        }

    }


}

