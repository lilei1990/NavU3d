package com.huida.navu3d.ui.fragment.workTask

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.huida.navu3d.R
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.common.liveEvenBus
import com.huida.navu3d.constants.BusConstants
import com.huida.navu3d.ui.activity.unity.UnityActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者 : lei
 * 时间 : 2020/11/28.
 * 邮箱 :416587959@qq.com
 * 描述 :工作任务
 */
class WorkTaskListAdapter(
    context: Context?,
    layoutId: Int,
    datas: ArrayList<WorkTaskData>
) :
    CommonAdapter<WorkTaskData>(context, layoutId, datas) {


    override fun convert(holder: ViewHolder?, t: WorkTaskData?, position: Int) {
        holder?.setOnClickListener(R.id.clRoot) {
//            ActivityUtils.startActivity(UnityActivity::class.java)

            liveEvenBus(BusConstants.TO_PAGE_HOME.name)
                .post(1)
            liveEvenBus(BusConstants.SELECT_WORK_TASK_DATA.name)
                .post(t)
        }
        holder?.setText(R.id.tv_name, t?.name)
        holder?.setText(R.id.tv_creator, "创建人:${t?.creator}")

    }
}