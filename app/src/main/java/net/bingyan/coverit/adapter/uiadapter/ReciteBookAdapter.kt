package net.bingyan.coverit.adapter.uiadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.bingyan.coverit.R

/**
 * Author       zdlly
 * Date         2017.12.10
 * Time         18:24
 */
class ReciteBookAdapter (private val context:Context?,
                         val titleList: List<String>,
                         val textNumList: List<String>,
                         val picNumList: List<String>,
                         val timeList: List<String>): RecyclerView.Adapter<ReciteBookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.apply {
           // tvTitle.text = titleList[position]
            //tvTextNum.text=textNumList[position]
           // tvPicNum.text=picNumList[position]
           // tvTime.text=timeList[position]
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    class ViewHolder(book: View):RecyclerView.ViewHolder(book){
        val tvTitle=book.findViewById<TextView>(R.id.book_title)
        val tvTextNum=book.findViewById<TextView>(R.id.text_num)
        val tvPicNum=book.findViewById<TextView>(R.id.pic_num)
        val tvTime=book.findViewById<TextView>(R.id.book_time)
    }
}