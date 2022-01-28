package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 云集仓库对照表
 * @author lian_
 */
public class YunjiDepotContrastModel extends PageModel implements Serializable{

    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 云集仓库名称
    */
    private String yunjiDepotName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * op仓库编码
    */
    private String opDepotCode;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库编码
    */
    private String depotCode;
    /**
    * id
    */
    private Long id;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    //备注
    private String remark;

    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*yunjiDepotName get 方法 */
    public String getYunjiDepotName(){
        return yunjiDepotName;
    }
    /*yunjiDepotName set 方法 */
    public void setYunjiDepotName(String  yunjiDepotName){
        this.yunjiDepotName=yunjiDepotName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*opDepotCode get 方法 */
    public String getOpDepotCode(){
        return opDepotCode;
    }
    /*opDepotCode set 方法 */
    public void setOpDepotCode(String  opDepotCode){
        this.opDepotCode=opDepotCode;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*depotCode get 方法 */
    public String getDepotCode(){
        return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
        this.depotCode=depotCode;
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
