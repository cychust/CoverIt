package net.bingyan.coverit.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider

import java.io.File

/**
 * Author       zdlly
 * Date         2017.12.23
 * Time         20:11
 */

object FileUtils {

    private val APP_DIR_NAME = "coverIt"
    private val FILE_DIR_NAME = "files"
    private var mRootDir: String? = null
    private var mAppRootDir: String? = null
    var fileDir: String? = null
        private set


    // filePath:  /sdcard/
    // filePath:  /data/data/
    val rootPath: String
        get() = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            Environment.getDataDirectory().absolutePath + "/data"
        }

    fun init() {
        mRootDir = rootPath
        if (mRootDir != null && "" != mRootDir) {
            mAppRootDir = mRootDir + "/" + APP_DIR_NAME
            fileDir = mAppRootDir + "/" + FILE_DIR_NAME
            val appDir = File(mAppRootDir!!)
            if (!appDir.exists()) {
                appDir.mkdirs()
            }
            val fileDir = File(mAppRootDir + "/" + FILE_DIR_NAME)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }

        } else {
            mRootDir = ""
            mAppRootDir = ""
            fileDir = ""
        }
    }

    /**
     * 打开文件
     * 兼容7.0
     *
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     * 当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     */
    @Throws(ActivityNotFoundException::class)
    fun startActionFile(context: Context?, file: File, contentType: String) {
        if (context == null) {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION//增加读写权限
        intent.setDataAndType(getUriForFile(context, file), contentType)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity    Activity
     * @param file        File
     * @param requestCode result requestCode
     */
    fun startActionCapture(activity: Activity?, file: File, requestCode: Int) {
        if (activity == null) {
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file))
        activity.startActivityForResult(intent, requestCode)
    }


    fun getUriForFile(context: Context?, file: File?): Uri {
        if (context == null || file == null) {
            throw NullPointerException()
        }
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context.applicationContext, "com.zdlly.coverIt.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
    }
}