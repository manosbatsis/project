package com.topideal.api.wx.w002;

public class QWXRequestMarkdownModel {
    /**消息类型 必填 text、markdown、image、news、file*/
    private String msgtype="markdown";
    /**文本内容*/
    private MarkdownModel markdown;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public MarkdownModel getMarkdown() {
        return markdown;
    }

    public void setMarkdown(MarkdownModel markdown) {
        this.markdown = markdown;
    }
}
