package com.pgy.meshim.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pgy.meshim.R
import kotlinx.android.synthetic.main.activity_net_setting.*

/**
 * Created by lzy on 2018/8/8.
 */
class NetSettingActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NetSettingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_setting)
        initToolBar(R.string.main_fragment_net_setting)
        tbNetStatus.isChecked = appPref.networklessStatus
//        tbNetStatus.setOnCheckedChangeListener { _, isChecked ->
//            appPref.networklessStatus = isChecked
//            if (isChecked)
//                if (BluetoothAdapter.getDefaultAdapter().enable())
//                    MeshManager.init(this, appContext.loginUser.userPassName)
//                else
//                    startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0x10)
//            else
//                MeshManager.stop(this)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 0x10 && resultCode == Activity.RESULT_OK)
//            MeshManager.init(this, appContext.loginUser.userPassName)
    }
}