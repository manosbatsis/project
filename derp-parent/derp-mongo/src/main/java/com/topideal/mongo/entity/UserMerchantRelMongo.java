package com.topideal.mongo.entity;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 用户公司关联表
 */
public class UserMerchantRelMongo {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 公司id
     */
    private Long merchantId;

    /*userId get 方法 */
    public Long getUserId(){
        return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
        this.userId=userId;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
}
