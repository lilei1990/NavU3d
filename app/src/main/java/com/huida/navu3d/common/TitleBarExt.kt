package com.huida.navu3d.common

import android.view.View
import androidx.navigation.Navigation
import com.huida.navu3d.databinding.TitleBarBinding
import com.lei.base_core.common.clickNoRepeat

/**
 * 作者 : lei
 * 时间 : 2021/01/26.
 * 邮箱 :416587959@qq.com
 * 描述 : titlebar 实现接口
 */
fun TitleBarBinding.initBackClick() {
    //返回按钮
    clBack.clickNoRepeat {
        Navigation.findNavController(it).popBackStack()
    }

}
fun TitleBarBinding.initConfirmClick( confirmCallBack: (View) -> Unit = {}) {

    //保存
    clConfirm.clickNoRepeat {
        confirmCallBack(it)
    }
}


