package com.topideal.mongo.entity;

public class UserBuRelMongo {

    /**
    * 用户Id
    */
    private Long userId;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部编码
    */
    private String buCode;
    /**
    * 事业部名称
    */
    private String buName;

    /*userId get 方法 */
    public Long getUserId(){
    return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
    this.userId=userId;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buCode get 方法 */
    public String getBuCode(){
    return buCode;
    }
    /*buCode set 方法 */
    public void setBuCode(String  buCode){
    this.buCode=buCode;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }




}
