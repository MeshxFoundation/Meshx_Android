package com.pgy.meshim.utils


import android.content.Context
import android.view.Gravity
import android.widget.Toast


/**
 * 显示冒泡提示信息工具类
 *
 * @author zz
 */
object ToastUtil {

    /**
     * 显示冒泡msg信息
     *
     * @param context
     * @param msg
     */
    fun show(context: Context, msg: CharSequence) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 显示冒泡resId资源信息
     *
     * @param context
     * @param resId
     */
    fun show(context: Context, resId: Int) {
        val toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
