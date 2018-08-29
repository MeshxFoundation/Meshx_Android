package com.pgy.meshim.utils

import android.app.Fragment
import android.content.Intent
import android.content.pm.ActivityInfo
import com.oden.syd_camera.SydCameraActivity
import com.oden.syd_camera.camera.CameraParaUtil
import com.pgy.meshim.fragment.main.MeFragment
import com.pgy.xmpplib.myglide.GlideEngineformati
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType

/**
 * Created by burning on 2018/7/9.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * -------------------------//┏┓　　　┏┓
 * -------------------------//┏┛┻━━━┛┻┓
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　━　　　┃
 * -------------------------//┃　┳┛　┗┳　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　┻　　　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┗━┓　　　┏━┛
 * -------------------------//┃　　　┃  神兽保佑
 * -------------------------//┃　　　┃  代码无BUG！
 * -------------------------//┃　　　┗━━━┓
 * -------------------------//┃　　　　　　　┣┓
 * -------------------------//┃　　　　　　　┏┛
 * -------------------------//┗┓┓┏━┳┓┏┛
 * -------------------------// ┃┫┫　┃┫┫
 * -------------------------// ┗┻┛　┗┻┛
 */
class PhotoUtils {

    private fun startCrop(fragment: Fragment, path: String) {

    }

    companion object {
        var REQUEST_CODE_FROM_Matisse = 0x1334
        var REQUEST_CODE_FROM_CAMERA = 0x3122
        fun getPhoto(fragment: MeFragment, type: Int) {
            if (type == 1) {
                val intent = Intent(fragment.activity, SydCameraActivity::class.java)
                intent.putExtra(CameraParaUtil.picQuality, 70) //图片质量0~100
                intent.putExtra(CameraParaUtil.picWidth, fragment.resources.getDisplayMetrics().heightPixels)  //照片最小宽度配置，高度根据屏幕比例自动配置
                intent.putExtra(CameraParaUtil.previewWidth, fragment.resources.getDisplayMetrics().widthPixels)  //相机预览界面最小宽度配置，高度根据屏幕比例自动配置
                fragment.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA)
            } else {
                Matisse.from(fragment)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .maxSelectable(1)
                        //   .addFilter(Gif(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(fragment.resources.getDisplayMetrics().widthPixels / 3 + 30)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(GlideEngineformati())
                        .forResult(REQUEST_CODE_FROM_Matisse)
            }
        }
    }
}
