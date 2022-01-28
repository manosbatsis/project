package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class NoticeUserRelModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 公告ID
    */
    private Long noticeId;
    /**
    * 状态 0-未读 1-已读
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

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*userId get 方法 */
    public Long getUserId(){
    return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
    this.userId=userId;
    }
    /*noticeId get 方法 */
    public Long getNoticeId(){
    return noticeId;
    }
    /*noticeId set 方法 */
    public void setNoticeId(Long  noticeId){
    this.noticeId=noticeId;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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






}
