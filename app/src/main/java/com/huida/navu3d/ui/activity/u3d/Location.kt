package com.huida.navu3d.ui.activity.u3d

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.blankj.utilcode.util.LogUtils


/**
 * 作者 : lei
 * 时间 : 2021/01/27.
 * 邮箱 :416587959@qq.com
 * 描述 : 地图定位
 */
class Location : AMapLocationListener {
    var mListener: OnLocationChangedListener? = null
    var mlocationClient: AMapLocationClient? = null
    var mLocationOption: AMapLocationClientOption? = null

    /**
     * 激活定位
     */
    fun activate(context: Context,listener: OnLocationChangedListener) {
        mListener = listener
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = AMapLocationClient(context)
            //初始化定位参数
            mLocationOption = AMapLocationClientOption()
            //设置定位回调监听
            mlocationClient!!.setLocationListener(this)
            //设置为高精度定位模式
            mLocationOption!!.locationMode = AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient!!.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient!!.startLocation() //启动定位
        }
    }

    /**
     * 停止定位
     */
    fun deactivate() {
        mListener = null
        if (mlocationClient != null) {
            mlocationClient!!.stopLocation()
            mlocationClient!!.onDestroy()
        }
        mlocationClient = null
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                && amapLocation.getErrorCode() === 0
            ) {
                mListener!!.onLocationChanged(amapLocation) // 显示系统小蓝点
            } else {
                val errText = "定位失败," + amapLocation.getErrorCode()
                    .toString() + ": " + amapLocation.getErrorInfo()
                LogUtils.e(errText)
            }
        }
    }
}