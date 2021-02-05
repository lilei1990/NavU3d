package com.lei.core.common

import android.app.Activity
import com.blankj.utilcode.util.SnackbarUtils

/**
 * 作者 : lei
 * 时间 : 2021/01/20.
 * 邮箱 :416587959@qq.com
 * 描述 :Snackbar显示信息
 */

fun Activity.showBarT(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .show(true)
}

fun Activity.showBarB(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .show(false)
}

/**
 * Snackbar显示错误信息
 */
fun Activity.showWarningBarT(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .showWarning(true)
}

fun Activity.showWarningBarB(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .showWarning(false)
}

/**
 * Snackbar显示错误信息
 */
fun Activity.showErrorBarT(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .showError(true)
}

fun Activity.showErrorBarB(message: String) {
    SnackbarUtils.with(findViewById(android.R.id.content))
        .setMessage(message)
        .showError(false)
}
