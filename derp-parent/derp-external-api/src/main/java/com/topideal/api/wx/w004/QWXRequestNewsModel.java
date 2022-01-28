package com.topideal.api.wx.w004;

public class QWXRequestNewsModel {
    /**消息类型 必填 text、markdown、image、news、file*/
    private String msgtype="news";
    /**文本内容*/
    private NewsModel news;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public NewsModel getNews() {
        return news;
    }

    public void setNews(NewsModel news) {
        this.news = news;
    }
}
