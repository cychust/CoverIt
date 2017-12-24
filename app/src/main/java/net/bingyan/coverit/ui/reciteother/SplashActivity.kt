package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import net.bingyan.coverit.R
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler= Handler()
        handler.postDelayed({
            startActivity(intentFor<ReciteMainActivity>())
            finish()
        },3000)

    }
}
