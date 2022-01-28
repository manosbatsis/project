package com.topideal.order.webapi.bill;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceivePaymentNotesModel;
import com.topideal.order.service.bill.ReceiveBillVerificationService;
import com.topideal.order.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * @Description: 收款核销跟踪
 * @Author: 杨创
 * @Date: 2020/07/27 14:53
 **/
@RestController
@RequestMapping("/webapi/order/receiveBillVerification")
@Api(tags = "收款核销跟踪")
public class APIReceiveBillVerificationController {

    @Autowired
    private ReceiveBillVerificationService receiveBillVerificationService;
	@Autowired
    private RMQProducer rocketMQProducer;

    /**
     * 访问列表页面
     * */
/*    @ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "receiveCode", value = "receiveCode")
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean toPage(Model model, String receiveCode) throws Exception{
    	try {
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,receiveCode);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

    }*/


    @ApiOperation(value = " 获取分页数据")
   	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
		@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
		@ApiImplicitParam(name = "receiveCode", value = "应收账单号"),
		@ApiImplicitParam(name = "customerId", value = "客户id"),
		@ApiImplicitParam(name = "billMonth", value = "账单月份"),
		@ApiImplicitParam(name = "overdueStatus", value = "账期逾期"),
		@ApiImplicitParam(name = "buId", value = "事业部id"),
		@ApiImplicitParam(name = "billStatus", value = "核销状态")
	})
   	@PostMapping(value="/listReceiveBillVerification.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean listReceiveBillVerification(String token,Integer begin,Integer pageSize,
    		String receiveCode,Long customerId,String creditMonth,String overdueStatus,Long buId, String billType, String billStatus) {
        try{
            User user= ShiroUtils.getUserByToken(token);           
            ReceiveBillVerificationDTO dto=new ReceiveBillVerificationDTO();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            dto.setReceiveCode(receiveCode);
            dto.setCustomerId(customerId);
            dto.setCreditMonth(creditMonth);
            dto.setOverdueStatus(overdueStatus);
            dto.setBuId(buId);
            dto.setBillType(billType);
            dto.setBillStatus(billStatus);

            // 响应结果集
            dto=receiveBillVerificationService.listReceiveBillVerificationByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

	@ApiOperation(value = "查询回款备注")
   	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "receiveCode", value = "应收账单号")
   	})
   	@PostMapping(value="/toNotesPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toNotesPage(String receiveCode) throws Exception{
		try{
			//查询所有商家		
			ReceivePaymentNotesModel notesModel =new ReceivePaymentNotesModel();
			notesModel.setReceiveCode(receiveCode);
			List<ReceivePaymentNotesModel> notesList = receiveBillVerificationService.getNotesList(notesModel);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,notesList);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
		
		
	}

	@ApiOperation(value = "保存备注")
   	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "receiveCode", value = "应收账单号"),
		@ApiImplicitParam(name = "notes", value = "备注内容")
   	})
   	@PostMapping(value="/saveNotes.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveNotes(String token,String receiveCode,String notes) throws Exception{
		try{
			User user= ShiroUtils.getUserByToken(token);
			//查询所有商家		
			ReceivePaymentNotesModel notesModel =new ReceivePaymentNotesModel();
			notesModel.setReceiveCode(receiveCode);
			boolean saveNotes = receiveBillVerificationService.saveNotes(user,receiveCode,notes);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saveNotes);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	/**
	 * 收款核销跟踪导出
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "receiveCode", value = "应收账单号"),
		@ApiImplicitParam(name = "customerId", value = "客户id"),
		@ApiImplicitParam(name = "billMonth", value = "账单月份"),
		@ApiImplicitParam(name = "overdueStatus", value = "账期逾期"),
		@ApiImplicitParam(name = "buId", value = "事业部id"),
		@ApiImplicitParam(name = "billStatus", value = "核销状态")
	})
	@GetMapping(value="/export.asyn")
	public void exportBillVerification(HttpServletResponse response, HttpServletRequest request,
			String token,String receiveCode,Long customerId,String creditMonth,String overdueStatus,Long buId, String billStatus) throws Exception {
	
		try{
			User user= ShiroUtils.getUserByToken(token);
	        // 设置商家id
			ReceiveBillVerificationDTO dto=new ReceiveBillVerificationDTO();
	        dto.setMerchantId(user.getMerchantId());
	        dto.setReceiveCode(receiveCode);
	        dto.setCustomerId(customerId);
	        dto.setCreditMonth(creditMonth);
	        dto.setOverdueStatus(overdueStatus);
	        dto.setBillStatus(billStatus);
	        dto.setBuId(buId);
			String sheetName = "收款核销跟踪";	
	 		List<Map<String, Object>>  exportBillVerificationList = receiveBillVerificationService.exportBillVerification(dto, user);
	 		for (Map<String, Object> map : exportBillVerificationList) {
	 			String billType = (String) map.get("bill_type");
	 			String currency = (String) map.get("currency");
	 			String accountPeriod = (String) map.get("account_period");
	 			String invoiceStatus = (String) map.get("invoice_status");
	 			Timestamp billDate = (Timestamp) map.get("bill_date");
	 			String billMonthQurey="";
	 			if (billDate!=null) creditMonth=TimeUtils.format(billDate, "yyyy-MM");
	 			
	 			String currencylabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
	 			String billTypelabel = DERP_ORDER.getLabelByKey(DERP_ORDER.billMonthlySnapshot_billTypeList, billType);
	 			String accountPeriodlabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_accountPeriodList, accountPeriod);
	 			String invoiceStatuslabel = DERP_SYS.getLabelByKey(DERP_ORDER.receiveBillVerification_invoiceStatusList, invoiceStatus);

	 			map.put("currency", currencylabel);
	 			map.put("account_period", accountPeriodlabel);
	 			map.put("invoiceStatus", invoiceStatuslabel);
	 			map.put("bill_month", billMonthQurey);
	 			map.put("bill_type", billTypelabel);

			}
	 		String[] columns={"应收账单号", "账单类型","事业部","发票编号","是否开发票", "入账月份","客户","应收金额","未核金额","结算币种","账单日期","开票日期","客户账期","账期逾期天数"};

	     	
	     	String[] keys={"receive_code", "bill_type","bu_name","invoice_no","invoice_status","credit_month","customer_name","receive_price","uncollected_price","currency","bill_date","invoice_date","account_period","account_overdue_days"};

	     	SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, exportBillVerificationList);
	     	//导出文件
	     	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        }catch(Exception e){
        	e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
		
	}
	
	/**
	 * 刷新
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@ApiOperation(value = "刷新")
	@ApiImplicitParams({
   		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "receiveCodes", value = "应收账单号多个英文逗号隔开")
	})
	@PostMapping(value="/flash.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean flash(String token, String receiveCodes) throws Exception{
		try{
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("receiveCodes", receiveCodes);
			body.put("notes", "页面刷新");
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
}
