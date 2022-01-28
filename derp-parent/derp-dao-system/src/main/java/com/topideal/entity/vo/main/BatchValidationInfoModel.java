package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 批次效期强校验明细
 * @author lian_
 */
public class BatchValidationInfoModel extends PageModel implements Serializable{

    /**
    * 仓库名称
    */
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 修改时间YYYY-MM-dd HH:mm:ss
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 操作员名称
    */
	@ApiModelProperty(value = "操作员名称")
    private String createUsername;
    /**
    * 生效时间YYYY-MM-dd HH:mm:ss
    */
	@ApiModelProperty(value = "生效时间")
    private Timestamp effectiveTime;
    /**
    * 仓库ID
    */
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
    /**
    * 批次效期强校验：1-是 0-否
    */
	@ApiModelProperty(value = "批次效期强校验：1-是 0-否")
    private String batchValidation;
    /**
    * 操作员ID
    */
	@ApiModelProperty(value = "操作员ID")
    private Long creater;
    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 创建时间YYYY-MM-dd HH:mm:ss
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    
    //拓展字段
    //仓库自编码
	@ApiModelProperty(value = "仓库自编码")
    private String depotCode;

    
    public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	/*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*createUsername get 方法 */
    public String getCreateUsername(){
        return createUsername;
    }
    /*createUsername set 方法 */
    public void setCreateUsername(String  createUsername){
        this.createUsername=createUsername;
    }
    /*effectiveTime get 方法 */
    public Timestamp getEffectiveTime(){
        return effectiveTime;
    }
    /*effectiveTime set 方法 */
    public void setEffectiveTime(Timestamp  effectiveTime){
        this.effectiveTime=effectiveTime;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*batchValidation get 方法 */
    public String getBatchValidation(){
        return batchValidation;
    }
    /*batchValidation set 方法 */
    public void setBatchValidation(String  batchValidation){
        this.batchValidation=batchValidation;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}
