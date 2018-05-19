package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import net.bingyan.coverit.R
import net.bingyan.coverit.widget.CustomToast
import org.jetbrains.anko.sdk25.coroutines.onClick


class FeedBackActivity : AppCompatActivity() {
    //private lateinit var titleBar:TitleBar
    private lateinit var etSuggestion:EditText
    private lateinit var etAddress:EditText
    private lateinit var btnSubmit:Button
    private lateinit var tvbtn:TextView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var statusBarView:View;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
       // window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
           // appBarLayout=findViewById(R.id.appBarLayout)
            //appBarLayout.setFitsSystemWindows(true)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            appBarLayout=findViewById(R.id.appBarLayout)
           // appBarLayout.setFitsSystemWindows(true)
        }*/
        initView()
    }

    private fun initView() {
        //titleBar=findViewById(R.id.titleBar)
        etSuggestion=findViewById(R.id.et_suggestion)
        etAddress=findViewById(R.id.et_address)
        btnSubmit=findViewById(R.id.btn_save)
        tvbtn=findViewById(R.id.back_text_feedback)
        //titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        //titleBar.setImmersive(true)

        //titleBar.setTitle("反馈")
      //  titleBar.setTitleColor(ContextCompat.getColor(this@FeedBackActivity,R.color.title_white))
        btnSubmit.onClick {
            val toast=CustomToast(this@FeedBackActivity,LayoutInflater.from(this@FeedBackActivity).inflate(R.layout.popup_view,null,false),Toast.LENGTH_SHORT)
            toast.show()
            finish()
        }
        tvbtn.isClickable=true
        tvbtn.onClick { finish() }

    }

}
