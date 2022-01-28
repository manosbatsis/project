package com.topideal.entity.dto.common;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class LogisticsContactTemplateDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 类型 1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信息
    */
	@ApiModelProperty("类型 1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信息")
    private String type;
	@ApiModelProperty("类型中文 1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信息")
	private String typeLabel;
    /**
    * 名称/公司
    */
	@ApiModelProperty("名称/公司")
    private String name;
    /**
    * 联系人详情
    */
	@ApiModelProperty("联系人详情")
    private String details;
    /**
    * 创建时间
    */
	@ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
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
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.logisticsContactTemplate_TypeList, type) ;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*details get 方法 */
    public String getDetails(){
    return details;
    }
    /*details set 方法 */
    public void setDetails(String  details){
    this.details=details;
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
