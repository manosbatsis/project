package com.topideal.mongo.entity;


/**
 * 品牌父级表
 * @author lian_
 */
public class BrandParentMongo {

    private Long brandParentId;

     //修改人
     private Long modifier;
     //品牌名称
     private String name;
     //创建人
     private Long creater;
     //英文名称
    private String englishName;
    //上级母品牌名称
    private String superiorParentBrand;
    ////上级母品牌NC编码
    private String superiorParentBrandCode;
    //上级母品牌id
    private Long superiorParentBrandId;
    
    
	public Long getBrandParentId() {
		return brandParentId;
	}
	public void setBrandParentId(Long brandParentId) {
		this.brandParentId = brandParentId;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
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
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
	}
	public String getSuperiorParentBrandCode() {
		return superiorParentBrandCode;
	}
	public void setSuperiorParentBrandCode(String superiorParentBrandCode) {
		this.superiorParentBrandCode = superiorParentBrandCode;
	}
	public Long getSuperiorParentBrandId() {
		return superiorParentBrandId;
	}
	public void setSuperiorParentBrandId(Long superiorParentBrandId) {
		this.superiorParentBrandId = superiorParentBrandId;
	}


}

