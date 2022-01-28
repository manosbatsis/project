package com.topideal.entity.vo.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌表
 * @author lchenxing
 */
public class BrandModel extends PageModel implements Serializable {

	// 品牌名称
	@ApiModelProperty(value = "品牌名称")
	private String name;
	// 图片URL
	@ApiModelProperty(value = "图片URL")
	private String image;
	// ID
	@ApiModelProperty(value = "ID")
	private Long id;
	// 创建人
	@ApiModelProperty(value = "创建人")
	private Long creater;
	// 创建日期
	@ApiModelProperty(value = "创建日期")
	private Timestamp createDate;
	// 操作时间
	@ApiModelProperty(value = "操作时间")
	private Timestamp operationDate;
	// 品牌父类名称
	@ApiModelProperty(value = "牌父类名称")
	private String parentName;
	// 品牌父类id
	@ApiModelProperty(value = "品牌父类id")
	private Long parentId;
	// 操作人
	@ApiModelProperty(value = "操作人")
	private Long operator;
	// 修改时间
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	// 是否匹配（1-是，0-否）
	@ApiModelProperty(value = "是否匹配（1-是，0-否）")
	private String isMatching;

	// 匹配品牌list
	@ApiModelProperty(value = "匹配品牌list")
	private List<BrandModel> brandList;
	// 储存选择过的id
	@ApiModelProperty(value = "储存选择过的id")
	private List ids;
	// 操作人名称
	@ApiModelProperty(value = "操作人名称")
	private String operatorName;
	//英文品牌名称
	@ApiModelProperty(value = "英文品牌名称")
	private String englishBrandName;

	//中文品牌名称
	@ApiModelProperty(value = "中文品牌名称")
	private String chinaBrandName;

	@ApiModelProperty(value = "创建人名称")
	private String createrName;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	public List<BrandModel> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<BrandModel> brandList) {
		this.brandList = brandList;
	}

	/* isMatching get 方法 */
	public String getIsMatching() {
		return isMatching;
	}

	/* isMatching set 方法 */
	public void setIsMatching(String isMatching) {
		this.isMatching = isMatching;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Timestamp getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Timestamp operationDate) {
		this.operationDate = operationDate;
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

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	/* name get 方法 */
	public String getName() {
		return name;
	}

	/* name set 方法 */
	public void setName(String name) {
		this.name = name;
	}

	/* image get 方法 */
	public String getImage() {
		return image;
	}

	/* image set 方法 */
	public void setImage(String image) {
		this.image = image;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* createName get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* createName set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
}
