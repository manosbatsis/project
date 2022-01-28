package com.topideal.order.web.bill;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderExportDTO;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.PlatformStatementOrderService;
import com.topideal.order.service.bill.ReceiveBillService;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台结算单
 **/
@Controller
@RequestMapping("/platformStatementOrder")
public class PlatformStatementOrderController {

	private static final Logger LOG = Logger.getLogger(PlatformStatementOrderController.class) ;
	
	private static final String[] KEYS = {"billCode", "month", "customerName", "typeLabel", "poNo", "barcode", "goodsName", "settlementNum", "settlementAmount", "settlementAmountRmb", "currency", "rate"} ;
	
	private static final String[] COLS = {"平台结算单号", "账单月份", "客户", "类型", "PO号", "商品条码", "商品名称", "结算数量", "结算金额(本币)", "结算金额(RMB)", "币种", "汇率"} ;
	
    @Autowired
    private PlatformStatementOrderService platformStatementOrderService;
    @Autowired
    private RMQProducer rocketMQProducer;
	@Autowired
	private FileTempService fileTempService;
	@Autowired
	private ReceiveBillService receiveBillService;

    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception{
        return "derp/bill/platform-statement-order-list";
    }

    /**
     * 获取分页数据
     * @param dto
     * */
    @RequestMapping("/listPlatformStatementOrder.asyn")
    @ResponseBody
    private ViewResponseBean listPlatformStatementOrder(PlatformStatementOrderDTO dto) {
        try{
            User user= ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto = platformStatementOrderService.listPlatformStatementOrder(dto);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }
    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Model model,Long id) throws Exception{
    	
    	PlatformStatementOrderDTO dto = platformStatementOrderService.getDetails(id);
    	
    	model.addAttribute("detail", dto);
    	
        return "derp/bill/platform-statement-order-details";
    }
    
    @RequestMapping("/listPlatformStatementItem.asyn")
    @ResponseBody
    private ViewResponseBean listPlatformStatementItem(PlatformStatementItemDTO dto) {

        try{       	
            // 响应结果集
        	dto = platformStatementOrderService.listPlatformStatementItem(dto);
        }catch(Exception e){
        	LOG.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }
	
    /**
     * 导出
     * @param response
     * @param request
     * @param dto
     * @throws Exception
     */
    @RequestMapping("/exportOrders.asyn")
	public void exportOrders(HttpServletResponse response, HttpServletRequest request,
			PlatformStatementOrderExportDTO dto) throws Exception {
    	
    	/*String sheetName = "平台结算单" ;
    	
    	User user = ShiroUtils.getUser();
    	dto.setMerchantId(user.getMerchantId());
    	
    	List<PlatformStatementOrderExportDTO> exportList = platformStatementOrderService.getExportOrders(dto) ;
    
    	SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLS, KEYS, exportList);
    	
    	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);*/
    }
    
    @RequestMapping("/flashPlatformStatementOrders.asyn")
	@ResponseBody
	private ViewResponseBean flashPlatformStatementOrders(PlatformStatementOrderDTO dto) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			
			if(StringUtils.isBlank(dto.getMonth())) {
				retMap.put("code", "01");
				retMap.put("message", "账单月份不能为空");
				return ResponseFactory.success(retMap);
			}
			
			if(StringUtils.isBlank(dto.getCustomerType())) {
				retMap.put("code", "01");
				retMap.put("message", "客户不能为空");
				return ResponseFactory.success(retMap);
			}
			
			User user=ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			body.put("month", dto.getMonth());
			body.put("customerType", dto.getCustomerType());
			body.put("billCode", dto.getBillCode() == null ? "" : dto.getBillCode());
			
			JSONObject json = JSONObject.fromObject(body);
			SendResult result =rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTopic(), MQOrderEnum.TIMER_PLATFORM_STATEMENT_ORDER.getTags());
			LOG.info(result);
			
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				retMap.put("code", "01");
				retMap.put("message", "刷新消息发送失败");
				return ResponseFactory.success(retMap);
			}
			
			retMap.put("code", "00");
			retMap.put("message", "成功");
			
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 校验应收单是否满足开票条件
	 * @param ids
	 */
	@RequestMapping("/validateInfo.asyn")
	@ResponseBody
	public ViewResponseBean validateInfo(String ids) {
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			Map<String, String> resultMap = platformStatementOrderService.validate(list);
			return ResponseFactory.success(resultMap);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}


	/**
	 * 开票
	 */
	@RequestMapping("/toInvoicePage.html")
	public String toInvoicePage(String ids, String tempId, String pageSource, Model model) throws Exception {
		User user = ShiroUtils.getUser();
		List list = StrUtils.parseIds(ids);
		List<String> codes = new ArrayList<>();
		Timestamp invoiceDate = TimeUtils.getNow(); //发票时间取开票时间
		//查询开票的平台结算单信息
		List<PlatformStatementOrderDTO> platformStatementOrderDTOS = platformStatementOrderService.listPlatformStatementOrderByIds(list);
		for (PlatformStatementOrderDTO dto : platformStatementOrderDTOS) {
			codes.add(dto.getBillCode());
		}
		FileTempDTO fileTempModel = fileTempService.searchById(Long.valueOf(tempId));
		MerchantInfoMongo merchantInfo = receiveBillService.getMerchantInfo(user.getMerchantId());
		CustomerInfoMogo customerInfo = receiveBillService.getCustomer(platformStatementOrderDTOS.get(0).getCustomerId());
		if (fileTempModel !=null && StringUtils.isNotBlank(fileTempModel.getToUrl())
				&& fileTempModel.getToUrl().contains("receive-bill-weiping-invoice-details")) {
			List<Map<String, Object>> items = platformStatementOrderService.listWPInvoiceItemInfos(list);
			model.addAttribute("itemLists", items);
		} else {
			List<Map<String, Object>> items = platformStatementOrderService.listInvoiceItemInfos(list);
			model.addAttribute("itemLists", items);
			if (items != null && items.size() > 1) {
				Map<String, Object> map = items.get(items.size() - 1);
				model.addAttribute("totalAllAmount", map.get("totalAllAmount"));
				model.addAttribute("totalAllNum", map.get("totalAllNum"));
			}
		}
		model.addAttribute("currency", platformStatementOrderDTOS.get(0).getCurrency());
		model.addAttribute("currencyLabel", platformStatementOrderDTOS.get(0).getCurrencyLabel());
		model.addAttribute("codes", StringUtils.join(codes.toArray(), ","));
		model.addAttribute("poNos", "");
		model.addAttribute("merchantInfo", merchantInfo);
		model.addAttribute("customerInfo", customerInfo);
		model.addAttribute("customerId", customerInfo.getCustomerid());
		model.addAttribute("pageSource", pageSource);
		model.addAttribute("ids", ids);
		model.addAttribute("tempId", tempId);
		model.addAttribute("fileTempCode", fileTempModel.getCode());
		model.addAttribute("invoiceDate", invoiceDate);
		model.addAttribute("invoiceSource", "2");
		return fileTempModel.getToUrl();
	}

	/**
	 * 生成应收单
	 */
	@RequestMapping("/saveToCReceiveBill.asyn")
	@ResponseBody
	public ViewResponseBean saveToCReceiveBill(String ids) throws Exception {
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				//输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			User user = ShiroUtils.getUser();
			Map<String, String> resultMap = platformStatementOrderService.saveToCReceiveBill(list, user);
			return ResponseFactory.success(resultMap);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}

}
