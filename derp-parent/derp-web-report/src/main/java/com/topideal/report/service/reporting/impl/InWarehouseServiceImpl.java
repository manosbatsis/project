package com.topideal.report.service.reporting.impl;

import com.alibaba.fastjson.JSON;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.reporting.BuInventorySummaryDao;
import com.topideal.dao.reporting.BuInventorySummaryItemDao;
import com.topideal.dao.reporting.InWarehouseDetailsDao;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.CommbarcodeDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryItemModel;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.InWareHouseDaysService;
import com.topideal.report.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InWarehouseServiceImpl implements InWareHouseDaysService{

	@Autowired
	InWarehouseDetailsDao inWarehouseDetailsDao ;
	
	@Autowired
	DepotInfoDao depotInfoDao ;
	
	@Autowired
	BuInventorySummaryItemDao buInventorySummaryItemDao ;
	
	@Autowired
	BuInventorySummaryDao buInventorySummaryDao ;
	
	@Autowired
	CommbarcodeDao commbarcodeDao;
	
	@Autowired
	BusinessUnitDao businessUnitDao ;
	
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;

	@Autowired
	private WeightedPriceDao weightedPriceDao ;
	/**
	 * 查出在库天数汇总
	 */
	@SuppressWarnings("unchecked")
	@Override
	public InWarehouseDetailsDTO listInWarehouseDays(User user,InWarehouseDetailsDTO dto) throws Exception {
		
		Map<String , Object > queryMap = new HashMap<String , Object >() ; 
		queryMap.put("month", dto.getMonth()) ;
		
		if(dto.getBuId() != null) {
			queryMap.put("buId", dto.getBuId()) ;
		}else {
			
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			
			queryMap.put("buIds", buIds) ;
		}
		
		InWarehouseDetailsDTO pageModel = new InWarehouseDetailsDTO() ;
		
		//占比金额总数
		Double warehouseAmontTotal = 0.0 ;
		List<Map<String, Object>> list = inWarehouseDetailsDao.listInWarehouseDays(queryMap) ; 
		
		for (Map<String, Object> map : list) {
			if(map.get("totalAmount") == null) {
				map.put("totalAmount" , new BigDecimal(0.0)) ;
			}
			
			if(map.get("currency") != null) {
				String currency = String.valueOf(map.get("currency"));
				currency = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
				
				map.put("currency", currency) ;
			}
			
			warehouseAmontTotal +=  ((BigDecimal)map.get("totalAmount")).setScale(2).doubleValue() ;
		}
		
		//计算库存金额占比
		for (Object mapOb : list) {
			
			Map<String, Object> resultMap = (Map<String, Object>) mapOb ;
			
			NumberFormat percent = NumberFormat.getPercentInstance();
	        percent.setMaximumFractionDigits(2);
			
			if(warehouseAmontTotal == 0.0) {
				resultMap.put("proportion", percent.format(0.0)) ;
			}else {
				double totalAmount = ((BigDecimal)resultMap.get("totalAmount")).setScale(2).doubleValue() ;
				//占比
				BigDecimal proportion = new BigDecimal(totalAmount) ;
				proportion = proportion.divide(new BigDecimal(warehouseAmontTotal),4 , BigDecimal.ROUND_HALF_UP) ;
				
				resultMap.put("proportion", percent.format(proportion.doubleValue())) ;
			}
			
		}
		
		pageModel.setTotal(list.size());
		pageModel.setList(list);
		
		return pageModel;
	}

	/**
	 * 查出当前月份统计时间
	 */
	/*@Override
	public String getRangeDate(InWarehouseDetailsModel inWarehouseDetailsModel) {
		
		Map<String , Object > queryMap = new HashMap<String , Object >() ; 
		
		queryMap.put("merchantId", inWarehouseDetailsModel.getMerchantId()) ;
		queryMap.put("month", inWarehouseDetailsModel.getMonth()) ;
		
		Map<String, Object> map = inWarehouseDetailsDao.getRangeDate(queryMap);
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd") ;
		String rangeDate = sdf.format((Timestamp)map.get("create_date")) ;
		
		return rangeDate ;
	}*/

	/**
	 * 查询所有信息详情
	 */
	@Override
	public InWarehouseDetailsModel listInWarehouseAllDetails(InWarehouseDetailsModel model) throws Exception {
		return inWarehouseDetailsDao.listInWarehouseDetails(model);
	}

	
	/**
	 * 根据入库时间倒序查询明细
	 */
	@Override
	public List<InWarehouseDetailsDTO> listInWarehouseOrderByInWarehousedays(InWarehouseDetailsDTO dto) {
		return inWarehouseDetailsDao.listInWarehouseOrderByInWarehousedays(dto);
	}

	/**
	 * 详情页查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getInwarehouseDetails(InWarehouseDetailsModel model,List<Long> buList) {
		
		Map<String , Object > queryMap = new HashMap<String , Object >() ; 
		queryMap.put("month", model.getMonth()) ;
		queryMap.put("buId", model.getBuId()) ;
		queryMap.put("buIds", buList) ;
		//占比金额总数
		Double warehouseAmontTotal = 0.0 ;
		List<Map<String, Object>> list = inWarehouseDetailsDao.listInWarehouseDaysRange(queryMap) ; 
		
		//表格显示列表
		List<Map<String, Object>> returnlist = new ArrayList<Map<String,Object>> (8) ;
		
		for(int i = 0 ; i < 8 ; i++) {
			returnlist.add(null) ;
		}
		
		for (Map<String, Object> map : list) {
			if(map.get("totalAmount") == null) {
				map.put("totalAmount" , new BigDecimal(0.0)) ;
			}
			
			if(map.get("currency") != null) {
				String currency = String.valueOf(map.get("currency"));
				currency = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
				
				map.put("currency", currency) ;
			}
			
			if(map.get("weightedAmount") == null) {
				map.put("weightedAmount" , new BigDecimal(0.0)) ;
			}
			
			warehouseAmontTotal +=  ((BigDecimal)map.get("totalAmount")).setScale(2).doubleValue() ;
		}
		
		//计算库存金额占比
		for (Object mapOb : list) {
			
			Map<String, Object> resultMap = (Map<String, Object>) mapOb ;
			
			NumberFormat percent = NumberFormat.getPercentInstance();
	        percent.setMaximumFractionDigits(2);
			
			if(warehouseAmontTotal == 0.0) {
				resultMap.put("proportion", percent.format(0.0)) ;
			}else {
				double totalAmount = ((BigDecimal)resultMap.get("totalAmount")).setScale(2).doubleValue() ;
				//占比
				BigDecimal proportion = new BigDecimal(totalAmount) ;
				proportion = proportion.divide(new BigDecimal(warehouseAmontTotal),4 , BigDecimal.ROUND_HALF_UP) ;
				
				resultMap.put("proportion", percent.format(proportion.doubleValue())) ;
			}
			
			switch (String.valueOf(resultMap.get("inWarehouseRange"))) {
			case "0~30天":
				returnlist.set(0, resultMap);
				break;
				
			case "30~60天":
				returnlist.set(1, resultMap);
				break;
				
			case "60~90天":
				returnlist.set(2, resultMap);
				break;
				
			case "90~120天":
				returnlist.set(3, resultMap);
				break;

			case "120~150天":
				returnlist.set(4, resultMap);
				break;
				
			case "150~180天":
				returnlist.set(5, resultMap);
				break;
				
			case "180天以上":
				returnlist.set(6, resultMap);
				break;
				
			case "未匹配":
				returnlist.set(7, resultMap);
				break;
				
			}
			
		}
		
		Iterator<Map<String, Object>> iterator = returnlist.iterator();
		while(iterator.hasNext()) {
			Map<String, Object> map = iterator.next();
			
			if(map == null) {
				iterator.remove();
			}
		}
		
		return returnlist;
	}

	@Override
	public List<Map<String, Object>> countInWarehouseOrderInvertoryByDepot(InWarehouseDetailsModel model,List<Long> buList) throws SQLException {
		
		Map<String, Object> map = new HashMap<>() ;
		map.put("month", model.getMonth()) ;
		map.put("buId", model.getBuId()) ;
		map.put("buIds", buList);
		List<Map<String, Object>> list = buInventorySummaryDao.getInWarehouseExport(map) ;
		
		for (Map<String, Object> resultMap : list) {
			
			if(resultMap.get("unit") == null) {
				resultMap.put("unit", "件") ;
			}else {
				String unit = String.valueOf(resultMap.get("unit")) ;
				
				unit = DERP.getLabelByKey(DERP.inventory_unitList, unit) ;
				
				resultMap.put("unit", unit) ;
			}
			
			if(resultMap.get("commbarcode") == null) {
				continue ;
			}
			
			//查询加权单价
			String commbarcode = String.valueOf(resultMap.get("commbarcode")) ;
			Long merchantId = (Long)resultMap.get("merchantId") ;
			Long buId = (Long)resultMap.get("buId") ;
			String month = model.getMonth() ;
			
			WeightedPriceModel queryModel = new WeightedPriceModel() ;
			
			queryModel.setEffectiveMonth(month);
			queryModel.setCommbarcode(commbarcode);
			queryModel.setMerchantId(merchantId);
			queryModel.setBuId(buId);
			
			queryModel = weightedPriceDao.searchByModel(queryModel) ;
			
			if(queryModel == null) {
				continue ;
			}
			
			BigDecimal inWarehouseNum = (BigDecimal)resultMap.get("inWarehouseNum") ;
			
			if(inWarehouseNum == null) {
				continue ;
			}
			
			BigDecimal price = queryModel.getPrice();
			BigDecimal amount = price.multiply(inWarehouseNum).setScale(2, BigDecimal.ROUND_HALF_UP) ;
			
			resultMap.put("price", price) ;
			resultMap.put("amount", amount) ;
		}
		
		return list;
	}

	@Override
	public List<Map<String, Object>> getInOutDepotInfo(BuInventorySummaryItemModel tempItem) throws SQLException {
		
		List<BuInventorySummaryItemModel> list = buInventorySummaryItemDao.getWarehouseExportlist(tempItem);
		
		List<Map<String, Object>> exportLsit = new ArrayList<Map<String, Object>>() ;
		for (BuInventorySummaryItemModel item : list) {
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("barcode", item.getBarcode());
			map.put("buName", item.getBuName());
			map.put("code", item.getCode());
			map.put("businessNo", item.getBusinessNo());
			map.put("sourceType", item.getSourceType());
			map.put("contractNo", item.getContractNo());
			map.put("customerName", item.getCustomerName());
			map.put("depotName", item.getDepotName());
			map.put("divergenceDate", item.getDivergenceDate());
			map.put("goodsName", item.getGoodsName());
			map.put("goodsNo", item.getGoodsNo());
			map.put("poNo", item.getPoNo());
			map.put("commbarcode", item.getCommbarcode());
			map.put("merchantName", item.getMerchantName());
			map.put("num", item.getNum());
			map.put("unit", item.getUnit());
			map.put("inDepotName", item.getInDepotName());
			
			CommbarcodeModel queryModel = new CommbarcodeModel() ;
			queryModel.setCommbarcode(item.getCommbarcode());
			queryModel = commbarcodeDao.searchByModel(queryModel) ;
			
			if(queryModel != null) {
				map.put("brandParentName", queryModel.getCommBrandParentName());
				map.put("typeName", queryModel.getCommTypeName());
			}
			
			
			exportLsit.add(map) ;
		}
		
		return exportLsit;
	}

	@Override
	public List<BusinessUnitModel> getAllBuUnit() throws SQLException {
		return businessUnitDao.list(new BusinessUnitModel());
	}

    @Override
    public String createExcel(FileTaskMongo task, String basePath) throws Exception {
        InWarehouseDetailsModel model = JSON.parseObject(task.getParam(), InWarehouseDetailsModel.class);
        InWarehouseDetailsDTO dto = JSON.parseObject(task.getParam(), InWarehouseDetailsDTO.class);
	    List<Long> buList=null;
	    String userId="";
	    if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}
	    dto.setBuIds(buList);
        // 在库总表响应结果集
        List<Map<String, Object>> mainList = getInwarehouseDetails(model,buList);
        String mainSheetName = "商品在库天数汇总";
        String[] mainColumns = {"事业部", "在库天数", "库存数量", "库存金额", "加权金额", "库存金额占比(%)", "归属月份", "数据统计截止时间"};
        String[] mainKeys = {"buName", "inWarehouseRange", "totalNum", "totalAmount", "weightedAmount", "proportion", "month", "statisticsDate"};

        ExportExcelSheet mainExcelSheet = new ExportExcelSheet();
		mainExcelSheet.setColums(mainColumns);
		mainExcelSheet.setKeys(mainKeys);
		mainExcelSheet.setResultList(mainList);
		mainExcelSheet.setSheetNames(mainSheetName);
		
		//商品在库天数明细结果集
		String subSheetName = "商品在库天数明细" ;
		List<InWarehouseDetailsDTO> subResult = listInWarehouseOrderByInWarehousedays(dto) ;
		String[] subColumns = { "商品条码", "标准条码", "商品名称", "标准品牌","母品牌", "二级品类", "在库天数", "在库单价", "在库金额", "在库数量" ,
				"加权单价", "加权金额", "入库单号" , "入库时间" , "入库量" , "入库仓库"};
		String[] subKeys = {"barcode" , "commbarcode" , "goodsName" , "brandName" ,"superiorParentBrand", "typeName", "inWarehouseDays", "inWarehousePrice", "inWarehouseAmount" , "inWarehouseNum" ,
				"weightedPrice", "weightedAmount", "warehouseNo" , "warehouseDate" , "warehouseNum" , "depotName"} ;
		
		ExportExcelSheet subExcelSheet = new ExportExcelSheet() ;
		subExcelSheet.setColums(subColumns);
		subExcelSheet.setKeys(subKeys);
		subExcelSheet.setResultList(subResult);
		subExcelSheet.setSheetNames(subSheetName);
		
		//库存统计结果集
		String inventorySheetName = "库存统计" ;
		List<Map<String, Object>> inventorySheetResult = countInWarehouseOrderInvertoryByDepot(model,buList) ;
		String[] inventorySheetColumns = {"商家", "事业部", "仓库名称", "标准条码", "商品名称", "标准品牌","母品牌", "好品库存量", "理货单位", "加权单价", "在库金额"};
		String[] inventorySheetKeys = {"merchantName", "buName" , "depotName" , "commbarcode" , "goodsName", "brandName" ,"superiorParentBrand", "inWarehouseNum", "unit", "price", "amount"} ;
		
		ExportExcelSheet inventoryExcelSheet = new ExportExcelSheet() ;
		inventoryExcelSheet.setColums(inventorySheetColumns);
		inventoryExcelSheet.setKeys(inventorySheetKeys);
		inventoryExcelSheet.setResultList(inventorySheetResult);
		inventoryExcelSheet.setSheetNames(inventorySheetName);
		
		//本月入库详情
		BuInventorySummaryItemModel tempItem = new BuInventorySummaryItemModel() ;
		tempItem.setOwnMonth(model.getMonth());
		tempItem.setBuId(model.getBuId());
		tempItem.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
		tempItem.setDetailType("001");
		
		List<Map<String, Object>> idepotList = getInOutDepotInfo(tempItem) ;
		String idepotSheetName = "本月入库详情" ;
		String[] idepotSheetColumns = { "公司", "事业部", "来源单号", "业务单号", "单据类型", "入库仓库", "入库时间",
				"合同号", "PO号", "供应商", "商品货号", "条码", "标准条码", "标准品牌", "二级品类", "商品名称", "入库数量", "理货单位"};
		String[] idepotSheetKeys = {"merchantName" , "buName" , "code" , "businessNo", "sourceTypeLabel" , "depotName", "divergenceDate", 
				"contractNo", "poNo", "customerName", "goodsNo", "barcode", "commbarcode", "brandParentName", "typeName", "goodsName", "num", "unitLabel"} ;
		
		ExportExcelSheet idepotSheet = new ExportExcelSheet() ;
		idepotSheet.setColums(idepotSheetColumns);
		idepotSheet.setKeys(idepotSheetKeys);
		idepotSheet.setResultList(idepotList);
		idepotSheet.setSheetNames(idepotSheetName);
		
		tempItem.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);
		List<Map<String, Object>> odepotList = getInOutDepotInfo(tempItem) ;
		String odepotSheetName = "本月出库详情" ;
		String[] odepotSheetColumns = { "公司", "事业部", "来源单号", "业务单号", "单据类型", "出库仓库", "出库时间",
				"合同号", "PO号", "客户", "入库仓库", "商品货号", "条码", "标准条码", "标准品牌", "二级品类", "商品名称", "出库数量", "理货单位"};
		String[] odepotSheetKeys = {"merchantName" , "buName" , "code" , "businessNo", "sourceTypeLabel" , "depotName", "divergenceDate", 
				"contractNo", "poNo", "customerName", "inDepotName","goodsNo", "barcode", "commbarcode", "brandParentName", "typeName", "goodsName", "num", "unitLabel"} ;
		
		ExportExcelSheet odepotSheet = new ExportExcelSheet() ;
		odepotSheet.setColums(odepotSheetColumns);
		odepotSheet.setKeys(odepotSheetKeys);
		odepotSheet.setResultList(odepotList);
		odepotSheet.setSheetNames(odepotSheetName);
		
		List<ExportExcelSheet> exportExcelSheetList = new ArrayList<ExportExcelSheet>() ;
		exportExcelSheetList.add(mainExcelSheet) ;
        exportExcelSheetList.add(subExcelSheet);
        exportExcelSheetList.add(inventoryExcelSheet);
        exportExcelSheetList.add(idepotSheet);
        exportExcelSheetList.add(odepotSheet);

        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(exportExcelSheetList);
        //删除目录下的子文件
        //DownloadExcelUtil.deleteFile(basePath);
        //创建目录
        basePath = basePath + "/" + task.getTaskType() + "/" + task.getMerchantId() + "/" + model.getMonth() + "/" + task.getCode();
        new File(basePath).mkdirs();

        BusinessUnitModel businessUnit = businessUnitDao.searchById(model.getBuId());

        //写出文件
        String fileName = "在库天数导出表-" + model.getMonth() + businessUnit.getName() + ".xlsx";
        FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
        wb.write(fileOut);
        fileOut.close();
		
		return basePath;
	}

}
