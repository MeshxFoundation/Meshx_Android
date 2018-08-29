package com.pgy.xmpplib.service

import android.content.Context
import com.pgy.xmpplib.callback.XmppResultCallback
import org.jivesoftware.smackx.vcardtemp.packet.VCard
import java.io.File

/**
 * Created by lzy on 2018/6/14.
 */
interface IXmppService {

    /**
     * 注册请求
     */
    fun requestRegister(context: Context, passName: String, passWord: String, params: XmppParams, callback: XmppResultCallback)

    /**
     * 登录请求
     */
    fun requestLogin(context: Context, passName: String, passWord: String, params: XmppParams, callback: XmppResultCallback, dbOperation: ((String, String, String) -> Unit)?)

    /**
     * 请求好友列表
     */
    fun requestRosterList(context: Context, params: XmppParams, callback: XmppResultCallback, dbOperation: ((Collection<XmppFriend>) -> Unit)?)

    /**
     * 发送消息
     */
    fun postMessage(context: Context, content: String, userId: String, params: XmppParams, callback: XmppResultCallback)

    /**
     * 搜索好友
     */
    fun searchFriends(context: Context, keyword: String, params: XmppParams, callback: XmppResultCallback)

    /**
     * 查看用户信息
     */
    fun getUserInfo(context: Context, userId: String, params: XmppParams, callback: XmppResultCallback)

    /**
     * 添加好友
     */
    fun addFriends(context: Context, passName: String, userId: String, params: XmppParams, callback: XmppResultCallback)

    /**
     * 删除好友
     */
    fun deleteFriend(context: Context, userId: String, params: XmppParams, callback: XmppResultCallback)

    /*离开聊天房间*/
    fun leaveChatRoom(userId: String)

    /*用户登出*/
    fun logout(context: Context, params: XmppParams, callback: XmppResultCallback)

    /*获取在线好友 UserID*/
    fun isOnline(userId: String): Boolean

    //获取用户头像
    fun getUserIcon(context: Context, jid: String): ByteArray

    //上传用户头像
    fun changeIcon(context: Context, jid: String, file: File, params: XmppParams, callback: XmppResultCallback)

    //保存用户名称
    fun saveUserName(context: Context, name: String, params: XmppParams, callback: XmppResultCallback)

    //保存用户信息
    fun saveVCard(context: Context, vcard: VCard, params: XmppParams, callback: XmppResultCallback)

    //保存用户性别
    fun saveGender(context: Context, genter: String, params: XmppParams, callback: XmppResultCallback)

    //获取用户信息
    fun getUser(context: Context, jid: String): VCard

    //创建群聊
    fun createChatRoom(context: Context, ownerPassName: String, callback: XmppResultCallback, params: XmppParams)

}