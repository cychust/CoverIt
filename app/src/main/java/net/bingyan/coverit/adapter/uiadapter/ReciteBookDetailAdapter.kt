package net.bingyan.coverit.adapter.uiadapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import io.realm.Realm
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.PicConfigBean
import net.bingyan.coverit.data.local.bean.RecitePicBean
import net.bingyan.coverit.data.local.bean.ReciteTextBean
import net.bingyan.coverit.data.local.bean.TextConfigBean
import net.bingyan.coverit.listener.PopupCallBack
import net.bingyan.coverit.ui.reciteother.ModifyPicActivity
import net.bingyan.coverit.ui.reciteother.ModifyTextActivity
import net.bingyan.coverit.widget.DeleteTopPopup
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author       zdlly
 * Date         2018.4.4
 * Time         1:22
 */
class ReciteBookDetailAdapter(private var context: Context,val parentActivity: Activity, var timeList: List<Date>, var titleList: List<String>, var picAddress: List<String>, var textList: List<String>) : RecyclerView.Adapter<ReciteBookDetailAdapter.ViewHolder>(), PopupCallBack {
    private var listRealm: Realm = Realm.getDefaultInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_detail, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            this.listItem.setOnClickListener{
                if (textList[position].trim().isEmpty()){
                    val itemResult=listRealm.where(RecitePicBean::class.java).equalTo("picDate",timeList[position]).findFirst()
                    val picIntent= Intent(context, ModifyPicActivity::class.java)
                    picIntent.putExtra("pic",listRealm.copyFromRealm(itemResult!!).picPath)
                    val configList= mutableListOf<PicConfigBean>()
                    configList.addAll(listRealm.copyFromRealm(itemResult).picConfigList)
                    picIntent.putExtra("picData",configList as Serializable)
                    context.startActivity(picIntent)
                }else{
                    val itemResult=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",timeList[position]).findFirst()
                    val textIntent= Intent(context, ModifyTextActivity::class.java)
                    textIntent.putExtra("title",listRealm.copyFromRealm(itemResult!!).textTitle)
                    textIntent.putExtra("content", listRealm.copyFromRealm(itemResult).text)
                    val configList= mutableListOf<TextConfigBean>()
                    configList.addAll(listRealm.copyFromRealm(itemResult).textConfigList)
                    textIntent.putExtra("redData",configList as Serializable)
                    context.startActivity(textIntent)
                }

            }
            this.listItem.setOnLongClickListener{
                val deleteTopPopup= DeleteTopPopup(parentActivity,this@ReciteBookDetailAdapter,false)
                deleteTopPopup.setTipVisibility(View.GONE)
                deleteTopPopup.showPopupWindow()
                return@setOnLongClickListener true
            }
        }
        holder.apply {
            if (textList[position].trim().isEmpty()) this.tvContent.visibility = View.GONE
            else {
                this.tvContent.visibility = View.VISIBLE
                tvContent.text = textList[position]
            }
            if (picAddress[position].trim().isEmpty()) this.ivContent.visibility = View.GONE
            else {
                this.ivContent.visibility = View.VISIBLE
                Glide.with(context).load(picAddress[position]).into(this.ivContent)
            }
            this.tvTime.text = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(timeList[position])
            this.tvTitle.text = titleList[position]
        }
    }

    override fun onTopClicked() {
        TODO("not implemented")
    }

    override fun onRenameClicked() {
        TODO("not implemented")
    }

    override fun onDeleteClicked() {
        TODO("not implemented")
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        listRealm.close()
    }

    class ViewHolder(list: View) : RecyclerView.ViewHolder(list) {
        val listItem=list.findViewById<LinearLayout>(R.id.ll_item)
        val tvTime = list.findViewById<TextView>(R.id.tv_time)
        val tvTitle = list.findViewById<TextView>(R.id.tv_title)
        val ivContent = list.findViewById<ImageView>(R.id.iv_content_pic)
        val tvContent = list.findViewById<TextView>(R.id.tv_content_text)
    }
}