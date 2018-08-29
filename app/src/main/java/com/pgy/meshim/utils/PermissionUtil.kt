package com.pgy.meshim.utils

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by Administrator on 2016/8/18.
 * android6.0 权限工具类
 */
class PermissionUtil(context: Context) {
    private val context: Context = context.applicationContext

    //检查权限时，判断系统的权限集合
    fun permissionSet(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (isLackPermission(permission)) {//是否添加完全部权限集合
                return true
            }
        }
        return false
    }

    //检查系统权限是，判断当前是否缺少权限(PERMISSION_DENIED:权限是否足够)
    private fun isLackPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED
    }


}
