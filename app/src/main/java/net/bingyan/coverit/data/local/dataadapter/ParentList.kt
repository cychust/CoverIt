package net.bingyan.coverit.data.local.dataadapter

import java.util.*

/**
 * Author       zdlly
 * Date         2018.4.2
 * Time         23:22
 */
data class ParentList(private var title: String,
                      var date: Date,
                      var text: String,
                      var picpath:String,
                      var isTop: Boolean)