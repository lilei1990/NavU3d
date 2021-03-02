package com.huida.navu3d.ui.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.esri.core.geometry.Point
import com.esri.core.geometry.Polyline
import com.huida.navu3d.bean.PointData
import com.unity3d.player.UnityPlayer
import com.zs.base_library.http.ApiException
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.Nullable
import org.json.JSONArray
import org.json.JSONObject

/**
 * 作者 : lei
 * 时间 : 2021/02/25.
 * 邮箱 :416587959@qq.com
 * 描述 :数据库的操作
 */
class DbRepo(
    val viewModelScope: CoroutineScope,
    val errorLiveData: MutableLiveData<ApiException>,
    homeVM: HomeVM
) {

}