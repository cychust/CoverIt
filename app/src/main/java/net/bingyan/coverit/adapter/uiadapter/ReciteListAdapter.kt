package net.bingyan.coverit.adapter.uiadapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.bingyan.coverit.R
import org.jetbrains.anko.image


/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         16:56
 */
class ReciteListAdapter(private var context: Context, var timeList:List<String>, var nameList: List<String>, var picAddress:List<String>):RecyclerView.Adapter<ReciteListAdapter.ViewHolder>() {
    val TYPE_TOP:Int=0
    val TYPE_NORMAL:Int=1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(getItemViewType(position)==TYPE_TOP){
            holder.apply {
                this.tvLine?.visibility=View.INVISIBLE
                this.tvDot?.setBackgroundResource(R.drawable.top)
                this.tvTime?.text = timeList[position]
                this.tvTitle?.text=nameList[position]
                this.ivContent?.image=ContextCompat.getDrawable(context,R.drawable.logo_beita)
            }
        }else if(getItemViewType(position)==TYPE_NORMAL){
            holder.apply {
                this.tvLine?.visibility=View.VISIBLE
                this.tvDot?.setBackgroundResource(R.drawable.cicle)
                this.tvTime?.text = timeList[position]
                this.tvTitle?.text=nameList[position]
                this.ivContent?.image=ContextCompat.getDrawable(context,R.drawable.logo_beita)
            }
        }
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==0){
            TYPE_TOP
        }else TYPE_NORMAL
    }

    class ViewHolder(list:View):RecyclerView.ViewHolder(list) {
        val tvTime =list.findViewById<TextView>(R.id.tv_time)
        val tvTitle=list.findViewById<TextView>(R.id.tv_title)
        val ivContent=list.findViewById<ImageView>(R.id.iv_content)
        val tvDot=list.findViewById<TextView>(R.id.tvDot)
        val tvLine=list.findViewById<TextView>(R.id.tvTopLine)
    }
}