package com.topideal.inventory.webapi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.dto.BuInventoryItemDTO;
import com.topideal.entity.vo.BuInventoryModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.BuInventoryService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.BuInventoryResponseDTO;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.DepotMerchantRelMongo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;


/**
 * 事业部库存表
 */
@RestController
@RequestMapping("/webapi/inventory/buInventory")
@Api(tags = " 事业部库存")
public class APIBuInventoryController {

	//  事业部库存表service
	@Autowired
	private BuInventoryService buInventoryService;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
    @Autowired
    private MonthlyAccountDao monthlyAccountDao;
    @Autowired
    private DepotMerchantRelMongoDao depotMerchantRelMongoDao;

	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部id")
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean toPage(String token,Long buId)throws Exception  {
		try {
			User user= ShiroUtils.getUserByToken(token);
			BuInventoryResponseDTO  responseDTO=new BuInventoryResponseDTO();
			responseDTO.setBuId(buId);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		

	}

	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "展开")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "id")
	})
	@PostMapping(value="/getBuInventoryItem.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<BuInventoryItemDTO>> develop(String token,Long id)throws Exception  {
		try {
			BuInventoryItemDTO  dto=new BuInventoryItemDTO();
			dto.setBuInventoryId(id);
			List<BuInventoryItemDTO> buInventoryItem = buInventoryService.getBuInventoryItem(dto);	
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buInventoryItem);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		

	}
	

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "month", value = "月份"),
			@ApiImplicitParam(name = "brandId", value = "品牌id"),
			@ApiImplicitParam(name = "barcode", value = "条码"),
			@ApiImplicitParam(name = "goodsNo", value = "商品货号"),
			@ApiImplicitParam(name = "goodsName", value = "商品名称")
	})
	@PostMapping(value="/listBuInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BuInventoryDTO> listBuInventory(String token,Integer begin,Integer pageSize,Long depotId,String month,Long buId,
			Long brandId,String barcode,String goodsNo,String goodsName) {
		try{
			BuInventoryDTO dto=new BuInventoryDTO();
			// 响应结果集
			User user= ShiroUtils.getUserByToken(token);
			dto.setMerchantId(user.getMerchantId());
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
			dto.setDepotId(depotId);
			dto.setMonth(month);
			dto.setBuId(buId);
			dto.setBrandId(brandId);
			dto.setBarcode(barcode);
			dto.setGoodsNo(goodsNo);
			dto.setGoodsName(goodsName);						
			dto = buInventoryService.listBuInventory(user,dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	

	
	

	/**
	 * 导出事业部库存明细
	 * */
	@ApiOperation(value = "导出")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "depotId", value = "仓库id"),
		@ApiImplicitParam(name = "month", value = "月份"),
		@ApiImplicitParam(name = "brandId", value = "品牌id"),
		@ApiImplicitParam(name = "barcode", value = "条码"),
		@ApiImplicitParam(name = "goodsNo", value = "商品货号"),
		@ApiImplicitParam(name = "goodsName", value = "商品名称")
	})
	@GetMapping(value="/exportBuInventory.asyn")
	private void exportBuInventory(HttpSession session, HttpServletResponse response, HttpServletRequest request, 
			Long depotId,String month,Long buId,Long brandId,String barcode,String goodsNo,String goodsName,String token){
		try {
			// 响应结果集
			User user= ShiroUtils.getUserByToken(token);
			String sheetName = "事业部库存";
        	//根据勾选的获取信息 
			BuInventoryDTO model=new  BuInventoryDTO();
    		model.setMerchantId(user.getMerchantId());
    		model.setDepotId(depotId);
    		model.setMonth(month);
    		model.setBuId(buId);
    		model.setBrandId(brandId);
    		model.setBarcode(barcode);
    		model.setGoodsNo(goodsNo);
    		model.setGoodsName(goodsName);
	
    		List<ExportExcelSheet> sheetList=new ArrayList<ExportExcelSheet>();
    		List<Map<String, Object>>  exportBuInventoryListMap = buInventoryService.exportBuInventory(user,model);
        	String[] columns={"事业部","公司","仓库名称","品牌","商品货号","商品名称","商品条形码","好品数量","坏品数量","库存数量","理货单位","月份","库存日期"};
        	String[] keys={"bu_name","merchant_name","depot_name","brand_name","goods_no","goods_name","barcode","ok_num","worn_num","surplus_num","unit","month","create_date"};
        	ExportExcelSheet sheet1=new ExportExcelSheet();
        	sheet1.setKeys(keys);
        	sheet1.setColums(columns);
        	sheet1.setResultList(exportBuInventoryListMap);
        	sheet1.setSheetNames(sheetName);
        	sheetList.add(sheet1);
    		
    		List<Map<String, Object>>  exportBuInventoryItemListMap = buInventoryService.exportBuInventoryItem(user,model);
    		for (Map<String, Object> map : exportBuInventoryItemListMap) {
    			String effective_interval = (String) map.get("effective_interval");
    			String type = (String) map.get("type");
    			type = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    			effective_interval = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_effectiveIntervalList, effective_interval);
    			map.put("type", type);
    			map.put("effective_interval", effective_interval);
			}
    		String[] columns2={"事业部","公司","仓库名称","商品货号","商品名称","批次号","生产日期","失效日期","效期区间","好坏品类型","库存数量","首次上架日期"};
        	String[] keys2={"bu_name","merchant_name","depot_name","goods_no","goods_name","batch_no","production_date","overdue_date","effective_interval","type","num","first_shelf_date"};

        	String sheetName2 = "事业部库存批次明细表";
        	ExportExcelSheet sheet2=new ExportExcelSheet();
        	sheet2.setKeys(keys2);
        	sheet2.setColums(columns2);
        	sheet2.setResultList(exportBuInventoryItemListMap);
        	sheet2.setSheetNames(sheetName2);
        	sheetList.add(sheet2);
        	SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
        	//导出文件
        	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
	}
	
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "month", value = "月份"),
			@ApiImplicitParam(name = "buId", value = "事业部id")
	})
	@PostMapping(value="/flashBuInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	private ResponseBean flashBuInventory(String token,Long depotId,String month,Long buId){
		String massage="";
		try {
			//根据勾选的获取信息
			User user= ShiroUtils.getUserByToken(token);
			Map<String, Object> params= new HashMap<>();
			params.put("merchantId", user.getMerchantId());
			if (depotId!=null) {
				params.put("depotId", depotId);
			}
			List<DepotMerchantRelMongo> depotMerchantRelList = depotMerchantRelMongoDao.findAll(params);
			if (depotMerchantRelList==null||depotMerchantRelList.size()==0) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"商家仓库关系表无对应的仓库");//未知异常
			}
			
			
			for (DepotMerchantRelMongo depotMerchantRelMongo : depotMerchantRelList) {
				JSONObject jsonMQ=new JSONObject();
	    		jsonMQ.put("merchantId",user.getMerchantId());
	    		jsonMQ.put("depotId", depotMerchantRelMongo.getDepotId());   		
	    		jsonMQ.put("month", month);
	    		if (buId!=null) {
	    			jsonMQ.put("buId", buId);
				}
	    		MonthlyAccountModel accountModel = new MonthlyAccountModel();
		        accountModel.setMerchantId(user.getMerchantId());
		        accountModel.setDepotId(depotMerchantRelMongo.getDepotId());
		        accountModel.setSettlementMonth(month);
		        accountModel = monthlyAccountDao.searchByModel(accountModel);
				// 状态：1未转结 2 已转结
		        if (accountModel != null && accountModel.getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)) {
		            // 计算结转时间跟当前时间相差天数
		            int dayNum = TimeUtils.daysBetween(accountModel.getSettlementDate(), new Date());
		            if (dayNum > 1&&depotMerchantRelList.size()==1) {
		            	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),"商家ID:" + user.getMerchantId() + ",仓库Id：" + depotId + "月份：" + month + "已结转");//未知异常
		            }else if(dayNum > 1&&depotMerchantRelList.size()>1) {
		            	massage=massage+depotMerchantRelMongo.getDepotId()+",";
						continue;
					}
		        }	    		   		
	    		rocketMQProducer.send(jsonMQ.toString(), MQInventoryEnum.INVENTORY_BU_INVENTORY.getTopic(),MQInventoryEnum.INVENTORY_BU_INVENTORY.getTags());
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
			
		}
	}
	
	

}
