package com.topideal.entity.vo.main;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class DepotCustomsRelModel extends PageModel implements Serializable{

    /**
    * 自增主键
    */
	@ApiModelProperty(value = "自增主键")
    private Long id;
    /**
    * 仓库ID
    */
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
    /**
    * 仓库名称
    */
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 关区ID
    */
	@ApiModelProperty(value = "关区ID")
    private Long customsAreaId;
    /**
    * 关区代码
    */
	@ApiModelProperty(value = "关区代码")
    private String customsAreaCode;
    /**
    * 关区名称
    */
	@ApiModelProperty(value = "关区名称")
    private String customsAreaName;
    /**
    * 收货人公司名称
    */
	@ApiModelProperty(value = "收货人公司名称")
    private String recCompanyName;
    /**
    * 收货人公司英文名称
    */
	@ApiModelProperty(value = "收货人公司英文名称")
    private String recCompanyEnname;
    /**
    * 仓库详细地址
    */
	@ApiModelProperty(value = "仓库详细地址")
    private String address;
    /**
     * 电商账册号
     */
	@ApiModelProperty(value = "电商账册号")
    private String onlineRegisterNo;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*customsAreaId get 方法 */
    public Long getCustomsAreaId(){
    return customsAreaId;
    }
    /*customsAreaId set 方法 */
    public void setCustomsAreaId(Long  customsAreaId){
    this.customsAreaId=customsAreaId;
    }
    /*customsAreaCode get 方法 */
    public String getCustomsAreaCode(){
    return customsAreaCode;
    }
    /*customsAreaCode set 方法 */
    public void setCustomsAreaCode(String  customsAreaCode){
    this.customsAreaCode=customsAreaCode;
    }
    /*customsAreaName get 方法 */
    public String getCustomsAreaName(){
    return customsAreaName;
    }
    /*customsAreaName set 方法 */
    public void setCustomsAreaName(String  customsAreaName){
    this.customsAreaName=customsAreaName;
    }
    /*recCompanyName get 方法 */
    public String getRecCompanyName(){
    return recCompanyName;
    }
    /*recCompanyName set 方法 */
    public void setRecCompanyName(String  recCompanyName){
    this.recCompanyName=recCompanyName;
    }
    /*recCompanyEnname get 方法 */
    public String getRecCompanyEnname(){
    return recCompanyEnname;
    }
    /*recCompanyEnname set 方法 */
    public void setRecCompanyEnname(String  recCompanyEnname){
    this.recCompanyEnname=recCompanyEnname;
    }
    /*address get 方法 */
    public String getAddress(){
    return address;
    }
    /*address set 方法 */
    public void setAddress(String  address){
    this.address=address;
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

    public String getOnlineRegisterNo() {
        return onlineRegisterNo;
    }

    public void setOnlineRegisterNo(String onlineRegisterNo) {
        this.onlineRegisterNo = onlineRegisterNo;
    }
}
