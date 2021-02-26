package com.huida.navu3d.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :Unity fragment具体实现类
 */
class UnityRepo(
    val viewModelScope: CoroutineScope,
    val errorLiveData: MutableLiveData<ApiException>
) {

}