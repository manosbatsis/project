package com.topideal.entity.dto;

/**
 * @Description: 品牌销售dto
 * @Author: Chen Yiluan
 * @Date: 2019/12/10 17:04
 **/
public class BrandSaleDTO {

    private String name;

    private Integer saleNum;

    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
