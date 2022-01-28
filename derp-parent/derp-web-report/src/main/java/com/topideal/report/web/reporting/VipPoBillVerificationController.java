package com.topideal.report.web.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.reporting.*;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.VipPoBillVerificationService;
import com.topideal.report.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * vipPO账单核销
 * @author gy
 *
 */
@Controller
@RequestMapping("vipPoBillVerification")
public class VipPoBillVerificationController {
	
	@Autowired
	private VipPoBillVerificationService vipPoBillVerificationService ;
	
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;
	
	@Autowired
	private FileTaskService fileTaskService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(String poNo , Model model)throws Exception  {
		
		if(StringUtils.isNotBlank(poNo)) {
			model.addAttribute("poNo", poNo) ;
		}
		
		return "derp/reporting/vipPoBillVerification-list";
	}
	
	/**
	 * 访问PO列表页面
	 * */
	@RequestMapping("/toPoPage.html")
	public String toPoPage(Model model)throws Exception  {
		return "derp/reporting/vipPoBillVerification-po-list";
	}
	
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetail.html")
	public String toDetail( String poNo , String commbarcode , Long depotId,Model model)throws Exception  {
		
		User user=ShiroUtils.getUser();
		
		//获取详情实体
		VipPoBillVerificationModel vipPoBillVerificationModel = new VipPoBillVerificationModel() ;
		vipPoBillVerificationModel.setMerchantId(user.getMerchantId());
		vipPoBillVerificationModel.setPo(poNo);
		vipPoBillVerificationModel.setCommbarcode(commbarcode);
		vipPoBillVerificationModel.setDepotId(depotId);
		
		vipPoBillVerificationModel = vipPoBillVerificationService.searchByModel(vipPoBillVerificationModel) ;

		//商品名称
		String goodsName = vipPoBillVerificationService.getGoodsNameByCommbarcode(commbarcode, user.getMerchantId()) ;
		
		//获取客户名
		String customerName = vipPoBillVerificationService.getCustomerName(vipPoBillVerificationModel) ;  
		
		model.addAttribute("detail", vipPoBillVerificationModel) ;
		model.addAttribute("customerName", customerName) ;
		model.addAttribute("goodsName" , goodsName) ;
		
		return "derp/reporting/vipPoBillVerification-details";
	}
	
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/getDetailsByType.asyn")
	@ResponseBody
	public ViewResponseBean getDetailsByType( String poNo , String commbarcode ,String type, Model model)throws Exception  {
		
		User user=ShiroUtils.getUser();
		
		//获取唯品代销仓ID
		Long depotId = vipPoBillVerificationService.getVipDepotId() ;

		VipPoBillVerificationModel vipPoBillVerificationModel = new VipPoBillVerificationModel();
		vipPoBillVerificationModel.setMerchantId(user.getMerchantId());
		vipPoBillVerificationModel.setPo(poNo);
		vipPoBillVerificationModel.setCommbarcode(commbarcode);
		vipPoBillVerificationModel.setDepotId(depotId);

		//获取详情实体
		VipPoBillVerificationDTO dto = new VipPoBillVerificationDTO() ;
		dto.setMerchantId(user.getMerchantId());
		dto.setPo(poNo);
		dto.setCommbarcode(commbarcode);
		dto.setDepotId(depotId);

		vipPoBillVerificationModel = vipPoBillVerificationService.searchByModel(vipPoBillVerificationModel) ;
		
		//获取上架明细
		if("shelf".equals(type)) {
			List<VipShelfDetailsDTO> shelfDetails = vipPoBillVerificationService.getShelfDetails(dto) ;
			return ResponseFactory.success(shelfDetails) ;
		}
		
		
		//获取销售出库明细
		if("saleOut".equals(type)) {
			List<VipOutDepotDetailsDTO> saleOutDetails = vipPoBillVerificationService.getSaleOutDetails(dto) ;
			return ResponseFactory.success(saleOutDetails) ;
		}
		
		//获取销售退货明细
		if("returnOdepot".equals(type)) {
			List<VipSaleReturnOdepotDetailsDTO> saleReturnOdepotDetails = vipPoBillVerificationService.getSaleReturnOdepotDetails(dto) ;
			return ResponseFactory.success(saleReturnOdepotDetails) ;
		}
		
		//获取国检抽样明细
		if("nationalInspection".equals(type)) {
			List<VipAdjustmentInventoryDetailsDTO> nationalInspectionDetails = vipPoBillVerificationService.getNationalInspectionDetails(dto);
			return ResponseFactory.success(nationalInspectionDetails) ;
		}
		
		//获取唯品红冲明细
		if("hc".equals(type)) {
			List<VipAdjustmentInventoryDetailsDTO> hcDetails = vipPoBillVerificationService.getVipHcDetails(dto);
			return ResponseFactory.success(hcDetails) ;
		}
		
		//获取唯品报废明细
		if("scrap".equals(type)) {
			List<VipAdjustmentInventoryDetailsDTO> scrapDetails = vipPoBillVerificationService.getVipScrapDetails(dto);
			return ResponseFactory.success(scrapDetails) ;
		}
		
		//获取唯品盘点明细
		if("takeStock".equals(type)) {
			List<VipTakesStockResultsDetailsDTO> stockDetails = vipPoBillVerificationService.getVipTakesStockResultsDetails(dto);
			return ResponseFactory.success(stockDetails) ;
		}
		
		//获取唯品调拨明细
		if("transfer".equals(type)) {
			List<VipTransferDepotDetailsDTO> transferDetails = vipPoBillVerificationService.getVipTransferDetails(dto);
			return ResponseFactory.success(transferDetails) ;
		}
		
		return ResponseFactory.success() ;
	}
	
	
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listVipPoBillVeriList.asyn")
	@ResponseBody
	public ViewResponseBean listVipPoBillVeriList(VipPoBillVerificationDTO dto ) {
		User user=ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());
		
		try {
			dto = vipPoBillVerificationService.listVipPoBillVeriList(dto, user) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		return ResponseFactory.success(dto) ;
	}
	
	/**
	 * 获取PO列表分页数据
	 * */
	@RequestMapping("/listVipPoBillVeriPoList.asyn")
	@ResponseBody
	public ViewResponseBean listVipPoBillVeriPoList(VipPoBillVerificationDTO model ) {
		User user=ShiroUtils.getUser();
		model.setMerchantId(user.getMerchantId());
		
		try {
			model = vipPoBillVerificationService.listVipPoBillVeriPoList(model) ;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		return ResponseFactory.success(model) ;
	}
	
	/**
	 * 刷新仓库、月汇总报表
	 * */
	@RequestMapping("/flashVipPoBillVeris.asyn")
	@ResponseBody
	private ViewResponseBean flashVipPoBillVeris(VipPoBillVerificationModel model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			//body.put("syn",syn);//是否同步数据 true-是 ，其他-否
			body.put("fromPage", "true") ;
			
			if(model.getPo() != null) {
				body.put("poNo", model.getPo());
			}
			
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.VIP_PO_BILL_VERI.getTopic(), MQReportEnum.VIP_PO_BILL_VERI.getTags());
			System.out.println(result);
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
	 * 导出PO汇总表
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportMain.asyn")
	public void exportMain(VipPoBillVerificationDTO model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user=ShiroUtils.getUser();
		model.setMerchantId(user.getMerchantId());
		//设置页数为0，导出所有列表数据
		model.setPageSize(0);
		// 商品总表响应结果集
		VipPoBillVerificationDTO mainModel = vipPoBillVerificationService.listVipPoBillVeriList(model, user) ;
		List<VipPoBillVerificationModel> mainResult = mainModel.getList();
		
		String fileName = "唯品ByPo核销汇总表";
		String mainSheetName = "唯品by po核销总表" ;
		String[] mainKey = { "merchantName" , "buName", "po" ,"customerName", "poDate" , "commbarcode", "goodsName", "brandParent", "superiorParentBrand", "currency", "salePrice", "unsettledAccount", "inventoryAmount",
				"shelfTotalAccount","shelfTotalAmount"  , "shelfDamagedAccount", "firstShelfDate" , "billTotalAccount" , "billRecentDate" , "outDepotTotalAccont" ,
				"nationalInspectionSampleAccount" , "saleReturnAccount" , "vipHcAccount" , "inventorySurplusAccount" , "inventoryDeficientAccount" ,"scrapAccount" , "transferInAccount", "transferOutAccount", "days" , "modifyDate"} ;
		String[] mainColumns = { "商家", "事业部", "PO号","客户名称", "PO时间", "标准条码", "商品名称", "标准品牌", "母品牌", "销售币种", "销售单价", "库存量" ,"库存金额",
				"上架总量", "上架总金额","上架残损量", "首次上架时间", "账单总量", "最近账单时间" ,"销售出库总量" ,"国检抽样" ,"销售退数量" ,"唯品红冲数量" ,"盘盈数量" , "盘亏数量" ,"唯品报废数量" , "调拨入库", "调拨出库", "天数" ,"更新时间"};
	
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, mainResult);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}
	
	/**
	 * 导出PO明细表
	 * */
	@RequestMapping("/exportDetails.asyn")
	@ResponseBody
	private ViewResponseBean exportDetails(VipPoBillVerificationModel model, HttpServletRequest request ,HttpServletResponse response) throws Exception {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "成功");
		try {
            User user = ShiroUtils.getUser();

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_VIPHX);//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表VIPHXMXB-唯品核销报表
            taskModel.setTaskName("唯品PO核销明细表");
            taskModel.setMerchantId(user.getMerchantId());
            taskModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            taskModel.setRemark("唯品PO核销明细表");
            taskModel.setUsername(user.getName());

            JSONObject paramJson = new JSONObject();
            paramJson.put("po", model.getPo());
            paramJson.put("days", model.getDays());
            paramJson.put("commbarcode", model.getCommbarcode());
            paramJson.put("status", model.getStatus());
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("merchantName", user.getMerchantName());
            paramJson.put("buId", model.getBuId());
            paramJson.put("customerId", model.getCustomerId());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);


        } catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "网络故障");
			e.printStackTrace();
			
		}
		
		return ResponseFactory.success(retMap);
        
	}
	
	/**
	 * 获取未结算量
	 */
	@RequestMapping("/countUnsettledAccount.asyn")
	@ResponseBody
	public ViewResponseBean countUnsettledAccount(VipPoBillVerificationModel model) {
		User user = ShiroUtils.getUser() ; 
		
		model.setMerchantId(user.getMerchantId());
		
		Integer unsettledAccount = 0 ;
		
		try {
			unsettledAccount = vipPoBillVerificationService.countUnsettledAccount(model) ;
		} catch (Exception e) {
			ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		} 
		
		return ResponseFactory.success(unsettledAccount) ;
	}
	
	/**
	 * 修改是否完结状态
	 */
	@RequestMapping("/changeStatus.asyn")
	@ResponseBody
	public ViewResponseBean changeStatus(VipPoBillVerificationModel model) {
		User user = ShiroUtils.getUser() ; 
		
		model.setMerchantId(user.getMerchantId());
		model.setOperator(user.getName());
		model.setOperatorId(user.getId());
		
		int rows = 0 ;
		
		try {
			rows = vipPoBillVerificationService.modifyStatus(model) ;
		} catch (Exception e) {
			ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		} 
		
		if(rows > 0) {
			return ResponseFactory.success() ;
		}else {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
	}
	
	/**
	 * 获取数据截止时间
	 */
	@RequestMapping("/getDataTime.asyn")
	@ResponseBody
	public ViewResponseBean getDataTime() {
		User user = ShiroUtils.getUser() ; 
		Map<String, Object> resultMap = null ;
		
		try {
			resultMap = vipPoBillVerificationService.getDataTime(user.getMerchantId()) ;
		} catch (Exception e) {
			ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		} 
		
		return ResponseFactory.success(resultMap) ;
	}
}
