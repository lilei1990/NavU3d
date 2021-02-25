package com.huida.navu3d.ui.fragment.dome

import android.os.Bundle
import com.huida.navu3d.common.NameProviderManager
import com.huida.navu3d.databinding.FragmentDomeBinding
import com.huida.navu3d.databinding.FragmentHomeBinding
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
                            NameProviderManager.setAngle(-offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                        }
                        RockerView.Direction.DIRECTION_UP -> {
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN -> {
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_LEFT -> {
                            NameProviderManager.setAngle(-offsetAngle)
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_UP_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                            NameProviderManager.setSpeedDistance(offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_LEFT -> {
                            NameProviderManager.setAngle(-offsetAngle)
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }
                        RockerView.Direction.DIRECTION_DOWN_RIGHT -> {
                            NameProviderManager.setAngle(offsetAngle)
                            NameProviderManager.setSpeedDistance(-offsetSpeedDistance)
                        }

                    }
                }

                override fun onFinish() {

                }
            })
    }
}