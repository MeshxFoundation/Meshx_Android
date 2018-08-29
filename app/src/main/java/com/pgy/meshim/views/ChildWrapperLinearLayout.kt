package com.pgy.meshim.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.pgy.meshim.R

/**
 * Created by lzy on 2018/7/5.
 */
class ChildWrapperLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var contentWrapperIndex = -1

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ChildWrapperLinearLayout).apply {
            contentWrapperIndex = getInt(R.styleable.ChildWrapperLinearLayout_cwll_index, -1)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == VERTICAL || contentWrapperIndex < 0 || contentWrapperIndex >= childCount) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        var horLength = 0
        var maxHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)

            val params = childView.layoutParams as LinearLayout.LayoutParams

            val childMeasureWidth = when (params.width) {
                LayoutParams.WRAP_CONTENT -> MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED)
                LayoutParams.MATCH_PARENT -> MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                else -> MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY)
            }
            val childMeasureHeight = when (params.height) {
                LayoutParams.WRAP_CONTENT -> MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED)
                LayoutParams.MATCH_PARENT -> MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                else -> MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY)
            }

            childView.measure(childMeasureWidth, childMeasureHeight)

            if (i == contentWrapperIndex) continue
            if (childView.measuredHeight > maxHeight) maxHeight = childView.measuredHeight
            horLength += (childView.measuredWidth + params.marginStart + params.marginEnd)
        }

        val wrapperChild = getChildAt(contentWrapperIndex)

        wrapperChild.measure(MeasureSpec.makeMeasureSpec(width - horLength, MeasureSpec.AT_MOST),
                0)
        if (wrapperChild.measuredHeight > maxHeight) maxHeight = wrapperChild.measuredHeight

        setMeasuredDimension(width, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (orientation == VERTICAL) {
            super.onLayout(changed, l, t, r, b)
            return
        }

        var end = width
        for (i in childCount - 1 downTo 0) {
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) continue

            childView.apply {
                val params = layoutParams as LinearLayout.LayoutParams
                end -= params.rightMargin
                val top = (this@ChildWrapperLinearLayout.height - measuredHeight) / 2f
                layout(end - measuredWidth, top.toInt(), end, (top + measuredHeight).toInt())
                end -= measuredWidth + params.marginStart
            }
        }
    }
}