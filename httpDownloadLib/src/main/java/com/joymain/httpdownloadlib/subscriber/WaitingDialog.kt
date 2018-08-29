package com.joymain.httpdownloadlib.subscriber

import android.app.ProgressDialog
import android.content.Context
import com.joymain.httpdownloadlib.R

/**
 * Created by lzy on 2018/7/17.
 */
class WaitingDialog(context: Context, res: Int) {
    companion object {
        fun create(context: Context, res: Int) = WaitingDialog(context, res)
        fun create(context: Context) = WaitingDialog(context, R.string.loading_default)
    }

    private val dialog: ProgressDialog = ProgressDialog(context).apply { setCancelable(false) }

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