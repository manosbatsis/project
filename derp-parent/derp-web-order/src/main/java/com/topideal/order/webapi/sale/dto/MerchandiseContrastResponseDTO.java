package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 爬虫商品对照表返回值 dto
 * @date 2021-02-07
 */
@ApiModel
public class MerchandiseContrastResponseDTO {
	@ApiModelProperty(value = "爬虫商品对照表表头")
	private MerchandiseContrastDTO merchandiseContrastDTO;
	
	@ApiModelProperty(value = "爬虫商品对照表表体Model")
	private List<MerchandiseContrastItemModel> itemModel;
	
	@ApiModelProperty(value = "爬虫商品对照表表体DTO")
	private List<MerchandiseContrastItemDTO> itemDTO;
	
	@ApiModelProperty(value = "返回编码，00：成功，01：失败")
	private String code;
	
	@ApiModelProperty(value = "返回成功/失败消息")
	private String message;
	
	@ApiModelProperty(value = "商品信息")
	private MerchandiseInfoMogo merchandiseInfoMogo;
	
	public MerchandiseContrastDTO getMerchandiseContrastDTO() {
		return merchandiseContrastDTO;
	}
	public void setMerchandiseContrastDTO(MerchandiseContrastDTO merchandiseContrastDTO) {
		this.merchandiseContrastDTO = merchandiseContrastDTO;
	}
	public List<MerchandiseContrastItemModel> getItemModel() {
		return itemModel;
	}
	public void setItemModel(List<MerchandiseContrastItemModel> itemModel) {
		this.itemModel = itemModel;
	}
	public List<MerchandiseContrastItemDTO> getItemDTO() {
		return itemDTO;
	}
	public void setItemDTO(List<MerchandiseContrastItemDTO> itemDTO) {
		this.itemDTO = itemDTO;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MerchandiseInfoMogo getMerchandiseInfoMogo() {
		return merchandiseInfoMogo;
	}
	public void setMerchandiseInfoMogo(MerchandiseInfoMogo merchandiseInfoMogo) {
		this.merchandiseInfoMogo = merchandiseInfoMogo;
	}	
}
