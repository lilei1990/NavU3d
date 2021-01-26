package com.huida.navu3d.ui.fragment.workTask

import android.content.Context
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.R
import com.huida.navu3d.ui.activity.U3dActivity
import com.huida.navu3d.bean.WorkTaskData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * 作者 : lei
 * 时间 : 2020/11/28.
 * 邮箱 :416587959@qq.com
 * 描述 :工作任务
 */
class WorkTaskListAdapter(context: Context?, layoutId: Int, datas: MutableList<WorkTaskData>?) :
    CommonAdapter<WorkTaskData>(context, layoutId, datas) {
    override fun convert(holder: ViewHolder?, t: WorkTaskData?, position: Int) {
        holder?.setOnClickListener(R.id.clRoot) {
            ToastUtils.showLong("${position}被点击")
            ActivityUtils.startActivity(U3dActivity::class.java)
        }
    }
}