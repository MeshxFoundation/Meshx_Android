package com.pgy.xmpplib.service

import com.google.gson.Gson
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smackx.vcardtemp.packet.VCard

/**
 * Created by lzy on 2018/7/26.
 */
data class XmppFriend(val roster: RosterEntry, private val card: VCard) {
    fun getInfoJson(): String =
            Gson().toJson(hashMapOf<String, String>().apply {
                put(XmVcardConstant.VCARD_NAME, card.getField(XmVcardConstant.VCARD_NAME))
                put(XmVcardConstant.VCARD_GENDER, card.getField(XmVcardConstant.VCARD_GENDER))
            })
}