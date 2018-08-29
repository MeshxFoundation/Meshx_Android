package com.pgy.meshim.activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.pgy.meshim.R
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.service.XmppParams
import kotlinx.android.synthetic.main.activity_setname.*
import kotlinx.android.synthetic.main.select_seting_title.*

/**
 * Created by burning on 2018/7/23.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
-------------------------//┏┓　　　┏┓
-------------------------//┏┛┻━━━┛┻┓
-------------------------//┃　　　　　　　┃ 　
-------------------------//┃　　　━　　　┃
-------------------------//┃　┳┛　┗┳　┃
-------------------------//┃　　　　　　　┃
-------------------------//┃　　　┻　　　┃
-------------------------//┃　　　　　　　┃
-------------------------//┗━┓　　　┏━┛
-------------------------//┃　　　┃  神兽保佑　　　　　　　　
-------------------------//┃　　　┃  代码无BUG！
-------------------------//┃　　　┗━━━┓
-------------------------//┃　　　　　　　┣┓
-------------------------//┃　　　　　　　┏┛
-------------------------//┗┓┓┏━┳┓┏┛
-------------------------// ┃┫┫　┃┫┫
-------------------------// ┗┻┛　┗┻┛
 */
class SetNameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)
        title_content.text = resources.getString(R.string.setname_setname)
        permission_quit.setOnClickListener {
            finish()
        }
        setname_deletename.setOnClickListener {
            setname_edt.setText(R.string.setname_activity_editexclear)
        }
        infotmation_complete.setOnClickListener {
            if (appContext.loginUser.userPassName == setname_edt.text.toString())
                return@setOnClickListener
            XmppManager.serviceImpl.apply {
                saveUserName(this@SetNameActivity, setname_edt.text.toString(), XmppParams(XmppRequestCode.UPLOAD_ICON), this@SetNameActivity)
            }
            // finish()
        }

        setname_edt.apply {
            setText(appContext.loginUser.displayName)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                    //   infotmation_complete.isEnabled = setname_edt.text.isNotEmpty() && !setname_edt.text.equals(appContext.loginUser.userName)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }

    }

    override fun onSuccess(params: XmppParams, result: Any) {
        appContext.loginUser.userName = setname_edt.text.toString()

        setResult(Activity.RESULT_OK)
        finish()
    }

}


