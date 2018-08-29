package com.pgy.xmpplib.manager

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import com.pgy.xmpplib.Log
import com.pgy.xmpplib.R
import com.pgy.xmpplib.XmppUtils
import com.pgy.xmpplib.callback.PresenceCallback
import com.pgy.xmpplib.callback.XmppResultCallback
import com.pgy.xmpplib.exception.NetWorkException
import com.pgy.xmpplib.service.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SASLAuthentication
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.chat.Chat
import org.jivesoftware.smack.chat.ChatManager
import org.jivesoftware.smack.chat.ChatMessageListener
import org.jivesoftware.smack.filter.AndFilter
import org.jivesoftware.smack.filter.StanzaFilter
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.roster.RosterListener
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.search.UserSearchManager
import org.jivesoftware.smackx.vcardtemp.VCardManager
import org.jivesoftware.smackx.vcardtemp.packet.VCard
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by lzy on 2018/6/14.
 */
class XmppServiceImpl(private val msgListener: ChatMessageListener, private val presenceCallback: PresenceCallback) : IXmppService {

    override fun saveVCard(context: Context, vcard: VCard, params: XmppParams, callback: XmppResultCallback) {
        if (!XmppManager.isNetConnection(context)) {
            callback.onFailed(params, XmppResultCode.ERROR_CODE_NET, context.getString(R.string.error_net_connection))
            return
        }
        exeRequestAsync(context, callback, params) {
            VCardManager.getInstanceFor(connection).apply {
                saveVCard(vcard)
            }
        }
    }

    override fun saveGender(context: Context, gender: String, params: XmppParams, callback: XmppResultCallback) {
        saveVCard(context, VCard().apply { setField(XmVcardConstant.VCARD_GENDER, gender) }, params, callback)
    }

    override fun saveUserName(context: Context, name: String, params: XmppParams, callback: XmppResultCallback) {
        saveVCard(context, VCard().apply { setField(XmVcardConstant.VCARD_NAME, name) }, params, callback)
    }


    private val onLineFriends = mutableListOf<String>()


    private lateinit var connection: AbstractXMPPConnection
    private lateinit var chatManager: ChatManager
    private val chatMap = hashMapOf<String, Chat>()

    private fun isNetConnection(context: Context): Boolean {
        val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return mConnectivityManager.activeNetworkInfo != null

    }

    private fun initConnection(passName: String?, passWord: String?): AbstractXMPPConnection {
        val configuration = XMPPTCPConnectionConfiguration.builder().apply {
            if (!TextUtils.isEmpty(passName) && !TextUtils.isEmpty(passWord))
                setUsernameAndPassword(passName, passWord)
            setServiceName(Constant.SERVICE_NAME)
            setHost(Constant.IP)
            setPort(Constant.LOGIN_PORT)
            setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            setSendPresence(false)
        }.build()

        //init connection
        val connection = XMPPTCPConnection(configuration)
        /**
         * 实现断线重连
         */
//        val manager = ReconnectionManager.getInstanceFor(connection)
//        manager.enableAutomaticReconnection()
        val filter = AndFilter(StanzaFilter { true })
        val listener = StanzaListener {
            if (it is Presence) {
//                val to = it.getFrom()// 接收方---一直是null
                val from = it.to// 发送方---这里是个smack的bug，只能收到发送方.

                if (from != null && from != passName) {
                    when (it.type) {
//                        Presence.Type.subscribe -> presenceCallback.onAddFriendRequest(from)
                        Presence.Type.subscribed -> presenceCallback.onAddFriendRequest(from)
                        Presence.Type.unsubscribed -> presenceCallback.onRemoveFriendRequest(from)

                        else -> {
                            Log.msg("other Presence.Type $from")
                        }
                    }
                }
            }
        }
        connection.addPacketInterceptor(listener, filter)
        return connection
    }

    private fun setChatManager() {
        //setChatManager
        chatManager = ChatManager.getInstanceFor(connection)
        Log.msg("setChatManager")

        chatManager.addChatListener { chat: Chat?, createdLocally: Boolean ->
            Log.msg("addChatListener createdLocally is $createdLocally")
//            if (!createdLocally) {
            chat?.addMessageListener(msgListener)
//            }
        }
    }


    override fun requestRegister(context: Context, passName: String, passWord: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            val connection = initConnection(null, null)
            connection.connect()
            AccountManager.getInstance(connection).createAccount(passName, passWord)
        }
    }

    override fun requestLogin(context: Context, passName: String, passWord: String, params: XmppParams,
                              callback: XmppResultCallback, dbOperation: ((String, String, String) -> Unit)?) {
        exeRequestAsync(context, callback, params) {
            connection = initConnection(passName, passWord)
            connection.connect()

            SASLAuthentication.registerSASLMechanism(SASLPlainMechanism())

            connection.login()
            //设置消息监听器
            setChatManager()
            //获取自己的昵称
            val card = VCardManager.getInstanceFor(connection).loadVCard(XmppUtils.getUserIdByPassName(passName))
            val nickName = card.getField(XmVcardConstant.VCARD_NAME) ?: ""
            //设置登录用户
            dbOperation?.invoke(passName, nickName, passWord)
//            //默认同意好友请求
            Roster.getInstanceFor(connection).subscriptionMode = Roster.SubscriptionMode.accept_all
            //在线状态
            connection.sendStanza(Presence(Presence.Type.available))
            nickName
        }
    }

    override fun requestRosterList(context: Context, params: XmppParams, callback: XmppResultCallback, dbOperation: ((Collection<XmppFriend>) -> Unit)?) {
        exeRequestAsync(context, callback, params) {

            //1s后获取好友
            Observable.interval(1L, 1, TimeUnit.SECONDS).take(1).map {

                val roster = Roster.getInstanceFor(connection)
                roster.entries?.apply {
                    val friends = mutableListOf<XmppFriend>()
                    forEach {
                        friends.add(XmppFriend(it, VCardManager.getInstanceFor(connection).loadVCard(it.user)))
                    }
                    dbOperation?.invoke(friends)

                    roster.addRosterListener(object : RosterListener {
                        override fun entriesDeleted(entries: MutableCollection<String>?) {
//                        Log.msg("entriesDeleted $entries")
                        }

                        override fun presenceChanged(presence: Presence?) {
                            presence?.apply {
                                val from = this.from.split("/")[0]
                                Log.msg("presenceChanged $from --> $type")

                                if (type == Presence.Type.available) {
                                    if (!onLineFriends.contains(from)) onLineFriends.add(from)
                                } else if (type == Presence.Type.unavailable)
                                    onLineFriends.remove(from)
                            }
                        }

                        override fun entriesUpdated(entries: MutableCollection<String>?) {
//                        Log.msg("entriesUpdated $entries")
                        }

                        override fun entriesAdded(entries: MutableCollection<String>?) {
//                        Log.msg("entriesAdded $entries")
                        }

                    })

                    //更新在线好友
                    onLineFriends.clear()
                    forEach { if (roster.getPresence(it.user).isAvailable) onLineFriends.add(it.user) }
                }

            }.subscribe(object : Observer<Set<RosterEntry>?> {
                override fun onComplete() {
                    Log.msg("roaster online finish")
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Set<RosterEntry>) {
                }

                override fun onError(e: Throwable) {
                }
            })
        }
    }


    override fun postMessage(context: Context, content: String, userId: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            //            chatMap[userId] ?: ChatManager.getInstanceFor(connection).createChat(userId).apply {
//                chatMap[userId] = this
//            }.sendMessage(content)

            ChatManager.getInstanceFor(connection).createChat(userId).sendMessage(content)
        }
    }

    override fun searchFriends(context: Context, keyword: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            val search = UserSearchManager(connection)
            val answerForm = search.getSearchForm("search." + connection.serviceName).createAnswerForm().apply {
                setAnswer("Username", true)
                setAnswer("Name", true)
                setAnswer("search", keyword)
            }
            search.getSearchResults(answerForm, "search." + connection.serviceName)?.rows
        }
    }

    override fun getUserInfo(context: Context, userId: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            VCardManager.getInstanceFor(connection).loadVCard(userId)
        }
    }

    override fun addFriends(context: Context, passName: String, userId: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            Roster.getInstanceFor(connection).createEntry(userId, passName, arrayOf("Friends"))
        }
    }

//    override fun receiveFriends(context: Context, passName: String, myPassName: String, params: XmppParams, callback: XmppResultCallback) {
//        exeRequestAsync(context, callback, params) {
//            Presence(Presence.Type.subscribed).apply {
//                to = passName
//                from = myPassName
//                connection.sendStanza(this)
//            }
//        }
//    }

    override fun deleteFriend(context: Context, userId: String, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            val roster = Roster.getInstanceFor(connection)
            roster.removeEntry(roster.getEntry(userId))
        }
    }

    override fun logout(context: Context, params: XmppParams, callback: XmppResultCallback) {
        exeRequestAsync(context, callback, params) {
            //离线状态
            connection.sendStanza(Presence(Presence.Type.unavailable))

            if (connection.isConnected)
                connection.disconnect()
        }
    }

    override fun isOnline(userId: String): Boolean = onLineFriends.contains(userId)

    override fun getUserIcon(context: Context, jid: String): ByteArray {
        if (!XmppManager.isNetConnection(context)) {
            return ByteArray(0)
        }

        val vcard = VCardManager.getInstanceFor(connection).loadVCard(jid)
        return vcard.avatar
    }

    override fun getUser(context: Context, jid: String): VCard {
        if (!XmppManager.isNetConnection(context)) {
            return VCard()
        }
        return VCardManager.getInstanceFor(connection).loadVCard(jid)
    }

    override fun changeIcon(context: Context, jid: String, file: File, params: XmppParams, callback: XmppResultCallback) {
        saveVCard(context, VCard().apply { setAvatar(file.readBytes(), "image/jpeg") }, params, callback)
    }


    private fun exeRequestAsync(context: Context, callback: XmppResultCallback, params: XmppParams, map: () -> Any?) {
        Observable.just(map).map {
            if (!isNetConnection(context)) throw NetWorkException()

            it.invoke()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(XmppSubscriber(context, callback, params))
    }

    override fun createChatRoom(context: Context, ownerPassName: String, callback: XmppResultCallback, params: XmppParams) {
        exeRequestAsync(context, callback, params) {
            val roomId = "$ownerPassName${System.currentTimeMillis()}@conference.${Constant.SERVICE_NAME}"
            MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(roomId).create(ownerPassName)
        }
    }

    override fun leaveChatRoom(userId: String) {
        chatMap.remove(userId)
    }
}