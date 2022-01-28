package com.topideal.api.wx.w001;

/**文本消息实体
 * */
public class TextModel {

    /**消息内容 必填*/
    private String content;
    /**需要提醒的人["wangqing","@all"]*/
    private String[] mentioned_list;
    /**需要提醒的人的手机号["13800001111","@all"]*/
    private String[] mentioned_mobile_list;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getMentioned_list() {
        return mentioned_list;
    }

    public void setMentioned_list(String[] mentioned_list) {
        this.mentioned_list = mentioned_list;
    }

    public String[] getMentioned_mobile_list() {
        return mentioned_mobile_list;
    }

    public void setMentioned_mobile_list(String[] mentioned_mobile_list) {
        this.mentioned_mobile_list = mentioned_mobile_list;
    }
}
