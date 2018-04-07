package net.bingyan.coverit.adapter.uiadapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.viewholder.ReciteBookViewHolder

/**
 * Author       zdlly
 * Date         2017.12.10
 * Time         18:24
 */
class ReciteBookAdapter(private val context: Context?,
                        val parentActivity:Activity,
                        val titleList: List<String>,
                        val textNumList: List<String>,
                        val picNumList: List<String>,
                        val timeList: List<String>) : RecyclerView.Adapter<ReciteBookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciteBookViewHolder {
        return ReciteBookViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false), context,parentActivity)

    }

    override fun onBindViewHolder(holder: ReciteBookViewHolder, position: Int) {
        holder.apply {
            tvTitle.text = titleList[position]
            tvTextNum.text = textNumList[position]
            tvPicNum.text = picNumList[position]
            tvTime.text = timeList[position]
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

}