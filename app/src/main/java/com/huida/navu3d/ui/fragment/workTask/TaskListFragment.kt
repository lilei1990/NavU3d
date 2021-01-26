package com.huida.navu3d.ui.fragment.workTask

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.huida.navu3d.R
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.databinding.FragmentTaskListBinding
import com.lei.base_core.base.BaseVmFragment
import com.lei.base_core.common.clickNoRepeat
import kotlinx.coroutines.launch


class TaskListFragment : BaseVmFragment<FragmentTaskListBinding>(FragmentTaskListBinding::inflate) {
    private lateinit var workTaskListAdapter: WorkTaskListAdapter
    private val dataList = ArrayList<WorkTaskData>()


    companion object {
        fun newInstance() =
            TaskListFragment()
    }

    private lateinit var viewModel: TaskListViewModel


    override fun init(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        initTab()
        initTitleBar()
        initRecycleView()
    }

    /**
     * 初始化title
     */
    private fun initTitleBar() {
        binding.incTitleBar.clConfirm.clickNoRepeat {
            ToastUtils.showLong("保存")

        }
        binding.incTitleBar.clBack.clickNoRepeat {
            ToastUtils.showLong("返回")
            Navigation.findNavController(it).popBackStack()
        }

    }

    /**
     * 初始化tab
     */
    private fun initTab() {
        //tab被选择
        binding.clNewTask.setOnClickListener {
            binding.rvContinue.visibility = View.GONE
            binding.incNew.root.visibility = View.VISIBLE
            it.setBackgroundResource(R.drawable.shape_rectangle_task_tab_left_select_tl)
            binding.clContinueTask.setBackgroundResource(R.drawable.shape_rectangle_task_tab_left_unselect_tr)
        }
        binding.clContinueTask.setOnClickListener {
            binding.incNew.root.visibility = View.GONE
            binding.rvContinue.visibility = View.VISIBLE
            it.setBackgroundResource(R.drawable.shape_rectangle_task_tab_left_select_tr)
            binding.clNewTask.setBackgroundResource(R.drawable.shape_rectangle_task_tab_left_unselect_tl)
        }
        //日期被修改
        binding.incNew.tvData.clickNoRepeat {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                val view: View =
                    layoutInflater.inflate(R.layout.dialog_data_picker, null) as LinearLayout
                var datePicker = view.findViewById<DatePicker>(R.id.datePicker)

                builder.setView(view)
                val dialog = builder.create()
                datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                    binding.incNew.tvData.text = "${year}年${monthOfYear + 1}月${dayOfMonth}日"
                    dialog.dismiss()
                }
                dialog.show();
            }
        }

        val arrayListOf = arrayListOf<Int>()
        for (i in 10..20) {
            arrayListOf.add(i)
        }
        binding.incNew.spArea.attachDataSource(arrayListOf)
        binding.incNew.spFarmTools.attachDataSource(arrayListOf)


    }

    /**
     * 初始化RecycleView
     */
    private fun initRecycleView() {
        binding.rvContinue.layoutManager = LinearLayoutManager(activity)

        workTaskListAdapter =
            WorkTaskListAdapter(
                getContext(),
                R.layout.item_task,
                arrayListOf()
            );

//        //订阅列表数据5s过后展示数据
        launch() {
//            delay(5000L)
            activity?.let {
                viewModel.loadInitData().observe(it, Observer {
                    workTaskListAdapter =
                        WorkTaskListAdapter(
                            getContext(),
                            R.layout.item_task,
                            it
                        );
                    binding.rvContinue.adapter = workTaskListAdapter
//                    dataList.addAll(it)
                    workTaskListAdapter.notifyDataSetChanged()
                })
            }
        }
    }



}