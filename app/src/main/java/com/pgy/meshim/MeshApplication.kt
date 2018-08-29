package com.pgy.meshim

import android.app.Application
import com.pgy.meshim.bean.UserInfo
import com.pgy.meshim.database.DaoMaster
import com.pgy.meshim.database.DaoSession
import com.pgy.meshim.utils.db.DbUserUtils

/**
 * Created by lzy on 2018/6/14.
 */
class MeshApplication : Application(){

//        private var mDaoMaster: DaoMaster? = null
//    var mDaoSession: DaoSession? = null//用户聊天数据库
//
    var mDaoUserSession: DaoSession? = null//用户数据库
////
    var loginUser: UserInfo = UserInfo()
        set(value) {
            field = value
//            setUserChatDatabase(field.userPassName)
        }


    override fun onCreate() {
        super.onCreate()
        initCommonUserDatabase()
    }

    private fun initCommonUserDatabase() {
        mDaoUserSession = DaoMaster(DaoMaster.DevOpenHelper(this, "pgy_user_db", null).writableDatabase).newSession()
    }

//    private fun setUserChatDatabase(passName: String) {
//        mDaoSession = DaoMaster(DaoMaster.DevOpenHelper(this, "pgy_db$passName", null).writableDatabase).newSession()
//        Log.msg("setUserChatDatabase success")
//    }

    fun setLoginUser(passName: String, nickName: String, password: String, status: Int) {
        if (status == loginUser.status) return

        if (status == UserInfo.UNLOGIN) {
            DbUserUtils.userLogout(this) {
//                loginStatusChangeListeners.forEach { it.onLoginStatusChange(loginUser) }
            }
        } else {
            loginUser = UserInfo(passName, password, nickName).apply {
                this.status = status
            }
//            loginStatusChangeListeners.forEach { it.onLoginStatusChange(loginUser) }
        }
    }
}