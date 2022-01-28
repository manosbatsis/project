package com.topideal.order.webapi.transfer;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.order.service.purchase.TallyingItemBatchService;
import com.topideal.order.service.purchase.TallyingOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.transfer.form.TallyingOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 调拨理货单 controller
 * 
 */
@RestController
@RequestMapping("/webapi/transfer/transferTallying")
@Api(tags = "调拨理货单")
public class APITransferTallyingOrderController {

	@Autowired
	private TallyingOrderService tallyingOrderService;
	 @Autowired
	 private TallyingItemBatchService tallyingItemBatchService;

	/**
	 * 获取分页数据
	 */
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listTallyingOrderTransfer.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<TallyingOrderDTO> listTallyingOrderTransfer(TallyingOrderForm form) {
		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}
		try {
			TallyingOrderDTO dto = new TallyingOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setType(DERP_ORDER.TALLYINGORDER_TYPE_2);
			dto.setCode(form.getCode());
			dto.setDepotId(form.getDepotId());
			dto.setBuId(form.getBuId());
			dto.setOrderCode(form.getOrderCode());
			dto.setState(form.getState());
			dto.setContractNo(form.getContractNo());
			dto.setTallyingStartDate(form.getTallyingStartDate());
			dto.setTallyingEndDate(form.getTallyingEndDate());
			dto = tallyingOrderService.listTallyingOrderPage(dto, user);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 根据ID查找详情
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID查找详情")
	@PostMapping("/toDetailPage.html")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "调拨理货单id", required = true) })
	public ResponseBean<TallyingOrderDTO> toDetailsPageTransfer(String token, Long id) throws Exception {

		try {
			TallyingOrderDTO tallyingOrder = tallyingOrderService.searchDetail(id);
			List<TallyingItemBatchDTO> list = tallyingItemBatchService.getGoodsAndBatch(id);
			tallyingOrder.setBatchList(list);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,tallyingOrder);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@PostMapping("/tallyConfirmTransfer.asyn")
	@ApiOperation(value = "拨理货单确认/驳回", notes="修改理货单状态，若修改成功，返回空字符。修改失败从data取失败原因")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "理货单ID，多个以','隔开", required = true),
			@ApiImplicitParam(name = "state", value = "状态（确认/驳回）", required = true)
	})
	public ResponseBean<String> tallyConfirmTransfer(String token, String ids, String state) {
		try {
			// 检查参数
			if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(state)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			User user= ShiroUtils.getUserByToken(token);
			String failCode = tallyingOrderService.updateTallyConfirmTransfer(ids, state, user.getId(),user.getName(),user.getTopidealCode());
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, failCode);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

}
