package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class ReceivePaymentNotesModel extends PageModel implements Serializable{


	@ApiModelProperty("id主键")
    private Long id;
	@ApiModelProperty("应收账单号")
    private String receiveCode;
	@ApiModelProperty("备注人id")
    private Long notesId;
	@ApiModelProperty("备注人名称")
    private String notesName;
	@ApiModelProperty("备注内容")
    private String notes;
	@ApiModelProperty("创建时间")
    private Timestamp createDate;
	@ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*receiveCode get 方法 */
    public String getReceiveCode(){
    return receiveCode;
    }
    /*receiveCode set 方法 */
    public void setReceiveCode(String  receiveCode){
    this.receiveCode=receiveCode;
    }
    /*notesId get 方法 */
    public Long getNotesId(){
    return notesId;
    }
    /*notesId set 方法 */
    public void setNotesId(Long  notesId){
    this.notesId=notesId;
    }
    /*notesName get 方法 */
    public String getNotesName(){
    return notesName;
    }
    /*notesName set 方法 */
    public void setNotesName(String  notesName){
    this.notesName=notesName;
    }
    /*notes get 方法 */
    public String getNotes(){
    return notes;
    }
    /*notes set 方法 */
    public void setNotes(String  notes){
    this.notes=notes;
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
