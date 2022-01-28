package com.topideal.mongo.entity;

import java.sql.Timestamp;
/**
 * 品牌
 * @author lian_
 */
public class BrandMongo {
	
	// 品牌名称
	private String name;
	// 图片URL
	private String image;
	// ID
	private Long brandId;
	// 品牌父类名称
	private String parentName;
	// 品牌父类id
	private Long parentId;
	// 是否匹配（1-是，0-否）
	private String isMatching;
	
	//英文品牌名称
	private String englishBrandName;

	//中文品牌名称
	private String chinaBrandName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getIsMatching() {
		return isMatching;
	}
	public void setIsMatching(String isMatching) {
		this.isMatching = isMatching;
	}
	public String getEnglishBrandName() {
		return englishBrandName;
	}
	public void setEnglishBrandName(String englishBrandName) {
		this.englishBrandName = englishBrandName;
	}
	public String getChinaBrandName() {
		return chinaBrandName;
	}
	public void setChinaBrandName(String chinaBrandName) {
		this.chinaBrandName = chinaBrandName;
	}
	
	
	
	
}
