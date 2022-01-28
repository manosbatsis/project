package com.topideal.mongo.entity;

public class UnitMongo {


    /**
     * 单位id
     */
    private Long unitId;
    /**
    * 单位编码
    */
    private String code;
    /**
    * 单位名称
    */
    private String name;
    /**
    * 创建人
    */
    private Long creater;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }
}
