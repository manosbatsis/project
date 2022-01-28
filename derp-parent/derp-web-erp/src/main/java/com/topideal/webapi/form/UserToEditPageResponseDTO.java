package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserBuRelModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *访问用户编辑页面响应
 * @author 杨创
 */
@ApiModel
public class UserToEditPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "用户DTO")
	private UserInfoDTO detail;
	@ApiModelProperty(value = "商家list")
	private List<MerchantInfoModel> merchantInfoModels;
	@ApiModelProperty(value = "用户事业部关系不list")
	List<UserBuRelModel> buRelList;
	public UserInfoDTO getDetail() {
		return detail;
	}
	public void setDetail(UserInfoDTO detail) {
		this.detail = detail;
	}
	public List<MerchantInfoModel> getMerchantInfoModels() {
		return merchantInfoModels;
	}
	public void setMerchantInfoModels(List<MerchantInfoModel> merchantInfoModels) {
		this.merchantInfoModels = merchantInfoModels;
	}
	public List<UserBuRelModel> getBuRelList() {
		return buRelList;
	}
	public void setBuRelList(List<UserBuRelModel> buRelList) {
		this.buRelList = buRelList;
	}


}
