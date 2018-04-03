package net.bingyan.coverit.adapter.uiadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import net.bingyan.coverit.R

/**
 * Author       zdlly
 * Date         2018.4.4
 * Time         1:22
 */
class ReciteBookDetailAdapter(private var context: Context, var timeList: List<String>, var titleList: List<String>, var picAddress: List<String>, var textList: List<String>) : RecyclerView.Adapter<ReciteBookDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_detail, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

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
            this.tvTime.text = timeList[position]
            this.tvTitle.text = titleList[position]
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    class ViewHolder(list: View) : RecyclerView.ViewHolder(list) {
        val tvTime = list.findViewById<TextView>(R.id.tv_time)
        val tvTitle = list.findViewById<TextView>(R.id.tv_title)
        val ivContent = list.findViewById<ImageView>(R.id.iv_content_pic)
        val tvContent = list.findViewById<TextView>(R.id.tv_content_text)
    }
}