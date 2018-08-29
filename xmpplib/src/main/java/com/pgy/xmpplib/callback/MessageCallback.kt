package com.pgy.xmpplib.callback

/**
 * Created by lzy on 2018/6/14.
 */
interface MessageCallback {
    fun onReceiveMsg(msg: String)

    fun onSendMessageSuccess(msgId: String)
    fun onSendMessageFailed(msgId: String)
}