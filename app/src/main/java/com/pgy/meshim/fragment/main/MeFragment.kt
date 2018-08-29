package com.pgy.meshim.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pgy.meshim.R
import com.pgy.meshim.activity.*
import com.pgy.meshim.fragment.BaseFragment
import com.pgy.meshim.utils.CommonUtils
import com.pgy.meshim.utils.ResiurceWraUtils
import com.pgy.meshim.utils.db.DbUserUtils
import com.pgy.xmpplib.XmppUtils
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.service.XmppParams
import kotlinx.android.synthetic.main.fragment_me.*


/**
 * Created by lzy on 2018/6/15.
 */
class MeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_me, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initToolBar(view, R.string.main_fragment_me, false)
        btnLogout.setOnClickListener {
            XmppManager.serviceImpl.logout(requireContext(), XmppParams(XmppRequestCode.LOGOUT), this)
        }

        mefragment_ivuserbtn.setOnClickListener {
            startActivity(Intent(requireContext(), MeinformationActivity::class.java))
        }
        btnaboutus.setOnClickListener {
            startActivity(Intent(requireContext(), AbloutUsActivity::class.java))
        }

        btnlanguage.setOnClickListener {
            startActivity(Intent(requireContext(), LanguageSettingActivity::class.java))
        }

        btnNetSetting.setOnClickListener {
            NetSettingActivity.start(requireContext())
        }

        when (ResiurceWraUtils.getLanguageType(requireContext())) {
            ResiurceWraUtils.LANGUAGE_CN -> textlanguage.setText(R.string.main_fragment_langungeforchinese)
            ResiurceWraUtils.LANGUAGE_SN -> textlanguage.setText(R.string.main_fragment_langungeforspanish)
            ResiurceWraUtils.LANGUAGE_EN -> textlanguage.setText(R.string.main_fragment_langungeforenglish)
        }

        if (isLogin())
            onLoginSuccess()

        mefragment_ivuserbtn.setPadding(0, getStatusBarHeight().toInt(), 0, 0)
    }

    private fun loadUserIcon(skipMemoryCache: Boolean) {
        CommonUtils.loadUserImage(requireContext(), XmppUtils.getUserIdByPassName(appContext.loginUser.userPassName), ivUser, skipMemoryCache)
    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        loadUserIcon(false)
        tvUserName.text = appContext.loginUser.displayName
    }

    override fun onResume() {
        super.onResume()
        if (isLogin()) {
            loadUserIcon(false)
            tvUserName.text = appContext.loginUser.displayName
        }
    }


    override fun onSuccess(params: XmppParams, result: Any) {
        super.onSuccess(params, result)

        when (params.requestCode) {
            XmppRequestCode.LOGOUT -> {
                XmppManager.logout()
                DbUserUtils.userLogout(appContext) {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            }

            XmppRequestCode.UPLOAD_ICON -> {
                loadUserIcon(true)
            }
        }
    }


}