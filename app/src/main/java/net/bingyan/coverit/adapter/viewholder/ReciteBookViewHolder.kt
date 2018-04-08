package net.bingyan.coverit.adapter.viewholder

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R

class ReciteBookViewHolder(book: View, val context: Context?, val parent: Activity) : RecyclerView.ViewHolder(book){
    val tvTitle = book.findViewById<TextView>(R.id.book_title)
    val tvTextNum = book.findViewById<TextView>(R.id.text_num)
    val tvPicNum = book.findViewById<TextView>(R.id.pic_num)
    val tvTime = book.findViewById<TextView>(R.id.book_time)
    val bookImage = book.findViewById<ImageView>(R.id.book)
    val ivTopImage=book.findViewById<ImageView>(R.id.iv_pin_top)


}
