package com.topideal.entity.vo.main;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 仓库附表
 * @author lian_
 *
 */
public class DepotScheduleModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
	@ApiModelProperty(value = "详细地址")
    private String address;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
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






}
