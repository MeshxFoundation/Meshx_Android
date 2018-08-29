package com.pgy.meshim.activity

import android.content.Intent
import android.os.Bundle
import com.pgy.meshim.R
import kotlinx.android.synthetic.main.activity_setting_more.*

class SettingMoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_more)
//        initStatusBar(R.color.color_blue)
        initToolBar(R.string.setingmore_more)
        settingmore_gender.setOnClickListener {
            var intent = Intent(this, GendersettingActivity::class.java)
            intent.putExtra("GendersettingActivity", 0)
            startActivity(intent)
        }
        settingmore_location.setOnClickListener {

        }
    }
}
