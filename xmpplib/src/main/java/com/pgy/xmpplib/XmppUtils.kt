package com.pgy.xmpplib

import com.pgy.xmpplib.service.Constant

/**
 * Created by lzy on 2018/7/2.
 */
object XmppUtils {
    fun getPassNameByUserId(userId: String) = userId.lastIndexOf('@').let {
        if (it > 0)
            userId.substring(0, it)
        else
            userId
    }

    fun getUserIdByPassName(passName: String) = "$passName@${Constant.SERVICE_NAME}"
}