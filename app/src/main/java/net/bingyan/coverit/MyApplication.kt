package net.bingyan.coverit

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * Author       zdlly
 * Date         2018.3.27
 * Time         23:19
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}