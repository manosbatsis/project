package com.topideal.mongo.entity;

public class DepartmentInfoMongo {
    /**
     * ID
     */
    private Long departmentInfoId;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 创建人id
     */
    private Long creater;
    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 修改人id
     */
    private Long modifier;
    /**
     * 修改人名称
     */
    private String modifiyName;


    public Long getDepartmentInfoId() {
        return departmentInfoId;
    }

    public void setDepartmentInfoId(Long departmentInfoId) {
        this.departmentInfoId = departmentInfoId;
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

}
