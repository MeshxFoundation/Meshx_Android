package com.pgy.meshim.utils.db

import com.pgy.meshim.MeshApplication
import com.pgy.meshim.bean.UserInfo
import com.pgy.meshim.database.UserInfoDao

/**
 * Created by lzy on 2018/6/26.
 */
object DbUserUtils : BaseDbUtils() {

    fun getLastLoginUser(appContext: MeshApplication, callback: (UserInfo?) -> Unit) {
        dbAsyncTask(callback) {
            appContext.mDaoUserSession?.userInfoDao?.queryBuilder()
                    ?.where(UserInfoDao.Properties.Status.eq(UserInfo.LAST_LOGIN))
                    ?.list()?.firstOrNull()
        }
    }

    fun getLastLogoutUser(appContext: MeshApplication, callback: (UserInfo?) -> Unit) {
        dbAsyncTask(callback) {
            appContext.mDaoUserSession?.userInfoDao?.queryBuilder()
                    ?.where(UserInfoDao.Properties.Status.eq(UserInfo.LOGOUT))
                    ?.list()?.lastOrNull()
        }
    }

    fun setLoginUserSync(appContext: MeshApplication, passName: String, nickName: String, password: String): UserInfo {
        val user = appContext.mDaoUserSession?.userInfoDao?.queryBuilder()
                ?.where(UserInfoDao.Properties.UserPassName.eq(passName))
                ?.list()?.firstOrNull()?.apply {
                    this.userPassWord = password
                    this.userName = nickName
                }
                ?: UserInfo(passName, password, nickName)
        user.status = UserInfo.LAST_LOGIN

        appContext.mDaoUserSession?.userInfoDao?.insertOrReplace(user)

        user.status = UserInfo.LOGINED
        return user
    }

    fun userLogout(appContext: MeshApplication, callback: (UserInfo?) -> Unit) {
        dbAsyncTask(callback) {
            val user = appContext.mDaoUserSession?.userInfoDao?.queryBuilder()
                    ?.where(UserInfoDao.Properties.Status.eq(UserInfo.LAST_LOGIN))
                    ?.list()?.firstOrNull()

            user?.apply {
                status = UserInfo.LOGOUT
                userPassWord = ""
                appContext.mDaoUserSession?.userInfoDao?.update(this)
            }

//            appContext.mDaoSession?.database?.close()
            user
        }

    }
}