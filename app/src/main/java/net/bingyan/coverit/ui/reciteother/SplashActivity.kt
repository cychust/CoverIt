package net.bingyan.coverit.ui.reciteother

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.statistics.common.MLog.DEBUG
import io.realm.Realm
import io.realm.RealmResults
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
import org.jetbrains.anko.intentFor
import java.util.*

class SplashActivity : AppCompatActivity() {
private lateinit var realm:Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MobclickAgent.setDebugMode(DEBUG)
        MobclickAgent.setDebugMode(true)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A
        window.decorView.fitsSystemWindows=true

        realm=Realm.getDefaultInstance()
        val bookResult:RealmResults<ReciteBookBean> = realm.where(ReciteBookBean::class.java)
                .equalTo("bookTitle","未命名分组")
                .findAll()

        if(bookResult.size==0){
            realm.beginTransaction()
            val unknownBook=realm.createObject(ReciteBookBean::class.java)
            unknownBook.bookDate= Date(System.currentTimeMillis())
            unknownBook.bookTitle="未命名分组"
            unknownBook.isTop=false
            unknownBook.picNum=0
            unknownBook.textNum=0
            realm.commitTransaction()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 2)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE), 3)
            }
        }else{
            val handler= Handler()
            handler.postDelayed({
                startActivity(intentFor<ReciteMainActivity>())
                finish()
            },2000)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val handler= Handler()
        if (requestCode==1){
           // val handler= Handler()
            handler.postDelayed({
                startActivity(intentFor<ReciteMainActivity>())
                finish()
            },1000)
        }
        if (requestCode==2){
            handler.postDelayed({
                startActivity(intentFor<ReciteMainActivity>())
                finish()
            },1000)
        }
        if (requestCode==3){
            handler.postDelayed({
                startActivity(intentFor<ReciteMainActivity>())
                finish()
            },1000)
        }
    }
}
