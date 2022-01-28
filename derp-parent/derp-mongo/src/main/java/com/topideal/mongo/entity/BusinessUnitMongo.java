package com.topideal.mongo.entity;

import java.sql.Timestamp;

/**
 * 事业部
 */
public class BusinessUnitMongo {
    /**
     * 事业部Id
     */
    private Long businessUnitId;
    /**
     * 事业部编码
     */
    private String code;
    /**
     * 事业部名称
     */
    private String name;
    /**
     * 部门id
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;


    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
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
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }



}
