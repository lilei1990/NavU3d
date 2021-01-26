package com.lei.base_core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.lei.base_core.utils.ColorUtils
import com.lei.base_core.utils.StatusUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * des mvvm 基础 activity
 * @date 2020/5/9
 */
abstract class BaseVmActivity<VB : ViewBinding>(val inflate: (LayoutInflater) -> VB) : AppCompatActivity(),
    CoroutineScope by MainScope() {
    lateinit var binding: VB
    private var mActivityProvider: ViewModelProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        setStatusColor()
        setSystemInvadeBlack()
        initViewModel()
        observe()
        init(savedInstanceState)
    }

    /**
     * 设置状态栏背景颜色
     */
    open fun setStatusColor() {
        StatusUtils.setUseStatusBarColor(this, ColorUtils.parseColor("#00ffffff"))
    }

    /**
     * 沉浸式状态
     */
    open fun setSystemInvadeBlack() {
        //第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtils.setSystemStatus(this, true, true)
    }

    /**
     * 初始化viewModel
     * 之所以没有设计为抽象，是因为部分简单activity可能不需要viewModel
     * observe同理
     */
    open fun initViewModel() {
    }

    /**
     * 注册观察者
     */
    open fun observe() {

    }


    /**
     * 通过activity获取viewModel，跟随activity生命周期
     */
    protected fun <T : ViewModel?> getActivityViewModel(modelClass: Class<T>): T? {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider?.get(modelClass)
    }

    /**
     * activity入口
     */
    abstract fun init(savedInstanceState: Bundle?)


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}