package com.topideal.api.wx.w004;

public class Article {
    String title;//标题，不超过128个字节，超过会自动截断
    String description;//描述，不超过512个字节，超过会自动截断
    String url;//点击后跳转的链接。
    String picurl;//图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
