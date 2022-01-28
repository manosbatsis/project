package com.topideal.order.webapi.sale;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.SaleCreditBillOrderItemModel;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;
import com.topideal.order.service.sale.SaleCreditOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.sale.form.SaleCreditOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RequestMapping("/webapi/order/saleCredit")
@RestController
@Api(tags = "赊销单")
public class APISaleCreditOrderController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(APISaleCreditOrderController.class);

	@Autowired
	private SaleCreditOrderService saleCreditOrderService;

	@ApiOperation(value = "获取列表信息")
	@PostMapping(value="/listDTOByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleCreditOrderDTO> listDTOByPage(SaleCreditOrderForm form) {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		try {
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto = saleCreditOrderService.listDTOByPage(dto, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "获取列表赊销单各类型数量", notes="返回类型-数量集合")
	@PostMapping(value="/getCreditTypeNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getDeclareTypeNum(SaleCreditOrderForm form) {
		try {
			SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<Map<String, Object>> resultMap = saleCreditOrderService.getCreditTypeNum(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售预申报id", required = true)
	})
	@PostMapping(value="/searchDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleDeclareOrderDTO> searchDetail(String token, Long id) {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		try {
			if(id == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			dto = saleCreditOrderService.searchDetail(id);
			List<SaleCreditOrderItemDTO> itemList = saleCreditOrderService.searchItemsByOrderId(id);
			List<SaleCreditBillOrderModel> billList = saleCreditOrderService.searchBillOrderByOrderId(id,"");

			dto.setItemList(itemList);
			dto.setBillList(billList);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "确认收款页面获取收款账单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售预申报id", required = true)
	})
	@PostMapping(value="/searchBillOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleCreditBillOrderModel>> searchBillOrder(String token, Long id) {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		try {
			if(id == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List<SaleCreditBillOrderModel> billList = saleCreditOrderService.searchBillOrderByOrderId(id, DERP_ORDER.SALECREDITBILL_STATUS_001);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,billList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	@ApiOperation(value = "详情页面获取收款账单详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "收款单id", required = true)
	})
	@PostMapping(value="/searchBillItemDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleCreditBillOrderItemModel>> searchBillItemDetail(String token, Long id) {
		try {
			if(id == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			List<SaleCreditBillOrderItemModel> billItemList = saleCreditOrderService.searchBillItemsByOrderId(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,billItemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "赊销申请")
	@PostMapping(value="/applyCreditOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean applyCreditOrder(@RequestBody SaleCreditOrderForm form) {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getSaleOrderId()).addObject(form.getCreditAmount())
					.addObject(form.getPayableMarginAmount()).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form, dto);
			saleCreditOrderService.saveCreditOrder(dto, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "提交赊销单")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销单id", required = true)
	})
	@PostMapping(value="/submitCreditOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean submitCreditOrder(Long id, String token) {
		try {
			if(id == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleCreditOrderService.submitOrder(id, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "收到保证金")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销单id", required = true),
		@ApiImplicitParam(value = "receiveMarginDate", name="收保证金日期", required = true),
		@ApiImplicitParam(value = "actualMarginAmount", name="实收保证金", required = true),
		@ApiImplicitParam(value = "remark", name="备注", required = false)
	})
	@PostMapping(value="/updateMarginOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean updateMarginOrder(Long id,String receiveMarginDate, String actualMarginAmount,String remark,String token) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(receiveMarginDate).addObject(actualMarginAmount).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleCreditOrderService.updateMarginOrder(id,receiveMarginDate, actualMarginAmount,remark, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "确认放款")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销单id", required = true),
		@ApiImplicitParam(value = "loanDate", name="放款日期", required = true),
		@ApiImplicitParam(value = "valueDate", name="起息日期", required = true),
		@ApiImplicitParam(value = "sapienceLoanDate", name="卓普信放款日期", required = true),
		@ApiImplicitParam(value = "remark", name="备注", required = false)
	})
	@PostMapping(value="/confirmOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean confirmOrder(Long id,String loanDate, String valueDate,String remark,String token,String sapienceLoanDate) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(loanDate).addObject(valueDate).addObject(sapienceLoanDate).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleCreditOrderService.confirmOrder(id,loanDate, valueDate,remark, user, sapienceLoanDate);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "访问申请还款页面")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销单id", required = true)
	})
	@PostMapping(value="/getRepayDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleCreditOrderDTO> getRepayDetail(Long id,String token) {
		SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			dto = saleCreditOrderService.getRepayDetail(id);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "还款试算")
	@PostMapping(value="/calRepayAmount.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<SaleCreditBillOrderDTO> calRepayAmount(@RequestBody SaleCreditOrderForm form) {
		SaleCreditBillOrderDTO billDTO = new SaleCreditBillOrderDTO();
		try {
			SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getId()).addObject(form.getCreditAmount())
					.addObject(form.getValueDate()).addObject(form.getReceiveDate()).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			BeanUtils.copyProperties(form, dto);
			dto.setValueDate(TimeUtils.parseDay(form.getValueDate()));
			User user = ShiroUtils.getUserByToken(form.getToken());
			billDTO = saleCreditOrderService.calRepayAmount(dto,user,form.getReceiveDate(),form.getDiscountDelayAmount());

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,billDTO);
	}

	@ApiOperation(value = "申请还款")
	@PostMapping(value="/applyRefund.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<SaleCreditBillOrderDTO> applyRefund(@RequestBody SaleCreditOrderForm form) {
		SaleCreditBillOrderDTO billDTO = new SaleCreditBillOrderDTO();
		try {
			SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getId()).addObject(form.getCreditAmount())
					.addObject(form.getValueDate()).addObject(form.getReceiveDate()).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			BeanUtils.copyProperties(form, dto);
			dto.setValueDate(TimeUtils.parseDay(form.getValueDate()));
			User user = ShiroUtils.getUserByToken(form.getToken());
			billDTO = saleCreditOrderService.applyRefund(dto,user,form.getReceiveDate(),form.getDiscountDelayAmount(),form.getDiscountReason());

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,billDTO);
	}
	@ApiOperation(value = "提交还款")
	@PostMapping(value="/saveCreditBill.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveCreditBill(@RequestBody SaleCreditOrderForm form) {
		try {
			SaleCreditOrderDTO dto = new SaleCreditOrderDTO();
			boolean isEmpty = new EmptyCheckUtils().addObject(form.getId()).addObject(form.getCreditAmount())
					.addObject(form.getValueDate()).addObject(form.getReceiveDate()).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			BeanUtils.copyProperties(form, dto);
			dto.setValueDate(TimeUtils.parseDay(form.getValueDate()));
			User user = ShiroUtils.getUserByToken(form.getToken());
			saleCreditOrderService.saveCreditBill(dto,user,form.getReceiveDate(),form.getDiscountDelayAmount(),form.getDiscountReason());

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "确认还款")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销收款单id", required = true),
		@ApiImplicitParam(value = "accountDate", name="到账日期", required = true),
		@ApiImplicitParam(value = "remark", name="备注", required = false)
	})
	@PostMapping(value="/confirmCreditBill.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean confirmCreditBill(Long id,String accountDate,String remark,String token) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(accountDate).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleCreditOrderService.confirmCreditBill(id,accountDate,remark, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "删除赊销单")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "ids", name="赊销单id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/deleteCreditOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean deleteCreditOrder(String ids,String token) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(ids).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleCreditOrderService.deleteCreditOrder(ids,user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "导出结算单")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "token", name="令牌", required = true),
		@ApiImplicitParam(value = "id", name="赊销单id", required = true)
	})
	@GetMapping(value="/exportCreditOrder.asyn")
	public void exportCreditOrder(Long id,String token ,HttpServletResponse response, HttpServletRequest request) {
		OutputStream out = null;
		try {
			SaleCreditOrderExportDTO dto = saleCreditOrderService.exportCreditOrder(id);
			Workbook workbook = DownloadExcelUtil.exportTemplateExcel("SALECREDITEXPORT", dto) ;
			String fileName = dto.getPoNo()+"结算单" ;

			FileExportUtil.setHeader(response, request, fileName +".xlsx");
			out = response.getOutputStream();
			workbook.write(out);

		}catch(Exception e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@ApiOperation(value = "导出试算结果")
	@PostMapping(value="/exportCalResult.asyn")
	public void exportCalResult(String token ,String json,HttpServletResponse response, HttpServletRequest request) {
		OutputStream out = null;
		try {
			SaleCreditOrderExportDTO resultDTO = saleCreditOrderService.exportCalResult(json);
			Workbook workbook = DownloadExcelUtil.exportTemplateExcel("SALECREDITEXPORT", resultDTO) ;
			String fileName = resultDTO.getPoNo()+"试算结果";

			FileExportUtil.setHeader(response, request, fileName +".xlsx");
			out = response.getOutputStream();
			workbook.write(out);

		}catch(Exception e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
