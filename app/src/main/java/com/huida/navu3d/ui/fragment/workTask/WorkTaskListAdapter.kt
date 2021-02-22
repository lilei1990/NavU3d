package com.huida.navu3d.ui.fragment.workTask

import android.content.Context
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.R
import com.huida.navu3d.bean.WorkTaskData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * 作者 : lei
 * 时间 : 2020/11/28.
 * 邮箱 :416587959@qq.com
 * 描述 :工作任务
 */
class WorkTaskListAdapter(context: Context?, layoutId: Int, datas: ArrayList<WorkTaskData>, workTaskViewModel: WorkTaskViewModel) :
        CommonAdapter<WorkTaskData>(context, layoutId, datas) {
    val viewModel = workTaskViewModel

    override fun convert(holder: ViewHolder?, t: WorkTaskData?, position: Int) {
        holder?.setOnClickListener(R.id.clRoot) {
            viewModel.selectWorkTaskData=t
            viewModel.selectWorkTaskData?.findLines()
            ToastUtils.showLong("${position}被点击")
            Navigation.findNavController(it)
                    .navigate(R.id.action_task_list_fragment_to_home_fragment)
        }
        holder?.setText(R.id.tv_name, t?.name)
        holder?.setText(R.id.tv_creator, "创建人:${t?.creator}")

    }
}