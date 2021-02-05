package com.lei.base_core.base

import android.os.Bundle
import android.view.View

/**
 * 作者 : LiLei
 * 时间 : 2020/11/04.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
interface IBaseView {
    fun initData(bundle: Bundle)

    fun bindLayout(): Int

    fun setContentView()

    fun initView(
        savedInstanceState: Bundle?,
        contentView: View?
    )

    fun doBusiness()

    fun onDebouncingClick(view: View?)
}