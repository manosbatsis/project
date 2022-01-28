package com.topideal.report.webapi.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.InventoryFallingPriceReservesService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.InventoryFallingPriceFlashForm;
import com.topideal.report.webapi.form.InventoryFallingPriceReservesForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 存货跌价准备控制器
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/report/inventoryFallingPriceReserves")
@Api(tags = "存货跌价准备报表")
public class APIInventoryFallingPriceReservesController {
	
	/**导出字段名*/
	private final static String[] exportColumns = { "公司","事业部", "仓库", "报表月份", "商品货号", "商品条码", "标准条码", "商品名称",
			"标准品牌", "母品牌", "二级分类", "生产日期", "失效日期", "首次上架日期", "生产批次号", "总效期(天)","库存状态", "总库存", "库存类型",
			"剩余失效(天)", "失效月份", "剩余效期占比(%)", "效期区间", "剩余效期占比(财务逻辑）",
			"2个月后剩余效期占比(%)", "2个月后剩余效期区间", "单价", "总金额", "跌价准备比例(%)", "计提总额","在途单号"};
	
	/**导出字段反射key,对应model字段名*/
	private final static String[] exportKeys = {"merchantName","buName","depotName", "reportMonth", "goodsNo", "barcode", "commbarcode", "goodsName",
			"brandParent", "superiorParentBrandName", "typeName", "productionDate", "overdueDate", "firstShelfDate", "batchNo", "totalDays","inverntoryStatusLabel", "surplusNum", "inverntoryTypeLabel",
            "surplusDays", "overdueMonth", "surplusProportion", "effectiveIntervalLabel", "financialSurplusProportionLabel",
            "twoMonthsSurplusProportion", "twoMonthsEffectiveIntervalLabel", "settlementPrice", "totalAmount", "depreciationReserveProportion", "totalProvision","noshelfCode"} ;

	@Autowired
	private InventoryFallingPriceReservesService inventoryFallingPriceReservesService ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;
	
	/**
	 * 获取分页信息
	 * @return
	 */
	@ApiOperation(value = "获取列表分页信息")   	
   	@PostMapping(value="listInventoryFallingPriceReserves.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<InventoryFallingPriceReservesDTO> listInventoryFallingPriceReserves(InventoryFallingPriceReservesForm form) {
		InventoryFallingPriceReservesDTO dto = new InventoryFallingPriceReservesDTO();
		try {
			BeanUtils.copyProperties(form, dto);
			User user=ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());			
			//如果月份为空，默认当前日期
			if(dto.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				dto.setReportMonth(month);
			}
			if(dto.getReportMonth().compareTo("2021-11")>=0&&StringUtils.isBlank(form.getBuIds())){
				List<Long> userBuList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(userBuList!=null&&userBuList.size()>0){
					String userBuStr = StringUtils.join(userBuList,",");
					dto.setBuIds(userBuStr);
				}else{
					dto.setList(new ArrayList());
					dto.setTotal(0);
					return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
				}
			}
			dto = inventoryFallingPriceReservesService.listInventoryFallingPriceReservesPage(dto);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 刷新仓库、月汇总报表
	 * */
	@ApiOperation(value = "刷新存货跌价报表")
   	@PostMapping(value="/flashInventoryFallingPriceReserves.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Map<String, Object>> flashInventoryFallingPriceReserves(InventoryFallingPriceFlashForm form) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{			
			User user=ShiroUtils.getUserByToken(form.getToken());
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			body.put("month", form.getReportMonth());
			body.put("depotIds", form.getDepotIds()==null?"":form.getDepotIds());
			body.put("buIds", form.getBuIds()==null?"":form.getBuIds());
			JSONObject json = JSONObject.fromObject(body);
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTopic(), MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTags());
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"刷新消息发送失败");//未知异常
			}
			retMap.put("code", "00");
			retMap.put("message", "成功");
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,retMap);
	}
	
	@ApiOperation(value = "获取导出数量")   	
   	@PostMapping(value="getCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Integer> getCount(InventoryFallingPriceReservesForm form) {
		Integer total = 0;
		try {
			InventoryFallingPriceReservesDTO dto = new InventoryFallingPriceReservesDTO();
			BeanUtils.copyProperties(form, dto);
			//如果月份为空，默认当前日期
			if(dto.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				dto.setReportMonth(month);
			}
			User user=ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			if(dto.getReportMonth().compareTo("2021-11")>=0&&StringUtils.isBlank(form.getBuIds())){
				List<Long> userBuList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(userBuList!=null&&userBuList.size()>0){
					String userBuStr = StringUtils.join(userBuList,",");
					dto.setBuIds(userBuStr);
				}else{

					return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
				}
			}
			// 响应结果集
			total = inventoryFallingPriceReservesService.getCount(dto);			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
   	@GetMapping(value="exportInventoryFallingPriceReserves.asyn")
	public void exportInventoryFallingPriceReserves(InventoryFallingPriceReservesForm form,HttpServletRequest request ,HttpServletResponse response) {
		try {
			InventoryFallingPriceReservesDTO dto = new InventoryFallingPriceReservesDTO();
			BeanUtils.copyProperties(form, dto);
			//如果月份为空，默认当前日期
			if(dto.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				dto.setReportMonth(month);
			}
			User user=ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			boolean buFlag = true;//事业部权限false-无 true-有
			if(dto.getReportMonth().compareTo("2021-11")>=0&&StringUtils.isBlank(form.getBuIds())){
				List<Long> userBuList = userBuRelMongoDao.getBuListByUser(user.getId());
				if(userBuList!=null&&userBuList.size()>0){
					String userBuStr = StringUtils.join(userBuList,",");
					dto.setBuIds(userBuStr);
				}else{
					buFlag = false;
				}
			}

			List<InventoryFallingPriceReservesDTO> exportList = new ArrayList<>();
			if(buFlag == true){
				exportList = inventoryFallingPriceReservesService.getExportList(dto);
			}

			String exportSheetName = "存货跌价准备报表" ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(exportSheetName, exportColumns, exportKeys, exportList) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, exportSheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
