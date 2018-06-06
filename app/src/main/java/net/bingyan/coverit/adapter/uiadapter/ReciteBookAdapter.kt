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
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.viewholder.ReciteBookViewHolder
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.data.local.bean.RecitePicBean
import net.bingyan.coverit.data.local.bean.ReciteTextBean
import net.bingyan.coverit.listener.PopupCallBack
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
import net.bingyan.coverit.ui.reciteother.ReciteBookDetailActivity
import net.bingyan.coverit.widget.DeleteTopPopup

/**
 * Author       zdlly
 * Date         2017.12.10
 * Time         18:24
 */
class ReciteBookAdapter(private val context: Context?,
                        val parentActivity: ReciteMainActivity,
                        val titleList: List<String>,
                        val textNumList: List<String>,
                        val picNumList: List<String>,
                        val timeList: List<String>) : RecyclerView.Adapter<ReciteBookViewHolder>(), PopupCallBack {
    private var bookRealm = Realm.getDefaultInstance()
    private lateinit var bookTitle: TextView
    private lateinit var deleteTopPopup: DeleteTopPopup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciteBookViewHolder {
        return ReciteBookViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false), context, parentActivity)

    }

    override fun onBindViewHolder(holder: ReciteBookViewHolder, position: Int) {
        holder.apply {
            bookImage.setOnClickListener {
                val detailIntent = Intent()
                detailIntent.putExtra("bookTitle", tvTitle.text)
                detailIntent.setClass(context, ReciteBookDetailActivity::class.java)
                context?.startActivity(detailIntent)
            }
            bookImage.setOnLongClickListener {
                bookTitle = tvTitle
                val topBook = bookRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", bookTitle.text.toString()).findFirst()
                deleteTopPopup = if (bookRealm.copyFromRealm(topBook!!).isTop) {
                    DeleteTopPopup(activityContext = parentActivity, popupCallBack = this@ReciteBookAdapter, isTop = true)
                } else {
                    DeleteTopPopup(activityContext = parentActivity, popupCallBack = this@ReciteBookAdapter, isTop = false)
                }
                deleteTopPopup.showPopupWindow()
                return@setOnLongClickListener true
            }
        }
        holder.apply {
            tvTitle.text = titleList[position]
            tvTextNum.text = textNumList[position]
            tvPicNum.text = picNumList[position]
            tvTime.text = timeList[position]
            val topBook = bookRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", tvTitle.text.toString()).findFirst()
            if (bookRealm.copyFromRealm(topBook!!).isTop) {
                ivTopImage.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun onTopClicked() {
        val topBook = bookRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", bookTitle.text.toString()).findFirst()
        bookRealm.executeTransaction {
            topBook?.isTop = !topBook!!.isTop
        }
        deleteTopPopup.dismiss()
        parentActivity.refreshBookData()
    }

    override fun onRenameClicked() {
        val renameBook = bookRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", bookTitle.text.toString()).findFirst()
        if (bookRealm.copyFromRealm(renameBook!!)?.bookTitle != "未命名分组") {

            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("重命名记背本")

            val dialogContent = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
            builder.setView(dialogContent)

            val textInput = dialogContent.findViewById<TextInputEditText>(R.id.input_text)
            textInput.hint = "请输入新的记背本名称"

            builder.setCancelable(false)


            builder.setPositiveButton("确定", { dialog, which ->
                run {
                    val resultText = textInput.text.toString()
                    if (!resultText.trim().isEmpty()) {
                        bookRealm.executeTransaction {
                            renameBook.bookTitle = resultText
                        }
                    }
                    dialog.dismiss()
                    parentActivity.refreshBookData()
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
        } else Toast.makeText(context, "未命名分组不可以重命名!", Toast.LENGTH_SHORT).show()

        deleteTopPopup.dismiss()
    }

    override fun onDeleteClicked() {
        val deleteBook = bookRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", bookTitle.text.toString()).findFirst()
        val textResults: RealmResults<ReciteTextBean> = bookRealm.where(ReciteTextBean::class.java)
                .equalTo("belonging", bookTitle.text.toString()).findAll()

        val picResult: RealmResults<RecitePicBean> = bookRealm.where(RecitePicBean::class.java)
                .equalTo("belonging", bookTitle.text.toString()).findAll()

        if (bookRealm.copyFromRealm(deleteBook!!)?.bookTitle != "未命名分组") {
            bookRealm.executeTransaction {
                deleteBook.deleteFromRealm()
                textResults.deleteAllFromRealm()
                picResult.deleteAllFromRealm()
            }
            parentActivity.refreshBookData()
            parentActivity.refreshListData()
        } else Toast.makeText(context, "未命名分组不可以删除!", Toast.LENGTH_SHORT).show()

        deleteTopPopup.dismiss()
        parentActivity.refreshBookData()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        bookRealm.close()
    }
}
