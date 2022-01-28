package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class CustomsFileConfigDTO extends PageModel implements Serializable{

	@ApiModelProperty("id")
    private Long id;
   
	@ApiModelProperty("配置编码")
    private String code;
 
	@ApiModelProperty("关联模板id")
    private Long fileTempId;
    
	@ApiModelProperty("关联模板编码")
    private String fileTempCode;
  
	@ApiModelProperty("关联模板名称")
    private String fileTempName;
    
	@ApiModelProperty("进出仓配置 0-进仓，1-出仓")
    private String depotConfig;
	@ApiModelProperty("进出仓配置（中文）")
    private String depotConfigLabel;
   
    @ApiModelProperty("状态 1-启用 0-禁用")
    private String status;
    @ApiModelProperty("状态（中文）")
    private String statusLabel;
   
    @ApiModelProperty("创建人")
    private Long creater;
   
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
   
    @ApiModelProperty("修改人")
    private Long modifier;
    
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    @ApiModelProperty("仓库名称集合，用于列表展示")
    private String depotNames;
    @ApiModelProperty("仓库id集合,用于检索多选")
    private String depotIds;
    
    @ApiModelProperty("是否同关区 0-否，1-是")
     private String isSameArea;
    @ApiModelProperty("是否同关区（中文）")
    private String isSameAreaLabel;
    @ApiModelProperty("仓库id")
    private Long depotId;
    @ApiModelProperty("仓库关区id")
    private Long customsId;
    
    /*id get 方法 */
    public Long getId(){
    	return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    	this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    	return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    	this.code=code;
    }
    /*fileTempId get 方法 */
    public Long getFileTempId(){
    	return fileTempId;
    }
    /*fileTempId set 方法 */
    public void setFileTempId(Long  fileTempId){
    	this.fileTempId=fileTempId;
    }
    /*fileTempCode get 方法 */
    public String getFileTempCode(){
    	return fileTempCode;
    }
    /*fileTempCode set 方法 */
    public void setFileTempCode(String  fileTempCode){
    	this.fileTempCode=fileTempCode;
    }
    public String getFileTempName() {
		return fileTempName;
	}
	public void setFileTempName(String fileTempName) {
		this.fileTempName = fileTempName;
	}
	/*depotConfig get 方法 */
    public String getDepotConfig(){
    	return depotConfig;
    }
    /*depotConfig set 方法 */
    public void setDepotConfig(String  depotConfig){
    	this.depotConfig = depotConfig;
    	this.depotConfigLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.customsFileConfig_DepotConfigList, depotConfig);
    }
    /*status get 方法 */
    public String getStatus(){
    	return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    	this.status=status;
    	this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.customsfileconfig_statusList, status) ;
    }
    /*creater get 方法 */
    public Long getCreater(){
    	return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    	this.creater=creater;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    	return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    	this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    	return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    	this.modifier=modifier;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    	return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    	this.modifyDate=modifyDate;
    }
	public String getDepotNames() {
		return depotNames;
	}
	public void setDepotNames(String depotNames) {
		this.depotNames = depotNames;
	}
	public String getDepotIds() {
		return depotIds;
	}
	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}
	public String getDepotConfigLabel() {
		return depotConfigLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public String getIsSameArea() {
		return isSameArea;
	}
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
		this.isSameAreaLabel = DERP_ORDER.getLabelByKey(DERP.isSameAreaList, isSameArea);
	}
	public String getIsSameAreaLabel() {
		return isSameAreaLabel;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public Long getCustomsId() {
		return customsId;
	}
	public void setCustomsId(Long customsId) {
		this.customsId = customsId;
	}
}
