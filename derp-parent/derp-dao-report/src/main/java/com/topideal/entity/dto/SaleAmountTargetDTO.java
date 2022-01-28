package com.topideal.entity.dto;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class SaleAmountTargetDTO extends PageModel implements Serializable{

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="事业部ID")
    private Long buId;
	
	@ApiModelProperty(value="事业部ID集合")
    private List<Long> buIds;

	@ApiModelProperty(value="事业部名称")
    private String buName;

	@ApiModelProperty(value="月份")
    private String month;

	@ApiModelProperty(value="部门id")
    private Long departmentId;
 
	@ApiModelProperty(value="部门名称")
    private String departmentName;
  
	@ApiModelProperty(value="母品牌ID")
    private Long parentBrandId;

    @ApiModelProperty(value="母品牌ID集合")
    private List<Long> parentBrandIds;

	@ApiModelProperty(value="母品牌名称")
    private String parentBrandName;

	@ApiModelProperty(value="计划总金额")
    private BigDecimal totalPlanAmount;
	
	@ApiModelProperty(value="创建人id")
    private Long creatorId;

	@ApiModelProperty(value="创建人")
    private String creator;

	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value="修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value="币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	
	@ApiModelProperty(value="单据id集合,多个用逗号隔开")
    private String ids;

    @ApiModelProperty(value="部门id集合")
    private List<Long> departmentIds;

    @ApiModelProperty(value="母品牌id集合")
    private List<Long> brandSuperiorIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public BigDecimal getTotalPlanAmount() {
        return totalPlanAmount;
    }

    public void setTotalPlanAmount(BigDecimal totalPlanAmount) {
        this.totalPlanAmount = totalPlanAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public List<Long> getBrandSuperiorIds() {
		return brandSuperiorIds;
	}

	public void setBrandSuperiorIds(List<Long> brandSuperiorIds) {
		this.brandSuperiorIds = brandSuperiorIds;
	}

    public List<Long> getParentBrandIds() {
        return parentBrandIds;
    }

    public void setParentBrandIds(List<Long> parentBrandIds) {
        this.parentBrandIds = parentBrandIds;
    }
}
