package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class CustomsFileDepotRelDTO extends PageModel implements Serializable{

	@ApiModelProperty("id")
    private Long id;

	@ApiModelProperty("清关模版配置id")
    private Long customsFileConfigId;

	@ApiModelProperty("仓库id")
    private Long depotId;

	@ApiModelProperty("仓库名称")
    private String depotName;

	@ApiModelProperty("仓库编码")
    private String depotCode;

	@ApiModelProperty("创建时间")
    private Timestamp createDate;
  
	@ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty("平台关区id")
    private Long customsId;

    @ApiModelProperty("平台关区编码")
    private String customsCode;

    @ApiModelProperty("平台关区名称")
    private String platformCustomsName;
    
    @ApiModelProperty("平台关区id集合")
    private String platformCustomsIds;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*customsFileConfigId get 方法 */
    public Long getCustomsFileConfigId(){
    return customsFileConfigId;
    }
    /*customsFileConfigId set 方法 */
    public void setCustomsFileConfigId(Long  customsFileConfigId){
    this.customsFileConfigId=customsFileConfigId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*depotCode get 方法 */
    public String getDepotCode(){
    return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
    this.depotCode=depotCode;
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

    public Long getCustomsId() {
        return customsId;
    }

    public void setCustomsId(Long customsId) {
        this.customsId = customsId;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
    }

    public String getPlatformCustomsName() {
        return platformCustomsName;
    }

    public void setPlatformCustomsName(String platformCustomsName) {
        this.platformCustomsName = platformCustomsName;
    }
	public String getPlatformCustomsIds() {
		return platformCustomsIds;
	}
	public void setPlatformCustomsIds(String platformCustomsIds) {
		this.platformCustomsIds = platformCustomsIds;
	}
    
}
