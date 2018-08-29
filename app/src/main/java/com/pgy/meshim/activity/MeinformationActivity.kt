package com.pgy.meshim.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.pgy.meshim.R
import com.pgy.meshim.utils.CommonUtils
import com.pgy.xmpplib.XmppUtils
import com.pgy.xmpplib.manager.XmppManager
import com.pgy.xmpplib.manager.XmppRequestCode
import com.pgy.xmpplib.myglide.PhotoLuban
import com.pgy.xmpplib.service.XmppParams
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_meinformation.*
import top.zibin.luban.OnCompressListener
import java.io.File

class MeinformationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meinformation)
//        initStatusBar(R.color.bg_dark_blue)
        initToolBar(R.string.infotmation_infotmation)
        information_photo.setOnClickListener {
            CropImage.activity().start(this)
        }
        texusername.text = appContext.loginUser.displayName
        CommonUtils.loadUserImage(this, XmppUtils.getUserIdByPassName(appContext.loginUser.userPassName), information_ivUser, true)
        btnameselect.setOnClickListener {
            startActivityForResult(Intent(this, SetNameActivity::class.java),0x10)
        }
        btnmore.setOnClickListener {
            startActivity(Intent(this, SettingMoreActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                photoCompre(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }else if(requestCode == 0x10 && resultCode == Activity.RESULT_OK){
            texusername.text = appContext.loginUser.displayName
        }

    }

    fun photoCompre(uri: Uri) {
        PhotoLuban.comprePhoto(this, uri, object : OnCompressListener {
            override fun onSuccess(file: File?) {
                file?.apply {
                    val jid = XmppUtils.getUserIdByPassName(appContext.loginUser.userPassName)
                    XmppManager.serviceImpl.changeIcon(this@MeinformationActivity, jid, file, XmppParams(XmppRequestCode.UPLOAD_ICON), this@MeinformationActivity)
                }
            }

            override fun onError(e: Throwable?) {
                showShortToast(R.string.fragment_me_upload_failed)
            }

            override fun onStart() {

            }

        })
    }
}
