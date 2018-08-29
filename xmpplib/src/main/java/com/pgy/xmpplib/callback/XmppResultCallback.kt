package com.pgy.xmpplib.callback

import android.text.InputFilter
import com.pgy.xmpplib.service.XmppParams

/**
 * Created by lzy on 2018/6/14.
 */
interface XmppResultCallback {

    /**
     * 请求成功结果返回
     */
    fun onSuccess(params: XmppParams, result: Any)

    /**
     * 请求失败结果返回
     */
    fun onFailed(params: XmppParams, errorCode: Int, msg: String)

    /**
     * 请求取消结果返回
     */
    fun onCancel(params: XmppParams)
}