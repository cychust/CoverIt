package net.bingyan.coverit.util

/**
 * Author       zdlly
 * Date         2018.4.15
 * Time         16:21
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object PhotoBitmapUtils {

    /**
     * 存放拍摄图片的文件夹
     */
    private val FILES_NAME = "/MyPhoto"
    /**
     * 获取的时间格式
     */
    val TIME_STYLE = "yyyyMMddHHmmss"
    /**
     * 图片种类
     */
    val IMAGE_TYPE = ".png"

    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private fun getPhoneRootPath(context: Context): String {
        // 是否有SD卡
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            context.externalCacheDir!!.path
        } else {
            // 获取apk包下的缓存路径
            context.cacheDir.path
        }
    }

    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    fun getPhotoFileName(context: Context): String {
        val file = File(getPhoneRootPath(context) + FILES_NAME)
        // 判断文件是否已经存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs()
        }
        // 设置图片文件名称
        val format = SimpleDateFormat(TIME_STYLE, Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        val time = format.format(date)
        val photoName = "/$time$IMAGE_TYPE"
        return file.toString() + photoName
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    fun savePhotoToSD(mbitmap: Bitmap?, context: Context): String? {
        var outStream: FileOutputStream? = null
        val fileName = getPhotoFileName(context)
        try {
            outStream = FileOutputStream(fileName)
            // 把数据写入文件，100表示不压缩
            mbitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close()
                }
                mbitmap?.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    fun getCompressPhoto(path: String): Bitmap {
        var options: BitmapFactory.Options? = BitmapFactory.Options()
        options!!.inJustDecodeBounds = false
        options.inSampleSize = 10  // 图片的大小设置为原来的十分之一
        val bmp = BitmapFactory.decodeFile(path, options)
        return bmp
    }

    /**
     * 处理旋转后的图片
     * @param originpath 原图路径
     * @return 返回修复完毕后的图片路径
     */
    fun amendRotatePhoto(originpath: String): String {

        // 取得图片旋转角度
        val angle = readPictureDegree(originpath)

        // 把原图压缩后得到Bitmap对象
        //val bmp = getCompressPhoto(originpath)
        val bmp = BitmapFactory.decodeFile(originpath)


        // 修复图片被旋转的角度
        val bitmap = rotaingImageView(angle, bmp)

        // 保存修复后的图片并返回保存后的图片路径
        return FileUtils.saveBitmap(bitmap)
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: OutOfMemoryError) {
        }

        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap != returnBm) {
            bitmap.recycle()
        }
        return returnBm
    }
}// 防止实例化