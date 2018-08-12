package com.example.customview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class VolumeBarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val barPaint = Paint()
    private val thumbPaint = Paint()

    private val defaultBarWidth = resources.getDimensionPixelSize(R.dimen.volume_bar_default_width)
    private val defaultBarHeight = resources.getDimensionPixelSize(R.dimen.volume_bar_default_height)

    private var volumeLevelsCount: Int? = null
    private var currentVolumeLevel: Int? = null

    private val colorEvaluator = ArgbEvaluator()
    private var dotColor = R.color.colorPrimary
    private var selectedDotColor = R.color.colorAccent
    private var visibleDot = 5


    init {
        barPaint.color = Color.GRAY
        thumbPaint.color = Color.RED
        //var attributes :TypedArray = context.obtainStyledAttributes(attrs, )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> defaultBarWidth
            View.MeasureSpec.UNSPECIFIED -> defaultBarWidth
            else -> defaultBarWidth
        }

        val height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> defaultBarHeight
            View.MeasureSpec.UNSPECIFIED -> defaultBarHeight
            else -> defaultBarHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawBar(canvas)
        drawThumb(canvas)
    }

    private fun drawBar(canvas: Canvas) {
        canvas.drawRect(0.0F, 0.0F, width.toFloat(), height.toFloat(), barPaint)
    }

    private fun drawThumb(canvas: Canvas) {
        val thumbX = calculateThumbX()
        val thumbY = height.toFloat() / 2.0F
        val radius = height.toFloat() / 2.0F
        thumbPaint.isAntiAlias = true
        thumbPaint.color = calculateDotColor(radius)
        canvas.drawCircle(thumbX, thumbY, radius, thumbPaint)
        // 애니메이션 적용
    }

    private fun calculateDotColor( dotScale: Float): Int{
        return colorEvaluator.evaluate(dotScale, dotColor, selectedDotColor) as Int
    }

    private fun calculateThumbX(): Float {
        val volumeLevelsCount = this.volumeLevelsCount
        val currentVolumeLevel = this.currentVolumeLevel

        return if (volumeLevelsCount != null && currentVolumeLevel != null) {
            ((width - height) / volumeLevelsCount * currentVolumeLevel).toFloat() + height / 2.0F
        } else {
            0.0F
        }
    }

    fun calibrateVolumeLevels(volumeLevelsCount: Int, currentVolumeLevel: Int) {
        this.volumeLevelsCount = volumeLevelsCount
        this.currentVolumeLevel = currentVolumeLevel
        // 다시 그리기
        invalidate()
    }

    fun setVolumeLevel(volumeLevel: Int) {
        // 이전 current VolumeLevel
        // 이후 volumeLeve
        var animator: ValueAnimator = ValueAnimator.ofInt(currentVolumeLevel!!.toInt(), volumeLevel)
        animator.setDuration(200)
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator?) {
                currentVolumeLevel = p0!!.getAnimatedValue() as Int?
                invalidate()
            }
        })
        animator.start()
        currentVolumeLevel = volumeLevel

        invalidate()
    }

}

