package com.topideal.report.service.automatic.impl;

import com.topideal.common.constant.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InventoryDetailsDao;
import com.topideal.dao.order.*;
import com.topideal.dao.storage.TakesStockResultItemDao;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.dto.DepotAutomaticCLDTO;
import com.topideal.entity.dto.DepotAutomaticGssDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.entity.vo.inventory.InventoryDetailsModel;
import com.topideal.entity.vo.order.*;
import com.topideal.entity.vo.storage.TakesStockResultItemModel;
import com.topideal.entity.vo.storage.TakesStockResultsModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.report.service.automatic.DepotAutomaticFileTaskService;
import com.topideal.report.tools.DownloadExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepotAutomaticFileTaskServiceImpl implements DepotAutomaticFileTaskService{
	
	@Autowired
	private OrderDao orderDao ;
	
	@Autowired
	private OrderItemDao orderItemDao ;
	
	@Autowired
	private OrderReturnIdepotDao orderReturnIdepotDao ;
	
	@Autowired
	private OrderReturnIdepotItemDao orderReturnIdepotItemDao ;
	
	@Autowired
	private TransferOrderDao transferOrderDao;
	
	@Autowired
	private TransferInDepotDao transferInDepotDao ;
	
	@Autowired
	private TransferOutDepotDao transferOutDepotDao ;
	
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao ;
	
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao ;
	
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao ;
	
	@Autowired
	private TakesStockResultsDao takesStockResultsDao ;
	
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao ;
	
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao ;
	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	@Autowired
	private DepotInfoDao depotInfoDao ;
	
	private static final String[] CL_SOURCE_TYPE = {"交易出库", "退货入库", "采购入库", "盘点出库", "盘点入库", "普通出库"} ;
	
	private static final String[] GSS_SOURCE_TYPE = {"3", "4", "5", "6", "13", "14", "15", "16", "31", 
												"32", "62", "63", "66", "67", "80", "81", 
												"84", "85", "100", "104"} ;
	
	private static final String[] GSS_SELLS_TYPE = {"3", "4", "5", "6"} ;
	
	private static final String[] GSS_EXCHANGE_TYPE = {"13", "14", "15", "16", "31", 
												"32", "62", "63", "66", "67", "80", "81", 
												"84", "85", "100", "104"} ;
	
	private static final String[] GSS_INCREASING_GOOD = {"13", "67", "80", "100"} ;
	
	private static final String[] GSS_INCREASING_WORN = {"14", "66", "81", "31"} ;
	
	private static final String[] GSS_DIMINISHING_GOOD = {"15", "63", "84", "104"} ;
	
	private static final String[] GSS_DIMINISHING_WORN = {"16", "62", "85", "32"} ;
	
	@Override
	public String createVeriExcel(AutomaticCheckTaskModel model, String basePath) throws Exception {
		
		//获取源文件
		List<Map<String, String>> sheetList = ExcelUtilXlsx.parseSheetOne(model.getSourcePath());
		
		String fileName = null ;
		
		if(DERP_REPORT.AUTOMATICCHECKTASK_DATASOURCE_2.equals(model.getDataSource())) {
			fileName = CLDepotVeri(model, sheetList, basePath) ;
		}else if(DERP_REPORT.AUTOMATICCHECKTASK_DATASOURCE_1.equals(model.getDataSource())) {
			fileName = GssDepotVeri(model, sheetList, basePath) ;
		}
		
		return fileName;
	}
	
	/**
	 * Gss报表校验
	 * @param model
	 * @param data
	 * @param basePath
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private String GssDepotVeri(AutomaticCheckTaskModel model, List<Map<String, String>> sheetList, String basePath) throws Exception {
		
		List<Long> idsList = new ArrayList<Long>() ;
		idsList.add(model.getMerchantId()) ;
		
		//查询商家及代理商家的所有商品货品条码、货号、标准条码
		Map<String, Object> merchandiseMaps = new HashMap<String, Object>();
		merchandiseMaps.put("list", idsList);
		List<Map<String,Object>> allMerchandiseList = merchandiseInfoDao.getAllMerchandiseByMerchantId(merchandiseMaps);
		if(allMerchandiseList==null||allMerchandiseList.size()<=0) {
			throw new RuntimeException(model.getMerchantName() + "商家商品未空") ;
		}
		
		merchandiseMaps.clear(); 
		for (Map<String, Object> map : allMerchandiseList) {
			merchandiseMaps.put(String.valueOf(map.get("goods_no")), map) ;
		}
		
		Map<String,DepotAutomaticGssDTO> cacheMap = new HashMap<String, DepotAutomaticGssDTO>() ;
		Map<String, List<DepotAutomaticGssDTO>> exportMap = new HashMap<String, List<DepotAutomaticGssDTO>>() ;
		
		//根据页面选择仓库判断是否海外仓
		Long depotId = model.getOutDepotId();
		DepotInfoModel selectDepot = depotInfoDao.searchById(depotId);
		
		for (int j = 1; j <= sheetList.size(); j++) {
			Map<String, String> map = sheetList.get(j - 1) ;
			DepotAutomaticGssDTO dto = excelMapExchangeGssDto(map);
			
			String msg = checkDtoIsEmpty(dto) ;
			
			if(StringUtils.isNotBlank(msg)) {
				setExceptionMsg(dto, exportMap, "是", msg);
				continue ;
			}
			
			Map<String, Object> merchandiseMap = (Map<String , Object>)merchandiseMaps.get(dto.getGoodsNo()) ;
			if(merchandiseMap == null) {
				setExceptionMsg(dto, exportMap, "是", "该商家下无该货号商品");
				continue ;
			}
			
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(selectDepot.getDepotType())) {
				
				String unit = null ;
				if(StringUtils.isBlank(dto.getUnit())) {
					unit = DERP.INVENTORY_UNIT_2 ;
				}
				if("箱".equals(dto.getUnit())) {
					unit = DERP.INVENTORY_UNIT_1 ;
				}else if(dto.getUnit().contains("托")) {
					unit = DERP.INVENTORY_UNIT_0 ;
				}
				
				try {
					Integer boxToUnit = merchandiseMap.get("box_to_unit") == null ? 0 : (Integer)merchandiseMap.get("box_to_unit") ;
					Integer torrToUnit = merchandiseMap.get("torr_to_unit") == null ? 0 : (Integer)merchandiseMap.get("torr_to_unit") ;
					Integer account = changeUnit(unit, new BigDecimal(dto.getAccount()), boxToUnit, torrToUnit) ;
					dto.setAccount(account);
					dto.setUnit("件");
				} catch (RuntimeException e) {
					setExceptionMsg(dto, exportMap, "是", "仓库为：海外仓，货号："+merchandiseMap.get("goods_no")+" 箱托转换数据未维护;");
					continue ;
				}
			}
			
			//合并相同记录
			String tradeTimeStr = TimeUtils.format(dto.getTradeCreateTime(), "yyyy-MM-dd") ;
			String key = dto.getDepotName() + "_" + dto.getGoodsNo() + "_" 
					+ dto.getSourceType() + "_" + dto.getSourceCode() + "_" 
					+ dto.getBatchNo() + "_" + tradeTimeStr;
			
			DepotAutomaticGssDTO tempDTO = cacheMap.get(key);
			if(tempDTO != null) {
				int tempAccount = tempDTO.getAccount() + dto.getAccount() ; 
				dto.setAccount(tempAccount);
			}
			
			cacheMap.put(key, dto) ;
		}
		
		for(DepotAutomaticGssDTO dto : cacheMap.values()) {
			/**
			 * 1）、检查该行数据的业务类型是否在（3 4 5 6 13 14 15 16 31 32 62 63 66 67 80 81 84 85 100 104）内，若不在标识该行数据“不在核对范围内”
			 */
			String sourceType = dto.getSourceType() ; 
			if(!Arrays.asList(GSS_SOURCE_TYPE).contains(sourceType)) {
				setExceptionMsg(dto, exportMap, "是", "不在核对范围内");
				continue ;
			}
			
			/**
			 * 2)、检查excel的交易创建时间是否在核对日期范围内，若不在标识该行数据“日期不在核对范围内”
			 */
			Timestamp checkStartDate = model.getCheckStartDate();
			Timestamp checkEndDate = model.getCheckEndDate();
			
			Timestamp tradeCreateTime = dto.getTradeCreateTime();
			
			if(TimeUtils.daysBetween(tradeCreateTime, checkStartDate) > 0
					|| TimeUtils.daysBetween(checkEndDate, tradeCreateTime) > 0) {
				setExceptionMsg(dto, exportMap, "是", "交易创建时间不在核对范围内");
				continue ;
			}
			
			/**
			 * 3）、若业务类型为3或4，使用业务单据号到ERP商品收发明细查询是否存在对应记录（条件excel-业务单据号=商品收发明细-业务单据号，限定当前商家），
			 * 		若不存在标识该excel数据“系统无单据”；
			 * 		若存在，则核对仓库名称、货号、变更日期（与excel-交易创建时间对比，核对日期，不对时间）、批次、数量是否一致，
			 * 		检查ERP操作类型是否为调增，检查库存类型是否正确（3：好品；4：坏品），
			 * 		若存在差异则标识该excel数据”XX不一致；XX不一致“，存在多个差异时以分号隔开；
			 * 		若无差异则标识该数据核验通过。
			 * 4）、若业务类型为5或6，使用业务单据号到ERP商品收发明细查询是否存在对应记录（条件excel-业务单据号=商品收发明细-业务单据号，限定当前商家），
			 * 		若不存在标识该excel数据“系统无单据”；
			 * 		若存在，则核对仓库名称、货号、变更日期（与excel-交易创建时间对比，核对日期，不对时间）、批次、数量是否一致，
			 * 		检查ERP操作类型是否为调减，检查库存类型是否正确（5：好品；6：坏品），
			 * 		若存在差异则标识该excel数据”XX不一致；XX不一致“，存在多个差异时以分号隔开；
			 * 		若无差异则标识该数据核验通过。
			 */
			if(Arrays.asList(GSS_SELLS_TYPE).contains(sourceType)) {
				veriGssSells(model, dto, exportMap, merchandiseMaps) ;
			}
			/**
			 * 5）、若业务类型为13/14/15/16，使用业务单据号到ERP商品收发明细查询是否存在对应记录（条件excel-业务单据号=商品收发明细-来源单据号，限定当前商家），
			 * 	若不存在标识该excel数据“系统无单据”；
			 * 	若存在，则核对仓库名称、货号、变更日期（与excel-交易创建时间对比，核对日期，不对时间）、批次、数量是否一致，
			 * 	检查ERP操作类型是否正确(13/14：调增；15/16：调减)，
			 * 	检查库存类型是否正确(13/15：好品；14/16：坏品)，若存在差异则标识该excel数据”XX不一致；
			 * 	XX不一致“，存在多个差异时以分号隔开；若无差异则标识该数据核验通过。
			 * 6）、若业务类型为62/63/66/67/80/81/84/85,使用业务单据号到ERP商品收发明细查询是否存在对应记录（条件excel-业务单据号=商品收发明细-来源单据号，限定当前商家），
			 * 	若不存在标识该excel数据“系统无单据”；
			 * 	若存在，则核对仓库名称、货号、变更日期（与excel-交易创建时间对比，核对日期，不对时间）、批次、数量是否一致，
			 * 	检查ERP操作类型是否正确(66/67/81/82：调增；62/63/84/85：调减)，
			 * 	检查库存类型是否正确(63/67/80/84：好品；62/66/81/85：坏品)，
			 * 	若存在差异则标识该excel数据”XX不一致；XX不一致“，存在多个差异时以分号隔开；
			 * 	若无差异则标识该数据核验通过。
			 * 7）、若业务类型为100/104,使用业务单据号到ERP商品收发明细查询是否存在对应记录（条件excel-业务单据号=商品收发明细-来源单据号，限定当前商家），
			 * 	若不存在标识该excel数据“系统无单据”；
			 * 	若存在，则核对仓库名称、货号、变更日期（与excel-交易创建时间对比，核对日期，不对时间）、批次、数量是否一致，检查ERP操作类型是否正确(100:调增；104：调减)，
			 * 	检查库存类型是否正确(100/104：好品；)，若存在差异则标识该excel数据”XX不一致；XX不一致“，存在多个差异时以分号隔开；
			 * 	若无差异则标识该数据核验通过。
			 */
			else if(Arrays.asList(GSS_EXCHANGE_TYPE).contains(sourceType)) {
				veriGssExchange(model, dto, exportMap, merchandiseMaps) ;
			}
		}
		
		List<DepotAutomaticGssDTO> dtoList = new ArrayList<DepotAutomaticGssDTO>() ;
		
		for(List<DepotAutomaticGssDTO> tempList : exportMap.values()) {
			dtoList.addAll(tempList) ;
		}
		
		String mainSheetName = "卓志保税仓核对结果表" ;
		String[] mainKey = { "merchantName" , "depotName" , "opgCode", "goodsNo", "goodsName", "billType" , "sourceType", "account", 
				"unit" , "sourceCode", "batchNo", "productionDate", "overdueDate", "tradeCreateTime", "isException", "excetionResult"} ;
		String[] mainColumns = { "商家名称", "仓库名称", "商品OPG号", "货号", "商品名称", "单据类型", "交易类型", "交易数量", "单位", "业务单据号", 
				"原始批次号", "生产日期", "失效日期", "交易创建时间", "是否异常", "异常原因"};
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, dtoList) ;
		
		basePath = basePath += "/SJXY/" + model.getTaskCode() ;
		//删除目录下的子文件
	   	DownloadExcelUtil.deleteFile(basePath);
	   	//创建目录
	   	new File(basePath).mkdirs();
	   	//写出文件
	   	String fileName = basePath+"/" + "卓志保税仓核对结果表 .xlsx";
	   	FileOutputStream fileOut=new FileOutputStream(fileName);
	   	wb.write(fileOut);
	    fileOut.close();
		
		return fileName ;
	}

	/**
	 * Gss转换类型校验
	 * @param model
	 * @param dto
	 * @param dtoList
	 * @param merchandiseMaps
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	private void veriGssExchange(AutomaticCheckTaskModel model, DepotAutomaticGssDTO dto,
			Map<String, List<DepotAutomaticGssDTO>> exportMap, Map<String, Object> merchandiseMaps) throws SQLException {

		InventoryDetailsModel inventoryDetailsModel = new InventoryDetailsModel() ;
		inventoryDetailsModel.setOrderNo(dto.getSourceCode());
		inventoryDetailsModel.setMerchantId(model.getMerchantId());
		
		List<InventoryDetailsModel> inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "系统无单据");
			return ;
		}
		
		inventoryDetailsModel.setGoodsNo(dto.getGoodsNo());
		
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "货号不一致");
			return ;
		}
		
		if(Arrays.asList(GSS_INCREASING_GOOD).contains(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
		}else if(Arrays.asList(GSS_INCREASING_WORN).contains(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
		}else if(Arrays.asList(GSS_DIMINISHING_GOOD).contains(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
		}else if(Arrays.asList(GSS_DIMINISHING_WORN).contains(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
		}
		
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "交易类型不一致");
			return ;
		}
		
		inventoryDetailsModel.setBatchNo(dto.getBatchNo());
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "批次不一致");
			return ;
		}
		
		StringBuffer exceptionMsgSB = new StringBuffer();
		
		boolean timeNotSame = false ;
		boolean produceTimeNotSame = false ;
		boolean overTimeNotSame = false ;
		
		Map<String , Object> merchandiseMap = new HashMap<String , Object>() ;
		
		int tempNum  = 0 ;
		for (InventoryDetailsModel tempModel : inventoryList) {
			
			Timestamp tradeCreateTime = dto.getTradeCreateTime() ;
			int between = TimeUtils.daysBetween(tradeCreateTime, tempModel.getDivergenceDate());
			
			if(between != 0) {
				timeNotSame = true ;
			}
			
			Date productionDate = dto.getProductionDate() ;
			between = TimeUtils.daysBetween(productionDate, tempModel.getProductionDate());
			
			if(between != 0) {
				produceTimeNotSame = true ;
			}
			
			Date overdueDate = dto.getOverdueDate() ;
			between = TimeUtils.daysBetween(overdueDate, tempModel.getOverdueDate());
			
			if(between != 0) {
				overTimeNotSame = true ;
			}
			
			Long depotId = tempModel.getDepotId();
			DepotInfoModel tempDepot = depotInfoDao.searchById(depotId);
			
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(tempDepot.getType())) {
				merchandiseMap = (Map<String , Object>)merchandiseMaps.get(String.valueOf(tempModel.getGoodsId())) ;
				
				if(merchandiseMap == null) {
					exceptionMsgSB.append("该商家下无该货号商品; ") ;
					break ;
				}
				
				try {
					Integer boxToUnit = merchandiseMap.get("box_to_unit") == null ? 0 : (Integer)merchandiseMap.get("box_to_unit") ;
					Integer torrToUnit = merchandiseMap.get("torr_to_unit") == null ? 0 : (Integer)merchandiseMap.get("torr_to_unit") ;
					tempNum += changeUnit(tempModel.getUnit(), new BigDecimal(tempModel.getNum()), boxToUnit, torrToUnit) ;
				} catch (RuntimeException e) {
					setExceptionMsg(dto, exportMap, "是", "仓库为：香港仓，货号："+tempModel.getGoodsNo()+" 箱托转换数据未维护;");
					return ;
				}
			}else {
				tempNum += tempModel.getNum() ;
			}
			
		}
		
		if(timeNotSame) {
			exceptionMsgSB.append("交易创建时间不一致; ") ;
		}
		
		if(produceTimeNotSame) {
			exceptionMsgSB.append("生产日期不一致; ") ;
		}
		
		if(overTimeNotSame) {
			exceptionMsgSB.append("失效日期不一致; ") ;
		}
		
		Integer account = dto.getAccount();
		if(account != tempNum) {
			exceptionMsgSB.append("交易数量不一致; ") ;
		}
		
		if(exceptionMsgSB.length() > 0) {
			setExceptionMsg(dto, exportMap, "是", exceptionMsgSB.toString());
		}else {
			setExceptionMsg(dto, exportMap, "否", null);
		}
		
	}

	/**
	 * Gss销售类型校验
	 * @param model
	 * @param dto
	 * @param dtoList
	 * @param merchandiseMaps 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	private void veriGssSells(AutomaticCheckTaskModel model, DepotAutomaticGssDTO dto,
			Map<String, List<DepotAutomaticGssDTO>> exportMap, Map<String, Object> merchandiseMaps) throws SQLException {
		
		InventoryDetailsModel inventoryDetailsModel = new InventoryDetailsModel() ;
		inventoryDetailsModel.setBusinessNo(dto.getSourceCode());
		inventoryDetailsModel.setMerchantId(model.getMerchantId());
		
		List<InventoryDetailsModel> inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "系统无单据");
			return ;
		}
		
		inventoryDetailsModel.setGoodsNo(dto.getGoodsNo());
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "货号不一致");
			return ;
		}
		
		if("3".equals(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
		}else if("4".equals(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
		}else if("5".equals(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
		}else if("6".equals(dto.getSourceType())) {
			inventoryDetailsModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
			inventoryDetailsModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
		}
		
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "交易类型不一致");
			return ;
		}
		
		inventoryDetailsModel.setBatchNo(dto.getBatchNo());
		inventoryList = inventoryDetailsDao.list(inventoryDetailsModel) ;
		
		if(inventoryList.isEmpty()) {
			setExceptionMsg(dto, exportMap, "是", "批次不一致");
			return ;
		}
		
		StringBuffer exceptionMsgSB = new StringBuffer();
		
		boolean timeNotSame = false ;
		boolean produceTimeNotSame = false ;
		boolean overTimeNotSame = false ;
		
		Map<String , Object> merchandiseMap = new HashMap<String , Object>() ;
		
		int tempNum  = 0 ;
		for (InventoryDetailsModel tempModel : inventoryList) {
			
			Timestamp tradeCreateTime = dto.getTradeCreateTime() ;
			int between = TimeUtils.daysBetween(tradeCreateTime, tempModel.getDivergenceDate());
			
			if(between != 0) {
				timeNotSame = true ;
			}
			
			Date productionDate = dto.getProductionDate() ;
			between = TimeUtils.daysBetween(productionDate, tempModel.getProductionDate());
			if(between != 0) {
				produceTimeNotSame = true ;
			}
			
			Date overdueDate = dto.getOverdueDate() ;
			between = TimeUtils.daysBetween(overdueDate, tempModel.getOverdueDate());
			if(between != 0) {
				overTimeNotSame = true ;
			}
			
			Long depotId = tempModel.getDepotId();
			DepotInfoModel tempDepot = depotInfoDao.searchById(depotId);
			
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(tempDepot.getType())) {
				merchandiseMap = (Map<String , Object>)merchandiseMaps.get(String.valueOf(tempModel.getGoodsId())) ;
				
				if(merchandiseMap == null) {
					exceptionMsgSB.append("该商家下无该货号商品") ;
					break ;
				}
				
				try {
					Integer boxToUnit = merchandiseMap.get("box_to_unit") == null ? 0 : (Integer)merchandiseMap.get("box_to_unit") ;
					Integer torrToUnit = merchandiseMap.get("torr_to_unit") == null ? 0 : (Integer)merchandiseMap.get("torr_to_unit") ;
					tempNum += changeUnit(tempModel.getUnit(), new BigDecimal(tempModel.getNum()), boxToUnit, torrToUnit) ;
				} catch (RuntimeException e) {
					setExceptionMsg(dto, exportMap, "是", "仓库为：香港仓，货号："+tempModel.getGoodsNo()+" 箱托转换数据未维护;");
					return ;
				}
			}else {
				tempNum += tempModel.getNum() ;
			}
			
		}
		
		if(timeNotSame) {
			exceptionMsgSB.append("交易创建时间不一致; ") ;
		}
		
		if(produceTimeNotSame) {
			exceptionMsgSB.append("生产日期不一致; ") ;
		}
		
		if(overTimeNotSame) {
			exceptionMsgSB.append("失效日期不一致; ") ;
		}
		
		Integer account = dto.getAccount();
		if(account != tempNum) {
			exceptionMsgSB.append("交易数量不一致; ") ;
		}
		
		if(exceptionMsgSB.length() > 0) {
			setExceptionMsg(dto, exportMap, "是", exceptionMsgSB.toString());
		}else {
			setExceptionMsg(dto, exportMap, "否", null);
		}
	}

	private String checkDtoIsEmpty(DepotAutomaticGssDTO dto) {
		
		if(StringUtils.isBlank(dto.getSourceCode())) {
			return "业务单号为空;" ;
		}
		
		if(StringUtils.isBlank(dto.getSourceType())) {
			return "交易类型为空;" ;
		}
		
		if(StringUtils.isBlank(dto.getDepotName())) {
			return "仓库名称为空;" ;
		}
		
		if(StringUtils.isBlank(dto.getGoodsNo())) {
			return "货号为空;" ;
		}
		
		if(StringUtils.isBlank(dto.getBatchNo())) {
			return "原始批次号为空;" ;
		}
		
		if(dto.getTradeCreateTime() == null) {
			return "交易创建时间为空;" ;
		}
		
		if(dto.getProductionDate() == null) {
			return "生产日期为空;" ;
		}
		
		if(dto.getOverdueDate() == null) {
			return "失效日期为空;" ;
		}
		
		if(dto.getAccount() == null) {
			return "交易数量为空;" ;
		}
		
		return null;
	}

	/**
	 * 菜鸟仓自动校验
	 * @param data
	 * @param basePath
	 * @return
	 * @throws Exception 
	 */
	private String CLDepotVeri(AutomaticCheckTaskModel model, List<Map<String, String>> sheetList, String basePath) throws Exception {
		
		List<DepotAutomaticCLDTO> dtoList = new ArrayList<DepotAutomaticCLDTO>() ;
		
		HashMap<String, DepotAutomaticCLDTO> cacheMap = new HashMap<String, DepotAutomaticCLDTO>() ;
		
		for (int j = 1; j <= sheetList.size(); j++) {
			Map<String, String> map = sheetList.get(j - 1);
			DepotAutomaticCLDTO dto = excelMapExchangeCLDto(map);
			
			if(dto.getAccount() == null) {
				setExceptionMsg(dto, dtoList, "是", "数量为空");
				continue ;
			}
			
			String lbxCode = dto.getLbxCode();
			String goodsNo = dto.getGoodsNo();
			String depotName = dto.getDepotName();
			String depotTimeStr = TimeUtils.format(dto.getDepotTime(), "yyyy-MM-dd") ;
			String billType = dto.getBillType();
			String erpCode = dto.getErpCode();
			String extraCode = dto.getExtraCode();
			
			String key = lbxCode + "_" +goodsNo + "_" + depotName + "_" + depotTimeStr
						+ "_" + billType + "_" + erpCode + "_" + extraCode ;
			
			DepotAutomaticCLDTO tempDTO = cacheMap.get(key);
			if(tempDTO == null) {
				tempDTO = dto ;
			}else {
				tempDTO.setAccount(tempDTO.getAccount() + dto.getAccount());
				tempDTO.setSettleAccount(tempDTO.getSettleAccount() + dto.getSettleAccount());
			}
			
			cacheMap.put(key, tempDTO) ;
			
		}
		
		for (DepotAutomaticCLDTO dto : cacheMap.values()) {
			
			/**
			 * 1）、检查该行数据的单据类型是否在（交易出库  退货入库  采购入库  盘点出库  普通出库）内，若不在标识该行数据“不在核对范围内”
			 */
			String billType = dto.getBillType() ; 
			if(!Arrays.asList(CL_SOURCE_TYPE).contains(billType)) {
				setExceptionMsg(dto, dtoList, "是", "不在核对范围内");
				continue ;
			}
			
			/**
			 * 2)、检查excel的出入库时间是否在核对日期范围内，若不在标识该行数据“日期不在核对范围内”
			 */
			Timestamp checkStartDate = model.getCheckStartDate();
			Timestamp checkEndDate = model.getCheckEndDate();
			
			Timestamp depotTime = dto.getDepotTime();
			
			if(depotTime == null 
					|| TimeUtils.daysBetween(depotTime, checkStartDate) > 0
					|| TimeUtils.daysBetween(checkEndDate, depotTime) > 0) {
				setExceptionMsg(dto, dtoList, "是", "日期不在核对范围内");
				continue ;
			}
			
			/**
			 * 3)、若单据类型为交易出库，使用ERP订单号到电商订单（发货）查询查询是否存在对应的订单
			 * 	（条件excel-ERP订单号=外部订单编号，限定当前商家），若不存在标识该excel数据“系统无单据”；
			 * 	若存在，检查系统单据状态是否为已发货、仓库是否为指定仓库，若不是则标识该数据“单据状态不是已发货“或”仓库不正确”；
			 * 	检查系统单据的发货时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“发货日期不一致”；
			 * 	检查excel的货品编码+出入数量（转为正数）在系统中是否存在对应记录，若未找到标识数据“货号或数量不一致”；
			 * 	若无差异则标识该数据核验通过。
			 */
			if("交易出库".equals(dto.getBillType())) {
				veriClJyck(model, dto, dtoList);
			}
			/**
			 * 4)、若单据类型为退货入库，使用外部流水号到电商订单（退货）查询查询是否存在对应的退单（条件excel-外部流水号=来源交易单号，限定当前商家），
			 * 	若不存在标识该excel数据“系统无单据”；
			 * 	若存在，检查系统单据状态是否为已入仓、仓库是否为指定仓库，若不是则标识该数据“单据状态不是已入仓”或“仓库不正确”；
			 * 	检查系统单据的入库时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“入库日期不一致”；
			 * 	检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+退货总数量）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
			 * 	若无差异则标识该数据核验通过。
			 */
			else if("退货入库".equals(dto.getBillType())) {
				veriClThck(model, dto, dtoList);
			}
			/**
			 * 5)、若单据类型为采购入库，使用单号到系统查询查询是否存在对应的采购单据（条件excel-单号=采购订单-LBX单号，限定当前商家），
			 * 		若不存在则查询系统是否存在调拨单据（条件excel-单号=调拨订单-LBX单号，限定当前商家）若都不存在，则标识该excel数据“系统无单据”；
				
				5-1）、若存在采购单，通过采购单号检查系统是否存在采购入库单、采购入库单的仓库是否为指定仓库、状态是否为已入仓，
				若不是则标识该数据“不存在采购入库单”或“仓库不正确”或“单据状态不是已入仓”；
				检查系统单据的入库时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“入库日期不一致”；
				检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+实收数量）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
				若无差异则标识该数据核验通过。
				
				5-2）、若存在调拨单，通过调拨单号检查系统是否存在调拨入库单、调拨入库单的调入仓库是否为指定仓库、状态是否为已入仓，
				若不是则标识该数据“不存在调拨入库单”或“仓库不正确”或“单据状态不是已入仓”；
				检查系统单据的调入时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“入库日期不一致”；
				检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+调入数量汇总）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
				若无差异则标识该数据核验通过。
			 */
			else if("采购入库".equals(dto.getBillType())) {
				veriClCgrk(model, dto, dtoList);
			}
			/**
			 * 6)、若单据类型为普通出库，使用单号到调拨订单查询查询是否存在对应的单据（条件excel-单号=调拨订单-LBX单号，限定当前商家），若不存在标识该excel数据“系统无单据”；
			 * 	若存在，通过调拨单号检查系统是否存在调拨出库单、调拨出库单的调出仓库是否为指定仓库、状态是否为已出仓，
			 * 	若不是则标识该数据“不存在调拨出库单”或“仓库不正确”或“单据状态不是已出仓”；
			 * 	检查系统单据的调出时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“入库日期不一致”；
			 * 	检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+调出数量汇总）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
			 * 	若无差异则标识该数据核验通过。
			 */
			else if("普通出库".equals(dto.getBillType())) {
				veriClPtck(model, dto, dtoList);
			}
			/**
			 * 7)、若单据类型为盘点出库/盘点入库，使用单号到盘点结果列表查询是否存在单据（条件：excel-单号=盘点单-盘点指令单号，限定当前商家），若不存在标识该excel数据“系统无单据”；
			 * 	若存在，检查系统单据状态是否为已确认、仓库是否为指定仓库，若不是则标识该数据“单据状态不是已确认”或“仓库不正确”；
			 * 	检查系统单据的单据时间（转换为日期）与excel出入库时间（转换为日期）是否一致，若不一致标识数据“入库日期不一致”；
			 * 	若是盘点出库，检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+盘亏数量）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
			 * 	若是盘点入库，检查excel的货品编码+出入数量（转为正数）在系统中（商品货号+盘盈数量）是否存在对应记录，若未找到标识数据“货号或数量不一致”；
			 * 	若无差异则标识该数据核验通过。
			 */
			else if("盘点出库".equals(dto.getBillType()) || "盘点入库".equals(dto.getBillType())) {
				veriClPd(model, dto, dtoList);
			}
			
		}
		
		String mainSheetName = "菜鸟仓库核对结果表" ;
		String[] mainKey = { "lbxCode" , "lbCode" , "goodsNo", "goodsName" ,"depotName" , "depotTime" , "billType" , "inventoryType" , "account" , 
				"settleAccount" , "erpCode", "extraCode", "isException", "excetionResult"} ;
		String[] mainColumns = { "单号", "LP单号", "货品编码","货品名称" ,"仓库名称", "出入库时间", "单据类型", "库存类型" ,"出入数量" ,"结存数量" ,"ERP订单号" ,"外部流水号" , "是否异常", "异常原因"};
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, dtoList) ;
		
		basePath = basePath += "/SJXY/" + model.getTaskCode() ;
		//删除目录下的子文件
	   	DownloadExcelUtil.deleteFile(basePath);
	   	//创建目录
	   	new File(basePath).mkdirs();
	   	//写出文件
	   	String fileName = basePath+"/" + "菜鸟仓库核对结果表 .xlsx";
	   	FileOutputStream fileOut=new FileOutputStream(fileName);
	   	wb.write(fileOut);
	    fileOut.close();
		
		return fileName ;
	}
	
	/**
	 * 菜鸟盘点
	 */
	private void veriClPd(AutomaticCheckTaskModel model, DepotAutomaticCLDTO dto, List<DepotAutomaticCLDTO> dtoList) throws SQLException {
		if(dto.getLbxCode() == null) {
			setExceptionMsg(dto, dtoList, "是", "单号为空");
			return ;
		}
		
		TakesStockResultsModel queryModel = new TakesStockResultsModel() ;
		queryModel.setTakesStockCode(dto.getLbxCode());
		queryModel.setMerchantId(model.getMerchantId());
		queryModel = takesStockResultsDao.searchByModel(queryModel) ;
		
		if(queryModel == null) {
			setExceptionMsg(dto, dtoList, "是", "系统无单据");
			return ;
		}
		
		if(!DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010.equals(queryModel.getStatus())) {
			setExceptionMsg(dto, dtoList, "是", "单据状态不是已确认");
			return ;
		}
		
		if(queryModel.getDepotId() != model.getOutDepotId()) {
			setExceptionMsg(dto, dtoList, "是", "仓库不正确");
			return ;
		}
		
		if(isNotSameTime(queryModel.getSourceTime(), dto.getDepotTime())) {
			setExceptionMsg(dto, dtoList, "是", "入库日期不一致");
			return ;
		}
		
		TakesStockResultItemModel itemModel = new TakesStockResultItemModel() ;
		itemModel.setTakesStockResultId(queryModel.getId());
		itemModel.setGoodsNo(dto.getGoodsNo());
		
		if("盘点出库".equals(dto.getBillType())) {
			itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);
		}else if("盘点入库".equals(dto.getBillType())){
			itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1);
		}
		
		List<TakesStockResultItemModel> itemList = takesStockResultItemDao.list(itemModel);
		
		if(itemList.isEmpty()) {
			setExceptionMsg(dto, dtoList, "是", "货号不一致");
			return ;
		}
		
		int num = 0 ;
		for (TakesStockResultItemModel item : itemList) {
			if("盘点出库".equals(dto.getBillType())) {
				num += item.getDeficientNum() ;
			}else if("盘点入库".equals(dto.getBillType())){
				num += item.getSurplusNum() ;
			}
		}
		
		if(Math.abs(dto.getAccount()) != num) {
			setExceptionMsg(dto, dtoList, "是", "数量不一致");
			return ;
		}
		
		setExceptionMsg(dto, dtoList, "否", null);
	}
	
	/**
	 * 菜鸟普通出库
	 */
	private void veriClPtck(AutomaticCheckTaskModel model, DepotAutomaticCLDTO dto, List<DepotAutomaticCLDTO> dtoList) throws SQLException {
		if(dto.getLbxCode() == null) {
			setExceptionMsg(dto, dtoList, "是", "单号为空");
			return ;
		}
		
		TransferOrderModel transferOrderModel = new TransferOrderModel() ;
		transferOrderModel.setLbxNo(dto.getLbxCode());
		transferOrderModel.setMerchantId(model.getMerchantId());
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
		
		if(transferOrderModel == null) {
			setExceptionMsg(dto, dtoList, "是", "系统无单据");
			return ;
		}
		
		String status = transferOrderModel.getStatus();
		
		if(DERP.DEL_CODE_006.equals(status)) {
			setExceptionMsg(dto, dtoList, "是", "系统无单据");
			return ;
		}
		
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel() ;
		transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
		
		if(transferOutDepotModel == null) {
			setExceptionMsg(dto, dtoList, "是", "不存在调拨出库单");
			return ;
		}
		
		if(!DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016.equals(transferOutDepotModel.getStatus())) {
			setExceptionMsg(dto, dtoList, "是", "单据状态不是已出仓");
			return ;
		}
		
		if(transferOutDepotModel.getOutDepotId() != model.getOutDepotId()) {
			setExceptionMsg(dto, dtoList, "是", "仓库不正确");
			return ;
		}

		if(isNotSameTime(transferOutDepotModel.getTransferDate(), dto.getDepotTime())) {
			setExceptionMsg(dto, dtoList, "是", "出库日期不一致");
			return ;
		}
		
		TransferOutDepotItemModel transferOutDepotItemModel = new TransferOutDepotItemModel() ;
		transferOutDepotItemModel.setOutGoodsNo(dto.getGoodsNo());
		transferOutDepotItemModel.setTransferDepotId(transferOutDepotModel.getId());
		List<TransferOutDepotItemModel> itemList = transferOutDepotItemDao.list(transferOutDepotItemModel) ;
		
		if(itemList.isEmpty()) {
			setExceptionMsg(dto, dtoList, "是", "货号不一致");
			return ;
		}
		
		int num = 0 ;
		for (TransferOutDepotItemModel item : itemList) {
			num += item.getTransferNum() ;
		}
		
		if(Math.abs(dto.getAccount()) != num ) {
			setExceptionMsg(dto, dtoList, "是", "数量不一致");
			return ;
		}
		
		setExceptionMsg(dto, dtoList, "否", null );
	}
	
	/**
	 * 菜鸟采购入库
	 * @param model
	 * @param dto
	 * @param dtoList
	 * @throws SQLException 
	 */
	private void veriClCgrk(AutomaticCheckTaskModel model, DepotAutomaticCLDTO dto, List<DepotAutomaticCLDTO> dtoList) throws SQLException {
		
		if(dto.getLbxCode() == null) {
			setExceptionMsg(dto, dtoList, "是", "单号为空");
			return ;
		}
		
		boolean flag = false ;
		
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setLbxNo(dto.getLbxCode());
		purchaseOrderModel.setMerchantId(model.getMerchantId());
		purchaseOrderModel = purchaseOrderDao.searchByModel(purchaseOrderModel);
		
		if(purchaseOrderModel == null) {
			
			TransferOrderModel transferOrderModel = new TransferOrderModel() ;
			transferOrderModel.setLbxNo(dto.getLbxCode());
			transferOrderModel.setMerchantId(model.getMerchantId());
			transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
			
			if(transferOrderModel == null) {
				setExceptionMsg(dto, dtoList, "是", "系统无单据");
				return ;
			}
			
			String status = transferOrderModel.getStatus();
			
			if(DERP.DEL_CODE_006.equals(status)) {
				setExceptionMsg(dto, dtoList, "是", "系统无单据");
				return ;
			}
			
			TransferInDepotModel transferInDepotModel = new TransferInDepotModel() ;
			transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
			transferInDepotModel = transferInDepotDao.searchByModel(transferInDepotModel);
			
			if(transferInDepotModel == null) {
				setExceptionMsg(dto, dtoList, "是", "不存在调拨入库单");
				return ;
			}
			
			if(!DERP_ORDER.TRANSFERINDEPOT_STATUS_012.equals(transferInDepotModel.getStatus())) {
				setExceptionMsg(dto, dtoList, "是", "单据状态不是已入仓");
				return ;
			}
			
			if(transferInDepotModel.getInDepotId() != model.getOutDepotId()) {
				setExceptionMsg(dto, dtoList, "是", "仓库不正确");
				return ;
			}
			
			if(isNotSameTime(transferInDepotModel.getTransferDate(), dto.getDepotTime()) ) {
				setExceptionMsg(dto, dtoList, "是", "入库日期不一致");
				return ;
			}
			
			TransferInDepotItemModel transferInDepotItemModel = new TransferInDepotItemModel() ;
			transferInDepotItemModel.setInGoodsNo(dto.getGoodsNo());
			transferInDepotItemModel.setTransferDepotId(transferInDepotModel.getId());
			List<TransferInDepotItemModel> itemList = transferInDepotItemDao.list(transferInDepotItemModel) ;
			
			if(itemList.isEmpty()) {
				setExceptionMsg(dto, dtoList, "是", "货号不一致");
				return ;
			}
			
			int num = 0 ;
			for (TransferInDepotItemModel item : itemList) {
				num += item.getTransferNum() + item.getExpireNum() + item.getWornNum() ;
			}
			
			if(Math.abs(dto.getAccount()) != num) {
				setExceptionMsg(dto, dtoList, "是", "数量不一致");
				return ;
			}
			
			flag = true ;
			
		}else {
			
			String status = purchaseOrderModel.getStatus();
			if(DERP.DEL_CODE_006.equals(status)) {
				setExceptionMsg(dto, dtoList, "是", "系统无单据");
				return ;
			}
			
			WarehouseOrderRelModel warehouseOrderRelModel = new WarehouseOrderRelModel() ;
			warehouseOrderRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
			
			List<WarehouseOrderRelModel> warePurchaseRelList = warehouseOrderRelDao.list(warehouseOrderRelModel);
			
			if(warePurchaseRelList.isEmpty()) {
				setExceptionMsg(dto, dtoList, "是", "不存在采购入库单");
				return ;
			}
			
			Map<String, Object> param = new HashMap<String, Object>() ;
			param.put("lbxNo", dto.getLbxCode()) ;
			param.put("merchantId", model.getMerchantId()) ;
			param.put("goodsNo", dto.getGoodsNo()) ;
			
			List<Map<String, Object>> resultMap = purchaseOrderDao.getDepotVeriClByParam(param) ;
			
			if(resultMap.isEmpty()) {
				setExceptionMsg(dto, dtoList, "是", "货号不一致");
				return ;
			}
			
			int index = 0;
			for (Map<String, Object> map : resultMap) {
				
				String state = (String) map.get("state") ;
				if(!DERP_ORDER.PURCHASEWAREHOUSE_STATE_012.equals(state)) {
					setExceptionMsg(dto, dtoList, "是", "单据状态不是已入仓");
					break ;
				}
				
				Long depotId = (Long) map.get("depot_id") ;
				if(depotId != model.getOutDepotId()) {
					setExceptionMsg(dto, dtoList, "是", "仓库不正确");
					break ;
				}
				
				Timestamp warehouseDate = (Timestamp)map.get("inbound_date") ;
				if(warehouseDate == null) {
					setExceptionMsg(dto, dtoList, "是", "入库日期不一致");
					break ;
				}
				
				if(isNotSameTime(warehouseDate, dto.getDepotTime())) {
					setExceptionMsg(dto, dtoList, "是", "入库日期不一致");
					break ;
				}
				
				BigDecimal accountBD = (BigDecimal) map.get("tallying_num") ;
				if(accountBD != null) {
					if(Math.abs(dto.getAccount()) != accountBD.intValue()) {
						setExceptionMsg(dto, dtoList, "是", "数量不一致");
						break ;
					}
				}else {
					setExceptionMsg(dto, dtoList, "是", "数量不一致");
					break ;
				}
				
				
				index ++ ;
			}
			
			if(index == resultMap.size()) {
				flag = true ;
			}
		}
		
		if(flag) {
			setExceptionMsg(dto, dtoList, "否", null );
		}
	}

	/**
	 * 菜鸟退货出库
	 * @param model
	 * @param dto
	 * @param dtoList
	 * @throws SQLException 
	 */
	private void veriClThck(AutomaticCheckTaskModel model, DepotAutomaticCLDTO dto, List<DepotAutomaticCLDTO> dtoList) throws SQLException {
		
		if(dto.getExtraCode() == null) {
			setExceptionMsg(dto, dtoList, "是", "外部单号为空");
			return ;
		}
		
		OrderReturnIdepotModel orderReturnIdepotModel = new OrderReturnIdepotModel() ;
		orderReturnIdepotModel.setExternalCode(dto.getExtraCode());
		orderReturnIdepotModel.setMerchantId(model.getMerchantId());
		orderReturnIdepotModel.setInDepotCode(dto.getLbxCode());
		orderReturnIdepotModel = orderReturnIdepotDao.getVeriNotDelList(orderReturnIdepotModel);
		
		if(orderReturnIdepotModel == null) {
			setExceptionMsg(dto, dtoList, "是", "系统无单据");
			return ;
		}
		
		if(dto.getGoodsNo() == null 
				|| dto.getAccount() == null) {
			setExceptionMsg(dto, dtoList, "是", "货号或数量为空");
			return ;
		}
		
		String status = orderReturnIdepotModel.getStatus();
		
		
		if(!DERP_ORDER.ORDER_RETURN_STATUS_012.equals(status)) {
			setExceptionMsg(dto, dtoList, "是", "单据状态不是已发货");
			return ;
		}
		
		if(orderReturnIdepotModel.getReturnInDepotId() != model.getOutDepotId()) {
			setExceptionMsg(dto, dtoList, "是", "仓库不正确");
			return ;
		}
		
		if(isNotSameTime(orderReturnIdepotModel.getReturnInDate(), dto.getDepotTime())) {
			setExceptionMsg(dto, dtoList, "是", "入库日期不一致");
			return ;
		}
		
		OrderReturnIdepotItemModel returnIdepotItemModel = new OrderReturnIdepotItemModel() ;
		returnIdepotItemModel.setInGoodsNo(dto.getGoodsNo());
		returnIdepotItemModel.setOreturnIdepotId(orderReturnIdepotModel.getId());
		List<OrderReturnIdepotItemModel> itemList = orderReturnIdepotItemDao.list(returnIdepotItemModel) ;
		
		if(itemList.isEmpty()) {
			setExceptionMsg(dto, dtoList, "是", "货号不一致");
			return ;
		}
		
		int num = 0 ;
		for (OrderReturnIdepotItemModel item : itemList) {
			num += item.getReturnNum() + item.getBadGoodsNum() ;
		}
		
		if(Math.abs(dto.getAccount()) != num) {
			setExceptionMsg(dto, dtoList, "是", "数量不一致");
			return ;
		}
			
		
		setExceptionMsg(dto, dtoList, "否", null);
		
	}

	/**
	 * 菜鸟交易出库
	 * @param model
	 * @param dto
	 * @param dtoList
	 * @throws SQLException
	 */
	private void veriClJyck(AutomaticCheckTaskModel model , DepotAutomaticCLDTO dto, List<DepotAutomaticCLDTO> dtoList ) throws SQLException {

		
		if(dto.getErpCode() == null) {
			setExceptionMsg(dto, dtoList, "是", "ERP订单号为空");
			return ;
		}
		
		OrderModel queryModel = new OrderModel() ;
		queryModel.setExternalCode(dto.getErpCode());
		queryModel.setMerchantId(model.getMerchantId());
		
		List<OrderModel> orderList = orderDao.list(queryModel);
		
		// 过滤已删除的订单
		orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
				.collect(Collectors.toList()) ;
		
		if(orderList.isEmpty()) {
			setExceptionMsg(dto, dtoList, "是", "系统无单据");
			return ;
		}
		
		if(dto.getGoodsNo() == null 
				|| dto.getAccount() == null) {
			setExceptionMsg(dto, dtoList, "是", "货号或数量为空");
			return ;
		}
		
		if(orderList.size() > 1) {
			setExceptionMsg(dto, dtoList, "是", "订单重复");
			return ;
		}
		
		boolean flag = false ;
		for (OrderModel orderModel : orderList) {
			String status = orderModel.getStatus();
			
			if(!DERP_ORDER.ORDER_STATUS_4.equals(status)) {
				setExceptionMsg(dto, dtoList, "是", "单据状态不是已发货");
				break ;
			}
			
			if(orderModel.getDepotId() != model.getOutDepotId()) {
				setExceptionMsg(dto, dtoList, "是", "仓库不正确");
				break ;
			}
			
			if(isNotSameTime(orderModel.getDeliverDate(), dto.getDepotTime())) {
				setExceptionMsg(dto, dtoList, "是", "发货日期不一致");
				break ;
			}
			
			OrderItemModel orderItemModel = new OrderItemModel() ;
			orderItemModel.setGoodsNo(dto.getGoodsNo());
			orderItemModel.setOrderId(orderModel.getId());
			List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel) ;
			
			if(orderItemList.isEmpty()) {
				setExceptionMsg(dto, dtoList, "是", "货号不一致");
				break ;
			}
			
			int num = 0 ;
			for (OrderItemModel item : orderItemList) {
				num += item.getNum() ;
			}
			
			if(num != Math.abs(dto.getAccount())) {
				setExceptionMsg(dto, dtoList, "是", "数量不一致");
				break ;
			}
			
			flag = true ;
		}
		
		if(flag) {
			setExceptionMsg(dto, dtoList, "否", null);
		}
	
	}
	
	private void setExceptionMsg(DepotAutomaticCLDTO dto , List<DepotAutomaticCLDTO> dtoList, String isException, String exceptionMsg) {
		dto.setIsException(isException);
		dto.setExcetionResult(exceptionMsg);
		
		dtoList.add(dto) ;
	}
	
	private void setExceptionMsg(DepotAutomaticGssDTO dto , Map<String, List<DepotAutomaticGssDTO>> exportMap, String isException, String exceptionMsg) {
		dto.setIsException(isException);
		dto.setExcetionResult(exceptionMsg);
		
		List<DepotAutomaticGssDTO> tempList = exportMap.get(dto.getSourceCode()) ;
		
		if(tempList == null) {
			tempList = new ArrayList<DepotAutomaticGssDTO>() ;
		}
		
		tempList.add(dto) ;
		exportMap.put(dto.getSourceCode(), tempList) ;
	}
	
	/**
	 * excel 行转成DTO 
	 * @param map
	 * @return
	 */
	private DepotAutomaticGssDTO excelMapExchangeGssDto(Map<String, String> map) {
		DepotAutomaticGssDTO dto = new DepotAutomaticGssDTO() ;
		dto.setOpgCode(StringUtils.isBlank(map.get("商品OPG号")) == true ? null : map.get("商品OPG号"));
		dto.setMerchantName(StringUtils.isBlank(map.get("商家名称")) == true ? null : map.get("商家名称"));
		dto.setGoodsNo(StringUtils.isBlank(map.get("货号")) == true ? null : map.get("货号"));
		dto.setGoodsName(StringUtils.isBlank(map.get("商品名称")) == true ? null : map.get("商品名称"));
		dto.setDepotName(StringUtils.isBlank(map.get("仓库名称")) == true ? null : map.get("仓库名称"));
		dto.setBillType(StringUtils.isBlank(map.get("单据类型")) == true ? null : map.get("单据类型"));
		dto.setSourceType(StringUtils.isBlank(map.get("交易类型")) == true ? null : map.get("交易类型"));
		dto.setUnit(StringUtils.isBlank(map.get("单位")) == true ? null : map.get("单位"));
		dto.setSourceCode(StringUtils.isBlank(map.get("业务单据号")) == true ? null : map.get("业务单据号"));
		dto.setBatchNo(StringUtils.isBlank(map.get("原始批次号")) == true ? null : map.get("原始批次号"));
		
		String tradeCreateTimeStr = map.get("交易创建时间") ;
		Timestamp tradeCreateTime = null;
		if(StringUtils.isNotBlank(tradeCreateTimeStr)) {
			tradeCreateTime = TimeUtils.parseFullTime(tradeCreateTimeStr) ;
		}
		dto.setTradeCreateTime(tradeCreateTime);
		
		String account = map.get("交易数量") ;
		if(StringUtils.isNotBlank(account)) {
			dto.setAccount(Integer.valueOf(account));
		}
		
		String productionDateStr = map.get("生产日期");
		if(StringUtils.isNotBlank(productionDateStr)) {
			Date productionDate = TimeUtils.strToSqlDate(productionDateStr);
			dto.setProductionDate(productionDate);
		}
		
		String overdueDateStr = map.get("失效日期");
		if(StringUtils.isNotBlank(overdueDateStr)) {
			Date overdueDate = TimeUtils.strToSqlDate(overdueDateStr);
			dto.setOverdueDate(overdueDate);
		}
		
		return dto ;
	}
	
	/**
	 * excel 行转成DTO 
	 * @param map
	 * @return
	 */
	private DepotAutomaticCLDTO excelMapExchangeCLDto(Map<String, String> map) {
		DepotAutomaticCLDTO dto = new DepotAutomaticCLDTO() ;
		dto.setLbxCode(StringUtils.isBlank(map.get("单号")) == true ? null : map.get("单号"));
		dto.setLbCode(StringUtils.isBlank(map.get("LP单号")) == true ? null : map.get("LP单号"));
		dto.setGoodsNo(StringUtils.isBlank(map.get("货品编码")) == true ? null : map.get("货品编码"));
		dto.setGoodsName(StringUtils.isBlank(map.get("货品名称")) == true ? null : map.get("货品名称"));
		dto.setDepotName(StringUtils.isBlank(map.get("仓库名称")) == true ? null : map.get("仓库名称"));
		dto.setInventoryType(StringUtils.isBlank(map.get("库存类型")) == true ? null : map.get("库存类型"));
		dto.setBillType(StringUtils.isBlank(map.get("单据类型")) == true ? null : map.get("单据类型"));
		dto.setErpCode(StringUtils.isBlank(map.get("ERP订单号")) == true ? null : map.get("ERP订单号"));
		dto.setExtraCode(StringUtils.isBlank(map.get("外部流水号")) == true ? null : map.get("外部流水号"));
		
		String depotTimeStr = map.get("出入库时间") ;
		Timestamp depotTime = null;
		if(StringUtils.isNotBlank(depotTimeStr)) {
			depotTime = TimeUtils.parseFullTime(depotTimeStr) ;
		}
		dto.setDepotTime(depotTime);
		
		String account = map.get("出入数量") ;
		String settleAccount = map.get("结存数量") ;
		
		if(StringUtils.isNotBlank(account)) {
			dto.setAccount(Integer.valueOf(account));
		}
		
		if(StringUtils.isNotBlank(settleAccount)) {
			dto.setSettleAccount(Integer.valueOf(settleAccount));
		}else {
			dto.setSettleAccount(0);
		}
		
		return dto ;
	}

	private boolean isNotSameTime(Timestamp sourceTime, Timestamp depotTime) {
		
		if(sourceTime == null) {
			return true ;
		}
		
		int between = TimeUtils.daysBetween(sourceTime, depotTime) ;
		
		if(between != 0) {
			return true ;
		}
		
		return false ;
	}
	
	/**
	 * 箱托转换
	 * @param unit
	 * @param num
	 * @param boxToUnit
	 * @param torrToUnit
	 * @param merchantName
	 * @param goodsNo
	 * @return
	 * @throws RuntimeException
	 */
	private Integer changeUnit(String unit,BigDecimal num,Integer boxToUnit,Integer torrToUnit) throws RuntimeException{
    	Integer numInt=0;
    	if(num==null) return numInt;
    	
		//转换单位为件后返回
		if(StringUtils.isEmpty(unit)||unit.equals(DERP.INVENTORY_UNIT_2)){
			numInt=num.intValue();
		}else if(unit.equals(DERP.INVENTORY_UNIT_1)){
			if(boxToUnit==null||boxToUnit.intValue()<=0){
				throw new RuntimeException();
			}
			numInt=num.intValue()*boxToUnit.intValue();
		}else if(unit.equals(DERP.INVENTORY_UNIT_0)){
			if(torrToUnit==null||torrToUnit.intValue()<=0){
				throw new RuntimeException();
			}
			numInt=num.intValue()*torrToUnit.intValue();
		}
    	return numInt;
    }
	
}
