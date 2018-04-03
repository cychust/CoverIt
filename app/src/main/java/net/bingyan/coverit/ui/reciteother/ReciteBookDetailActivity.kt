package net.bingyan.coverit.ui.reciteother

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_recite_book_detail.*
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteBookDetailAdapter
import net.bingyan.coverit.data.local.bean.ParentListBean
import net.bingyan.coverit.widget.MenuPopup
import org.jetbrains.anko.intentFor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ReciteBookDetailActivity : AppCompatActivity() {
    private lateinit var addNewView:ImageView

    private lateinit var rvList: RecyclerView
    private var titleList= mutableListOf<String>()
    private var timeList= mutableListOf<String>()
    private var picPathList= mutableListOf<String>()
    private var textList= mutableListOf<String>()

    private lateinit var bookTitle: String
    private val TAKE_PHOTO = 1
    private val CHOOSE_PHOTO = 2


    private var reciteBookDetailRealm= Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_book_detail)
        bookTitle=intent.getStringExtra("bookTitle")
        initView()
        loadListData()
    }

    private fun initView() {
        rvList=findViewById(R.id.rv_list)
        addNewView=findViewById(R.id.iv_add_new)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle(bookTitle)
        titleBar.setTitleColor(ContextCompat.getColor(this,R.color.title_white))

        addNewView.setOnClickListener { v->
            run {
                val selectPopup = MenuPopup(this)
                selectPopup.showPopupWindow(v)
            }
        }
    }

    private fun loadListData() {
        val parentList:MutableList<ParentListBean> = mutableListOf()
        val parentListResults=reciteBookDetailRealm.where(ParentListBean::class.java).equalTo("belonging",bookTitle).findAll().sort("date", Sort.DESCENDING)
        for(parentItem in parentListResults){
            parentList.add(reciteBookDetailRealm.copyFromRealm(parentItem))
        }
        timeList.clear()
        titleList.clear()
        picPathList.clear()
        textList.clear()
        parentList.forEach {
            timeList.add(SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(it.date))
            titleList.add(it.title)
            picPathList.add(it.picpath)
            textList.add(it.text)
        }

        rvList.layoutManager= LinearLayoutManager(this)
        rvList.adapter= ReciteBookDetailAdapter(this,timeList,titleList,picPathList,textList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                val outputImage = File(this.externalCacheDir, "output_image.jpg")
                if (outputImage.path!=null)
                    startActivity(intentFor<ModifyPicActivity>("pic" to outputImage.path))
            }
            CHOOSE_PHOTO -> {
                var imagePath: String? = null
                if (data != null) {
                    val uri = data.data
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        assert(uri != null)
                        when {
                            "com.android.providers.media.documents" == uri!!.authority -> {
                                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                                val selection = MediaStore.Images.Media._ID + "=" + id
                                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                            }
                            "com.android.providers.downloads.documents" == uri.authority -> {
                                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public/public_downloads"), java.lang.Long.valueOf(docId)!!)
                                imagePath = getImagePath(contentUri, null)
                            }
                            "content".equals(uri.scheme, ignoreCase = true) -> imagePath = getImagePath(uri, null)
                            "file".equals(uri.scheme, ignoreCase = true) -> imagePath = uri.path
                        }
                    }
                }
                if (imagePath!=null)
                    startActivity(intentFor<ModifyPicActivity>("pic" to imagePath))
            }

            else -> {
            }
        }
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }
}
