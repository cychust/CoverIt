package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.umeng.analytics.MobclickAgent
import net.bingyan.coverit.R
import net.bingyan.coverit.push.NetUtils
import net.bingyan.coverit.util.DialogUtil.DialogUtil
import net.bingyan.coverit.widget.CustomToast
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.IOException


class FeedBackActivity : AppCompatActivity() {
    //private lateinit var titleBar:TitleBar
    private lateinit var etSuggestion: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvbtn: TextView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var statusBarView: View
    private var tDialog1:DialogUtil?=null
    private var time = 0L
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

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("feedbackActivity")
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd("feedbackActivity")
    }
    private fun initView() {
        //titleBar=findViewById(R.id.titleBar)
        etSuggestion = findViewById(R.id.et_suggestion)
        etAddress = findViewById(R.id.et_address)
        btnSubmit = findViewById(R.id.btn_save)
        tvbtn = findViewById(R.id.back_text_feedback)
        //titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        //titleBar.setImmersive(true)

        //titleBar.setTitle("反馈")
        //  titleBar.setTitleColor(ContextCompat.getColor(this@FeedBackActivity,R.color.title_white))
        btnSubmit.onClick {
            dialogShow()
            if (time==0L||(time != 0L && (System.currentTimeMillis() - time >= 60 * 1000)))
                time=System.currentTimeMillis()

                if (!etSuggestion.text.isEmpty()) {
                    var paras = hashMapOf<String, String>()
                    paras["info"] ="[背它Android反馈]" +etSuggestion.text.toString() +"  联系方式:"+ etAddress.text.toString()
                    NetUtils.getInstance().postDataAsynToNet("http://cqmu.wangluyuan.cc/feedback/", paras, object : NetUtils.MyNetCall {
                        @Throws(IOException::class)
                        override fun success(call: Call?, response: Response?) {
                            var handler: Handler = Handler(Looper.getMainLooper())
                            handler.post({
                                //Log.d("message",response?.code().toString())
                                if (!response?.body()?.string()?.trim().equals("{\"state\": \"ok\"}")) {
                                    Toast.makeText(this@FeedBackActivity, "发送错误，请联系相关人员", Toast.LENGTH_SHORT).show()
                                    dialogDismiss()
                                    finish()
                                } else {
                                    //   Log.d("feedback_response", response?.body()?.string())
                                    val toast = CustomToast(this@FeedBackActivity, LayoutInflater.from(this@FeedBackActivity).inflate(R.layout.popup_view, null, false), Toast.LENGTH_SHORT)
                                    toast.show()
                                    dialogDismiss()
                                    finish()
                                }
                            }
                            )

                        }

                        @Throws(IOException::class)
                        override fun failed(call: Call?, e: IOException?) {
                            //To change body of created functions use File | Settings | File Templates.
                            var handler: Handler = Handler(Looper.getMainLooper())
                            handler.post({
                                Toast.makeText(this@FeedBackActivity, "网络错误,请稍后重试", Toast.LENGTH_SHORT).show()
                            })
                            dialogDismiss()
                            finish()
                        }
                    })

                } else if (etSuggestion.text.isEmpty()) {
                    Toast.makeText(this@FeedBackActivity, "网络错误,请稍后重试", Toast.LENGTH_SHORT).show()
                }
        }
        tvbtn.isClickable = true
        tvbtn.onClick { finish() }
    }
    private fun dialogDismiss() {
        if (tDialog1 == null) {
            tDialog1 = DialogUtil.Builder(supportFragmentManager)
                    .setLayoutRes(R.layout.dialog_loading)
                    .setHeight(300)
                    .setWidth(300)
                    .setCancelable(false)
                    .setCancelableOutside(true).create()
        }
        tDialog1!!.dismiss()
    }

    private fun dialogShow() {
        if (tDialog1 == null) {
            tDialog1 = DialogUtil.Builder(supportFragmentManager)
                    .setLayoutRes(R.layout.dialog_loading)
                    .setHeight(300)
                    .setWidth(300)
                    .setCancelable(false)
                    .setCancelableOutside(true).create()
        }
        tDialog1!!.show()
    }

}

/*

 */
