package com.huida.navu3d

/**
 * 作者 : lei
 * 时间 : 2021/01/28.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
fun main() {
    1.takeIf { it==null }.apply { this }
////    val pointA = PointXYData()
////    pointA.X=300.0
////    pointA.Y=400.0
////    val pointB= PointXYData()
////    pointB.X=600.0
////    pointB.Y=800.0
////
////    ParallelLine.extLine(pointB,pointA,45)
////    ParallelLine.extLine(pointA,pointB,45)
//    //116.407387,39.904179
//    var latitude = 39.904179
//    var longitude = 116.407388
//    val convertLatLonToUTM = GeoConvert.INSTANCE.convertLatLonToUTM(latitude, longitude)
//    val point = Point()
//    point.x=convertLatLonToUTM[0]
//    point.y=convertLatLonToUTM[1]
//    val utmLatlng = GeoConvert.INSTANCE.convertUTMToLatLon(point.x, point.y)
//    println("utmLatlng--${utmLatlng[0]}--${utmLatlng[1]}")
////    val utmToGaoDe = ParallelLine.utmToGaoDe(point)
////    println("utmToGaoDe--${utmToGaoDe.toString()}")
////    {"paths":[[[449433.4289060007,4416851.087770141],[449251.1533241979,4417748.918379956]]]}
////    {"paths":[[[449433.43380603974,4416851.088764937],[449251.158224237,4417748.919374752]]]}
////    {"paths":[[[449433.4293960046,4416851.087869621],[449251.1538142018,4417748.918479436]]]}
////    {"paths":[[[449433.43331603584,4416851.0886654565],[449251.1577342331,4417748.919275272]]]}
////    {"paths":[[[449349.64529884775,4416840.576213984],[449338.1632718387,4417745.369415121]]]}
////    {"paths":[[[449349.6502984452,4416840.576277429],[449338.1682714361,4417745.369478567]]]}
//    GeoConvert.INSTANCE.convertUTMToLatLon(449349.64529884775, 4416840.576213984)
//    GeoConvert.INSTANCE.convertUTMToLatLon(449349.6502984452, 4416840.576277429)
}

