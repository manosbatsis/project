package com.topideal.api.wx.w001;

public class QWXRequestTextModel {
    /**消息类型 必填 text、markdown、image、news、file*/
    private String msgtype="text";
    /**文本内容*/
    private TextModel text;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public TextModel getText() {
        return text;
    }

    public void setText(TextModel text) {
        this.text = text;
    }
}
