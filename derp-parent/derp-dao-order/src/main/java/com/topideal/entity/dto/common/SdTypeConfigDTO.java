package com.topideal.entity.dto.common;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class SdTypeConfigDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "SD类型名称")
    private String sdTypeName;

    @ApiModelProperty(value = "简称")
    private String sdSimpleName;

    @ApiModelProperty(value = "类型 1-单比例 2-多比例")
    private String type;
    @ApiModelProperty(value = "类型（中文）")
    private String typeLabel;

    @ApiModelProperty(value = "创建人名称")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;

    @ApiModelProperty(value = "状态 1- 启用 2-禁用")
    private String status;
    @ApiModelProperty(value = "状态（中文）")
    private String statusLabel;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "修改人ID")
    private Long modifierId;

    @ApiModelProperty(value = "修改人名称")
    private String modifier;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
    return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
    this.sdTypeName=sdTypeName;
    }
    /*sdSimpleName get 方法 */
    public String getSdSimpleName(){
    return sdSimpleName;
    }
    /*sdSimpleName set 方法 */
    public void setSdSimpleName(String  sdSimpleName){
    this.sdSimpleName=sdSimpleName;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.sdtypeconfig_typeList, type) ;
    }
    /*creator get 方法 */
    public String getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(String  creator){
    this.creator=creator;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
    return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
    this.creatorId=creatorId;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.sdtypeconfig_statusList, status) ;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*modifierId get 方法 */
    public Long getModifierId(){
    return modifierId;
    }
    /*modifierId set 方法 */
    public void setModifierId(Long  modifierId){
    this.modifierId=modifierId;
    }
    /*modifier get 方法 */
    public String getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(String  modifier){
    this.modifier=modifier;
    }
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}



}
