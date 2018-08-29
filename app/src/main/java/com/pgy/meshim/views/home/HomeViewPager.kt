package com.pgy.meshim.views.home

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by lzy on 2017/12/1.
 */

class HomeViewPager : ViewPager {

    private var indicators: Array<HomeIndicatorView?>? = null

    private var isCanScroll = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (indicators == null || position >= indicators!!.size) return
                for (i in indicators!!.indices) {
                    indicators!![i]?.hideIndicator()
                }
                indicators!![position]?.showIndicator()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun setIndicators(indicators: Array<HomeIndicatorView?>) {
        this.indicators = indicators
    }

    fun getIndicator(position: Int): HomeIndicatorView? {
        return if (indicators == null || position < 0 || position >= indicators!!.size) null else indicators!![position]

    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

    fun setCanScroll(canScroll: Boolean) {
        isCanScroll = canScroll
    }
}
