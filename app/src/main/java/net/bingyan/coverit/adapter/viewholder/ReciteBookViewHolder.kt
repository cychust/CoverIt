package net.bingyan.coverit.adapter.viewholder

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R
import net.bingyan.coverit.ui.reciteother.ReciteBookDetailActivity

class ReciteBookViewHolder(book: View, val context:Context?): RecyclerView.ViewHolder(book), View.OnClickListener, View.OnLongClickListener {
    val tvTitle=book.findViewById<TextView>(R.id.book_title)

    val tvTextNum=book.findViewById<TextView>(R.id.text_num)
    val tvPicNum=book.findViewById<TextView>(R.id.pic_num)
    val tvTime=book.findViewById<TextView>(R.id.book_time)
    val bookImage=book.findViewById<ImageView>(R.id.book)
    init {
        bookImage.setOnClickListener(this)
        bookImage.setOnLongClickListener(this)
    }

    override fun onLongClick(p0: View?): Boolean {
        TODO("not implemented")
    }

    override fun onClick(p0: View?) {

        context?.startActivity(Intent(context,ReciteBookDetailActivity::class.java))
    }

}
