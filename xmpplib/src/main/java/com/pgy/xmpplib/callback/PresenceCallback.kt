package com.pgy.xmpplib.callback

/**
 * Created by lzy on 2018/6/21.
 */
interface PresenceCallback {

    fun onAddFriendRequest(userId: String)
    fun onRemoveFriendRequest(userId: String)
}