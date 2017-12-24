package net.bingyan.coverit.widget

/**
 * Author       zdlly
 * Date         2017.12.21
 * Time         11:42
 */
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import net.bingyan.coverit.R

class CustomToast(context: Context, view: View, duration: Int) {
    /**
     * 获取Toast
     */
    var toast: Toast? = null
        private set
    private var toastView: LinearLayout? = null

    init {
        toast = Toast(context)
        toast!!.view = view
        toast!!.duration = duration
    }

    /**
     * 向Toast中添加自定义View
     */
    fun addView(view: View, position: Int): CustomToast {
        toastView = toast!!.view as LinearLayout
        toastView!!.addView(view, position)
        return this
    }

    /**
     * 设置Toast字体及背景
     */
    fun setToastBackground(messageColor: Int, background: Int): CustomToast {
        val view = toast!!.view
        if (view != null) {
            val message = view.findViewById<TextView>(R.id.message)
            message.setBackgroundResource(background)
            message.setTextColor(messageColor)
        }

        return this
    }

    /**
     * 短时间显示Toast
     */
    fun Short(context: Context, message: CharSequence): CustomToast {
        if (toast == null || toastView != null && toastView!!.childCount > 1) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toastView = null
        } else {
            toast!!.setText(message)
            toast!!.duration = Toast.LENGTH_SHORT
        }
        return this
    }

    /**
     * 长时间显示toast
     */
    fun Long(context: Context, message: CharSequence): CustomToast {
        if (toast == null || toastView != null && toastView!!.childCount > 1) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toastView = null
        } else {
            toast!!.setText(message)
            toast!!.duration = Toast.LENGTH_LONG
        }
        return this
    }

    /**
     * 自定义显示Toast的时长
     */
    fun Indefinite(context: Context, message: CharSequence,
                   duration: Int): CustomToast {
        if (toast == null || toastView != null && toastView!!.childCount > 1) {
            toast = Toast.makeText(context, message, duration)
            toastView = null
        } else {
            toast!!.setText(message)
            toast!!.duration = duration
        }

        return this
    }

    /**
     * 显示Toast
     */
    fun show(): CustomToast {
        toast!!.show()
        return this
    }
}