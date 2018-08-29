package com.pgy.meshim.activity

import android.content.Intent
import android.os.Bundle
import com.pgy.meshim.R
import com.pgy.meshim.utils.ResiurceWraUtils
import com.pgy.xmpplib.XmppUtils
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.service.XmVcardConstant
import com.pgy.xmpplib.service.XmppParams
import kotlinx.android.synthetic.main.activity_gendersetting.*
import kotlinx.android.synthetic.main.select_seting_title.*


class GendersettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gendersetting)
        permission_quit.setOnClickListener {
            finish()
        }

        val intExtra = intent.getIntExtra("GendersettingActivity", 0)
        if (intExtra == 0) {
            initGenter()//设置性别
        } else {
            initLanguage()//设置语言
        }
    }

    override fun onSuccess(params: XmppParams, result: Any) {
        finish()
    }

    fun initLanguage() {
        title_content.text = resources.getString(R.string.main_fragment_langunge)
        gendrt_textman.text = resources.getString(R.string.main_fragment_langungeforenglish)
        gendrt_textwomen.text = resources.getString(R.string.main_fragment_langungeforchinese)
        if (ResiurceWraUtils.getLanguageType(this) == ResiurceWraUtils.LANGUAGE_CN) {
            rbt_check_woman.isChecked = true
        }

        infotmation_complete.setOnClickListener {
            var locale = if (rbt_check_man.isChecked) {
                0
            } else {
                1
            }
            ResiurceWraUtils.setLanguage(this, locale)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }

    fun initGenter() {
        title_content.text = resources.getString(R.string.permission_settings) + resources.getString(R.string.sex)
        //---初始化 性别
        var gender = XmppManager.serviceImpl.getUser(this, XmppUtils.getUserIdByPassName(appContext.loginUser.userPassName)).getField(XmVcardConstant.VCARD_GENDER)
        rbt_check_woman.isChecked = !"1".equals(gender)
        infotmation_complete.setOnClickListener {
            XmppManager.serviceImpl.apply {
                var gender = if (rbt_check_man.isChecked) {
                    "1"
                } else {
                    "0"
                }
                saveGender(this@GendersettingActivity, gender, XmppParams(XmppRequestCode.UPLOAD_ICON), this@GendersettingActivity)
            }
        }
    }
}
