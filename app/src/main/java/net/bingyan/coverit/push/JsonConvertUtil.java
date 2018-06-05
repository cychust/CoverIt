package net.bingyan.coverit.push;

import android.widget.Toast;

import com.google.gson.Gson;

import net.bingyan.coverit.data.local.bean.ReciteBookBean;
import net.bingyan.coverit.data.local.bean.ReciteTextBean;
import net.bingyan.coverit.data.local.bean.TextConfigBean;
import net.bingyan.coverit.push.bean.JsonFromWeb;
import net.bingyan.coverit.push.bean.Shadow;
import net.bingyan.coverit.push.bean.TextItem;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Response;

public class JsonConvertUtil {
    private static Realm textRealm;
    private static ReciteBookBean reciteBook;
    private static List<ReciteTextBean>reciteTextBeans;
    private static Gson gson;
    public static JsonFromWeb jsonCovert(String json){
        if (json==null||json.isEmpty()){
            return null;
        }
        if (textRealm==null){
            textRealm=Realm.getDefaultInstance();
            //reciteBook=textRealm.where(ReciteBookBean.class).findFirst();//todo
        }
        if (gson==null){
            gson=new Gson();
        }
        JsonFromWeb bookFromWeb=gson.fromJson(json,JsonFromWeb.class);
        String bookName=bookFromWeb.getNotebookName();

        textRealm.beginTransaction();

        reciteBook=textRealm.createObject(ReciteBookBean.class);
        reciteBook.setBookDate(new Date(System.currentTimeMillis()));
        reciteBook.setTop(false);
        reciteBook.setBookTitle(bookName);
        //reciteBook.setPicList(null);
        reciteBook.setPicNum(0);
        reciteBook.setTextNum(0);

        for (TextItem textItem:bookFromWeb.getTextItems()){
            reciteBook.getTextList().add(textItemConvert(textItem,bookName));
        }
        reciteBook.setTextNum(bookFromWeb.getTextItems().size());

        textRealm.commitTransaction();

        textRealm.close();

        return bookFromWeb;
    }
    public static ReciteTextBean textItemConvert(TextItem textItem,String bookName){
        ReciteTextBean textBean=textRealm.createObject(ReciteTextBean.class);
        textBean.setBelonging(bookName);
        textBean.setText(textItem.getText());
        textBean.setTextTitle(textItem.getTitle());
        Date date=new Date(System.currentTimeMillis());
        textBean.setTextDate(date);
        textBean.setTop(false);
        for (Shadow shadow:textItem.getShadows()){
            TextConfigBean textConfigBean=new TextConfigBean();
            textConfigBean.setPrevious(shadow.getFromNumber());
            textConfigBean.setNext(shadow.getToNumber());
            textBean.getTextConfigList().add(textConfigBean);
        }
        return textBean;
    }
    private void get(){
        NetUtils.getInstance().getDataAsynFromNet("", new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException {

            }

            @Override
            public void failed(Call call, IOException e) {
            }
        });
    }
}
