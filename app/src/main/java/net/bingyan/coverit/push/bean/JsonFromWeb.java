package net.bingyan.coverit.push.bean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.bingyan.coverit.push.bean.TextItem;

/*
https://shadows.tooyoungtoosimple.gq/notebooks/1?code=asdfa
{
    "id":1,
    "notebookName":"四级必过",
    "textItems":[
        {
            "id":1,
            "title": "桃花原记",
            "text":"与平\n根治不足以自己",
            "shadows":[
                {
                    "fromNumber":0,
                    "toNumber": 5
                },
                {
                    "fromNumber":8,
                    "toNumber":15
                }
           ]
       },
      {
            "id":2,
            "title": "桃花原记",
            "text":"与平\n根治不足以自己",
            "shadows":[
                {
                    "from":0,
                    "to": 5
                },
                {
                    "from":8,
                    "to":15
                }
           ]
       }
     ]
}

 */
public class JsonFromWeb {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("codeList")
    @Expose
    private List<String> codeList = null;
    @SerializedName("notebookName")
    @Expose
    private String notebookName;
    @SerializedName("textItems")
    @Expose
    private List<TextItem> textItems = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }

    public List<TextItem> getTextItems() {
        return textItems;
    }

    public void setTextItems(List<TextItem> textItems) {
        this.textItems = textItems;
    }
}
