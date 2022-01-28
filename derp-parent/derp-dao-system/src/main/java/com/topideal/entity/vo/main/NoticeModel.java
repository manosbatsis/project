package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class NoticeModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 公告标题
    */
    private String title;
    /**
    * 公告内容
    */
    private String contentBody;
    /**
    * 状态
    */
    private String status;
    /**
    * 类型
    */
    private String type;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 发布时间
    */
    private Timestamp publishDate;

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
    /*contentBody get 方法 */
    public String getContentBody(){
    return contentBody;
    }
    /*contentBody set 方法 */
    public void setContentBody(String  contentBody){
    this.contentBody=contentBody;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
    /*publishDate get 方法 */
    public Timestamp getPublishDate(){
    return publishDate;
    }
    /*publishDate set 方法 */
    public void setPublishDate(Timestamp  publishDate){
    this.publishDate=publishDate;
    }






}
