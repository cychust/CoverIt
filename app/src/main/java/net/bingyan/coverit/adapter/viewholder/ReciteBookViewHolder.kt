package net.bingyan.coverit.adapter.viewholder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R
import net.bingyan.coverit.ui.reciteother.ReciteBookDetailActivity
import net.bingyan.coverit.widget.DeleteTopPopup

class ReciteBookViewHolder(book: View, val context: Context?, val parent: Activity) : RecyclerView.ViewHolder(book), View.OnClickListener, View.OnLongClickListener {
    val tvTitle = book.findViewById<TextView>(R.id.book_title)
    val tvTextNum = book.findViewById<TextView>(R.id.text_num)
    val tvPicNum = book.findViewById<TextView>(R.id.pic_num)
    val tvTime = book.findViewById<TextView>(R.id.book_time)
    val bookImage = book.findViewById<ImageView>(R.id.book)

    init {
        bookImage.setOnClickListener(this)
        bookImage.setOnLongClickListener(this)
    }

    override fun onLongClick(p0: View?): Boolean {
        val deleteTopPopup = DeleteTopPopup(activityContext = parent)
        deleteTopPopup.showPopupWindow()
        return true
    }

    override fun onClick(p0: View?) {

        val detailIntent = Intent()
        detailIntent.putExtra("bookTitle", tvTitle.text)
        detailIntent.setClass(context, ReciteBookDetailActivity::class.java)
        context?.startActivity(detailIntent)
    }

}
