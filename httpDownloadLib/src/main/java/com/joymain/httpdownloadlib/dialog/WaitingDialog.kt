package com.joymain.httpdownloadlib.dialog

import android.app.ProgressDialog
import android.content.Context

/**
 * Created by lzy on 2018/7/17.
 */
class WaittingDialog(context: Context, res: Int) {
    companion object {
        fun create(context: Context, res: Int) = WaittingDialog(context, res)
    }

    private val dialog: ProgressDialog = ProgressDialog(context)

    init {
        dialog.setMessage(context.getString(res))
    }

    fun show() {
        if (dialog.isShowing) return
        dialog.show()
    }

    fun dismiss() {
        if (!dialog.isShowing) return
        dialog.dismiss()
    }
}