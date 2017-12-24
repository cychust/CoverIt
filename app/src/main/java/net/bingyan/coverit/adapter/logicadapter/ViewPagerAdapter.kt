package net.bingyan.coverit.adapter.logicadapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         2:22
 */
open class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val mFragmentList = ArrayList<Fragment>()
    override fun getItem(position: Int): Fragment= mFragmentList[position]
    override fun getCount(): Int=mFragmentList.size
    open fun addFragment(fragment: Fragment){
        mFragmentList.add(fragment)
    }

}