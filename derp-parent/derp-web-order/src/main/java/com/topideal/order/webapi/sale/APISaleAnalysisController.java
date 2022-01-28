package com.topideal.order.webapi.sale;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.order.service.sale.SaleAnalysisService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.SaleAnalysisForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * webapi 销售出库分析
 * 
 */
@RequestMapping("/webapi/order/saleAnalysis")
@RestController
@Api(tags = "销售出库分析")
public class APISaleAnalysisController {

	// 销售出库分析service
	@Autowired
	private SaleAnalysisService saleAnalysisService;
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取销售出库分析列表信息")   	
   	@PostMapping(value="/listSaleAnalysis.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleAnalysisDTO> listSaleAnalysis(HttpSession session, SaleAnalysisForm form) {
		SaleAnalysisDTO dto = new SaleAnalysisDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken()); 
			dto.setSaleOutDepotCode(form.getSaleOutDepotCode());
			dto.setOrderCode(form.getOrderCode());
			dto.setIsEnd(form.getIsEnd());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setSaleType(form.getSaleType());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()):null);
			
			//设置时间相关的条件
			if(StringUtils.isNotBlank(form.getEndDateStr())){
				Timestamp endDate = Timestamp.valueOf(form.getEndDateStr()+" 00:00:00"); 
				dto.setEndDate(endDate);
			}
			dto.setMerchantId(user.getMerchantId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = saleAnalysisService.listSaleAnalysisDTO(dto,user);
			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
}
