package net.bingyan.coverit.util

import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent

/**
 * Author       zdlly
 * Date         2018.4.12
 * Time         23:49
 */
object ToolScaleViewUtil {
    internal fun spacing(event: MotionEvent): Float {
        return try {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            Math.sqrt((x * x + y * y).toDouble()).toFloat()
        } catch (ex: IllegalArgumentException) {
            Log.v("TAG", ex.localizedMessage)
            0f
        }

    }

    internal fun midPoint(event: MotionEvent): PointF {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        return PointF(x / 2, y / 2)
    }
}
