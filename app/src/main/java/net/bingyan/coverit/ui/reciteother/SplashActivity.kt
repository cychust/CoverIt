package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
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

        val handler= Handler()
        handler.postDelayed({
            startActivity(intentFor<ReciteMainActivity>())
            finish()
        },2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
