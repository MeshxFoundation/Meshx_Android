package com.pgy.meshim.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.pgy.meshim.R


class TriangleView : View {

    //边框画笔
    private lateinit var mStrokePaint: Paint

    //三角方向
    var direction: Int = DIRECTION_UP
        set(value) {
            field = value
            invalidate()
        }

    //填充颜色
    private var fillColor: Int = 0

    //边宽
    private var strokeWidth: Float = 0f

    //其他边的颜色
    private var borderColor: Int = 0

    private var isArrow: Boolean = false

    private var mPath: Path? = null

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView)
        direction = a.getInt(R.styleable.TriangleView_triangleDirection, DIRECTION_LEFT)
        fillColor = a.getColor(R.styleable.TriangleView_triangleColor, DEFAULT_COLOR)

        strokeWidth = a.getDimension(R.styleable.TriangleView_TGStrokeWidth, 1f)
        borderColor = a.getColor(R.styleable.TriangleView_TGStrokeColor, DEFAULT_COLOR)
        isArrow = a.getBoolean(R.styleable.TriangleView_is_arrow, false)

        a.recycle()
        initTool()
    }

    constructor(context: Context) : super(context) {
        initTool()
    }

    private fun initTool() {

        mStrokePaint = Paint()
        if (isArrow) {
            mStrokePaint.style = Paint.Style.STROKE
            mStrokePaint.color = borderColor
        } else {
            mStrokePaint.style = Paint.Style.FILL
            mStrokePaint.color = fillColor
        }

        mStrokePaint.strokeWidth = strokeWidth
        mStrokePaint.isAntiAlias = true

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSpecSize, heightSpecSize)
    }

    fun setTriangleColor(color: Int) {
        mStrokePaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom
        makePath(width, height, canvas)
    }

    private fun makePath(width: Int, height: Int, canvas: Canvas) {
        mPath = Path()
        val left = paddingLeft
        val right = left + width
        val top = paddingTop
        val bottom = top + height

        when (direction) {
            DIRECTION_UP -> {
                mPath?.moveTo(left.toFloat(), bottom.toFloat())
                mPath?.lineTo(left + width / 2f, top.toFloat())
                mPath?.lineTo(right.toFloat(), bottom.toFloat())
            }
            DIRECTION_DOWN -> {
                mPath?.moveTo(left.toFloat(), top.toFloat())
                mPath?.lineTo(left + width / 2f, bottom.toFloat())
                mPath?.lineTo(right.toFloat(), top.toFloat())
            }
            DIRECTION_LEFT -> {
                mPath?.moveTo(right.toFloat(), top.toFloat())
                mPath?.lineTo(left.toFloat(), top + height / 2f)
                mPath?.lineTo(right.toFloat(), bottom.toFloat())
            }
            DIRECTION_RIGHT -> {
                mPath?.moveTo(left.toFloat(), top.toFloat())
                mPath?.lineTo(right.toFloat(), top + height / 2f)
                mPath?.lineTo(left.toFloat(), bottom.toFloat())
            }
        }
        if (!isArrow)
            mPath?.close()
        canvas.drawPath(mPath, mStrokePaint)
    }

    companion object {

        /*
     *<attr name="triangleEnableStroke" format="boolean"/>
     <attr name="triangleStrokeWidth" format="dimension|reference"/>
     <attr name="triangleDirectionStrokeColor" format="fillColor|reference"/>
     <attr name="triangleStrokeColor" format="fillColor|reference"/>
     */
        private val DEFAULT_COLOR = Color.WHITE
        val DIRECTION_UP = 0
        val DIRECTION_DOWN = 1
        val DIRECTION_LEFT = 2
        val DIRECTION_RIGHT = 3
    }
}
