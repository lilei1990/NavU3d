package com.huida.navu3d.ui.fragment.workTask

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.huida.navu3d.R
import com.huida.navu3d.bean.WorkTaskData
import com.huida.navu3d.databinding.FragmentTaskListBinding
import com.lei.base_core.base.BaseVmFragment
import com.lei.base_core.common.clickNoRepeat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.internal.MainDispatcherFactory


class WorkTaskFragment : BaseVmFragment<FragmentTaskListBinding>(FragmentTaskListBinding::inflate) {
    private lateinit var workTaskListAdapter: WorkTaskListAdapter
    private val viewModel by lazy { getFragmentViewModel(WorkTaskViewModel::class.java) }


    companion object {
        fun newInstance() =
            WorkTaskFragment()
    }


    override fun init(savedInstanceState: Bundle?) {

        initTab()
        initTitleBar()
        initRecycleView()
        loadWorkListData()
    }

    /**
     * 初始化title
     */
    private fun initTitleBar() {
        binding.incTitleBar.clConfirm.clickNoRepeat {
            viewModel.workTaskData.name = binding.incNew.etTaskName.text.toString()
            viewModel.workTaskData.creator = binding.incNew.etCreator.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                //添加到数据库,更新列表
                viewModel.addFarmToolsData()
                loadWorkListData()
            }
            binding.clContinueTask.performClick()
        }
        binding.incTitleBar.clBack.clickNoRepeat {
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
                activity,
                R.layout.item_task,
                arrayListOf()
            );
        binding.rvContinue.adapter = workTaskListAdapter


    }

    /**
     * 加载列表数据
     */
    private fun loadWorkListData() {
        lifecycleScope.launch(Dispatchers.IO) {
           val loadInitData = viewModel.loadInitData()
            lifecycleScope.launch(Dispatchers.Main) {
                loadInitData.observe(this@WorkTaskFragment, Observer{
                    workTaskListAdapter.datas.clear()
                    workTaskListAdapter.datas.addAll(it)
                    workTaskListAdapter.notifyDataSetChanged()
                })
            }
        }
    }


}