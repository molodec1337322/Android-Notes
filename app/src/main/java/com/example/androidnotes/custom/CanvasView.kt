package com.example.androidnotes.custom

import android.content.Context
import android.graphics.Bitmap

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.example.androidnotes.R

class CanvasView(context: Context, attrs: AttributeSet): View(context, attrs) {
    lateinit var canvas: Canvas
    lateinit var bitmap: Bitmap

    var isBitmapChanged = false

    var mStrokeWidth = 12f
    var color = ResourcesCompat.getColor(resources, R.color.black, null)
    var paint = Paint().apply {
        color //= ResourcesCompat.getColor(resources, R.color.black, null)
        isAntiAlias = false
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = mStrokeWidth
    }
    private var path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(::bitmap.isInitialized){
            bitmap.recycle()
        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(ResourcesCompat.getColor(resources, R.color.white, null))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun touchStart(){
        isBitmapChanged = true
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove(){
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)

        if(dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            canvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp(){
        path.reset()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event!!.x
        motionTouchEventY = event!!.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true //super.onTouchEvent(event)
    }
}