package com.topideal.order.web.bill;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.bill.ReceiveBillVerificationDTO;
import com.topideal.entity.vo.bill.ReceivePaymentNotesModel;
import com.topideal.order.service.bill.ReceiveBillVerificationService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 收款核销跟踪
 * @Author: 杨创
 * @Date: 2020/07/27 14:53
 **/
@Controller
@RequestMapping("/receiveBillVerification")
public class ReceiveBillVerificationController {

    @Autowired
    private ReceiveBillVerificationService receiveBillVerificationService;
	@Autowired
    private RMQProducer rocketMQProducer;

    /**
     * 访问列表页面
     * @param model
     * */
    @RequestMapping("/toPage.html")
    public String toPage(Model model, String receiveCode) throws Exception{
    	model.addAttribute("receiveCode", receiveCode);
        return "derp/bill/receive-bill-verification-list";
    }

    /**
     * 获取分页数据
     * @param dto
     * */
    @RequestMapping("/listReceiveBillVerification.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBillVerification(ReceiveBillVerificationDTO dto) {
        try{
            User user= ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            dto=receiveBillVerificationService.listReceiveBillVerificationByPage(dto, user);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(dto);
    }
	@SuppressWarnings("unchecked")
	@RequestMapping("/toNotesPage.asyn")
	@ResponseBody
	public ViewResponseBean toNotesPage(String receiveCode) throws Exception{
		try{
			//查询所有商家		
			ReceivePaymentNotesModel notesModel =new ReceivePaymentNotesModel();
			notesModel.setReceiveCode(receiveCode);
			List<ReceivePaymentNotesModel> notesList = receiveBillVerificationService.getNotesList(notesModel);
			return ResponseFactory.success(notesList);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		
		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/saveNotes.asyn")
	@ResponseBody
	public ViewResponseBean saveNotes(String receiveCode,String notes) throws Exception{
		try{
			User user= ShiroUtils.getUser();
			//查询所有商家		
			ReceivePaymentNotesModel notesModel =new ReceivePaymentNotesModel();
			notesModel.setReceiveCode(receiveCode);
			boolean saveNotes = receiveBillVerificationService.saveNotes(user,receiveCode,notes);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		
		return ResponseFactory.success();
	}
	
	/**
	 * 收款核销跟踪导出
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/export.asyn")
	public void exportBillVerification(HttpServletResponse response, HttpServletRequest request,
			ReceiveBillVerificationDTO dto) throws Exception {
		User user= ShiroUtils.getUser();
        // 设置商家id
        dto.setMerchantId(user.getMerchantId());

		String sheetName = "收款核销跟踪";	
 		List<Map<String, Object>>  exportBillVerificationList = receiveBillVerificationService.exportBillVerification(dto, user);
 		for (Map<String, Object> map : exportBillVerificationList) {
 			String currency = (String) map.get("currency");
 			String accountPeriod = (String) map.get("account_period");
 			Timestamp billDate = (Timestamp) map.get("bill_date");
 			String billMonth="";
 			if (billDate!=null) billMonth=TimeUtils.format(billDate, "yyyy-MM");
 			
 			String currencylabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
 			String accountPeriodlabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_accountPeriodList, accountPeriod);
 			map.put("currency", currencylabel);
 			map.put("account_period", accountPeriodlabel);
 			map.put("bill_month", billMonth);
		}

 		String[] columns={"应收账单号","事业部","发票编号","是否开发票","账单月份","客户","应收金额","未核金额","结算币种","账单日期","开票日期","客户账期","账期逾期天数"};

     	
     	String[] keys={"receive_code","bu_name","invoice_no","invoice_status","bill_month","customer_name","receive_price","uncollected_price","currency","bill_date","invoice_date","account_period","account_overdue_days"};

     	SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, exportBillVerificationList);
     	//导出文件
     	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	/**
	 * 刷新
	 * @param receiveCode
	 * @param notes
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/flash.asyn")
	@ResponseBody
	public ViewResponseBean flash(String receiveCodes) throws Exception{
		try{
			User user= ShiroUtils.getUser();
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("receiveCodes", receiveCodes);
			body.put("notes", "页面刷新");
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return ResponseFactory.error(StateCodeEnum.ERROR_305);
			}
			
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
		
		return ResponseFactory.success();
	}
	
}
