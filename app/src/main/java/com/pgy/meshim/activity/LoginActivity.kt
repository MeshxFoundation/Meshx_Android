package com.pgy.meshim.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.pgy.meshim.R
import com.pgy.meshim.utils.db.DbUserUtils
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.service.XmppParams
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by lzy on 2018/6/15.
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initToolBar(R.string.login, false)
        initView()
        enterUserFromDb()
    }

    private fun initView() {
        //init listener
        tvLogin.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
        tvRegister.setOnClickListener(this)
        ivPassNameClear.setOnClickListener(this)
        ivPasswordClear.setOnClickListener(this)
        ivPasswordShow.setOnClickListener(this)

        etPassName.addTextChangedListener(InputTextWatcher(ivPassNameClear))
        etPassword.addTextChangedListener(InputTextWatcher(ivPasswordClear))
    }


    private fun enterUserFromDb() {
        DbUserUtils.getLastLoginUser(appContext) {
            it?.apply {
                etPassName.setText(userPassName)
                etPassword.setText(userPassWord)
            } ?: DbUserUtils.getLastLogoutUser(appContext) {
                it?.apply {
                    etPassName.setText(userPassName)
                    etPassword.requestFocus()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvLogin -> {
                XmppManager.serviceImpl.requestLogin(this, etPassName.text.toString(),
                        etPassword.text.toString(), XmppParams(XmppRequestCode.LOGIN), this) { passName, name, passWord ->

                    appContext.loginUser = DbUserUtils.setLoginUserSync(appContext, passName, name,passWord)
                }
            }
            R.id.tvForgetPassword -> {
            }
            R.id.tvRegister -> {
//                startActivityForResult(Intent(this, RegisterActivity::class.java), 0x10)
            }
            R.id.ivPassNameClear -> {
                etPassName.setText("")
                etPassword.setText("")
            }
            R.id.ivPasswordClear -> etPassword.setText("")
            R.id.ivPasswordShow -> {
                ivPasswordShow.isSelected = !ivPasswordShow.isSelected

                etPassword.inputType = if (ivPasswordShow.isSelected)
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
            }
        }
    }

    override fun onSuccess(params: XmppParams, result: Any) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x10 && resultCode == Activity.RESULT_OK && data != null) {
            etPassName.setText(data.getStringExtra("passName"))
            etPassword.setText(data.getStringExtra("password"))
        }
    }

    inner class InputTextWatcher(private var clearView: View) : TextWatcher {

        override fun afterTextChanged(s: Editable) {
            clearView.visibility = if (s.isEmpty()) View.INVISIBLE else View.VISIBLE
            tvLogin.isEnabled = etPassName.text.isNotEmpty() && etPassword.text.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}