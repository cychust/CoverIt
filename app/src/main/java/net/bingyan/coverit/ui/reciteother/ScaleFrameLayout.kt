package net.bingyan.coverit.ui.reciteother

import android.content.Context
import android.graphics.PointF
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.FrameLayout
import net.bingyan.coverit.util.ViewHelper
import kotlin.math.sqrt


/**
 * Author       cychust
 * Date         2018.5.07
 * Time         23：50
 */

class ScaleFrameLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attributeSet, defStyle) {


    private  var screenWidth:Int=0
    private  var screenHeight:Int=0

    private var scaleGestureDetector: ScaleGestureDetector

    private var scale: Float = 1.0F

    private var lastMultiTouchTime: Long = System.currentTimeMillis()

    private var oldTime = System.currentTimeMillis()

    private var oldDis = 1f

    private var midPoint: PointF = PointF()             //中点

    private var leftFromScreen:Int=0
    private var rightFromScreen:Int=0

    private var preScale:Float=1f
    private var flag:Int=0
  //  private var viewDragHelper:ViewDragHelper



 /*   private val viewDragCallback=object:ViewDragHelper.Callback(){
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (preScale>1){
                return true
            }
            return false
        }
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (left < (screenWidth - screenWidth * preScale) / 2)
                left = (screenWidth - screenWidth * preScale) / 2 // 限制mainView可向左移动到的位置
            if (left > (screenWidth * preScale - screenWidth) / 2)
                left = (Int) (screenWidth * preScale - screenWidth) / 2;// 限制mainView可向右移动到的位置
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (top < (screenHeight - screenHeight * preScale) / 2) {
                top = (Int) (screenHeight - screenHeight * preScale) / 2;// 限制mainView可向上移动到的位置
            }
            if (top > (screenHeight * preScale - screenHeight) / 2) {
                top = (Int) (screenHeight * preScale - screenHeight) / 2;// 限制mainView可向上移动到的位置
            }
            return top;
        }

    }
*/
    init {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener())
      //  viewDragHelper= ViewDragHelper.create(this,viewDragCallback)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.pointerCount == 2) {
            Log.d("double tab", "intercept")

            oldDis = distance(ev)
            if (oldDis > 10f) {
                //savedMatrix.set(matrix)
                midPoint = middle(ev)
            }
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.pointerCount==1){


            return false
        }
        else if (event?.pointerCount == 2) {
            return scaleGestureDetector.onTouchEvent(event)
            /* when (event.action) {
                 MotionEvent.ACTION_DOWN -> {
                     Log.d("action", "action down")
                 }
                 MotionEvent.ACTION_POINTER_DOWN -> {
                     Log.d("action", "action down pointer")
                     //oldDis=distance(event)
                 }
                 MotionEvent.ACTION_MOVE -> {
                    // if (System.currentTimeMillis() - oldTime > 100) {   //控制跟新时间
                         Log.d("action", "action move")
                         val newDist = distance(event)
                         if (Math.abs(newDist - oldDis) > 20f) {
                             scale = newDist / oldDis
                             if (scale>=1) {
                                 Log.d("distance ", "scale" + scale)
                                 ViewHelper.setScaleX(this, scale)
                                 ViewHelper.setScaleY(this, scale)
                             }
                         oldTime = System.currentTimeMillis()
                     }
                 }
             }
             return true*/

        } else {
            return super.onTouchEvent(event)
        }
    }

    fun distance(event: MotionEvent): Float {
        val x: Float = event.getX(event.findPointerIndex(0)) - event.getX(event.findPointerIndex(1))
        val y: Float = event.getY(event.findPointerIndex(0)) - event.getY(event.findPointerIndex(1))
        return sqrt(x * x + y * y)
    }

    fun middle(event: MotionEvent): PointF {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        return PointF(x / 2, y / 2)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        leftFromScreen=getLeft()
        rightFromScreen=getRight()

    }


    inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {

        private var previousSpan:Float=0f
        private var currentSpan:Float=0f
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (flag==0) {
                previousSpan = detector!!.previousSpan
                currentSpan = detector.currentSpan
            }else{
                previousSpan = detector!!.currentSpan
                currentSpan = detector.currentSpan
                flag=0
            }
            if (System.currentTimeMillis() - oldTime > 100) {
                Log.d("scaleGesture", "onScale" + scale)
                // 缩小
                // scale = preScale-detector.getScaleFactor()/3;
                if (currentSpan>=previousSpan){
                    scale = (currentSpan - previousSpan)/1000+preScale
                }else {
                    if (scale>=1)
                    scale = -(previousSpan-currentSpan)/1000 +preScale
                }
                Log.d("scale",scale.toString())
                if (scale >= 1) {
                    ViewHelper.setScaleX(this@ScaleFrameLayout, scale);// x方向上缩放
                    ViewHelper.setScaleY(this@ScaleFrameLayout, scale);// y方向上缩放
                }

                oldTime = System.currentTimeMillis()
               // preScale=scale

            }
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            preScale=scale
            flag=1
        }
    }
}