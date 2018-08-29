package com.pgy.meshim.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.DisplayMetrics
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pgy.meshim.MeshApplication
import com.pgy.meshim.R
import java.io.File

/**
 * Created by lzy on 2017/11/29.
 */

object CommonUtils {

    const val TAG = "CommonUtils"

    fun getApplication(context: Context): MeshApplication {
        return context.applicationContext as MeshApplication
    }

    fun getScreenWidth(activity: Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun isMobilePhone(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && phone.startsWith("1") && phone.length == 11
    }

    //---------------------load image------------------------------

    fun loadUserImage(context: Context, jid: String?, iv: ImageView,skipMemoryCache:Boolean = false) {
        RequestOptions()
                .placeholder(R.mipmap.icon_default_user)
                .error(R.mipmap.icon_default_user)
                .timeout(10 * 1000)
                .centerCrop()
                .skipMemoryCache(skipMemoryCache).apply {
                    Glide.with(context)
                            .load(jid)
                            .apply(this)
                            .into(iv)
                }
    }

    fun loadImage(context: Context, url: String?, placeholderRes: Int, iv: ImageView) {
        loadImage(context, url, placeholderRes, placeholderRes, 0, iv)
    }

    fun loadImage(context: Context, url: String?, placeholderRes: Int, errorRes: Int, roundRadius: Int, iv: ImageView) {
        Glide.with(context)
                .load(if (TextUtils.isEmpty(url)) "error" else url)
                .apply(getRequestOptions(context, placeholderRes, errorRes, roundRadius))
                .into(iv)
    }

    fun loadImage(context: Context, file: File?, placeholderRes: Int, errorRes: Int, roundRadius: Int, iv: ImageView) {
        if (file == null) return
        Glide.with(context)
                .load(file)
                .apply(getRequestOptions(context, placeholderRes, errorRes, roundRadius))
                .into(iv)
    }

    fun loadImage(context: Context, res: Int, roundRadius: Int, iv: ImageView) {
        Glide.with(context)
                .load(res)
                .apply(getRequestOptions(context, res, res, roundRadius))
                .into(iv)
    }

    fun loadImage(context: Context, data: ByteArray?, errorRes: Int, roundRadius: Int, iv: ImageView) {
        Glide.with(context)
                .load(data)
                .apply(getRequestOptions(context, errorRes, errorRes, roundRadius))
                .into(iv)
    }

    private fun getRequestOptions(context: Context, placeholderRes: Int, errorRes: Int, roundRadius: Int): RequestOptions {
        val option = RequestOptions()
                .placeholder(placeholderRes)
                .error(errorRes)
                .timeout(10 * 1000)
                .centerCrop()
                .skipMemoryCache(false)

        return option.takeIf { roundRadius > 0 }?.transform(GlideRoundTransform(context, roundRadius))
                ?: option
    }
    //---------------------load image------------------------------
}

