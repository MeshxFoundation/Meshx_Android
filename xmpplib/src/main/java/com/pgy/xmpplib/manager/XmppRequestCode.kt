package com.pgy.xmpplib.manager

/**
 * Created by lzy on 2018/6/14.
 */
object XmppRequestCode {
    const val LOGIN = 1//登录
    const val REGISTER = 2//注册
    const val ROSTER_LIST = 3//好友列表
    const val SEND_MEESAGE = 4//发送消息
    const val SEARCH_FRIENDS = 5//查找好友
    const val ADD_FRIENDS = 6//添加好友
    const val DELETE_FRIENDS = 7//删除好友
    const val USER_INFO = 8//用户资料
    const val LOGOUT = 9//用户登出
    const val UPLOAD_ICON = 10//上传头像
    const val CREATE_CHAT_ROOM = 11//穿件群聊
}

object XmppResultCode {
    const val ERROR_CODE_UNKNOW = 0
    const val ERROR_CODE_NET = 1
    const val ERROR_CODE_LOGIN_PASSWORD = 2
    const val ERROR_CODE_REGISTER = 3
}