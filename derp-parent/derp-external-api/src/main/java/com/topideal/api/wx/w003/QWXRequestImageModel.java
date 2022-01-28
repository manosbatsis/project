package com.topideal.api.wx.w003;

import com.topideal.api.wx.w002.MarkdownModel;

public class QWXRequestImageModel {
    /**消息类型 必填 text、markdown、image、news、file*/
    private String msgtype="image";
    /**文本内容*/
    private ImageModel image;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
