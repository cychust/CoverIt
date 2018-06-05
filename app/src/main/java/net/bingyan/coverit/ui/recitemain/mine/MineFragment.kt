package net.bingyan.coverit.ui.recitemain.mine

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.push.JsonConvertUtil
import net.bingyan.coverit.push.NetUtils
import net.bingyan.coverit.push.bean.JsonFromWeb
import net.bingyan.coverit.ui.reciteother.AboutActivity
import net.bingyan.coverit.ui.reciteother.FeedBackActivity
import net.bingyan.coverit.util.DialogUtil.DialogUtil
import net.bingyan.coverit.util.DialogUtil.base.BindViewHolder
import net.bingyan.coverit.util.DialogUtil.listener.OnBindViewListener
import net.bingyan.coverit.util.DialogUtil.listener.OnViewClickListener
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.support.v4.intentFor
import org.json.JSONException
import java.io.IOException
import java.util.*


/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:40
 */
class MineFragment : Fragment(), MineContract.View, View.OnClickListener {
    override lateinit var presenter: MineContract.Presenter

    private companion object {
        val url: String = "https://git.bingyan.net/BeBeBerr/ShadowsTempAPI/raw/master/app.json"
    }

    private lateinit var feedBack: LinearLayout

    private lateinit var score: LinearLayout
    private lateinit var about: LinearLayout
    private lateinit var getData: LinearLayout


    private var textResult: String = ""
    private lateinit var tDialog1: DialogUtil
    private lateinit var tDialog2: DialogUtil
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_mine, container, false)
        with(root) {
            feedBack = findViewById(R.id.feed_back)
            score = findViewById(R.id.score)
            about = findViewById(R.id.about)
            getData = findViewById(R.id.get_data)
        }
        feedBack.setOnClickListener(this)
        score.setOnClickListener(this)
        about.setOnClickListener(this)
        getData.setOnClickListener(this)
        return root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.feed_back -> {
                startActivity(intentFor<FeedBackActivity>())
            }
            R.id.score -> {
                val uri = Uri.parse("market://details?id=" + activity!!.packageName)
                val intentpf = Intent(Intent.ACTION_VIEW, uri)
                intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentpf)
            }
            R.id.about -> {
                startActivity(intentFor<AboutActivity>())
            }
            R.id.get_data -> {
                if (ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission()
                } else {
                    if (NetUtils.isNetworkAvailable(activity)) {
                        showDialog()
                       /* tDialog1 = DialogUtil.Builder(fragmentManager)
                                .setLayoutRes(R.layout.dialog_loading)
                                .setHeight(300)
                                .setWidth(300)
                                .setCancelable(false)
                                .setCancelableOutside(true)
                                .create().show()*/
                    } else {
                        Toast.makeText(activity, "请检查网络状态", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDialog() {
        val gson = Gson()

        DialogUtil.Builder(fragmentManager).setLayoutRes(R.layout.dialog_costome_tmp)
                .setScreenWidthAspect(activity, 0.8f)
                .addOnClickListener(R.id.push_cancel, R.id.push_ok)
                .setOnBindViewListener(object : OnBindViewListener {
                    override fun bindView(viewHolder: BindViewHolder?) {
                        val text: EditText? = viewHolder?.getView(R.id.dialog_edit)
                        text?.post(object : Runnable {
                            override fun run() {
                                var imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm!!.showSoftInput(text, 0)
                            }
                        })
                    }
                })
                .setOnViewClickListener(object : OnViewClickListener {
                    override fun onViewClick(viewHolder: BindViewHolder?, view: View?, tDialog: DialogUtil?) {
                        when(view?.id){
                            R.id.push_ok->{
                                val editText: EditText = viewHolder?.getView(R.id.dialog_edit)!!
                                val content = editText.getText().toString().trim()
                                if (!content.trim().isEmpty()) {
                                    NetUtils.getInstance().getDataAsynFromNet(url, object : NetUtils.MyNetCall {
                                        @Throws(IOException::class)
                                        override fun success(call: Call, response: Response) {
                                            val re: String = response.body().toString()
                                            val handler:Handler= Handler(Looper.getMainLooper());
                                            handler.post({
                                                Toast.makeText(activity,re,Toast.LENGTH_SHORT).show()
                                            })
                                            try {
                                                val book = gson.fromJson(re, JsonFromWeb::class.java)
                                                book.codeList.forEach {
                                                    if (!it.equals(textResult)) {
                                                        tDialog1.dismiss()
                                                        handler.post( {
                                                            Toast.makeText(activity, "验证码错误", Toast.LENGTH_SHORT).show()
                                                        })

                                                    } else {
                                                        JsonConvertUtil.jsonCovert(re)
                                                        tDialog1.dismiss()
                                                        handler.post( {
                                                            Toast.makeText(activity, "获取成功", Toast.LENGTH_SHORT).show()
                                                        })

                                                    }
                                                }
                                            }catch (s:JsonSyntaxException){
                                                s.printStackTrace()
                                            }catch (s:JsonIOException){
                                                s.printStackTrace()
                                            }catch (s:JSONException){
                                                s.printStackTrace()
                                            }
                                            finally {
                                                    tDialog1.dismiss()
                                            }
                                        }

                                        override fun failed(call: Call, e: IOException) {
                                            val handler:Handler= Handler(Looper.getMainLooper());
                                            handler.post( {
                                                Toast.makeText(activity, "获取成功", Toast.LENGTH_SHORT).show()
                                            })
                                        }
                                    })
                                }
                                 tDialog1 = DialogUtil.Builder(fragmentManager)
                                .setLayoutRes(R.layout.dialog_loading)
                                .setHeight(300)
                                .setWidth(300)
                                .setCancelable(false)
                                .setCancelableOutside(true)
                                .create().show()
                                tDialog?.dismiss()
                            }
                            R.id.push_cancel->{
                                tDialog?.dismiss()
                            }
                        }
                    }
                }).create().show()


    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.INTERNET), 2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val s: String = permissions[0]
                        Toast.makeText(activity, s + "权限被拒绝了", Toast.LENGTH_SHORT).show()
                    } else {
                        showDialog()
                    }
                }
            }
        }
    }
}
