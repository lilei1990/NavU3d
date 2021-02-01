package com.huida.navu3d.ui.activity

import android.os.Bundle
import android.util.Log
import com.esri.core.geometry.*
import com.huida.navu3d.common.Constants
import com.huida.navu3d.databinding.ActivityMainBinding
import com.lei.base_core.base.BaseVmActivity
import com.lei.base_core.utils.PrefUtils
import com.lei.base_core.utils.StatusUtils





/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : 入口activity
 */
class MainActivity : BaseVmActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
//    //偏移操作
//    val offseter = OperatorFactoryLocal
//        .getInstance()
//        .getOperator(Operator.Type.Offset) as OperatorOffset
    override fun init(savedInstanceState: Bundle?) {
    val geomList: ArrayList<Geometry> = createAllPolylines()
    val inputGeoms = SimpleGeometryCursor(geomList)
    val spatialRef = SpatialReference.create(102382)
    val distances = doubleArrayOf(120.0, 50.0)

    val outputGeoms =
        OperatorBuffer.local().execute(inputGeoms, spatialRef, distances, false, null)
    var geom: Geometry? = null
    while (outputGeoms.next().also { geom = it } != null) {
        Log.d("TAGlilei", "${geom.toString()}")
    }
}

    fun createAllPolylines(): ArrayList<Geometry> {

        // Create a list of input geometries.
        val geomList =
            ArrayList<Geometry>(5)

        // Create a list of coordinates to use for creating the polylines.
        val coords = arrayOf(
            doubleArrayOf(-11686713.0, 4828005.0),
            doubleArrayOf(-11687175.0, 4828005.0),
            doubleArrayOf(-11687337.0, 4827898.0),
            doubleArrayOf(-11687461.0, 4828009.0),
            doubleArrayOf(-11687461.0, 4828250.0),
            doubleArrayOf(-11687421.0, 4828250.0),
            doubleArrayOf(-11687305.0, 4828331.0),
            doubleArrayOf(-11687143.0, 4828237.0),
            doubleArrayOf(-11686716.0, 4828237.0),
            doubleArrayOf(-11686713.0, 4828237.0),
            doubleArrayOf(-11686713.0, 4828005.0)
        )
        geomList.add(createPolyline(coords))
        val coords2 = arrayOf(
            doubleArrayOf(-11686998.0, 4828712.0),
            doubleArrayOf(-11686998.0, 4828240.0)
        )
        geomList.add(createPolyline(coords2))
        coords2[0][0] = (-11686998).toDouble()
        coords2[0][1] = 4828001.0
        coords2[1][0] = (-11686998).toDouble()
        coords2[1][1] = 4827533.0
        geomList.add(createPolyline(coords2))
        coords2[0][0] = (-11687848).toDouble()
        coords2[0][1] = 4828618.0
        coords2[1][0] = (-11687480).toDouble()
        coords2[1][1] = 4828251.0
        geomList.add(createPolyline(coords2))
        coords2[0][0] = (-11688017).toDouble()
        coords2[0][1] = 4828250.0
        coords2[1][0] = (-11687461).toDouble()
        coords2[1][1] = 4828250.0
        geomList.add(createPolyline(coords2))
        return geomList
    }
    fun createPolyline(pts: Array<DoubleArray>): Polyline{
        val line = Polyline()
        line.startPath(pts[0][0], pts[0][1])
        for (i in 1 until pts.size) line.lineTo(pts[i][0], pts[i][1])
        return line
    }

//        binding.glsv.init()
    /**
     * 沉浸式状态,随主题改变
     */
    override fun setSystemInvadeBlack() {
        val theme = PrefUtils.getBoolean(Constants.SP_THEME_KEY, false)
        if (theme) {
            StatusUtils.setSystemStatus(this, true, false)
        } else {
            StatusUtils.setSystemStatus(this, true, true)
        }
    }
}



