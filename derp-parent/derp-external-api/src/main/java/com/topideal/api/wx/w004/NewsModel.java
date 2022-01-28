package com.topideal.api.wx.w004;

import java.util.List;

/**文本消息实体
 * */
public class NewsModel {
    //图文消息，一个图文消息支持1到8条图文
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
