package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R

class AboutActivity : AppCompatActivity() {
    private lateinit var backPic:ImageView
    private lateinit var backText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)//B

        backPic=findViewById(R.id.back_pic)
        backText=findViewById(R.id.back_text)

        backPic.setOnClickListener{finish()}
        backText.setOnClickListener { finish() }
    }
}
