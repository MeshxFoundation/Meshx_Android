package com.pgy.xmpplib.manager

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.pgy.xmpplib.Log
import com.pgy.xmpplib.callback.MessageCallback
import com.pgy.xmpplib.callback.PresenceCallback
import com.pgy.xmpplib.service.IXmppService
import org.jivesoftware.smack.chat.Chat
import org.jivesoftware.smack.chat.ChatMessageListener
import org.jivesoftware.smack.packet.Message

/**
 * Created by lzy on 2018/6/14.
 */
object XmppManager : PresenceCallback, ChatMessageListener {

    var msgCallback: MessageCallback? = null
    var serviceImpl: IXmppService = XmppServiceImpl(this, this)

    var presenceCallback: PresenceCallback? = null

    /**
     * 判断网络是否连接
     */
    fun isNetConnection(context: Context): Boolean {
        val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = mConnectivityManager.activeNetworkInfo
        return networkInfo != null
    }

    override fun processMessage(chat: Chat, msg: Message) {
        Log.msg("processMessage msg = $msg")

        //忽略心跳包
        if (TextUtils.isEmpty(msg.body)) return

        msgCallback?.onReceiveMsg(msg.body)
    }

    override fun onAddFriendRequest(userId: String) {
        presenceCallback?.onAddFriendRequest(userId)
    }

    override fun onRemoveFriendRequest(userId: String) {
        presenceCallback?.onRemoveFriendRequest(userId)
    }

    fun logout() {
        serviceImpl = XmppServiceImpl(this, this)
    }
}