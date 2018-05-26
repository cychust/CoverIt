package net.bingyan.coverit.util

import android.content.Context

class ResUtil{
    companion object {
        fun dp2px(ctx: Context, dp: Float): Float {
            val scale = ctx.resources.displayMetrics.density
            return dp * scale + 0.5f
        }

        fun px2dp(ctx: Context, px: Float): Float {
            val scale = ctx.resources.displayMetrics.density
            return px / scale + 0.5f
        }

        fun sp2px(ctx: Context, sp: Float): Float {
            val scale = ctx.resources.displayMetrics.scaledDensity
            return sp * scale + 0.5f
        }

        fun px2sp(ctx: Context, px: Float): Float {
            val scale = ctx.resources.displayMetrics.scaledDensity
            return px / scale + 0.5f
        }

    }
}