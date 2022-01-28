package com.topideal.order.webapi.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.order.service.purchase.TallyingItemBatchService;
import com.topideal.order.service.purchase.TallyingOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.TallyingOrderForm;
import com.topideal.order.webapi.sale.dto.SaleReturnTallyingOrderResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 销售退理货单
 * 
 */
@RequestMapping("/webapi/order/saleReturnTallyin")
@RestController
@Api(tags = "销售退理货单")
public class APISaleReturnTallyingOrderController {

	// 理货单service
	@Autowired
	private TallyingOrderService tallyingOrderService;
	// 理货单批次service
	 @Autowired
	 private TallyingItemBatchService tallyingItemBatchService;
	
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;

	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "查询销售退理货单列表信息")   	
   	@PostMapping(value="/listTallyingOrderTransfer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TallyingOrderDTO> listTallyingOrderTransfer(TallyingOrderForm form, HttpSession session) {
		TallyingOrderDTO dto = new TallyingOrderDTO();
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setType("3");// 销售退理货单
			dto.setBuId(form.getBuId());
			dto.setCode(form.getCode());
			dto.setOrderCode(form.getOrderCode());
			dto.setState(form.getState());
			dto.setDepotId(form.getDepotId());
			dto.setContractNo(form.getContractNo());
			dto.setTallyingStartDate(form.getTallyingStartDate());
			dto.setTallyingEndDate(form.getTallyingEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = tallyingOrderService.listTallyingOrderPage(dto, user);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 访问详情页面
	 */
	@ApiOperation(value = "查看某个商品的详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnTallyingOrderResponseDTO> toDetailsPageTransfer(String token,Long id) throws Exception {
		SaleReturnTallyingOrderResponseDTO responseDTO = new SaleReturnTallyingOrderResponseDTO();
		try {
			TallyingOrderDTO tallyingOrder = tallyingOrderService.searchDetail(id);
			List<TallyingItemBatchDTO> list = tallyingItemBatchService.getGoodsAndBatch(id);
			responseDTO.setTallyingOrderDTO(tallyingOrder);
			responseDTO.setTallyingItemBatchList(list);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}


	 /**
	  * 销售退货理货单确认/驳回
	  * @param ids
	  * 理货单id 多个时用,号分隔
	  * 操作指令 010:确认 004:驳回
	  */
	@ApiOperation(value = "销售退货理货单确认/驳回")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true),
		@ApiImplicitParam(name = "state", value = "操作指令,010:确认 004:驳回", required = true)
	})
	@ApiResponses({
		@ApiResponse(code = 10000,message="data = > 保存操作 成功/失败 信息")
})
	@PostMapping(value="/tallyConfirmTransfer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> tallyConfirmTransfer(String token,String ids, String state,HttpSession session) {
		String failCode = "";
		try {
			User user= ShiroUtils.getUserByToken(token); 
			Map<String, String> data = new HashMap<String, String>();
			// 检查参数
			if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(state)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			failCode = tallyingOrderService.updateTallyConfirmTransfer(ids, state, user.getId(),user.getName(),user.getTopidealCode());
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,failCode);		
	}

}
