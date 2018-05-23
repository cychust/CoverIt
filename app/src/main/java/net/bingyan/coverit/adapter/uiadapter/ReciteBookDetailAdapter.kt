package net.bingyan.coverit.adapter.uiadapter

import android.content.Context
import android.content.Intent
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import io.realm.Realm
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.*
import net.bingyan.coverit.listener.PopupCallBack
import net.bingyan.coverit.ui.reciteother.ModifyPicActivity
import net.bingyan.coverit.ui.reciteother.ModifyTextActivity
import net.bingyan.coverit.ui.reciteother.ReciteBookDetailActivity
import net.bingyan.coverit.widget.DeleteTopPopup
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author       zdlly
 * Date         2018.4.4
 * Time         1:22
 */
class ReciteBookDetailAdapter(private var context: Context,val parentActivity: ReciteBookDetailActivity, var timeList: List<Date>, var titleList: List<String>, var picAddress: List<String>, var textList: List<String>) : RecyclerView.Adapter<ReciteBookDetailAdapter.ViewHolder>(), PopupCallBack {
    val TYPE_PIC=0

    val TYPE_TEXT=1

    private var curType=0

    private lateinit var curDate: Date
    private lateinit var deleteTopPopup:DeleteTopPopup
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
                    val data=itemResult.picDate
                    picIntent.putExtra("picDate",data)
                    val picTitle=itemResult.belonging
                    picIntent.putExtra("picTitle",picTitle)
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
                if (textList[position].trim().isEmpty()){
                    curType=TYPE_PIC//pic
                    val topItem = listRealm.where(RecitePicBean::class.java).equalTo("picDate",timeList[position]).findFirst()
                    deleteTopPopup = if (listRealm.copyFromRealm(topItem!!).isTop){
                        DeleteTopPopup(parentActivity,this@ReciteBookDetailAdapter,true)
                    }else DeleteTopPopup(parentActivity,this@ReciteBookDetailAdapter,false)
                    curDate=timeList[position]
                }
                else{
                    curType=TYPE_TEXT//text
                    val topItem = listRealm.where(ReciteTextBean::class.java).equalTo("textDate",timeList[position]).findFirst()
                    deleteTopPopup = if (listRealm.copyFromRealm(topItem!!).isTop){
                        DeleteTopPopup(parentActivity,this@ReciteBookDetailAdapter,true)
                    }else DeleteTopPopup(parentActivity,this@ReciteBookDetailAdapter,false)
                }
                curDate=timeList[position]
                deleteTopPopup.setTipVisibility(View.GONE)
                deleteTopPopup.showPopupWindow()
                return@setOnLongClickListener true
            }
        }
        holder.apply {
            if (textList[position].trim().isEmpty()) this.linearLayout.visibility = View.GONE
            else {
                val topItem=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",timeList[position]).findFirst()
                ivTop.visibility=if(listRealm.copyFromRealm(topItem!!).isTop) View.VISIBLE
                else View.INVISIBLE

                this.linearLayout.visibility = View.VISIBLE
                tvContent.text = textList[position]
                rvTilte.text=titleList[position]
            }
            if (picAddress[position].trim().isEmpty()) this.ivContent.visibility = View.GONE
            else {
                val topItem=listRealm.where(RecitePicBean::class.java).equalTo("picDate",timeList[position]).findFirst()
                ivTop.visibility=if(listRealm.copyFromRealm(topItem!!).isTop) View.VISIBLE
                else View.INVISIBLE

                this.ivContent.visibility = View.VISIBLE
                Glide.with(context).load(picAddress[position]).into(this.ivContent)
            }
            this.tvTime.text = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(timeList[position])
            this.tvTitle.text = titleList[position]
        }
    }

    override fun onTopClicked() {
        if(curType== TYPE_TEXT){
            val topItem=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",curDate).findFirst()
            listRealm.executeTransaction {
                topItem!!.isTop= !topItem.isTop
            }
        }else{
            val topItem=listRealm.where(RecitePicBean::class.java).equalTo("picDate",curDate).findFirst()
            listRealm.executeTransaction {
                topItem!!.isTop= !topItem.isTop
            }
        }
        deleteTopPopup.dismiss()
        parentActivity.refreshListData()
    }

    override fun onRenameClicked() {
        deleteTopPopup.dismiss()
        parentActivity.refreshListData()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("重命名")

        val dialogContent = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
        builder.setView(dialogContent)

        val textInput = dialogContent.findViewById<TextInputEditText>(R.id.input_text)
        textInput.hint = "请输入新的名称"

        builder.setCancelable(false)

        builder.setPositiveButton("确定", { dialog, which ->
            run {
                val resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()) {
                    if(curType== TYPE_TEXT){
                        val renameItem=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",curDate).findFirst()
                        listRealm.executeTransaction {
                            renameItem?.textTitle=textInput.text.toString()
                        }
                    }else{
                        val renameItem=listRealm.where(RecitePicBean::class.java).equalTo("picDate",curDate).findFirst()
                        listRealm.executeTransaction {
                            renameItem?.picTitle=textInput.text.toString()
                        }
                    }
                    parentActivity.refreshListData()
                }
                dialog.dismiss()
            }
        })
        builder.setNegativeButton("取消", { dialog, which ->
            run {
                dialog.dismiss()
            }
        })
        val dialog = builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.show()
    }

    override fun onDeleteClicked() {
        if(curType== TYPE_TEXT){
            val deleteItem=listRealm.where(ReciteTextBean::class.java).equalTo("textDate",curDate).findFirst()
            val deleteBookTitle=listRealm.copyFromRealm(deleteItem!!).belonging
            val textNumBook=listRealm.where(ReciteBookBean::class.java).equalTo("bookTitle",deleteBookTitle).findFirst()
            listRealm.executeTransaction {
                deleteItem.deleteFromRealm()
                textNumBook!!.textNum-=1
            }
        }else{
            val deleteItem=listRealm.where(RecitePicBean::class.java).equalTo("picDate",curDate).findFirst()
            val deleteBookTitle=listRealm.copyFromRealm(deleteItem!!).belonging
            val picNumBook=listRealm.where(ReciteBookBean::class.java).equalTo("bookTitle",deleteBookTitle).findFirst()
            listRealm.executeTransaction {
                deleteItem.deleteFromRealm()
                picNumBook!!.picNum-=1
            }
        }
        deleteTopPopup.dismiss()
        parentActivity.refreshListData()
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
        val linearLayout:LinearLayout=list.findViewById(R.id.linear)
        val ivContent = list.findViewById<ImageView>(R.id.iv_content_pic)
        val tvContent = list.findViewById<TextView>(R.id.tv_content_text)
        val rvTilte:TextView=list.findViewById(R.id.rv_title_text)
        val ivTop=list.findViewById<ImageView>(R.id.iv_pin_top)
    }
}