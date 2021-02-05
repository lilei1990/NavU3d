package com.lei.base_core.helper

import android.content.Context
import android.util.Pair
import android.view.View

import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.*

/**
 * ```
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2018/01/06
 * desc  : helper about permission
 * ```
 */
object PermissionHelper {

    fun request(
        context: Context, callback: PermissionUtils.SimpleCallback,
        @PermissionConstants.PermissionGroup vararg permissions: String
    ) {
        PermissionUtils.permission(*permissions)
            .rationale { activity, shouldRequest -> showRationaleDialog(activity, shouldRequest) }
            .callback(object : PermissionUtils.SingleCallback {
                override fun callback(
                    isAllGranted: Boolean, granted: MutableList<String>,
                    deniedForever: MutableList<String>, denied: MutableList<String>
                ) {
                    LogUtils.d(isAllGranted, granted, deniedForever, denied)
                    if (isAllGranted) {
                        callback.onGranted()
                        return
                    }
                    if (deniedForever.isNotEmpty()) {
//                        showOpenAppSettingDialog(context)
                        return
                    }
                    val activity = ActivityUtils.getActivityByContext(context)
                    if (activity != null) {
                        SnackbarUtils.with(activity.findViewById(android.R.id.content))
                            .setMessage("Permission denied: ${permissions2String(denied)}")
                            .showError(true)
                    }
                    callback.onDenied()
                }

                fun permissions2String(permissions: MutableList<String>): String {
                    if (permissions.isEmpty()) return "[]"
                    val sb: StringBuilder = StringBuilder()
                    for (permission in permissions) {
                        sb.append(", " + permission.substring(permission.lastIndexOf('.') + 1))
                    }
                    return "[${sb.substring(2)}]"
                }
            })
            .request()
    }

    fun showRationaleDialog(
        context: Context,
        shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest
    ) {
        ToastUtils.showLong("ssssssssssssssssss")
    }


}