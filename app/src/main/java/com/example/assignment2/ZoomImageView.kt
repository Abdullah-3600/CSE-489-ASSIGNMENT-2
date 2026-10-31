package com.example.assignment2

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class ZoomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val imageMatrixState = Matrix()
    private val scaleGestureDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector

    private var scaleFactor = 1.0f
    private val minScale = 0.5f
    private val maxScale = 5.0f

    private var mode = NONE
    private val lastTouchPoint = PointF()
    private val startTouchPoint = PointF()

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }

    init {
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        val currentPoint = PointF(event.x, event.y)

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchPoint.set(currentPoint)
                startTouchPoint.set(lastTouchPoint)
                mode = DRAG
            }

            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG && scaleFactor > 1.0f) {
                    val deltaX = currentPoint.x - lastTouchPoint.x
                    val deltaY = currentPoint.y - lastTouchPoint.y
                    imageMatrixState.postTranslate(deltaX, deltaY)
                    lastTouchPoint.set(currentPoint.x, currentPoint.y)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
        }

        imageMatrix = imageMatrixState
        invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var factor = detector.scaleFactor
            val prevScale = scaleFactor
            scaleFactor *= factor

            if (scaleFactor > maxScale) {
                scaleFactor = maxScale
                factor = maxScale / prevScale
            } else if (scaleFactor < minScale) {
                scaleFactor = minScale
                factor = minScale / prevScale
            }

            if (scaleFactor in minScale..maxScale) {
                imageMatrixState.postScale(factor, factor, detector.focusX, detector.focusY)
                mode = ZOOM
            }
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            resetZoom()
            return true
        }
    }

    fun zoomIn() {
        if (scaleFactor < maxScale) {
            scaleFactor *= 1.25f
            imageMatrixState.postScale(1.25f, 1.25f, width / 2f, height / 2f)
            imageMatrix = imageMatrixState
            invalidate()
        }
    }

    fun zoomOut() {
        if (scaleFactor > minScale) {
            scaleFactor /= 1.25f
            imageMatrixState.postScale(1 / 1.25f, 1 / 1.25f, width / 2f, height / 2f)
            imageMatrix = imageMatrixState
            invalidate()
        }
    }

    fun resetZoom() {
        scaleFactor = 1.0f
        imageMatrixState.reset()
        imageMatrix = imageMatrixState
        invalidate()
    }
}