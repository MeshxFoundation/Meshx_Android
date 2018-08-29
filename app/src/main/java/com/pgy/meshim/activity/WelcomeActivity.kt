package com.pgy.meshim.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.pgy.meshim.R
import com.pgy.meshim.utils.CommonUtils
import com.pgy.meshim.utils.db.DbUserUtils

class WelcomeActivity : BaseActivity() {

    private lateinit var ivWelcome: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivWelcome = ImageView(this)
        ivWelcome.scaleType = ImageView.ScaleType.FIT_XY
        CommonUtils.loadImage(this, R.mipmap.bg_welcome, 0, ivWelcome)
        setContentView(ivWelcome)
        loadAnimation()
    }

    private fun loadAnimation() {

        val animator = ObjectAnimator
                .ofFloat(ivWelcome, "alpha", 0.2f, 1.0f)
                .setDuration(4000)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                DbUserUtils.getLastLoginUser(appContext) {
                    it?.takeIf { it.userPassName.isNotEmpty() && it.userPassWord.isNotEmpty() }?.apply {
                        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    } ?: startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    finish()
                }
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        animator.start()
    }
}
