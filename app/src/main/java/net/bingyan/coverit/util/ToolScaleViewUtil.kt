package net.bingyan.coverit.util

import android.graphics.PointF
import android.view.MotionEvent

/**
 * Author       cychust
 * Date         2018.4.12
 * Time         23:49
 */
object ToolScaleViewUtil {
    @JvmStatic
    fun spacing(event: MotionEvent): Float {

        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt(((x * x + y * y).toDouble())).toFloat()
    }

    @JvmStatic
    fun midPoint(event: MotionEvent): PointF {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        return PointF(x / 2, y / 2)
    }
}
