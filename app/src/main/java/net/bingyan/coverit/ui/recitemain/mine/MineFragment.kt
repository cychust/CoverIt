package net.bingyan.coverit.ui.recitemain.mine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import net.bingyan.coverit.R
import net.bingyan.coverit.ui.reciteother.AboutActivity
import net.bingyan.coverit.ui.reciteother.FeedBackActivity
import org.jetbrains.anko.support.v4.intentFor


/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:40
 */
class MineFragment:Fragment(),MineContract.View, View.OnClickListener {
    override lateinit var presenter: MineContract.Presenter

    private lateinit var feedBack:LinearLayout

    private lateinit var score:LinearLayout
    private lateinit var about:LinearLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_mine, container, false)
        with(root){
            feedBack=findViewById(R.id.feed_back)
            score=findViewById(R.id.score)
            about=findViewById(R.id.about)
        }
        feedBack.setOnClickListener(this)
        about.setOnClickListener(this)
        return root
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.feed_back-> {
                startActivity(intentFor<FeedBackActivity>())
            }
            R.id.score -> {
                val uri = Uri.parse("market://details?id=" + activity!!.packageName)
                val intentpf = Intent(Intent.ACTION_VIEW, uri)
                intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentpf)
            }
            R.id.about->{
                startActivity(intentFor<AboutActivity>())
            }
        }
    }
}
