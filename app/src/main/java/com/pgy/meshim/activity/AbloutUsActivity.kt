package com.pgy.meshim.activity

import android.os.Bundle
import com.pgy.meshim.R
import kotlinx.android.synthetic.main.activity_ablout_us.*

class AbloutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ablout_us)
        initToolBar(R.string.main_fragment_about_us)
        ablout_version.text = String.format(resources.getString(R.string.ablout_us_version_code),
                packageManager.getPackageInfo(packageName, 0).versionName)
    }
}
