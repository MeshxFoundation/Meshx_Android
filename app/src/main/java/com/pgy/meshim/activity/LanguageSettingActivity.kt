package com.pgy.meshim.activity

import android.content.Intent
import android.os.Bundle
import com.pgy.meshim.R
import com.pgy.meshim.utils.ResiurceWraUtils
import kotlinx.android.synthetic.main.activity_language_setting.*
import kotlinx.android.synthetic.main.select_seting_title.*

/**
 * Created by lzy on 2018/8/16.
 */
class LanguageSettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_setting)
        permission_quit.setOnClickListener {
            finish()
        }

        title_content.text = resources.getString(R.string.main_fragment_langunge)
        when (ResiurceWraUtils.getLanguageType(this)) {
            ResiurceWraUtils.LANGUAGE_CN -> rbChinese.isChecked = true
            ResiurceWraUtils.LANGUAGE_SN -> rbSpanish.isChecked = true
            else -> rbEnglish.isChecked = true
        }

        infotmation_complete.setOnClickListener {
            val locale = when {
                rbChinese.isChecked -> ResiurceWraUtils.LANGUAGE_CN
                rbSpanish.isChecked -> ResiurceWraUtils.LANGUAGE_SN
                else -> ResiurceWraUtils.LANGUAGE_EN
            }

            ResiurceWraUtils.setLanguage(this, locale)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }
}