package com.pgy.meshim.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import com.pgy.meshim.bean.UserInfo
import com.pgy.meshim.utils.CommonUtils
import com.pgy.xmpplib.Log
import com.pgy.xmpplib.callback.XmppResultCallback
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.manager.XmppResultCode
import com.pgy.xmpplib.service.XmppParams

/**
 * Created by lzy on 2018/7/11.
 */
class LoginService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val PASS_NAME = "passName"
        private const val PASS_WORD = "passWord"
        @JvmStatic
        fun start(context: Context, passName: String, password: String) {
            context.startService(Intent(context, LoginService::class.java).apply {
                putExtra(PASS_NAME, passName)
                putExtra(PASS_WORD, password)
            })
        }
    }

    private val xmppResultCallback = object : XmppResultCallback {
        override fun onSuccess(params: XmppParams, result: Any) {
            Log.msg("auto login success")
            (params.extraInfo as? UserInfo)?.apply {
                CommonUtils.getApplication(applicationContext).setLoginUser(userPassName, result as String, userPassWord, UserInfo.LOGINED)
            }
        }

        override fun onFailed(params: XmppParams, errorCode: Int, msg: String) {
            val status = if (errorCode == XmppResultCode.ERROR_CODE_LOGIN_PASSWORD) UserInfo.UNLOGIN else UserInfo.OFFLINE_LOGINED
            Log.msg("auto failed $status")
            (params.extraInfo as? UserInfo)?.apply {
                CommonUtils.getApplication(applicationContext).setLoginUser(userPassName, userName, userPassWord, status)
            }
        }

        override fun onCancel(params: XmppParams) {
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            Log.msg("auto login")
            val passName = getStringExtra(PASS_NAME)
            val password = getStringExtra(PASS_WORD)

            if (TextUtils.isEmpty(passName) || TextUtils.isEmpty(password)) return super.onStartCommand(intent, flags, startId)

            XmppManager.serviceImpl.requestLogin(applicationContext, passName
                    , password, XmppParams(XmppRequestCode.LOGIN, false, UserInfo(passName, passName, passName)), xmppResultCallback, null)
        }

        return Service.START_STICKY
    }

}
