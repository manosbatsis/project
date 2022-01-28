package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 部门管理
 */
public class DepartmentInfoDTO  extends PageModel implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码",required = false)
    private String code;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称",required = false)
    private String name;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id",required = false)
    private Long creater;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称",required = false)
    private String createName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required = false)
    private Timestamp createDate;
    /**
     * 修改人id
     */
    @ApiModelProperty(value = "修改人id",required = false)
    private Long modifier;
    /**
     * 修改人名称
     */
    @ApiModelProperty(value = "修改人名称",required = false)
    private String modifiyName;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required = false)
    private Timestamp modifyDate;

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
    /*name get 方法 */
    public String getName(){
        return name;
    }
    /*name set 方法 */
    public void setName(String  name){
        this.name=name;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
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
    /*modifiyName get 方法 */
    public String getModifiyName(){
        return modifiyName;
    }
    /*modifiyName set 方法 */
    public void setModifiyName(String  modifiyName){
        this.modifiyName=modifiyName;
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
