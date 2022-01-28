package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class FileTempModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 模版标题
    */
    private String title;
    /**
    * 模版编码
    */
    private String code;
    /**
    * 模版内容
    */
    private String contentBody;
    /**
    * 公告类型 1-采购合同
    */
    private String type;
    /**
     * 状态 1-启用 0-禁用
     */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    //适用类型  1-客户  2-供应商
    private String cusType;
    //跳转页面地址
    private String toUrl;
    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 商家名称
     */
    private String merchantName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*title get 方法 */
    public String getTitle(){
    return title;
    }
    /*title set 方法 */
    public void setTitle(String  title){
    this.title=title;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*contentBody get 方法 */
    public String getContentBody(){
    return contentBody;
    }
    /*contentBody set 方法 */
    public void setContentBody(String  contentBody){
    this.contentBody=contentBody;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
