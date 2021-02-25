package com.huida.navu3d.ui.fragment.dome

import android.os.Bundle
import com.huida.navu3d.common.NmeaProviderManager
import com.huida.navu3d.databinding.FragmentDomeBinding
import com.kongqw.rockerlibrary.view.RockerView
import com.lei.core.base.BaseVmFragment

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 : dome控件
 */
class DomeFragment: BaseVmFragment<FragmentDomeBinding>(FragmentDomeBinding::inflate) {
    override fun init(savedInstanceState: Bundle?) {
        binding.apply {
            initRockView()
        }
    }

    private fun initRockView() {
        binding.rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
        binding.rockerView.setOnShakeListener(
            RockerView.DirectionMode.DIRECTION_8,
            object : RockerView.OnShakeListener {
                override fun onStart() {

                }

                var offsetAngle = 0.01
                var offsetSpeedDistance = 0.005
                override fun direction(direction: RockerView.Direction) {
                    when (direction) {
                        RockerView.Direction.DIRECTION_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                            NmeaProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                            NmeaProviderManager.setAngle(-offsetAngle)
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                            NmeaProviderManager.setAngle(offsetAngle)
                            NmeaProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }

                    }
                }

                override fun onFinish() {

                }
            })
    }
}