package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R

class AboutActivity : AppCompatActivity() {
    private lateinit var backPic:ImageView
    private lateinit var backText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        backPic=findViewById(R.id.back_pic)
        backText=findViewById(R.id.back_text)

        backPic.setOnClickListener{finish()}
        backText.setOnClickListener { finish() }
    }
}
