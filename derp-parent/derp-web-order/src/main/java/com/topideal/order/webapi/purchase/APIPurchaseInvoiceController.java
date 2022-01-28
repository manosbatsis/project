package com.topideal.order.webapi.purchase;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;
import com.topideal.order.service.purchase.PurchaseInvoiceService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseInvoiceAddForm;
import com.topideal.order.webapi.purchase.form.PurchaseInvoiceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webapi/order/purchaseInvoice")
@Api(tags = "采购发票")
public class APIPurchaseInvoiceController {

	private static final Logger LOG = Logger.getLogger(APIPurchaseInvoiceController.class) ;

	@Autowired
	PurchaseInvoiceService purchaseInvoiceService ;

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getInvocieByPurchaseId.asyn" ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "根据采购订单ID获取发票信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "purchaseId", value = "采购订单ID", required = true),
	})
	public ResponseBean<PurchaseInvoiceDTO> getInvocieByPurchaseId(@RequestParam(value="token", required = true)String token,
			@RequestParam(value="purchaseId", required = true) Long purchaseId) {

		try {

			PurchaseInvoiceDTO dto = purchaseInvoiceService.getInvocieByPurchaseId(purchaseId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/saveInvoice.asyn" , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存发票")
	public ResponseBean<String> saveInvoice(@RequestBody PurchaseInvoiceAddForm form) {

		try {

			boolean isEmpty = new EmptyCheckUtils().addObject(form.getPurchaseOrderId())
				.addObject(form.getBuId()).addObject(form.getSupplierId())
				.addObject(form.getDrawInvoiceDate()).empty();

			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "数据为空") ;
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			PurchaseInvoiceDTO dto = new PurchaseInvoiceDTO() ;

			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());

			if(StringUtils.isNotBlank(form.getInvoiceDate())) {
				dto.setInvoiceDate(TimeUtils.parse(form.getInvoiceDate(), "yyyy-MM-dd HH:mm:ss"));
			}

			if(StringUtils.isNotBlank(form.getDrawInvoiceDate())) {
				dto.setDrawInvoiceDate(TimeUtils.parse(form.getDrawInvoiceDate(), "yyyy-MM-dd HH:mm:ss"));
			}

			if(StringUtils.isNotBlank(form.getPayDate())) {
				dto.setPaymentDate(TimeUtils.parse(form.getPayDate(), "yyyy-MM-dd HH:mm:ss"));
			}

			BeanUtils.copyProperties(form, dto);

			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());

			purchaseInvoiceService.saveInvoice(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getDetailsById.asyn" ,  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "根据ID获取发票信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "采购发票ID", required = true),
	})
	public ResponseBean<PurchaseInvoiceDTO> getDetailsById(@RequestParam(value="token", required = true)String token,
			@RequestParam(value="id", required = true) Long id) {

		try {

			PurchaseInvoiceDTO dto = purchaseInvoiceService.getDetailsById(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 获取分页数据
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/getListByPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PurchaseInvoiceDTO> getListByPage(PurchaseInvoiceForm form) {

		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {

			PurchaseInvoiceDTO dto = new PurchaseInvoiceDTO();

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseInvoiceService.getListByPage(dto,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@PostMapping(value = "/delInvoice.asyn" , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "删除发票")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购发票ID，多个以英文逗号隔开", required = true),
	})
	public ResponseBean<String> delInvoice(String token, String ids) {

		try {

			boolean isEmpty = new EmptyCheckUtils().addObject(ids).empty();

			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "数据为空") ;
			}

			User user = ShiroUtils.getUserByToken(token);

			purchaseInvoiceService.delInvoice(ids, user); ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}
}
