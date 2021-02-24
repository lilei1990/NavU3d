package com.huida.navu3d

import com.huida.navu3d.bean.PointData

/**
 * 作者 : lei
 * 时间 : 2021/02/24.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object GeoCvUt {
    /* ---从刘欢处拿的经纬度转XY的函数 START--- */
    /* B：纬度(latitude), L:经度(longitude) */
    fun GaussProject(B: Double, L: Double) {
        //3度带投影

        //3度带投影
        val beltWidth = 3
        //不同的坐标系实质上是不同的椭球参数和中央经度
        val a = 6378137.0
        val f = 298.25722101
        var beltNum: Double
        //int milatitude = 0;


        //如果没有设置中央经度
        //此段逻辑由于历史问题存在冗余，可以化简删除
        beltNum = Math.ceil((L - 1.5) / beltWidth)
        if (beltWidth == 3 && beltNum == L - 1.5) {
            beltNum += 1
        }
//        L -= beltNum * beltWidth - if (beltWidth == 6) 3 else 0
    }

    private fun MeridianLength(
        B: Double,
        a: Double,
        f: Double
    ): Double {
        var ee = 0.0
        var rB = 0.0
        var cA = 0.0
        var cB = 0.0
        var cC = 0.0
        var cD = 0.0
        var cE = 0.0


        ee = (2 * f - 1) / f / f;
        rB = B * Math.PI / 180;
        cA = 1 + 3 * ee / 4 + 45 * Math.pow(ee, 2.0) / 64 + 175 * Math.pow(ee, 3.0) / 256 +
                11025 * Math.pow(ee, 4.0) / 16384;
        cB = 3 * ee / 4 + 15 * Math.pow(ee, 2.0) / 16 + 525 * Math.pow(ee, 3.0) / 512 +
                2205 * Math.pow(ee, 4.0) / 2048;
        cC = 15 * Math.pow(ee, 2.0) / 64 + 105 * Math.pow(ee, 3.0) / 256 +
                2205 * Math.pow(ee, 4.0) / 4096;
        cD = 35 * Math.pow(ee, 3.0) / 512 + 315 * Math.pow(ee, 4.0) / 2048;
        cE = 315 * Math.pow(ee, 4.0) / 131072;
        //??П??3¤
        return a * (1 - ee) * (cA * rB - cB * Math.sin(2 * rB) / 2 + cC * Math.sin(4 * rB) / 4 -
                cD * Math.sin(6 * rB) / 6 + cE * Math.sin(8 * rB) / 8);
    }

    private fun Bl_xy(
        B: Double,
        dL: Double,
        a: Double,
        f: Double,
        beltWidth: Int
    ): PointData? {
        var ee = 0.0 //μ???фêμ??•?
        var ee2 = 0.0 //μ?????•?
        var rB = 0.0
        var tB = 0.0
        var m = 0.0
        var N = 0.0
        var it2 = 0.0
        val temp = PointData()
        ee = (2 * f - 1) / f / f
        ee2 = ee / (1 - ee)
        rB = B * Math.PI / 180
        tB = Math.tan(rB)
        m = Math.cos(rB) * dL * Math.PI / 180
        N = a / Math.sqrt(1 - ee * Math.sin(rB) * Math.sin(rB))
        it2 = ee2 * Math.pow(Math.cos(rB), 2.0)
        temp.X = m * m / 2 + (5 - tB * tB + 9 * it2 + 4 * it2 * it2) * Math.pow(
            m,
            4.0
        ) / 24 + (61 - 58 * tB * tB + Math.pow(tB, 4.0)) * Math.pow(m, 6.0) / 720
        temp.X = MeridianLength(B, a, f) + N * tB * temp.X
        temp.Y = N * (m + (1 - tB * tB + it2) * Math.pow(m, 3.0) / 6 + (5 - 18 * tB * tB + Math.pow(
            tB,
            4.0
        ) + 14 * it2 - 58 * tB * tB * it2) *
                Math.pow(m, 5.0) / 120)
        return temp
    }


    /**
     * 由经纬度反算成高斯投影坐标
     *
     * @param longitude
     * @param latitude
     * @return
     */
    fun GaussToBLToGauss(
        longitude: Double,
        latitude: Double
    ): DoubleArray? {
        var ProjNo = 0
        val ZoneWide: Int // //带宽
        val output = DoubleArray(2)
        val longitude1: Double
        val latitude1: Double
        var longitude0: Double
        val X0: Double
        val Y0: Double
        var xval: Double
        var yval: Double
        val a: Double
        val f: Double
        val e2: Double
        val ee: Double
        val NN: Double
        val T: Double
        val C: Double
        val A: Double
        val M: Double
        val iPI: Double
        iPI = 0.0174532925199433 // //3.1415926535898/180.0;
        ZoneWide = 6 // //6度带宽
        a = 6378245.0
        f = 1.0 / 298.3 // 54年北京坐标系参数
        // //a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ProjNo = (longitude / ZoneWide).toInt()
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2.toDouble()
        longitude0 = longitude0 * iPI
        longitude1 = longitude * iPI // 经度转换为弧度
        latitude1 = latitude * iPI // 纬度转换为弧度
        e2 = 2 * f - f * f
        ee = e2 / (1.0 - e2)
        NN = (a
                / Math.sqrt(
            1.0 - (e2 * Math.sin(latitude1)
                    * Math.sin(latitude1))
        ))
        T = Math.tan(latitude1) * Math.tan(latitude1)
        C = ee * Math.cos(latitude1) * Math.cos(latitude1)
        A = (longitude1 - longitude0) * Math.cos(latitude1)
        M = (a
                * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256)
                * latitude1
                - (3 * e2 / 8 + 3 * e2 * e2 / 32 + (45 * e2 * e2 * e2
                / 1024)) * Math.sin(2 * latitude1)
                + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024)
                * Math.sin(4 * latitude1) - 35 * e2 * e2 * e2 / 3072
                * Math.sin(6 * latitude1)))
        // 因为是以赤道为Y轴的，与我们南北为Y轴是相反的，所以xy与高斯投影的标准xy正好相反;
        xval = (NN
                * (A + (1 - T + C) * A * A * A / 6 + ((5 - 18 * T + T * T + (14
                * C) - 58 * ee)
                * A * A * A * A * A) / 120))
        yval = (M
                + (NN
                * Math.tan(latitude1)
                * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (((61
                - 58 * T) + T * T + 270 * C - 330 * ee)
                * A * A * A * A * A * A) / 720)))
        X0 = 1000000L * (ProjNo + 1) + 500000L.toDouble()
        Y0 = 0.0
        xval = xval + X0
        yval = yval + Y0
        output[0] = xval
        output[1] = yval
        return output
    }
}