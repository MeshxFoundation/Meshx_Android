package com.pgy.meshim.views.decoration


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.View
import com.pgy.meshim.R

/**
 * Created by lzy on 2017/11/29.
 */

class DefaultDecoration @JvmOverloads constructor(mContext: Context, dividerHeight: Float = 1f) : RecyclerView.ItemDecoration() {
    private var mOrientation = VERTICAL_LIST
    private val lineHeight: Float
    private val linePaint: Paint = Paint()

    init {
        linePaint.isAntiAlias = true

        val version = Build.VERSION.SDK_INT
        val color: Int
        color = if (version >= Build.VERSION_CODES.M)
            ContextCompat.getColor(mContext, R.color.line_color)
        else
            mContext.resources.getColor(R.color.line_color)
        linePaint.color = color

        lineHeight = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dividerHeight,
                mContext.resources.displayMetrics)
        linePaint.strokeWidth = lineHeight
    }

    //设置屏幕的方向
    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(c, parent)
        } else {
            drawHorizontalLine(c, parent)
        }
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    private fun drawHorizontalLine(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        (0 until childCount)
                .map { parent.getChildAt(it) }
                .filter {
                    val position = parent.layoutManager.getPosition(it)
                    isNormalItem(parent.adapter, position)
                }
                .forEach {
                    //获得child的布局信息
                    val params = it.layoutParams as RecyclerView.LayoutParams
                    val bottom = it.bottom - params.bottomMargin
                    c.drawLine(left.toFloat(), bottom.toFloat(), right.toFloat(), bottom.toFloat(), linePaint)
                }
    }

    //画竖线
    private fun drawVerticalLine(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        (0 until childCount)
                .map { parent.getChildAt(it) }
                .filter {
                    val position = parent.layoutManager.getPosition(it)
                    isNormalItem(parent.adapter, position)
                }
                .forEach {
                    //获得child的布局信息
                    val params = it.layoutParams as RecyclerView.LayoutParams
                    val left = it.right + params.rightMargin - (lineHeight / 2f)
                    c.drawLine(left, top.toFloat(), left, bottom.toFloat(), linePaint)
                }

    }

    private fun isNormalItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, position: Int) =true

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.layoutManager.getPosition(view)

        if (!isNormalItem(parent.adapter, position)) {
            outRect.set(0, 0, 0, 0)
            return
        }

        if (mOrientation == HORIZONTAL_LIST) {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, lineHeight.toInt(), 0)
        } else {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, lineHeight.toInt())
        }
    }

    companion object {

        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}
