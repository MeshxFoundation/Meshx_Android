package com.pgy.meshim.views.home

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.pgy.meshim.R


/**
 * Created by lzy on 2017/12/1.
 */

class HomeIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var ivIndicator: ImageView
    private var tvIndicator: TextView
    private var tvIndicatorTip: TextView
    var index: Int = 0
    private var mCallback: IndicatorCallback? = null
    private var tipCount = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.indicator_home, this, true)
        //        addView(contentView);
        ivIndicator = findViewById<View>(R.id.iv_indicator) as ImageView
        tvIndicator = findViewById<View>(R.id.tv_indicator) as TextView
        tvIndicatorTip = findViewById<View>(R.id.tv_tip) as TextView
        tvIndicatorTip.visibility = View.GONE
        setOnClickListener(this)
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.HomeIndicatorView)
            array.getResourceId(R.styleable.HomeIndicatorView_hiv_src, -1).takeIf { it > 0 }
                    ?.let { ivIndicator.setImageResource(it) }
            tvIndicator.text = array.getString(R.styleable.HomeIndicatorView_hiv_text)
            tvIndicator.setTextColor(array.getColor(R.styleable.HomeIndicatorView_hiv_text_color, Color.WHITE))
            array.recycle()
        }
    }

    fun setIndicatorImage(resId: Int) {
        ivIndicator.setImageResource(resId)
    }

    fun setIndicatorText(text: String) {
        tvIndicator.text = text
    }

    fun showIndicator() {
        ivIndicator.isSelected = true
        tvIndicator.setTextColor(ContextCompat.getColor(context, R.color.text_color_blue))
    }

    fun hideIndicator() {
        ivIndicator.isSelected = false
        tvIndicator.setTextColor(ContextCompat.getColor(context, R.color.text_color_black))
    }

    fun setIndicatorTip(count: Int) {
        tipCount = count

        if (count <= 0)
            tvIndicatorTip.visibility = View.GONE
        else {
            tvIndicatorTip.visibility = View.VISIBLE
            tvIndicatorTip.text = count.takeIf { it <= 99 }?.toString() ?: "99+"
        }
    }

    fun addTipCount(count: Int) {
        tipCount += count
        setIndicatorTip(tipCount)
    }

    fun setIndicatorCallback(mCallback: IndicatorCallback) {
        this.mCallback = mCallback
    }

    override fun onClick(v: View) {
        if (mCallback == null) return
        mCallback?.onSelect(index)
        showIndicator()
    }

    interface IndicatorCallback {
        fun onSelect(index: Int)
    }

    companion object {

        val INDICATOR_INDEX = 1
        val INDICATOR_SERVICE = 2
        val INDICATOR_ME = 3
    }
}
