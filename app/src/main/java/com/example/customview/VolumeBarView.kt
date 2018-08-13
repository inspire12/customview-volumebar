package com.example.customview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class VolumeBarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val thumbPaint = Paint()

    private val defaultBarWidth = resources.getDimensionPixelSize(R.dimen.volume_bar_default_width)
    private val defaultBarHeight = resources.getDimensionPixelSize(R.dimen.volume_bar_default_height)

    private val colorEvaluator = ArgbEvaluator()
    private var dotColor = R.color.colorPrimary
    private var selectedDotColor = R.color.colorAccent

    /* 페이지 네이트 값 처리*/
    private var visibleDot:Int = 5
    private var total = 100
    private var interval = 20
    private var curLoc = 0
    private var isPrev = true

    /* 애니메이션 처리 */
    private var mDuration = 500L
    /**
     * 50 으로 시작
     * 좌우 20씩으로 해서
     * 10 30 50 70 90 5개 보여주기
     * 일단 반지름은 똑같게 처리 + 애니메이션 처리
     * 안보이는 거 -10 110 도 있어야 할거 같다
     *
     */



    fun settingIndicator(total: Int, visibleDot: Int){
        this.total = total
        this.visibleDot = visibleDot
        this.interval = total / visibleDot
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
        curLoc = height / 2
        interval = (width - height) / (visibleDot-1)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {

        drawThumb(canvas)
    }

    fun prevPage(){
        isPrev = true
        setAnimation(interval+ height/2, height/2)
    }

    fun nextPage(){
        isPrev = false
        setAnimation(height/2, interval+height/2)
    }

    private fun drawThumb(canvas: Canvas) {
        var thumbX = 0F
        val thumbY = height.toFloat() / 2.0F
        var radius = height.toFloat() / 2.0F
        thumbPaint.isAntiAlias = true
        thumbPaint.color = calculateDotColor(radius)
//var i = 0
        for (i in -1 until visibleDot +1 ){
            thumbX = calcualate(curLoc.toFloat(),  0F, (interval+height/2).toFloat()) + (interval * i).toFloat()
            if(radius > interval){
                radius = interval.toFloat()
            }
            if(isPrev){
                if(i == visibleDot / 2 ){
                    // 가운데 는 다른 색깔?
                    thumbPaint.color = Color.RED
                    canvas.drawCircle(thumbX, thumbY, radius, thumbPaint)
                }else{
                    thumbPaint.color = Color.GRAY
                }
            }else{
                if(i == visibleDot / 2 - 1){
                    // 가운데 는 다른 색깔?
                    thumbPaint.color = Color.RED
                    canvas.drawCircle(thumbX, thumbY, radius, thumbPaint)
                }else{
                    thumbPaint.color = Color.GRAY
                }
            }


            canvas.drawCircle(thumbX, thumbY, radius, thumbPaint)
        }
        // 애니메이션 적용
    }

    private fun calculateDotColor( dotScale: Float): Int{
        return colorEvaluator.evaluate(dotScale, dotColor, selectedDotColor) as Int
    }


    /**
     * currentLoc :
     */
    fun calibrateLevels(currentLoc: Int) {

        // 다시 그리기
        invalidate()
    }

    fun  setAnimation(prevLoc:Int, nextLoc: Int) {
        // 이전 current VolumeLevel
        // 이후 volumeLeve

        /**
         * 인터폴레이션
         */
        var animator: ValueAnimator = ValueAnimator.ofInt(prevLoc, nextLoc)
        animator.setDuration(mDuration)
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator?) {
                curLoc = p0!!.getAnimatedValue() as Int
                invalidate()
            }
        })
        animator.start()
        invalidate()
    }

    /**
     * https://github.com/daimajia/AnimationEasingFunctions
     */
    fun calcualate(curLoc: Float, preLoc: Float, nextLoc: Float): Float{
        CustomLog.d(curLoc.toString())
        CustomLog.d(preLoc.toString())
        CustomLog.d(nextLoc.toString())
        // linear
        // t 0 ~ mDration
        // b = 0
        // c = 최대

        var t = curLoc / nextLoc * mDuration
        val b = preLoc
        val c = nextLoc
        val d = mDuration

        /* linear*/
         var value = c*t /d + b
//      /*
//        t = t/d-1
//        return c * Math.sqrt((1 - t * t).toDouble()).toFloat() + b;
//        t = t / d -1
//        var value = c * (t * t * t * t * t + 1) + b

        /* 애니메이션 범위 */
        if(value < preLoc) value = preLoc
        else if(value > nextLoc) value = nextLoc

        return value
    }


}

