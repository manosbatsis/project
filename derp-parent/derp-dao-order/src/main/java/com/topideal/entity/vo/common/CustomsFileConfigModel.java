package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class CustomsFileConfigModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 配置编码
    */
    private String code;
    /**
    * 关联模板id
    */
    private Long fileTempId;
    /**
     * 关联模板名称
     */
    private String fileTempName;
    /**
    * 关联模板编码
    */
    private String fileTempCode;
    /**
    * 进出仓配置 0-进仓，1-出仓
    */
    private String depotConfig;
    /**
    * 状态 1-启用 0-禁用
    */
    private String status;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    // 是否同关区 0-否，1-是
    private String isSameArea;

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
    /*depotConfig get 方法 */
    public String getDepotConfig(){
    return depotConfig;
    }
    /*depotConfig set 方法 */
    public void setDepotConfig(String  depotConfig){
    this.depotConfig=depotConfig;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
	public String getFileTempName() {
		return fileTempName;
	}
	public void setFileTempName(String fileTempName) {
		this.fileTempName = fileTempName;
	}
	public String getIsSameArea() {
		return isSameArea;
	}
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}






}
