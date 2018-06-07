package net.bingyan.coverit.ui.recitemain.mine

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.delete
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.push.JsonConvertUtil
import net.bingyan.coverit.push.NetUtils
import net.bingyan.coverit.push.bean.JsonFromWeb
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
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


/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:40
 */
class MineFragment : Fragment(), MineContract.View, View.OnClickListener {
    override lateinit var presenter: MineContract.Presenter

    companion object {
        val url: String = "https://git.bingyan.net/BeBeBerr/ShadowsTempAPI/raw/master/app.json"
    }

    private lateinit var feedBack: LinearLayout

    private lateinit var score: LinearLayout
    private lateinit var about: LinearLayout
    private lateinit var getData: LinearLayout


    //  private var textResult: String = ""
    private var tDialog1: DialogUtil? = null
    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>
    //private lateinit var tDialog2: DialogUtil
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
        tDialog1 = DialogUtil.Builder(fragmentManager)
                .setLayoutRes(R.layout.dialog_loading)
                .setHeight(300)
                .setWidth(300)
                .setCancelable(false)
                .setCancelableOutside(true).create()

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
                showDialog()
            }
        }


    }


    private fun showDialog() {
        val gson = Gson()
        DialogUtil.Builder(fragmentManager).setLayoutRes(R.layout.dialog_push)
                .setScreenWidthAspect(activity, 0.8f)
                .addOnClickListener(R.id.push_cancel, R.id.push_ok)
                .setOnBindViewListener(object : OnBindViewListener {
                    override fun bindView(viewHolder: BindViewHolder?) {
                        val text: EditText? = viewHolder?.getView(R.id.dialog_edit)
                        text?.post(object : Runnable {
                            override fun run() {
                                var imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm?.showSoftInput(text, 0)
                            }
                        })
                    }
                })
                .setOnViewClickListener(object : OnViewClickListener {
                    override fun onViewClick(viewHolder: BindViewHolder?, view: View?, tDialog: DialogUtil?) {
                        when (view?.id) {
                            R.id.push_ok -> {
                                val editText: EditText = viewHolder?.getView(R.id.dialog_edit)!!
                                val content = editText.getText().toString().trim()
                                if (content.isEmpty()) {
                                    Toast.makeText(activity, "验证码不能为空", Toast.LENGTH_SHORT).show()
                                } else {
                                    if (!content.trim().isEmpty()) {
                                        dialogShow()
                                        NetUtils.getInstance().getDataAsynFromNet(url, object : NetUtils.MyNetCall {
                                            @Throws(IOException::class)
                                            override fun success(call: Call, response: Response) {
                                                val re: String? = response.body()?.string()
                                                val handler: Handler = Handler(Looper.getMainLooper());
                                                //                    handler.post({
                                                //                       if (re!=null)
                                                //                        Toast.makeText(activity, re, Toast.LENGTH_SHORT).show()
                                                //                    })
                                                try {
                                                    val book = gson.fromJson(re, JsonFromWeb::class.java)
                                                    var fla=0
                                                    book.codeList.forEach {
                                                        if (it.equals(content)){
                                                            var flag = 0
                                                            dialogDismiss()
                                                            val realm: Realm = Realm.getDefaultInstance()
                                                            reciteBookResults = realm.where(ReciteBookBean::class.java)
                                                                    .findAll()
                                                            reciteBookResults.forEach {
                                                                if (it.bookTitle.equals(book.notebookName)) {
                                                                    showConfirmDialog(re)
                                                                    flag = 1             //记背本已经存在flag==1
                                                                    return@forEach
                                                                }
                                                            }
                                                            if (flag == 0) {
                                                                JsonConvertUtil.jsonCovert(re, 0)

                                                                flag=0
                                                            }
                                                            handler.post({
                                                                (activity as ReciteMainActivity).refreshBookData()
                                                                Toast.makeText(activity, "获取成功", Toast.LENGTH_SHORT).show()
                                                            })
                                                            fla=1
                                                            return@forEach
                                                        }
                                                    }
                                                    if (fla==0){
                                                        handler.post({
                                                            Toast.makeText(activity, "领取码有误，请重试", Toast.LENGTH_SHORT).show()
                                                        })
                                                    }
                                                    dialogDismiss()
                                                } catch (s: JsonSyntaxException) {
                                                    s.printStackTrace()
                                                } catch (s: JsonIOException) {
                                                    s.printStackTrace()
                                                } catch (s: JSONException) {
                                                    s.printStackTrace()
                                                } catch (s: IllegalStateException) {
                                                    s.printStackTrace()
                                                } finally {
                                                    dialogDismiss()
                                                }
                                            }
                                            @Throws(IOException::class)
                                            override fun failed(call: Call, e: IOException) {
                                                val handler: Handler = Handler(Looper.getMainLooper());
                                                handler.post({
                                                    Toast.makeText(activity, "网络错误请重试", Toast.LENGTH_SHORT).show()
                                                })
                                                dialogDismiss()
                                            }
                                        })
                                    }
                                    tDialog?.dismiss()
                                }
                            }
                            R.id.push_cancel -> {
                                tDialog?.dismiss()
                            }
                        }
                    }
                }).create().show()


    }


    private fun dialogDismiss() {
        if (tDialog1 == null) {
            tDialog1 = DialogUtil.Builder(fragmentManager)
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
            tDialog1 = DialogUtil.Builder(fragmentManager)
                    .setLayoutRes(R.layout.dialog_loading)
                    .setHeight(300)
                    .setWidth(300)
                    .setCancelable(false)
                    .setCancelableOutside(true).create()
        }
        tDialog1!!.show()
    }

    private fun showConfirmDialog(re: String?) {
        DialogUtil.Builder(fragmentManager)
                .setLayoutRes(R.layout.dialog_confirm)
                .setScreenWidthAspect(activity, 0.7f)
                .setCancelableOutside(false)
                .addOnClickListener(R.id.tv_cancel, R.id.tv_confirm)
                .setOnViewClickListener(object : OnViewClickListener {
                    override fun onViewClick(viewHolder: BindViewHolder?, view: View?, tDialog: DialogUtil?) {
                        when (view?.id) {
                            R.id.tv_confirm -> {
                                JsonConvertUtil.jsonCovert(re, 1)
                                (activity as ReciteMainActivity).refreshBookData()
                                tDialog?.dismiss()

                            }
                            R.id.tv_cancel -> {
                                tDialog?.dismiss()
                            }

                        }
                    }

                }
                )
                .create().show()

    }
}
