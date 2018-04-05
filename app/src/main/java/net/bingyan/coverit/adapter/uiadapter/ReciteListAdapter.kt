package net.bingyan.coverit.adapter.uiadapter

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
import net.bingyan.coverit.data.local.bean.ReciteTextBean
import net.bingyan.coverit.data.local.bean.TextConfigBean
import net.bingyan.coverit.ui.reciteother.ModifyTextActivity
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         16:56
 */
class ReciteListAdapter(var context: Context, var timeList: List<Date>, var titleList: List<String>, var picAddress: List<String>, var textList: List<String>) : RecyclerView.Adapter<ReciteListAdapter.ViewHolder>() {
    val TYPE_TOP: Int = 0

    val TYPE_NORMAL: Int = 1
    private var listRealm: Realm= Realm.getDefaultInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            this.listItem.setOnClickListener{
                if (textList[position].trim().isEmpty()){

                }else{
                    val itemResult=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",timeList[position]).findFirst()
                    val textIntent=Intent(context,ModifyTextActivity::class.java)
                    textIntent.putExtra("title",listRealm.copyFromRealm(itemResult!!).textTitle)
                    textIntent.putExtra("content", listRealm.copyFromRealm(itemResult).text)
                    val configList= mutableListOf<TextConfigBean>()
                    configList.addAll(listRealm.copyFromRealm(itemResult).textConfigList)
                    textIntent.putExtra("redData",configList as Serializable)
                    context.startActivity(textIntent)
                }

            }
            this.listItem.setOnLongClickListener{
                return@setOnLongClickListener true
            }
        }
        if (getItemViewType(position) == TYPE_TOP) {
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
                this.tvLine.visibility = View.INVISIBLE
                this.tvDot.setBackgroundResource(R.drawable.top)
                this.tvTime.text = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(timeList[position])
                this.tvTitle.text = titleList[position]
            }
        } else if (getItemViewType(position) == TYPE_NORMAL) {
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
                this.tvLine.visibility = View.VISIBLE
                this.tvDot.setBackgroundResource(R.drawable.cicle)
                this.tvTime.text = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(timeList[position])
                this.tvTitle.text = titleList[position]
            }
        }
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        listRealm.close()
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_TOP
        } else TYPE_NORMAL
    }

    class ViewHolder(list: View) : RecyclerView.ViewHolder(list){
        val listItem=list.findViewById<LinearLayout>(R.id.ll_item)
        val tvTime = list.findViewById<TextView>(R.id.tv_time)
        val tvTitle = list.findViewById<TextView>(R.id.tv_title)
        val ivContent = list.findViewById<ImageView>(R.id.iv_content_pic)
        val tvContent = list.findViewById<TextView>(R.id.tv_content_text)
        val tvDot = list.findViewById<TextView>(R.id.tvDot)
        val tvLine = list.findViewById<TextView>(R.id.tvTopLine)
    }
}
