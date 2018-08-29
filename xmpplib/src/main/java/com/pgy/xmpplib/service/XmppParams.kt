package com.pgy.xmpplib.service

/**
 * Created by lzy on 2018/6/19.
 * xmpp请求参数
 */
data class XmppParams(var requestCode: Int, var showWaitingDialog: Boolean = true, var extraInfo: Any? = null)