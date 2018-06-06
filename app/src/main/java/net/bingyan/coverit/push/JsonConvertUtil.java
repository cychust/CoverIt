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
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Response;

public class JsonConvertUtil {
    //private static Realm textRealm;
    // private static ReciteBookBean reciteBook;
    //  private static List<ReciteTextBean>reciteTextBeans;
    //private static Gson gson;
    public static JsonFromWeb jsonCovert(String json, int flag) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        Realm textRealm = Realm.getDefaultInstance();
        Gson gson = new Gson();
        JsonFromWeb bookFromWeb = gson.fromJson(json, JsonFromWeb.class);
        String bookName = bookFromWeb.getNotebookName();
        if (flag == 1) {
            RealmResults<ReciteBookBean> reciteBookBeans = textRealm.where(ReciteBookBean.class).findAll();
            int count = 0;
            String titleTmp = bookName;
            int flag1;
            while (count <= reciteBookBeans.size()) {
                flag1 = 0;
                titleTmp += "!";
                for (ReciteBookBean reciteBookBean : reciteBookBeans) {
                    if (reciteBookBean.getBookTitle().equals(titleTmp)) {
                        flag1 = 1;
                        break;
                    }
                }
                if (flag1 == 0) {
                    bookName = titleTmp;
                    break;
                }
                count++;
            }

        }

        textRealm.beginTransaction();
        ReciteBookBean reciteBook;

        reciteBook = textRealm.createObject(ReciteBookBean.class);
        reciteBook.setBookDate(new Date(System.currentTimeMillis()));
        reciteBook.setTop(false);
        reciteBook.setBookTitle(bookName);
        //reciteBook.setPicList(null);
        reciteBook.setPicNum(0);
        reciteBook.setTextNum(0);

        for (TextItem textItem : bookFromWeb.getTextItems()) {
            reciteBook.getTextList().add(textItemConvert(textItem, bookName, textRealm));
        }
        reciteBook.setTextNum(bookFromWeb.getTextItems().size());

        textRealm.commitTransaction();
        if (!textRealm.isClosed())
            textRealm.close();

        return bookFromWeb;
    }

    public static ReciteTextBean textItemConvert(TextItem textItem, String bookName, Realm textRealm) {
        ReciteTextBean textBean = textRealm.createObject(ReciteTextBean.class);
        textBean.setBelonging(bookName);
        textBean.setText(textItem.getText());
        textBean.setTextTitle(textItem.getTitle());
        Date date = new Date(System.currentTimeMillis());
        textBean.setTextDate(date);
        textBean.setTop(false);
        for (Shadow shadow : textItem.getShadows()) {
            TextConfigBean textConfigBean = new TextConfigBean();
            textConfigBean.setPrevious(shadow.getFrom());
            textConfigBean.setNext(shadow.getTo());
            textBean.getTextConfigList().add(textConfigBean);
        }
        return textBean;
    }

    private void get() {
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
