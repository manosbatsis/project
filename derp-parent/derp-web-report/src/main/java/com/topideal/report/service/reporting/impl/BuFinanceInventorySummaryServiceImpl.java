package com.topideal.report.service.reporting.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.OperateReportLogDao;
import com.topideal.dao.reporting.BuFinanceAddPurchaseNotshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddSaleNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceDecreasePurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceDecreaseSaleNoshelfDao;
import com.topideal.dao.reporting.BuFinanceDestroyDao;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.dao.reporting.BuFinanceMoveDetailsDao;
import com.topideal.dao.reporting.BuFinancePurchaseDamagedDao;
import com.topideal.dao.reporting.BuFinanceSaleDamagedDao;
import com.topideal.dao.reporting.BuFinanceSaleNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSaleShelfDao;
import com.topideal.dao.reporting.BuFinanceSdAddPurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSdWarehouseDetailsDao;
import com.topideal.dao.reporting.BuFinanceTakesStockResultsDao;
import com.topideal.dao.reporting.BuFinanceWarehouseDetailsDao;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.CustomerInfoDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.vo.common.OperateReportLogModel;
import com.topideal.entity.vo.reporting.BuFinanceAddPurchaseNotshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddSaleNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddTransferNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;
import com.topideal.entity.vo.reporting.BuFinanceDestroyModel;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceTakesStockResultsModel;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.CustomerInfoModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.InventoryLocationMappingMongo;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.tools.DownloadExcelUtil;
import com.topideal.report.tools.PdfUtils;

import net.sf.json.JSONObject;

/**
 * 新财务进销存报表
 *
 */
@Service
public class BuFinanceInventorySummaryServiceImpl implements BuFinanceInventorySummaryService{

	@Autowired
	private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;// 事业部财务经销存汇总
	@Autowired
	private BuFinancePurchaseDamagedDao buFinancePurchaseDamagedDao;//(事业部财务经销存)采购残损明细表
	//@Autowired
	//private BuFinancePurchaseNotshelfDao buFinancePurchaseNotshelfDao;//(财务经销存)采购在途
	@Autowired
	private BuFinanceSaleDamagedDao buFinanceSaleDamagedDao;//(财务经销存)销售残损明细表
	@Autowired
	private BuFinanceSaleNotshelfDao buFinanceSaleNotshelfDao;//(财务经分销)销售未上架
	@Autowired
	private BuFinanceSaleShelfDao buFinanceSaleShelfDao;//(财务经销存)销售已经上架
	@Autowired
	private BuFinanceTakesStockResultsDao buFinanceTakesStockResultsDao;//(财务经分销)盘盈盘亏明细表
	@Autowired
	private BuFinanceWarehouseDetailsDao buFinanceWarehouseDetailsDao;//(财务经销存)采购入库明细
	@Autowired
	private BuFinanceDestroyDao buFinanceDestroyDao;// 销毁明细
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品
	@Autowired
	private BuFinanceAddPurchaseNotshelfDetailsDao buFinanceAddPurchaseNotshelfDetailsDao;// (事业部财务经销存)累计采购在途明细表
	@Autowired
	private BuFinanceAddSaleNoshelfDetailsDao buFinanceAddSaleNoshelfDetailsDao;// (事业部财务经分销)累计销售在途明细表
	@Autowired
	private BuFinanceDecreaseSaleNoshelfDao buFinanceDecreaseSaleNoshelfDao;//(事业部财务经分销)本期减少销售在途
	@Autowired
	private BuFinanceDecreasePurchaseNotshelfDao buFinanceDecreasePurchaseNotshelfDao;//(事业部财务经销存)本期减少采购在途表
	@Autowired
	private BuFinanceAddTransferNoshelfDetailsDao buFinanceAddTransferNoshelfDetailsDao;// (事业部财务经销存)累计调拨在途
	@Autowired
	private BuFinanceMoveDetailsDao  buFinanceMoveDetailsDao;// (事业部财务经销存)本期移库单
	@Autowired
	private BuFinanceSdWarehouseDetailsDao buFinanceSdWarehouseDetailsDao;//(事业部财务经销存)采购入库SD明细
	@Autowired
	private BuFinanceSdAddPurchaseNotshelfDao buFinanceSdAddPurchaseNotshelfDao;//
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private BusinessUnitDao businessUnitDao;
	@Autowired
	private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao;
	@Autowired
	private WeightedPriceDao weightedPriceDao;// 加权单价表
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;	
	// 报表操作日志
	@Autowired
	private OperateReportLogDao operateReportLogDao;
	
	/**
	 * 分页
	 */
	@Override
	public BuFinanceInventorySummaryDTO getListByPage(User user,BuFinanceInventorySummaryDTO dto) {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return buFinanceInventorySummaryDao.getListByPage(dto);
	}

    /**
     * 生成商家、月份的excel文件报表
     */
    public String createExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> jsonMap = jsonData;
        Integer merchantId = (Integer) jsonMap.get("merchant_id");
        String buIdStr = (String) jsonMap.get("buId");
        String month = (String) jsonMap.get("own_month");
        String buNameSheetName = "";
        List<Long> buList=null;
        String userId="";
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}
        
        if (StringUtils.isBlank(buIdStr)) {
            basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month+userId;
        } else {
			BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buIdStr));
			buNameSheetName="("+businessUnitModel.getName()+")";
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+buIdStr+"/"+month+userId;
		}
		
		System.out.println("事业部财务经销存 商家Id="+merchantId+"事业部"+ buNameSheetName+",月份="+month+"---生成excel文件开始----------");
		// 查询商家
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("month",month);
		if (StringUtils.isNotBlank(buIdStr)) {
			paramMap.put("buId", Long.valueOf(buIdStr));
		}
		paramMap.put("buList", buList);
		//查询商家、月份财务汇总报表
		List<Map<String,Object>> summaryList = buFinanceInventorySummaryDao.getBuListForMap(paramMap);
		//查询本期所有仓库
		List<Map<String, Object>> depotList = buFinanceInventorySummaryDao.getBuDepotListForMap(paramMap);


		// 如果已经月结获取 月结量 如果没有月结获取库存余量
		List<Map<String, Object>> depotEndNumList = buFinanceInventorySummaryDao.getBuDepotEndNumForMap(paramMap);
		// 箱托转化,相同货号合并成件
		//期末结存数量转换为map存储便于获取
		Map<String,Integer> depotEndNumMap = new HashMap<String, Integer>();
		for(Map<String, Object> map:depotEndNumList){
			String unit = (String)map.get("unit");
			Long goodsId = (Long)map.get("goods_id");
			String depotIdBuIdGoodsNo = (String)map.get("depot_id_bu_id_goods_no");
			BigDecimal endNum = (BigDecimal)map.get("surplus_num");
			depotEndNumMap.put(depotIdBuIdGoodsNo, endNum!=null?endNum.intValue():0);
		}
		//基础信息
		String sheetName1 = "事业部财务进销存汇总";
		ArrayList<String> columnsList = new ArrayList<String>(){{add("事业部");add("母品牌");
			add("标准品牌");add("二级分类");add("货号");add("标准条码");add("条形码");add("商品名称");add("期初数量");add("期初金额");add("期初单价");
			add("本期采购入库数量");add("来货残损数量");add("事业部移库入数量");
			add("本期采购结转数量");add("本期采购结转金额");add("本期单价");add("本期销售已上架数量");add("出库残损数量");add("事业部移库出数量");
			add("本期销售结转数量");add("本期销售结转金额");add("本月销毁数量");add("盘盈数量");
			add("盘亏数量");add("本期损益结转数量");add("本期损益结转金额");
			add("期末结存数量");add("期末结存金额");
			add("累计采购在途数量");add("累计采购在途金额");add("累计销售在途数量");
			add("累计调拨在途数量");
		}};
		//拼装仓库到标题
		for(Map<String, Object> depotMap:depotList){
			String depotName = (String)depotMap.get("depot_name");
			String buName = (String)depotMap.get("bu_name");
			columnsList.add(depotName+"-"+buName);
		}
		//转为字符串数组
		String[] columns = new String[columnsList.size()];
		columnsList.toArray(columns);

		ArrayList<String> keyList = new ArrayList<String>(){{add("bu_name");add("superior_parent_brand");
			add("brand_name");add("type_name");add("goods_no");add("commbarcode");add("barcode");add("goods_name");add("init_num");add("init_amount");add("price");
			add("warehouse_num");
			add("in_damaged_num");add("move_in_num");
			add("purchase_end_num");add("purchase_end_amount");add("tz_price");add("sale_shelf_num");add("out_damaged_num");add("move_out_num");
			add("sale_end_num");add("sale_end_amount");add("destroy_num");add("profit_num");
			add("loss_num");add("loss_overflow_num");add("loss_overflow_amount");
			add("end_num");add("end_amount");
			add("add_purchase_notshelf_num");
			add("add_purchase_notshelf_amount");
			add("add_sale_noshelf_num");add("add_transfer_noshelf_num");
		}};

		//把仓库拼装到key
		for(Map<String, Object> depotMap:depotList){
			Long depotId = (Long)depotMap.get("depot_id");
			Long buId = (Long)depotMap.get("bu_id");
			String columnName = depotId+""+buId;
			keyList.add(columnName);
		}
		//拼装仓库、货号到汇总list key=depotid
		for(Map<String, Object> valueMap:summaryList){
			String goodsNo = (String)valueMap.get("goods_no");
			Long buId = (Long) valueMap.get("bu_id");
			for(Map<String, Object> depotMap:depotList){
				Long depotId = (Long)depotMap.get("depot_id");
				Long buIdDepot = (Long)depotMap.get("bu_id");
				// 仓库事业部和总表事业部要一样
				if (buId.toString().equals(buIdDepot.toString())) {
					Integer depotEndNum = depotEndNumMap.get(depotId+"_"+buIdDepot+"_"+goodsNo);
					if(depotEndNum==null) depotEndNum=0;
					//拼装货号在本仓库的期末结存数量
					valueMap.put(String.valueOf(depotId+""+buIdDepot),depotEndNum);//把货号仓库期末结存数量存进汇总list
				}else {
					valueMap.put(String.valueOf(depotId+""+buIdDepot),0);//把货号仓库期末结存数量存进汇总list
				}
			}
		}
		//转为字符串数组
		String[] keys = new String[keyList.size()];
		keyList.toArray(keys);

		//查询本期仓库期末结存数量，按仓库、组码分组统计
		List<Map<String, Object>> depotEndNumListA = buFinanceInventorySummaryDao.getBuDepotEndNumForMapA(paramMap);
		//期末结存数量转换为map存储便于获取
		Map<String,Integer> depotEndNumMapA = new HashMap<String, Integer>();
		for(Map<String, Object> map:depotEndNumListA){
			String depotIdBuIdGroupCommbarcode = (String)map.get("depot_id_bu_id_group_commbarcode");
			BigDecimal endNum = (BigDecimal)map.get("surplus_num");
			depotEndNumMapA.put(depotIdBuIdGroupCommbarcode, endNum!=null?endNum.intValue():0);
		}

		//根据组码分组查询财务经销存总表  财务进销存标准条码汇总
		List<Map<String,Object>> summaryListA = buFinanceInventorySummaryDao.getBuListForGroupCommbarcodeMap(paramMap);
		//基础信息
		String sheetName1A = "事业部财务进销存标准条码汇总";
		ArrayList<String> columnsListA = new ArrayList<String>(){{add("事业部");add("母品牌");
			add("标准品牌");add("二级分类");add("标准条码");add("商品名称");add("期初数量");add("期初金额");add("期初单价");
			add("本期采购入库数量");add("来货残损数量");add("事业部移库入数量");
			add("本期采购结转数量");add("本期采购结转金额");add("本期单价");add("本期销售已上架数量");add("出库残损数量");add("事业部移库出数量");
			add("本期销售结转数量");add("本期销售结转金额");add("本月销毁数量");add("盘盈数量");
			add("盘亏数量");add("本期损益结转数量(汇总)");add("本期损益结转金额");
			add("期末结存数量");add("期末结存金额");
			add("累计采购在途数量");add("累计采购在途金额");add("累计销售在途数量");
			add("累计调拨在途数量");
		}};
		//拼装仓库到标题  和 财务总表公用
		for(Map<String, Object> depotMap:depotList){
			String depotName = (String)depotMap.get("depot_name");
			String buName = (String)depotMap.get("bu_name");
			columnsListA.add(depotName+"-"+buName);
		}
		//转为字符串数组
		String[] columnsA = new String[columnsListA.size()];
		columnsListA.toArray(columnsA);

		ArrayList<String> keyListA = new ArrayList<String>(){{add("bu_name");add("superior_parent_brand");
			add("brand_name");add("type_name");add("group_commbarcode");add("goods_name");add("init_num");add("init_amount");add("price");
			add("warehouse_num");
			add("in_damaged_num");add("move_in_num");
			add("purchase_end_num");add("purchase_end_amount");add("tz_price");
			add("sale_shelf_num");add("out_damaged_num");add("move_out_num");
			add("sale_end_num");add("sale_end_amount");add("destroy_num");add("profit_num");
			add("loss_num");add("loss_overflow_num");add("loss_overflow_amount");
			add("end_num");add("end_amount");
			add("add_purchase_notshelf_num");
			add("add_purchase_notshelf_amount");
			add("add_sale_noshelf_num");add("add_transfer_noshelf_num");
		}};

		//把仓库拼装到key
		for(Map<String, Object> depotMap:depotList){
			Long depotId = (Long)depotMap.get("depot_id");
			Long buId = (Long)depotMap.get("bu_id");
			String columnName = depotId+""+buId;
			keyListA.add(columnName);
		}
		//拼装仓库到汇总list key=depotid
		for(Map<String, Object> valueMap:summaryListA){
			String groupCommbarcode = (String)valueMap.get("group_commbarcode");
			Long buId = (Long) valueMap.get("bu_id");
			for(Map<String, Object> depotMap:depotList){
				Long depotId = (Long)depotMap.get("depot_id");
				Long buIdDepot = (Long)depotMap.get("bu_id");
				// 值算本事业部下的仓库
				if (buId.toString().equals(buIdDepot.toString())) {
					Integer depotEndNum = depotEndNumMapA.get(depotId+"_"+buIdDepot+"_"+groupCommbarcode);
					if(depotEndNum==null) depotEndNum=0;
					//拼装货号在本仓库的期末结存数量
					valueMap.put(String.valueOf(depotId+""+buIdDepot),depotEndNum);//把货号仓库期末结存数量存进汇总list
				}else {
					valueMap.put(String.valueOf(depotId+""+buIdDepot),0);//把货号仓库期末结存数量存进汇总list
				}

			}
		}
		//转为字符串数组
		String[] keysA = new String[keyListA.size()];
		keyListA.toArray(keysA);

		//财务进销存入账汇总表 (采购)
		String sheetName2 = "财务进销存入账汇总表 (采购)";	
		String title2=merchantInfo.getName()+month+"月采购入库汇总表";
		String[] columns2={"事业部","供应商","采购币种","采购金额","入库币种","入库金额","采购在途金额","在途金额（本币）","事业部移库入协议金额","事业部移库入金额（本币）"};
		String[] keys2={"bu_name","supplier_name","order_currency","order_amount","account_currency","warehouse_amount","zt_order_amount","zt_warehouse_amount","agreement_amount","amount"};
		//统计 "采购币种","采购金额","入库币种","入库金额"
		List<Map<String, Object>> purchaseSummaryList = buFinanceWarehouseDetailsDao.getPurchaseSummaryExpotr(paramMap);
		// 循环添加合计
		BigDecimal warehouseAmountAll=new BigDecimal("0");
		BigDecimal ztOrdreAmountAll=new BigDecimal("0");BigDecimal ztWarehouseAmountAll=new BigDecimal("0");
		BigDecimal amountAll=new BigDecimal("0");
		for (int i = 0; i < purchaseSummaryList.size(); i++) {
			Map<String, Object> map = purchaseSummaryList.get(i);
			BigDecimal warehouseAmountMap = (BigDecimal) map.get("warehouse_amount");
			BigDecimal ztOrdreAmountMap = (BigDecimal) map.get("zt_order_amount");
			BigDecimal ztWarehouseAmountMap = (BigDecimal) map.get("zt_warehouse_amount");	
			BigDecimal amountMap = (BigDecimal) map.get("amount");
			warehouseAmountAll=warehouseAmountAll.add(warehouseAmountMap);
			ztOrdreAmountAll=ztOrdreAmountAll.add(ztOrdreAmountMap);
			ztWarehouseAmountAll=ztWarehouseAmountAll.add(ztWarehouseAmountMap);
			amountAll=amountAll.add(amountMap);
		}
		Map<String, Object>addMap2=new HashMap<String, Object>();
		addMap2.put("bu_name", "合计");
		addMap2.put("supplier_name", "");
		addMap2.put("order_currency", "");
		addMap2.put("order_amount", "");
		addMap2.put("account_currency", "");
		addMap2.put("warehouse_amount",warehouseAmountAll);
		addMap2.put("zt_order_amount",ztOrdreAmountAll);
		addMap2.put("zt_warehouse_amount", ztWarehouseAmountAll);
		addMap2.put("agreement_amount", "");
		addMap2.put("amount", amountAll);
		purchaseSummaryList.add(addMap2);
		//财务进销存入账汇总表（库存）
		String sheetName2A = "财务进销存入账汇总表（库存）";
		String title2A=merchantInfo.getName()+month+"月库存结转汇总表";
		String[] columns2A={"事业部","渠道","品牌","客商","销售结转出库金额","事业部移库结转出库金额","残损结转出库金额","损益结转出库金额","销毁结转出库金额","尾数差结转金额","本月合计出库金额"};
		String[] keys2A={"bu_name","channel_type","superior_parent_brand","customer_name","out_depot_amount","move_amount","sale_damaged_amount","stock_results_amount","destroy_amount","num_differ","total_out_depot_amount"};
		//统计 "采购币种","采购金额","入库币种","入库金额"
		List<Map<String, Object>> inventorySummaryList = buFinanceSaleShelfDao.getInventorySummaryExpotr(paramMap);
		// 导出重新拼装的数据
		List<Map<String, Object>> inventorySummaryListExportList=new ArrayList<Map<String,Object>>();
		/**
		 * inventorySummaryList 已经按 事业部+母品牌排序+渠道类型 降序排列 判断是否有公共的渠道类型 没有在 本分组后面添加
		 * 并且重新计算 公共渠道的 尾数差结转金额 和 本月合计出库金额
		 */
		Map<String, List<Map<String, Object>>>newInventorySummaryListMap=new HashMap<>();
		// 按 事业部+母品牌分组
		for (Map<String, Object> map : inventorySummaryList) {
			Long buId = (Long) map.get("bu_id");
			String superiorParentBrand = (String) map.get("superior_parent_brand");
			if (StringUtils.isBlank(superiorParentBrand)) superiorParentBrand="";
			String buSupKey=buId+","+superiorParentBrand;
			if (newInventorySummaryListMap.containsKey(buSupKey)) {
				List<Map<String, Object>> newSummaryList = newInventorySummaryListMap.get(buSupKey);
				newSummaryList.add(map);
				newInventorySummaryListMap.put(buSupKey, newSummaryList);
			}else {
				List<Map<String, Object>> newSummaryList =  new  ArrayList<Map<String,Object>>();	
				newSummaryList.add(map);
				newInventorySummaryListMap.put(buSupKey, newSummaryList);
			}						
		}
		// 循环重新计算公共部分的 尾数差结转金额 和 本月合计出库金额
		Set<String> keySet = newInventorySummaryListMap.keySet();
		int k=0;
		BigDecimal outDepotAmountAll=new BigDecimal("0");BigDecimal moveAmountAll=new BigDecimal("0");
		BigDecimal stockResultsAmountAll=new BigDecimal("0");BigDecimal saleDamagedAmountAll=new BigDecimal("0");
		BigDecimal destroyAmountAll=new BigDecimal("0");BigDecimal saleEndAmountAll=new BigDecimal("0");
		BigDecimal numDifferAll=new BigDecimal("0");BigDecimal totalOutDepotAmountAll=new BigDecimal("0");	 
		
		for (String buSupKey : keySet) {			
			List<Map<String, Object>> newInventorySummaryList = newInventorySummaryListMap.get(buSupKey);
			BigDecimal numDiffer=new BigDecimal("0");
			BigDecimal totalOutDepotAmount=new BigDecimal("0");			
			for (int i = 0; i < newInventorySummaryList.size(); i++) {
				Map<String, Object> map = newInventorySummaryList.get(i);
				//Integer numDifferMap= (Integer) map.get("num_differ");// 都是0
				BigDecimal totalOutDepotAmountMap = (BigDecimal) map.get("total_out_depot_amount");
				BigDecimal outDepotAmountMap = (BigDecimal) map.get("out_depot_amount");
				BigDecimal moveAmountMap = (BigDecimal) map.get("move_amount");
				BigDecimal stockResultsAmountMap = (BigDecimal) map.get("stock_results_amount");				
				BigDecimal saleDamagedAmountMap = (BigDecimal) map.get("sale_damaged_amount");				
				BigDecimal destroyAmountMap = (BigDecimal) map.get("destroy_amount");
				BigDecimal saleEndAmountMap = (BigDecimal) map.get("sale_end_amount");
				//本期销售结转金额汇总-(销售结转出库金额+事业部移库结转出库金额+残损结转出库金额)
				BigDecimal subtract = saleEndAmountMap.subtract(outDepotAmountMap).subtract(moveAmountMap).subtract(saleDamagedAmountMap);
				numDiffer=numDiffer.add(subtract);
				//本月合计出库金额=销售结转出库金额+事业部移库结转出库金额+损益结转出库金额+残损结转出库金额+销毁结转出库金额+尾数差结转金额
				totalOutDepotAmount= totalOutDepotAmount.add(totalOutDepotAmountMap);
				
				//最后一条
				if (newInventorySummaryList.size()-1==i) {
					String channelType = (String) map.get("channel_type");
					BigDecimal addTotalOutDepotAmount = totalOutDepotAmountMap.add(numDiffer);
					if (!"公共".equals(channelType)) {
						Map<String, Object>addMap=new HashMap<String, Object>();
						addMap.put("bu_name", (String)map.get("bu_name"));
						addMap.put("superior_parent_brand", (String)map.get("superior_parent_brand"));
						addMap.put("channel_type", "公共");
						addMap.put("customer_name", "/");
						addMap.put("out_depot_amount", new BigDecimal("0"));
						addMap.put("move_amount", new BigDecimal("0"));
						addMap.put("stock_results_amount", new BigDecimal("0"));
						addMap.put("sale_damaged_amount", new BigDecimal("0"));	
						addMap.put("destroy_amount", new BigDecimal("0"));							
						addMap.put("num_differ", numDiffer);
						addMap.put("total_out_depot_amount", addTotalOutDepotAmount);
						inventorySummaryListExportList.add(addMap);
						totalOutDepotAmountAll=totalOutDepotAmountAll.add(addTotalOutDepotAmount);
					}else {
						map.put("num_differ", numDiffer);
						map.put("total_out_depot_amount", addTotalOutDepotAmount);
						inventorySummaryListExportList.add(map);
						totalOutDepotAmountAll=totalOutDepotAmountAll.add(addTotalOutDepotAmount);
					}
				}else {
					totalOutDepotAmountAll=totalOutDepotAmountAll.add(totalOutDepotAmountMap);
					inventorySummaryListExportList.add(map);
				}	
				
				//-----------------------------合计-------------------------				
				outDepotAmountAll=outDepotAmountAll.add(outDepotAmountMap);
				moveAmountAll=moveAmountAll.add(moveAmountMap);
				stockResultsAmountAll=stockResultsAmountAll.add(stockResultsAmountMap);
				saleDamagedAmountAll=saleDamagedAmountAll.add(saleDamagedAmountMap);
				destroyAmountAll=destroyAmountAll.add(destroyAmountMap);
				numDifferAll=numDifferAll.add(subtract);				
				//-----------------------------合计-------------------------
				
				
				// 最后一行添加合计
				if (k==keySet.size()-1&&newInventorySummaryList.size()-1==i) {
					Map<String, Object>addMap=new HashMap<String, Object>();
					addMap.put("bu_name", "合计");
					addMap.put("superior_parent_brand","");
					addMap.put("channel_type", "");
					addMap.put("customer_name", "");
					addMap.put("out_depot_amount", outDepotAmountAll);
					addMap.put("move_amount",moveAmountAll);
					addMap.put("sale_damaged_amount", saleDamagedAmountAll);
					addMap.put("stock_results_amount", stockResultsAmountAll);
					addMap.put("destroy_amount", destroyAmountAll);
					addMap.put("num_differ", numDifferAll);
					addMap.put("total_out_depot_amount", totalOutDepotAmountAll);
					inventorySummaryListExportList.add(addMap);
				}				
			}						
			k+=1;
		}
		
				
		//采购入库明细表
		String sheetName3 = "采购入库明细表";
		String[] columns3={"事业部","母品牌","标准品牌","供应商名称","订单日期","采购订单号","入库单号","单据类型","入库时间","勾稽时间","PO号","发票日期","发票号码","货号","标准条码","商品名称","单位","数量","采购币种","采购单价","采购金额","入库币种","入库单价","入库金额","入库仓库","创建人名称"};
		// (财务经销存)采购入库明细	
		List<BuFinanceWarehouseDetailsModel> warehouseDetailsList = buFinanceWarehouseDetailsDao.getWarehouseDetailExport(paramMap);

		//采购在途明细表
		String sheetName4 = "采购在途明细表";
		String[] columns4={"事业部","母品牌","标准品牌","供应商名称","订单日期","采购订单号","订单完结时间","入库单号","PO号","在途开始日期","发票号码","货号","标准条码","商品名称","单位","数量","采购币种","采购单价","采购金额","入库币种","入库单价","入库金额","入库仓库","创建人名称"};
		// 查询采购在途
		List<BuFinanceAddPurchaseNotshelfDetailsModel> purchaseNotshelfList = buFinanceAddPurchaseNotshelfDetailsDao.getFinanceSaleNotshelfList(paramMap);


		//销售已上架明细表  (说明 销售已上架涉及电商订单 要分页查询)
		String sheetName5 = "销售已上架明细表";
		String[] columns5={"事业部","母品牌","标准品牌","客户名称","平台名称","店铺名称","单据类型","订单日期","销售订单号","外部单号","平台订单号","PO号","上架时间","销售出库单号","出库时间","订单总金额","税费","运费","优惠金额","佣金","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","出库单价","出库金额","出仓仓库","上架人名称","渠道类型","运营类型"};
		String[] keys5={"bu_name","superior_parent_brand","brand_name","customer_name","store_platform_name","shop_name","order_type","order_create_date","order_code","external_code","ex_order_id","po_no","shelf_date","out_order_code","deliver_date","payment","taxes","way_frt_fee","discount","sale_com","goods_no","commbarcode","goods_name","tallying_unit","num","sale_currency","sale_price","sale_amount","out_depot_currency","out_depot_price","out_depot_amount","out_depot_name","shelf_name","channel_type","shop_type_code"};
		int startIndex = 0;
		int pageSize = 10000;//每页10000
		int sheetPageLenth = 10;//每个sheet10页
		int maxSize=100000;//每个文件存放最大记录数
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("merchantId", Long.valueOf(merchantId));
		itemMap.put("ownMonth", month);
		itemMap.put("pageSize", pageSize);
		if (StringUtils.isNotBlank(buIdStr)) {			
			itemMap.put("buId", Long.valueOf(buIdStr));
		}
		itemMap.put("buList", buList);
		List<Map<String,Object>> inItemList = new ArrayList<Map<String,Object>>();
		// 用于判断订单是否存在 存在金额设置为0
		List<String> orderCodeList=new ArrayList<>();
		for(int i=0;i<sheetPageLenth;i++){//30页等于30万
			itemMap.put("startIndex", startIndex);
			List<Map<String,Object>> tempList = buFinanceSaleShelfDao.exportBuFinanceSaleShelfMapList(itemMap);
			if(tempList==null||tempList.size()<=0) break;
			for (Map<String, Object> map : tempList) {
				String shopTypeCode = (String) map.get("shop_type_code");
				if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
					shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"一件代发";
				}
				if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
					shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"POP";
				}
				map.put("shop_type_code", shopTypeCode);
				String orderCode = (String) map.get("order_code");
				if (orderCodeList.contains(orderCode)) {
					map.put("payment", 0);//实付总金额
					map.put("taxes", 0);//税费
					map.put("discount", 0);//优化金额
					map.put("way_frt_fee", 0);//运费
					map.put("sale_com", 0);//运费
				}else {
					orderCodeList.add(orderCode);
				}

			}
			inItemList.addAll(tempList);

			tempList=null;
			startIndex += pageSize;
			System.out.println("0-10万入startIndex="+startIndex);
		}

		// 获取销售已上架退款数据 (退款不用分页)
		
		
		//销售已上架明细表  (说明 销售已上架涉及电商订单 要分页查询)
		String sheetName5A = "2C跨境库外售后退款";
		String[] columns5A={"事业部","母品牌","标准品牌","客户名称","平台名称","店铺名称","单据类型","订单日期","销售订单号","外部单号","平台订单号","出库时间","订单总金额","税费","运费","优惠金额","佣金","货号","标准条码","商品名称","单位","数量","销售币种","退款金额","出仓仓库","渠道类型","运营类型"};
		String[] keys5A={"bu_name","superior_parent_brand","brand_name","customer_name","store_platform_name","shop_name","order_type","order_create_date","order_code","external_code","ex_order_id","deliver_date","payment","taxes","way_frt_fee",
				"discount","sale_com","goods_no","commbarcode","goods_name","tallying_unit","num","sale_currency","sale_amount","out_depot_name","channel_type","shop_type_code"};	
		// 用于判断订单是否存在 存在金额设置为0
		List<String> orderCodeTKList=new ArrayList<>();
		List<Map<String,Object>> saleShelfTKList = buFinanceSaleShelfDao.exportBuFinanceSaleShelfTKMapList(itemMap);
		for (Map<String, Object> map : saleShelfTKList) {
			String shopTypeCode = (String) map.get("shop_type_code");
			if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
				shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"一件代发";
			}
			if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
				shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"POP";
			}
			map.put("shop_type_code", shopTypeCode);
			String orderCode = (String) map.get("order_code");
			if (orderCodeTKList.contains(orderCode)) {
				map.put("payment", 0);//实付总金额
				map.put("taxes", 0);//税费
				map.put("discount", 0);//优化金额
				map.put("way_frt_fee", 0);//运费
				map.put("sale_com", 0);//运费
			}else {
				orderCodeTKList.add(orderCode);
			}
		}
		
		//销售未上架明细表
		String sheetName6 = "销售未上架明细表";
		String[] columns6={"事业部","母品牌","标准品牌","客户名称","订单日期","销售订单号","销售出库单号","销售类型","PO号","出库时间","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","出库单价","出库金额","出仓仓库","创建人名称"};
		List<BuFinanceAddSaleNoshelfDetailsModel> saleNotshelfList = buFinanceAddSaleNoshelfDetailsDao.getBuFinanceAddSaleNoshelfDetails(paramMap);
		//采购残损明细表
		String sheetName7 = "采购残损明细表";
		String[] columns7={"事业部","母品牌","标准品牌","残损类型","采购订单号","入库单号","PO号","入库时间","商品货号","标准条码","商品名称","短缺/残次数量","入库仓库","创建人名称"};	
		List<BuFinancePurchaseDamagedModel> purchaseDamagedList = buFinancePurchaseDamagedDao.getPurchaseDamagedExport(paramMap);
		//出库残损明细表
		String sheetName8 = "出库残损明细表";
		String[] columns8={"事业部","母品牌","标准品牌","订单日期","销售订单号","出库单号","客户","PO号","销售类型","发货时间","上架时间","商品货号","标准条码","商品名称","出库单价","残损数量","缺货数量","销售单价","销售金额","销售币种","残损金额","出仓仓库","上架人名称"};
		List<BuFinanceSaleDamagedModel> saleDamagedList = buFinanceSaleDamagedDao.getsaleDamagedExport(paramMap);
		//盘盈盘亏明细表
		String sheetName9 = "盘盈盘亏明细表";
		String[] columns9={"事业部","母品牌","标准品牌","盘点单据","盘点时间","盘点仓库","商品货号","标准条码","商品名称","盘盈数量","盘亏数量","本期单价","盘点结转金额","批次号","是否坏品","生产日期","失效日期","理货单位"};
		List<BuFinanceTakesStockResultsModel> takesStockResultsList = buFinanceTakesStockResultsDao.getTakesStockResultExport(paramMap);	


		String sheetName10 = "销毁明细表";
		String[] columns10={"事业部","母品牌","标准品牌","商品货号","标准条码","商品条码","商品名称","商品货号","标准条码","调整单据号","调整类型","原始批次号","是否坏品","调整数量","本期单价","销毁结转金额","海外仓理货单位","生产日期","失效日期","调整时间","归属月份","调整仓库名称","创建人名称"};

		List<BuFinanceDestroyModel> financeDestroyList = buFinanceDestroyDao.getDestroyExport(paramMap);	


		String sheetName12 = "累计采购在途明细表";
		String[] columns12={"事业部","母品牌","标准品牌","订单日期","采购订单号","入库单号","PO号","在途开始日期","标准条码","货号","商品名称","单位","数量","采购币种","采购单价","采购金额","入库币种","入库单价","入库金额","入库仓库"};
		List<BuFinanceAddPurchaseNotshelfDetailsModel> addPurchaseNotshelfDetailsList = buFinanceAddPurchaseNotshelfDetailsDao.getAddPurchaseNotshelfExport(paramMap);
		String sheetName13 = "累计销售在途明细表";
		String[] columns13={"事业部","母品牌","标准品牌","订单日期","销售订单号","销售出库单号","销售类型","出库时间","PO号","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","出库单价","出库金额","出仓仓库"};
		List<BuFinanceAddSaleNoshelfDetailsModel> addSaleNoshelfDetailsList = buFinanceAddSaleNoshelfDetailsDao.getAddSaleNoshelfExport(paramMap);
		/**
		 String sheetName14 = "累计销售在途量汇总表";
		 String[] columns14={"商品货号","标准条码","商品名称","期初销售在途数量","期初销售在途金额","本期新增在途量","本期减少在途量","期末销售在途数量","期末销售在途金额","出仓仓库"};
		 FinanceAddSaleNoshelfSummaryModel addSaleNoshelfSummaryModel=new FinanceAddSaleNoshelfSummaryModel();
		 addSaleNoshelfSummaryModel.setMerchantId(Long.valueOf(merchantId));
		 addSaleNoshelfSummaryModel.setMonth(month);
		 List<FinanceAddSaleNoshelfSummaryModel> addSaleNoshelfSummaryList = financeAddSaleNoshelfSummaryDao.list(addSaleNoshelfSummaryModel);
		 */
		String sheetName15 = "本期减少销售在途明细";
		String[] columns15={"事业部","母品牌","标准品牌","订单日期","销售订单号","销售出库单号","销售类型","出库时间","上架时间","PO号","货号","标准条码","商品名称","单位","本期好品数量","本期残次量","本期少货量","数量","销售币种","销售单价","销售金额","出库币种","出库单价","出库金额","出仓仓库","上架人名称"};
		List<BuFinanceDecreaseSaleNoshelfModel> decreaseSaleNoshelfList = buFinanceDecreaseSaleNoshelfDao.getdecreaseSaleNoshelfExport(paramMap);
		/**
		String sheetName16 = "本期减少采购在途明细";
		String[] columns16={"事业部","母品牌","标准品牌","订单日期","采购订单号","入库单号","PO号","发票日期","入库时间","标准条码","货号","标准条码","商品名称","单位","数量","采购币种","采购单价","采购金额","入库币种","入库单价","入库金额","入库仓库"};
		BuFinanceDecreasePurchaseNotshelfModel decreasePurchaseNotshelfModel=new BuFinanceDecreasePurchaseNotshelfModel();
		decreasePurchaseNotshelfModel.setMerchantId(Long.valueOf(merchantId));
		decreasePurchaseNotshelfModel.setMonth(month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			decreasePurchaseNotshelfModel.setBuId(Long.valueOf(buIdStr));
		}	
		List<BuFinanceDecreasePurchaseNotshelfModel> decreasePurchaseNotshelfList = buFinanceDecreasePurchaseNotshelfDao.getDecreasePurchaseNotshelfExport(decreasePurchaseNotshelfModel);
		**/
		String sheetName17 = "累计调拨在途明细";
		String[] columns17 = {"事业部","母品牌","标准品牌","调拨订单号","调拨出单号","调出仓库","调入仓库","出库时间","归属月份","商品名称","商品货号","条码","标准条码","PO号","理货单位","在途数量","出库币种","出库单价","出库金额"};
		List<BuFinanceAddTransferNoshelfDetailsModel> addTransferNoshelfList = buFinanceAddTransferNoshelfDetailsDao.getAddTransferNoshelfExport(paramMap);

		String sheetName17A = "销售渠道汇总";
		String[] columns1A={"事业部","母品牌","标准品牌","客户名称","平台名称","单据类型","标准条码","商品名称","数量"};
		String[] keys117A={"bu_name","superior_parent_brand","brand_name","customer_name","store_platform_name","order_type","commbarcode","goods_name","num"};
		Map<String, Object>parmMap=new HashMap<>();
		parmMap.put("merchantId", Long.valueOf(merchantId));
		parmMap.put("ownMonth", month);
		if (StringUtils.isNotBlank(buIdStr)) {		
			parmMap.put("buId", Long.valueOf(buIdStr));
		}
		parmMap.put("buList", buList);
		List<Map<String, Object>> saleChannelSummaryList = buFinanceSaleShelfDao.exportSaleChannelSummary(parmMap);

		String sheetName18 = "本期事业部移库明细";
		String[] columns18={"移库单号","母品牌","标准品牌","来源业务单号","事业部","移动类型","仓库","移库日期","创建时间","商品货号","标准条码","商品条码","商品名称","销售数量","协议币种","协议单价","协议金额","移库币种","移库单价","移库金额"};
		String[] keys18={"order_code","superior_parent_brand","brand_name","external_code","bu_name","order_type","depot_name","deliver_date","move_create_date","goods_no","commbarcode","barcode","goods_name","num","agreement_currency","agreement_price","agreement_amount","currency","price","amount"};

		int startMoveIndex = 0;
		int pageMoveSize = 10000;//每页10000
		int sheetMovePageLenth = 10;//每个sheet10页
		int maxMoveSize=100000;//每个文件存放最大记录数
		Map<String, Object> itemMoveMap = new HashMap<String, Object>();
		itemMoveMap.put("merchantId", Long.valueOf(merchantId));
		itemMoveMap.put("ownMonth", month);
		itemMoveMap.put("pageSize", pageMoveSize);
		if (StringUtils.isNotBlank(buIdStr)) {		
			itemMoveMap.put("buId", Long.valueOf(buIdStr));
		}
		itemMoveMap.put("buList", buList);
		List<Map<String,Object>> itemMoveList = new ArrayList<Map<String,Object>>();

		for(int i=0;i<sheetMovePageLenth;i++){//30页等于30万
			itemMoveMap.put("startIndex", startMoveIndex);
			List<Map<String,Object>> tempList = buFinanceMoveDetailsDao.exportFinanceMoveDetailsList(itemMoveMap);
			if(tempList==null||tempList.size()<=0) break;
			itemMoveList.addAll(tempList);

			tempList=null;
			startMoveIndex += pageMoveSize;
			System.out.println("0-10万startMoveIndex="+startMoveIndex);
		}

		//美赞FG负库存金额
		String sheetName19 = "美赞FG负库存金额";
		String[] columns19={"事业部","母品牌","标准品牌","二级分类","标准条码","商品名称","期末数量","本期单价","期末金额"};
		String[] keys19={"bu_name","superior_parent_brand","brand_name","type_name","group_commbarcode","goods_name","end_num","end_price","end_amount"};
		List<Map<String, Object>> fgInventList=null;
		if ("0000134".equals(merchantInfo.getTopidealCode())) {
			fgInventList= buFinanceInventorySummaryDao.getFgInventByGroupCommbar(paramMap);
		}
		if (fgInventList==null)fgInventList=new ArrayList<>();
		for (Map<String, Object> map : fgInventList) {
			Long goodsId = (Long) map.get("goods_id");// 随机取一条商品
			BigDecimal endNum = (BigDecimal) map.get("end_num");
			BigDecimal endPrice = (BigDecimal) map.get("end_price");
			if (endNum==null)endNum=new BigDecimal(0);
			if (endPrice==null)endPrice=new BigDecimal(0);
			BigDecimal fgPrice=new BigDecimal(0);
			Long buId = (Long) map.get("bu_id");
			//if (endNum.intValue()>0)fgPrice=endPrice;
			if (endNum.intValue()<0)fgPrice=getFgPrice(buId,goodsId,Long.valueOf(merchantId),month);
			//期末金额：等于本期单价*期末数量
			BigDecimal endAmount=endNum.multiply(fgPrice);
			map.put("end_price", fgPrice);
			map.put("end_amount", endAmount);
		}

		// 生产财务经销存汇总和详情数据
		SXSSFWorkbook wb = DownloadExcelUtil.createBuExcelSXSS6(sheetName1, columns, keys,summaryList,
				sheetName1A, columnsA, keysA,summaryListA,
				sheetName2, columns2,keys2, purchaseSummaryList,title2,
				sheetName2A, columns2A,keys2A, inventorySummaryListExportList,title2A,
				sheetName3, columns3, warehouseDetailsList,
				sheetName4, columns4, purchaseNotshelfList,
				sheetName5, columns5, keys5, inItemList,
				sheetName5A, columns5A, keys5A,saleShelfTKList,
				sheetName6, columns6, saleNotshelfList,
				sheetName7, columns7, purchaseDamagedList,
				sheetName8, columns8, saleDamagedList,
				sheetName9, columns9, takesStockResultsList,
				sheetName10, columns10, financeDestroyList,
				//sheetName11, columns11, addPurchaseNotshelfSummaryList,
				sheetName12, columns12, addPurchaseNotshelfDetailsList,
				sheetName13, columns13, addSaleNoshelfDetailsList,
				//sheetName14, columns14,addSaleNoshelfSummaryList,
				sheetName15, columns15,decreaseSaleNoshelfList,
				//sheetName16, columns16,decreasePurchaseNotshelfList,
				sheetName17,columns17,addTransferNoshelfList,
				sheetName17A,columns1A,keys117A,saleChannelSummaryList,
				sheetName18, columns18, keys18, itemMoveList,
				sheetName19, columns19, keys19,fgInventList);


		summaryListA=null;
		//inventorySummaryAnalysisList=null;
		purchaseSummaryList=null;
		inventorySummaryListExportList=null;
		warehouseDetailsList=null;
		purchaseNotshelfList=null;
		inItemList=null;
		saleNotshelfList=null;
		purchaseDamagedList=null;
		saleDamagedList=null;
		takesStockResultsList=null;
		financeDestroyList=null;
		//addPurchaseNotshelfSummaryList=null;
		addPurchaseNotshelfDetailsList=null;
		addSaleNoshelfDetailsList=null;
		//addSaleNoshelfSummaryList=null;
		decreaseSaleNoshelfList=null;
		//decreasePurchaseNotshelfList=null;
		itemMoveList=null;


		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		//写出文件
		String fileName = "事业部财务进销存汇总"+buNameSheetName+month+".xlsx";
		FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
		wb.write(fileOut);
		fileOut.close();
		wb=null;
		//inItemList=null;
		System.out.println("第一个文件创建结束");

		//统计明细数量超过10万则另外生成过文件存储
		Integer inCount = buFinanceSaleShelfDao.getExportBuFinanceSaleShelfListCount(itemMap);
		if(inCount.intValue()>maxSize){
			int startIndex2 = maxSize;
			int k2=2;//第几个文件
			while(startIndex2<inCount.intValue()){
				createExcelInItem2(buNameSheetName,itemMap,k2,month,basePath,startIndex2,pageSize,sheetName5,columns5,keys5,sheetPageLenth,orderCodeList);
				k2++;
				startIndex2 = startIndex2+maxSize;
			}
		}
		orderCodeList=null;

		// 移库单超过10条生成另外一个文件存储
		int moveCount = buFinanceMoveDetailsDao.getExportBuFinanceMoveDetailsCount(itemMoveMap);
		if(moveCount>maxMoveSize){
			int startMoveIndex2 = maxMoveSize;
			int k2=2;//第几个文件
			while(startMoveIndex2<inCount.intValue()){
				createExcelMoveItem2(buNameSheetName,itemMoveMap,k2,month,basePath,startMoveIndex2,pageMoveSize,sheetName18,columns18,keys18,sheetMovePageLenth);
				k2++;
				startMoveIndex2 = startMoveIndex2+maxMoveSize;
			}
		}


		System.out.println("事业部财务经销存商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

    }

    /**
     * 生成商家、月份的excel文件报表
     */
    public String createSdExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> jsonMap = jsonData;
        Integer merchantId = (Integer) jsonMap.get("merchant_id");
        String month = (String) jsonMap.get("own_month");
        String buIdStr = (String) jsonMap.get("buId");
        String buNameSheetName = "";
        List<Long> buList=null;
        String userId="";
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}
        if (StringUtils.isBlank(buIdStr)) {
            basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month+userId;
        } else {
			BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buIdStr));
			buNameSheetName="("+businessUnitModel.getName()+")";
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+buIdStr+"/"+month+userId;
		}

		System.out.println("SD事业部财务经销存 商家Id="+merchantId+",月份="+month+"---生成excel文件开始----------");
		// 查询商家
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("month",month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			paramMap.put("buId", Long.valueOf(buIdStr));
		}
		paramMap.put("buList", buList);
		//查询商家、月份财务汇总报表
		List<Map<String,Object>> summaryList = buFinanceInventorySummaryDao.getBuListForMap(paramMap);
		//查询本期所有仓库
		List<Map<String, Object>> depotList = buFinanceInventorySummaryDao.getBuDepotListForMap(paramMap);
		// 如果已经月结获取 月结量 如果没有月结获取库存余量
		List<Map<String, Object>> depotEndNumList = buFinanceInventorySummaryDao.getBuDepotEndNumForMap(paramMap);
		// 箱托转化,相同货号合并成件
		//期末结存数量转换为map存储便于获取
		Map<String,Integer> depotEndNumMap = new HashMap<String, Integer>();
		for(Map<String, Object> map:depotEndNumList){
			String unit = (String)map.get("unit");
			Long goodsId = (Long)map.get("goods_id");
			String depotIdBuIdGoodsNo = (String)map.get("depot_id_bu_id_goods_no");
			BigDecimal endNum = (BigDecimal)map.get("surplus_num");
			depotEndNumMap.put(depotIdBuIdGoodsNo, endNum!=null?endNum.intValue():0);
		}
		//基础信息
		String sheetName1 = "SD事业部财务进销存汇总";
		ArrayList<String> columnsList = new ArrayList<String>(){{add("事业部");add("母品牌");
			add("标准品牌");add("二级分类");add("货号");add("标准条码");add("条形码");add("商品名称");add("期初数量");add("期初SD金额");add("期初SD单价");
			add("本期采购入库数量");add("来货残损数量");add("事业部移库入数量");
			add("本期采购结转数量");add("本期采购SD金额");add("本期SD单价");add("本期销售已上架数量");add("出库残损数量");add("事业部移库出数量");
			add("本期销售结转数量");add("本期销售SD金额");add("本月销毁数量");add("盘盈数量");
			add("盘亏数量");add("本期损益结转数量");add("本期损益SD金额");
			add("期末结存数量");add("期末SD金额");
			add("累计采购在途数量");add("累计采购在途SD金额");add("累计销售在途数量");
			add("累计调拨在途数量");
		}};
		//拼装仓库到标题
		for(Map<String, Object> depotMap:depotList){
			String depotName = (String)depotMap.get("depot_name");
			String buName = (String)depotMap.get("bu_name");
			columnsList.add(depotName+"-"+buName);
		}
		//转为字符串数组
		String[] columns = new String[columnsList.size()];
		columnsList.toArray(columns);

		ArrayList<String> keyList = new ArrayList<String>(){{add("bu_name");add("superior_parent_brand");
			add("brand_name");add("type_name");add("goods_no");add("commbarcode");add("barcode");add("goods_name");add("init_num");add("sd_init_amount");add("sd_price");
			add("warehouse_num");
			add("in_damaged_num");add("move_in_num");
			add("purchase_end_num");add("sd_warehouse_amount");add("sd_tz_price");add("sale_shelf_num");add("out_damaged_num");add("move_out_num");
			add("sale_end_num");add("sd_sale_end_amount");add("destroy_num");add("profit_num");
			add("loss_num");add("loss_overflow_num");add("sd_loss_overflow_amount");
			add("end_num");add("sd_end_amount");
			add("add_purchase_notshelf_num");
			add("sd_add_purchase_notshelf_amount");
			add("add_sale_noshelf_num");add("add_transfer_noshelf_num");
		}};

		//把仓库拼装到key
		for(Map<String, Object> depotMap:depotList){
			Long depotId = (Long)depotMap.get("depot_id");
			Long buId = (Long)depotMap.get("bu_id");
			String columnName = depotId+""+buId;
			keyList.add(columnName);
		}
		//拼装仓库、货号到汇总list key=depotid
		for(Map<String, Object> valueMap:summaryList){
			String goodsNo = (String)valueMap.get("goods_no");
			Long buId = (Long) valueMap.get("bu_id");
			for(Map<String, Object> depotMap:depotList){
				Long depotId = (Long)depotMap.get("depot_id");
				Long buIdDepot = (Long)depotMap.get("bu_id");
				// 仓库事业部和总表事业部要一样
				if (buId.toString().equals(buIdDepot.toString())) {
					Integer depotEndNum = depotEndNumMap.get(depotId+"_"+buIdDepot+"_"+goodsNo);
					if(depotEndNum==null) depotEndNum=0;
					//拼装货号在本仓库的期末结存数量
					valueMap.put(String.valueOf(depotId+""+buIdDepot),depotEndNum);//把货号仓库期末结存数量存进汇总list
				}else {
					valueMap.put(String.valueOf(depotId+""+buIdDepot),0);//把货号仓库期末结存数量存进汇总list
				}
			}
		}
		//转为字符串数组
		String[] keys = new String[keyList.size()];
		keyList.toArray(keys);

		//查询本期仓库期末结存数量，按仓库、组码分组统计
		List<Map<String, Object>> depotEndNumListA = buFinanceInventorySummaryDao.getBuDepotEndNumForMapA(paramMap);
		//期末结存数量转换为map存储便于获取
		Map<String,Integer> depotEndNumMapA = new HashMap<String, Integer>();
		for(Map<String, Object> map:depotEndNumListA){
			String depotIdBuIdGroupCommbarcode = (String)map.get("depot_id_bu_id_group_commbarcode");
			BigDecimal endNum = (BigDecimal)map.get("surplus_num");
			depotEndNumMapA.put(depotIdBuIdGroupCommbarcode, endNum!=null?endNum.intValue():0);
		}

		//根据组码分组查询财务经销存总表  财务进销存标准条码汇总
		List<Map<String,Object>> summaryListA = buFinanceInventorySummaryDao.getBuListForGroupCommbarcodeMap(paramMap);
		//基础信息
		String sheetName1A = "SD事业部财务进销存标准条码汇总";
		ArrayList<String> columnsListA = new ArrayList<String>(){{add("事业部");add("母品牌");
			add("标准品牌");add("二级分类");add("标准条码");add("商品名称");add("期初数量");add("期初SD金额");add("期初SD单价");
			add("本期采购入库数量");add("来货残损数量");add("事业部移库入数量");
			add("本期采购结转数量");add("本期采购SD金额");add("本期SD单价");add("本期销售已上架数量");add("出库残损数量");add("事业部移库出数量");
			add("本期销售结转数量");add("本期销售SD金额");add("本月销毁数量");add("盘盈数量");
			add("盘亏数量");add("本期损益结转数量");add("本期损益SD金额");
			add("期末结存数量");add("期末SD金额");
			add("累计采购在途数量");add("累计采购在途SD金额");add("累计销售在途数量");
			add("累计调拨在途数量");
		}};
		//拼装仓库到标题  和 财务总表公用
		for(Map<String, Object> depotMap:depotList){
			String depotName = (String)depotMap.get("depot_name");
			String buName = (String)depotMap.get("bu_name");
			columnsListA.add(depotName+"-"+buName);
		}
		//转为字符串数组
		String[] columnsA = new String[columnsListA.size()];
		columnsListA.toArray(columnsA);

		ArrayList<String> keyListA = new ArrayList<String>(){{add("bu_name");add("superior_parent_brand");
			add("brand_name");add("type_name");add("group_commbarcode");add("goods_name");add("init_num");add("sd_init_amount");add("sd_price");
			add("warehouse_num");
			add("in_damaged_num");add("move_in_num");
			add("purchase_end_num");add("sd_warehouse_amount");add("sd_tz_price");
			add("sale_shelf_num");add("out_damaged_num");add("move_out_num");
			add("sale_end_num");add("sd_sale_end_amount");add("destroy_num");add("profit_num");
			add("loss_num");add("loss_overflow_num");add("sd_loss_overflow_amount");
			add("end_num");add("sd_end_amount");
			add("add_purchase_notshelf_num");
			add("sd_add_purchase_notshelf_amount");
			add("add_sale_noshelf_num");add("add_transfer_noshelf_num");
		}};

		//把仓库拼装到key
		for(Map<String, Object> depotMap:depotList){
			Long depotId = (Long)depotMap.get("depot_id");
			Long buId = (Long)depotMap.get("bu_id");
			String columnName = depotId+""+buId;
			keyListA.add(columnName);
		}
		//拼装仓库到汇总list key=depotid
		for(Map<String, Object> valueMap:summaryListA){
			String groupCommbarcode = (String)valueMap.get("group_commbarcode");
			Long buId = (Long) valueMap.get("bu_id");
			for(Map<String, Object> depotMap:depotList){
				Long depotId = (Long)depotMap.get("depot_id");
				Long buIdDepot = (Long)depotMap.get("bu_id");
				// 值算本事业部下的仓库
				if (buId.toString().equals(buIdDepot.toString())) {
					Integer depotEndNum = depotEndNumMapA.get(depotId+"_"+buIdDepot+"_"+groupCommbarcode);
					if(depotEndNum==null) depotEndNum=0;
					//拼装货号在本仓库的期末结存数量
					valueMap.put(String.valueOf(depotId+""+buIdDepot),depotEndNum);//把货号仓库期末结存数量存进汇总list
				}else {
					valueMap.put(String.valueOf(depotId+""+buIdDepot),0);//把货号仓库期末结存数量存进汇总list
				}

			}
		}
		//转为字符串数组
		String[] keysA = new String[keyListA.size()];
		keyListA.toArray(keysA);


		//采购入库明细表
		String sheetName3 = "采购入库明细表";
		String[] columns3={"事业部","母品牌","标准品牌","供应商名称","订单日期","采购订单号","入库单号","单据类型","入库时间","勾稽时间","PO号","发票日期","发票号码","货号","标准条码","商品名称","单位","数量","采购币种","入库币种","采购SD金额（本币）","入库仓库","创建人名称"};
		// (财务经销存)采购入库明细
		List<BuFinanceWarehouseDetailsModel> warehouseDetailsList = buFinanceWarehouseDetailsDao.getWarehouseDetailExport(paramMap);
		String sheetName3A = "采购SD入库明细表";
		String[] columns3A={"SD单据编码","单据类型","事业部","标准品牌","母品牌","供应商名称","采购订单号","入库时间","PO号","货号","标准条码","商品名称","入库数量","SD类型","入库币种","SD金额（本币）","采购币种","SD金额（原币）"};
		List<String> columns3AList=new ArrayList<String>(Arrays.asList(columns3A));
		List<String> List3A = new ArrayList<String>(){{add("sd_order_code");add("order_type");add("bu_name");add("brand_name");
			add("superior_parent_brand");add("supplier_name");add("order_code");
			add("warehouse_create_date");add("po_no");add("goods_no");add("commbarcode");add("goods_name");
			add("warehouse_num");add("sd_type_name");add("warehouse_currency");add("amount");add("order_currency");add("order_amount");
		}};
		
		// 从新填写各个类型金额
		List<Map<String, Object>> buFinanceSdWarehouseDetailsMapList = buFinanceSdWarehouseDetailsDao.getBuFinanceSdWarehouseDetailsMap(paramMap);
		for (Map<String, Object> map : buFinanceSdWarehouseDetailsMapList) {
			String orderType = (String) map.get("order_type");
			String orderName = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseSdOrder_typeList, orderType);
			map.put("order_type", orderName);
		}


      
		String sheetNameA4 = "累计采购SD在途明细表";
		String[] columnsA4={"SD单据编码","单据类型","事业部","标准品牌","母品牌","供应商名称","采购订单号","入库时间","PO号","货号","标准条码","商品名称","在途数量","SD类型","SD金额"};
		List<String> columns4AList=new ArrayList<String>(Arrays.asList(columnsA4));
		List<String> ListA4 = new ArrayList<String>(){{add("sd_order_code");add("order_type");add("bu_name");add("brand_name");
			add("superior_parent_brand");add("supplier_name");add("order_code");
			add("warehouse_create_date");add("po_no");add("goods_no");add("commbarcode");add("goods_name");
			add("warehouse_num");add("sd_type_name");add("amount");
		}};

		//获取(事业部财务经销存)采购在途SD明细 导出数据
	List<Map<String, Object>> buFinanceSdAddPurchaseNotshelfList = buFinanceSdAddPurchaseNotshelfDao.getBuFinanceSdAddPurchaseNotshelf(paramMap);
		//采购在途明细表
		String sheetName4 = "采购在途明细表";
		String[] columns4={"事业部","母品牌","标准品牌","供应商名称","订单日期","采购订单号","订单完结时间","入库单号","PO号","在途开始日期","发票号码","货号","标准条码","商品名称","单位","数量","采购币种","入库币种","采购SD金额（本币）","入库仓库","创建人名称"};
		// 查询采购在途
		List<BuFinanceAddPurchaseNotshelfDetailsModel> purchaseNotshelfList = buFinanceAddPurchaseNotshelfDetailsDao.getFinanceSaleNotshelfList(paramMap);
		//销售已上架明细表  (说明 销售已上架涉及电商订单 要分页查询)
		String sheetName5 = "销售已上架明细表";
		String[] columns5={"事业部","母品牌","标准品牌","客户名称","平台名称","店铺名称","单据类型","订单日期","销售订单号","外部单号","平台订单号","PO号","上架时间","销售出库单号","出库时间","订单总金额","税费","运费","优惠金额","佣金","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","本期SD单价","本期SD金额","出仓仓库","上架人名称","渠道类型","运营类型"};
		String[] keys5={"bu_name","superior_parent_brand","brand_name","customer_name","store_platform_name","shop_name","order_type","order_create_date","order_code","external_code","ex_order_id","po_no","shelf_date","out_order_code","deliver_date","payment","taxes","way_frt_fee","discount","sale_com","goods_no","commbarcode","goods_name","tallying_unit","num","sale_currency","sale_price","sale_amount","out_depot_currency","sd_order_price","sd_order_amount","out_depot_name","shelf_name","channel_type","shop_type_code"};
		int startIndex = 0;
		int pageSize = 10000;//每页10000
		int sheetPageLenth = 10;//每个sheet10页
		int maxSize=100000;//每个文件存放最大记录数
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("merchantId", Long.valueOf(merchantId));
		itemMap.put("ownMonth", month);
		itemMap.put("pageSize", pageSize);
		if (StringUtils.isNotBlank(buIdStr)) {			
			itemMap.put("buId", Long.valueOf(buIdStr));
		}
		itemMap.put("buList", buList);
		List<Map<String,Object>> inItemList = new ArrayList<Map<String,Object>>();
		// 用于判断订单是否存在 存在金额设置为0
		List<String> orderCodeList=new ArrayList<>();
		for(int i=0;i<sheetPageLenth;i++){//30页等于30万
			itemMap.put("startIndex", startIndex);
			List<Map<String,Object>> tempList = buFinanceSaleShelfDao.exportBuFinanceSaleShelfMapList(itemMap);
			if(tempList==null||tempList.size()<=0) break;
			for (Map<String, Object> map : tempList) {
				String shopTypeCode = (String) map.get("shop_type_code");
				if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
					shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"一件代发";
				}
				if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
					shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"POP";
				}
				map.put("shop_type_code", shopTypeCode);
				String orderCode = (String) map.get("order_code");
				if (orderCodeList.contains(orderCode)) {
					map.put("payment", 0);//实付总金额
					map.put("taxes", 0);//税费
					map.put("discount", 0);//优化金额
					map.put("way_frt_fee", 0);//运费
					map.put("sale_com", 0);//运费
				}else {
					orderCodeList.add(orderCode);
				}

			}
			inItemList.addAll(tempList);

			tempList=null;
			startIndex += pageSize;
			System.out.println("0-10万入startIndex="+startIndex);
		}



		//销售未上架明细表
		String sheetName6 = "销售未上架明细表";
		String[] columns6={"事业部","母品牌","标准品牌","客户名称","订单日期","销售订单号","销售出库单号","销售类型","PO号","出库时间","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","本期SD单价","本期SD金额","出仓仓库","创建人名称"};

		List<BuFinanceAddSaleNoshelfDetailsModel> saleNotshelfList = buFinanceAddSaleNoshelfDetailsDao.getBuFinanceAddSaleNoshelfDetails(paramMap);
		//采购残损明细表
		String sheetName7 = "采购残损明细表";
		String[] columns7={"事业部","母品牌","标准品牌","残损类型","采购订单号","入库单号","PO号","入库时间","商品货号","标准条码","商品名称","本期SD单价","短缺/残次数量","短缺/残次SD金额","入库仓库","创建人名称"};
		BuFinancePurchaseDamagedModel financePurchaseDamagedModel=new BuFinancePurchaseDamagedModel();	
		List<BuFinancePurchaseDamagedModel> purchaseDamagedList = buFinancePurchaseDamagedDao.getPurchaseDamagedExport(paramMap);
		//出库残损明细表
		String sheetName8 = "出库残损明细表";
		String[] columns8={"事业部","母品牌","标准品牌","订单日期","销售订单号","出库单号","客户","PO号","销售类型","发货时间","上架时间","商品货号","标准条码","商品名称","本期SD单价","残损数量","缺货数量","销售单价","销售金额","销售币种","本期SD金额","出仓仓库","上架人名称"};
		BuFinanceSaleDamagedModel financeSaleDamagedModel=new BuFinanceSaleDamagedModel();
		financeSaleDamagedModel.setMerchantId(Long.valueOf(merchantId));
		financeSaleDamagedModel.setMonth(month);
		List<BuFinanceSaleDamagedModel> saleDamagedList = buFinanceSaleDamagedDao.getsaleDamagedExport(paramMap);

		//盘盈盘亏明细表
		String sheetName9 = "盘盈盘亏明细表";
		String[] columns9={"事业部","母品牌","标准品牌","盘点单据","盘点时间","盘点仓库","商品货号","标准条码","商品名称","盘盈数量","盘亏数量","本期单价","盘点结转金额","批次号","是否坏品","生产日期","失效日期","理货单位"};
		List<BuFinanceTakesStockResultsModel> takesStockResultsList = buFinanceTakesStockResultsDao.getTakesStockResultExport(paramMap);

		String sheetName10 = "销毁明细表";
		String[] columns10={"事业部","母品牌","标准品牌","商品货号","标准条码","商品条码","商品名称","商品货号","标准条码","调整单据号","调整类型","原始批次号","是否坏品","调整数量","本期单价","销毁结转金额","海外仓理货单位","生产日期","失效日期","调整时间","归属月份","调整仓库名称","创建人名称"};
		List<BuFinanceDestroyModel> financeDestroyList = buFinanceDestroyDao.getDestroyExport(paramMap);


		String sheetName12 = "累计采购在途明细表";
		String[] columns12={"事业部","母品牌","标准品牌","订单日期","采购订单号","入库单号","PO号","在途开始日期","标准条码","货号","商品名称","单位","数量","采购币种","入库币种","采购SD金额（本币）","入库仓库"};
		List<BuFinanceAddPurchaseNotshelfDetailsModel> addPurchaseNotshelfDetailsList = buFinanceAddPurchaseNotshelfDetailsDao.getAddPurchaseNotshelfExport(paramMap);
		String sheetName13 = "累计销售在途明细表";
		String[] columns13={"事业部","母品牌","标准品牌","订单日期","销售订单号","销售出库单号","销售类型","出库时间","PO号","货号","标准条码","商品名称","单位","数量","销售币种","销售单价","销售金额","出库币种","本期SD单价","本期SD金额","出仓仓库"};
		List<BuFinanceAddSaleNoshelfDetailsModel> addSaleNoshelfDetailsList = buFinanceAddSaleNoshelfDetailsDao.getAddSaleNoshelfExport(paramMap);
		String sheetName15 = "本期减少销售在途明细";
		String[] columns15={"事业部","母品牌","标准品牌","订单日期","销售订单号","销售出库单号","销售类型","出库时间","上架时间","PO号","货号","标准条码","商品名称","单位","本期好品数量","本期残次量","本期少货量","数量","销售币种","销售单价","销售金额","出库币种","本期SD单价","本期SD金额","出仓仓库","上架人名称"};
	
		List<BuFinanceDecreaseSaleNoshelfModel> decreaseSaleNoshelfList = buFinanceDecreaseSaleNoshelfDao.getdecreaseSaleNoshelfExport(paramMap);
		/**
		String sheetName16 = "本期减少采购在途明细";
		String[] columns16={"事业部","母品牌","标准品牌","订单日期","采购订单号","入库单号","PO号","发票日期","入库时间","标准条码","货号","标准条码","商品名称","单位","数量","采购币种","入库币种","采购SD金额（本币）","入库仓库"};
		BuFinanceDecreasePurchaseNotshelfModel decreasePurchaseNotshelfModel=new BuFinanceDecreasePurchaseNotshelfModel();
		decreasePurchaseNotshelfModel.setMerchantId(Long.valueOf(merchantId));
		decreasePurchaseNotshelfModel.setMonth(month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			decreasePurchaseNotshelfModel.setBuId(Long.valueOf(buIdStr));
		}	
		List<BuFinanceDecreasePurchaseNotshelfModel> decreasePurchaseNotshelfList = buFinanceDecreasePurchaseNotshelfDao.getDecreasePurchaseNotshelfExport(decreasePurchaseNotshelfModel);
**/
		String sheetName17 = "累计调拨在途明细";
		String[] columns17 = {"事业部","母品牌","标准品牌","调拨订单号","调拨出单号","调出仓库","调入仓库","出库时间","归属月份","商品名称","商品货号","条码","标准条码","PO号","理货单位","在途数量","出库币种","本期SD单价","本期SD金额"};
		List<BuFinanceAddTransferNoshelfDetailsModel> addTransferNoshelfList = buFinanceAddTransferNoshelfDetailsDao.getAddTransferNoshelfExport(paramMap);


		String sheetName17A = "销售渠道汇总";
		String[] columns1A={"事业部","母品牌","标准品牌","客户名称","平台名称","单据类型","标准条码","商品名称","数量"};
		String[] keys117A={"bu_name","superior_parent_brand","brand_name","customer_name","store_platform_name","order_type","commbarcode","goods_name","num"};
		Map<String, Object>parmMap=new HashMap<>();
		parmMap.put("merchantId", Long.valueOf(merchantId));
		parmMap.put("ownMonth", month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			parmMap.put("buId", Long.valueOf(buIdStr));
		}
		parmMap.put("buList", buList);
		List<Map<String, Object>> saleChannelSummaryList = buFinanceSaleShelfDao.exportSaleChannelSummary(parmMap);

		String sheetName18 = "本期事业部移库明细";
		String[] columns18={"移库单号","母品牌","标准品牌","来源业务单号","事业部","移动类型","仓库","移库日期","创建时间","商品货号","标准条码","商品条码","商品名称","销售数量","协议币种","协议单价","协议金额","移库币种","移库单价","移库金额"};
		String[] keys18={"order_code","superior_parent_brand","brand_name","external_code","bu_name","order_type","depot_name","deliver_date","move_create_date","goods_no","commbarcode","barcode","goods_name","num","agreement_price","agreement_amount","currency","price","amount"};

		int startMoveIndex = 0;
		int pageMoveSize = 10000;//每页10000
		int sheetMovePageLenth = 10;//每个sheet10页
		int maxMoveSize=100000;//每个文件存放最大记录数
		Map<String, Object> itemMoveMap = new HashMap<String, Object>();
		itemMoveMap.put("merchantId", Long.valueOf(merchantId));
		itemMoveMap.put("ownMonth", month);
		itemMoveMap.put("pageSize", pageMoveSize);
		if (StringUtils.isNotBlank(buIdStr)) {			
			itemMoveMap.put("buId", Long.valueOf(buIdStr));
		}
		itemMoveMap.put("buList", buList);
		List<Map<String,Object>> itemMoveList = new ArrayList<Map<String,Object>>();

		for(int i=0;i<sheetMovePageLenth;i++){//30页等于30万
			itemMoveMap.put("startIndex", startMoveIndex);
			List<Map<String,Object>> tempList = buFinanceMoveDetailsDao.exportFinanceMoveDetailsList(itemMoveMap);
			if(tempList==null||tempList.size()<=0) break;
			itemMoveList.addAll(tempList);

			tempList=null;
			startMoveIndex += pageMoveSize;
			System.out.println("0-10万startMoveIndex="+startMoveIndex);
		}
		//美赞FG负库存金额
        	/*1、其中事业部、母品牌、 标准品牌、 二级分类 、标准条码、 商品名称、 期末数量均取值原有货号维度汇总表数据（按标准条码做汇总，有组码则按组码统计）；本期单价、期末金额取值逻辑即数据生成逻辑如下：
    		（1）本期单价：当期末数量为负数时，以报表中商品货号查询库位映射表中的库位货号，找到对应的原货号，取原货号库位类型为常规品的本期加权单价。常规品有N20的优先取N20，没有N20再取N19；
    		当期末数量为正数时，直接取原有进销存汇总表对应的本期加权单价（即直接拷贝原有汇总表数据）；
    		（2）按照标准条码汇总时，有组码则按组码统计，单价随机取一个（现有逻辑下，组码下对应的标准条码本期加权单价均一致；
    		（3）期末金额：等于本期单价*期末数量；*/
		String sheetName19 = "美赞FG负库存金额";
		String[] columns19={"事业部","母品牌","标准品牌","二级分类","标准条码","商品名称","期末数量","本期单价","期末金额"};
		String[] keys19={"bu_name","superior_parent_brand","brand_name","type_name","group_commbarcode","goods_name","end_num","end_price","end_amount"};
	
		List<Map<String, Object>> fgInventList=null;
		if ("0000134".equals(merchantInfo.getTopidealCode())) {
			fgInventList= buFinanceInventorySummaryDao.getFgInventByGroupCommbar(paramMap);
		}
		if (fgInventList==null)fgInventList=new ArrayList<>();
		for (Map<String, Object> map : fgInventList) {
			Long goodsId = (Long) map.get("goods_id");// 随机取一条商品
			BigDecimal endNum = (BigDecimal) map.get("end_num");
			BigDecimal endPrice = (BigDecimal) map.get("end_price");
			Long buId = (Long) map.get("bu_id");
			if (endNum==null)endNum=new BigDecimal(0);
			if (endPrice==null)endPrice=new BigDecimal(0);
			BigDecimal fgPrice=new BigDecimal(0);
			//if (endNum.intValue()>0)fgPrice=endPrice;
			if (endNum.intValue()<0)fgPrice=getFgPrice(buId,goodsId,Long.valueOf(merchantId),month);
			//期末金额：等于本期单价*期末数量
			BigDecimal endAmount=endNum.multiply(fgPrice);
			map.put("end_price", fgPrice);
			map.put("end_amount", endAmount);
		}
				
		// 生成Sd财务经销存汇总和详情数据
		SXSSFWorkbook wb = DownloadExcelUtil.createBuSdExcelSXSS6(sheetName1, columns, keys,summaryList,
				sheetName1A, columnsA, keysA,summaryListA,
				/*sheetName2, columns2, inventorySummaryAnalysisList,*/
				sheetName3, columns3, warehouseDetailsList,
				sheetName3A, columns3A, List3A,buFinanceSdWarehouseDetailsMapList,
				sheetNameA4, columnsA4, ListA4,buFinanceSdAddPurchaseNotshelfList,
				sheetName4, columns4, purchaseNotshelfList,
				sheetName5, columns5, keys5, inItemList,
				sheetName6, columns6, saleNotshelfList,
				sheetName7, columns7, purchaseDamagedList,
				sheetName8, columns8, saleDamagedList,
				sheetName9, columns9, takesStockResultsList,
				sheetName10, columns10, financeDestroyList,
				//sheetName11, columns11, addPurchaseNotshelfSummaryList,
				sheetName12, columns12, addPurchaseNotshelfDetailsList,
				sheetName13, columns13, addSaleNoshelfDetailsList,
				//sheetName14, columns14,addSaleNoshelfSummaryList,
				sheetName15, columns15,decreaseSaleNoshelfList,
				//sheetName16, columns16,decreasePurchaseNotshelfList,
				sheetName17,columns17,addTransferNoshelfList,
				sheetName17A,columns1A,keys117A,saleChannelSummaryList,
				sheetName18, columns18, keys18, itemMoveList,
				sheetName19, columns19, keys19,fgInventList);


		summaryListA=null;
		//inventorySummaryAnalysisList=null;
		warehouseDetailsList=null;
		purchaseNotshelfList=null;
		inItemList=null;
		saleNotshelfList=null;
		purchaseDamagedList=null;
		saleDamagedList=null;
		takesStockResultsList=null;
		financeDestroyList=null;
		//addPurchaseNotshelfSummaryList=null;
		addPurchaseNotshelfDetailsList=null;
		addSaleNoshelfDetailsList=null;
		//addSaleNoshelfSummaryList=null;
		decreaseSaleNoshelfList=null;
		//decreasePurchaseNotshelfList=null;
		itemMoveList=null;


		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		//写出文件
		String fileName = "SD事业部财务进销存汇总"+buNameSheetName+month+".xlsx";
		FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
		wb.write(fileOut);
		fileOut.close();
		wb=null;
		//inItemList=null;
		System.out.println("第一个文件创建结束");
		//统计明细数量超过10万则另外生成过文件存储
		Integer inCount = buFinanceSaleShelfDao.getExportBuFinanceSaleShelfListCount(itemMap);
		if(inCount.intValue()>maxSize){
			int startIndex2 = maxSize;
			int k2=2;//第几个文件
			while(startIndex2<inCount.intValue()){
				createExcelInItem2(buNameSheetName,itemMap,k2,month,basePath,startIndex2,pageSize,sheetName5,columns5,keys5,sheetPageLenth,orderCodeList);
				k2++;
				startIndex2 = startIndex2+maxSize;
			}
		}
		orderCodeList=null;

		// 移库单超过10条生成另外一个文件存储
		int moveCount = buFinanceMoveDetailsDao.getExportBuFinanceMoveDetailsCount(itemMoveMap);
		if(moveCount>maxMoveSize){
			int startMoveIndex2 = maxMoveSize;
			int k2=2;//第几个文件
			while(startMoveIndex2<inCount.intValue()){
				createExcelMoveItem2(buNameSheetName,itemMoveMap,k2,month,basePath,startMoveIndex2,pageMoveSize,sheetName18,columns18,keys18,sheetMovePageLenth);
				k2++;
				startMoveIndex2 = startMoveIndex2+maxMoveSize;
			}
		}

		System.out.println("事业部财务经销存商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

	}


	/**
	 * 生成商家、月份的excel 事业部财务利息经销存报表
	 */
	public String createInterestTask(FileTaskMongo task, String basePath) throws Exception {
		//解析json参数
		JSONObject jsonData = JSONObject.fromObject(task.getParam());
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchant_id");
		String month = (String) jsonMap.get("own_month");
		String buIdStr = (String) jsonMap.get("buId");
		List<Long> buList=null;
        String userId="";
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}		
		String buNameSheetName = "";
		if (StringUtils.isBlank(buIdStr)) {
			basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month+userId;
		} else {
			BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buIdStr));
			buNameSheetName="("+businessUnitModel.getName()+")";
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+buIdStr+"/"+month+userId;
		}

		System.out.println("SD事业部财务经销存 商家Id="+merchantId+",月份="+month+"---生成excel文件开始----------");
		// 查询商家
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("month",month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			paramMap.put("buId", Long.valueOf(buIdStr));
		}
		//查询商家、月份财务汇总报表
		String sheetName1 = "利息进销存汇总表";																																														
		String[] columns1={"事业部","母品牌","标准品牌","二级分类","标准条码","商品名称","期初数量","期初金额",
				"利息期初金额","期初利息单价","本期采购结转数量","本期采购结转金额","本期采购结转利息金额","本期加权单价",
				"本期利息单价","本期销售结转数量","本期销售结转金额","本期销售结转利息金额","本期损益结转数量","本期损益结转金额",
				"本期损益结转利息金额","期末结存数量","期末结转金额","期末结存利息金额"};
		String[] keys1={"bu_name","superior_parent_brand","brand_name","type_name","group_commbarcode","goods_name","init_num","init_amount",
				"sd_interest_init_amount","sd_interest_price","purchase_end_num","purchase_end_amount","sd_interest_purchase_end_amount","tz_price",
				"sd_interest_tz_price","sale_end_num","sale_end_amount","sd_interest_sale_end_amount","loss_overflow_num","loss_overflow_amount",
				"sd_interest_loss_overflow_amount","end_num","end_amount","sd_interest_end_amount"};
		Map<String, Object>parmMap=new HashMap<>();
		parmMap.put("merchantId", Long.valueOf(merchantId));
		parmMap.put("ownMonth", month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			parmMap.put("buId", Long.valueOf(buIdStr));
		}
		paramMap.put("buList", buList);
		//根据组码分组查询财务经销存总表  财务进销存标准条码汇总
		List<Map<String,Object>> summaryList = buFinanceInventorySummaryDao.getBuListForGroupCommbarcodeMap(paramMap);
		
		List<Map<String, Object>>excelMapList=new ArrayList<>();
		Map<String, Object> map1=new HashMap<>();
		map1.put("sheetName", sheetName1);
		map1.put("columns", columns1);
		map1.put("keys", keys1);
		map1.put("list", summaryList);
		excelMapList.add(map1);
		
		String sheetName2 = "采购SD入库明细表";
		String[] columns2={"SD单据编码","单据类型","事业部","标准品牌","母品牌","供应商名称","采购订单号","入库时间",
				"PO号","货号","标准条码","商品名称","入库数量","SD类型","SD金额"};
		String[] keys2={"sd_order_code","order_type","bu_name","brand_name","superior_parent_brand","supplier_name","order_code","warehouse_create_date",
				"po_no","goods_no","commbarcode","goods_name","warehouse_num","sd_type_name","amount"};

		// 查询 类型是  "INT"("融资利息"）的 采购sd入库明细
		paramMap.put("sdTypeName", "INT");
		// 从新填写各个类型金额
		List<Map<String, Object>> buFinanceSdWarehouseDetailsMapList = buFinanceSdWarehouseDetailsDao.getBuFinanceSdWarehouseDetailsMap(paramMap);
		for (Map<String, Object> map : buFinanceSdWarehouseDetailsMapList) {
			String orderType = (String) map.get("order_type");
			String orderName = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseSdOrder_typeList, orderType);
			map.put("order_type", orderName);
		}
		
		Map<String, Object> map2=new HashMap<>();
		map2.put("sheetName", sheetName2);
		map2.put("columns", columns2);
		map2.put("keys", keys2);
		map2.put("list", buFinanceSdWarehouseDetailsMapList);
		excelMapList.add(map2);
		
		String sheetName3 = "采购SD在途明细表"; 
		String[] columns3={"SD单据编码","单据类型","事业部","标准品牌","母品牌","供应商名称","采购订单号","入库时间","PO号","货号","标准条码","商品名称","在途数量","SD类型","SD金额"};

		String[] keys3={"sd_order_code","order_type","bu_name","brand_name","superior_parent_brand","supplier_name","order_code",
				"warehouse_create_date","po_no","goods_no","commbarcode","goods_name","warehouse_num","sd_type_name","amount"};
		
		List<Map<String, Object>> buFinanceSdAddPurchaseNotshelfList = buFinanceSdAddPurchaseNotshelfDao.getBuFinanceSdAddPurchaseNotshelf(paramMap);
		Map<String, Object> map3=new HashMap<>();
		map3.put("sheetName", sheetName3);
		map3.put("columns", columns3);
		map3.put("keys", keys3);
		map3.put("list", buFinanceSdAddPurchaseNotshelfList);
		excelMapList.add(map3);
		
		
		
		// 生成Sd财务经销存汇总和详情数据
		SXSSFWorkbook wb = DownloadExcelUtil.createInterestTask(excelMapList);
		summaryList=null;
		buFinanceSdWarehouseDetailsMapList=null;
		buFinanceSdAddPurchaseNotshelfList=null;;
		excelMapList=null;

		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		//写出文件
		String fileName = "SD事业部财务利息进销存汇总"+buNameSheetName+month+".xlsx";
		FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
		wb.write(fileOut);
		fileOut.close();
		wb=null;
		System.out.println("事业部财务利息经销存商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

	}

	
	
	/**
	 * 生成商家、月份的excel 美赞成本差异导出
	 */
	public String createCostDifferenceTask(FileTaskMongo task, String basePath) throws Exception {
		//解析json参数
		JSONObject jsonData = JSONObject.fromObject(task.getParam());
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchant_id");
		String month = (String) jsonMap.get("own_month");
		String buIdStr = (String) jsonMap.get("buId");
		List<Long> buList=null;
        String userId="";
        
        BusinessUnitModel businessUnit0=new BusinessUnitModel();
        businessUnit0.setCode("E080600");
        businessUnit0 = businessUnitDao.searchByModel(businessUnit0);
        BusinessUnitModel businessUnit1=new BusinessUnitModel();
        businessUnit1.setCode("E080602");
        businessUnit1 = businessUnitDao.searchByModel(businessUnit1);
        
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	List<Long> buListAll = userBuRelMongoDao.getBuListByUser(task.getUserId());
        	if (buListAll.contains(businessUnit0.getId())||buListAll.contains(businessUnit1.getId())) {
        		buList=new ArrayList<Long>();
			}
        	if (buListAll.contains(businessUnit0.getId())) {
        		buList.add(businessUnit0.getId());
			}
        	if (buListAll.contains(businessUnit1.getId())) {
        		buList.add(businessUnit1.getId());
			}

		}
        
		String buNameSheetName = "";
		if (StringUtils.isBlank(buIdStr)) {
			basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month+userId;
		} else {
			BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buIdStr));
			buNameSheetName="("+businessUnitModel.getName()+")";
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+buIdStr+"/"+month+userId;
		}

		System.out.println("SD事业部财务经销存 商家Id="+merchantId+",月份="+month+"---生成excel文件开始----------");
		// 查询商家
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("month",month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			paramMap.put("buId", Long.valueOf(buIdStr));
		}
		//查询商家、月份财务入库成本差异
		String sheetName1 = "入库成本差异";	
																	
		String[] columns1={"公司","事业部","报表月份","采购订单号","采购入库单号","条形码","库位类型","供应商","PO号",
				"入库数量","采购单价","采购币种","成本单价","入库成本金额","单价差异","金额差异"};

		String[] keys1={"merchant_name","bu_name","month","order_code","warehouse_code","barcode","stock_location_type_name","supplier_name",
				"po_no","warehouse_num","order_price","order_currency","warehouse_price","warehouse_amount","difference_price","difference_amount"};
		Map<String, Object>parmMap=new HashMap<>();
		parmMap.put("merchantId", Long.valueOf(merchantId));
		parmMap.put("month", month);
		if (StringUtils.isNotBlank(buIdStr)) {			
			parmMap.put("buId", Long.valueOf(buIdStr));
		}
		paramMap.put("buList", buList);
		//查询入库成本差异
		List<Map<String,Object>> inCostDifferenceMapList=new ArrayList<Map<String,Object>>();
		// 出库差异
		List<Map<String, Object>> outCostDifferenceMapList = new ArrayList<Map<String,Object>>();
		// 焯烨的才导出 
		if ("0000134".equals(merchantInfo.getTopidealCode())) {
			inCostDifferenceMapList = buFinanceWarehouseDetailsDao.getInCostDifferenceExport(paramMap);
			outCostDifferenceMapList = buFinanceSaleShelfDao.getOutCostDifferenceExport(paramMap);
		}

		List<Map<String, Object>>excelMapList=new ArrayList<>();
		Map<String, Object> map1=new HashMap<>();
		map1.put("sheetName", sheetName1);
		map1.put("columns", columns1);
		map1.put("keys", keys1);
		map1.put("list", inCostDifferenceMapList);
		excelMapList.add(map1);
		
		String sheetName2 = "出库成本差异";
		String[] columns2={"公司","事业部","报表月份","明细类型","单据类型","客户名称","电商平台","系统单号","外部订单号","条行码","库位类型",
				"出库结转数量","出库单价","成本单价差异","出库结转差异金额","成本币种"};
		
		
		String[] keys2={"merchant_name","bu_name","month","table_type","order_type","customer_name","store_platform_name",
				"order_code","external_code","barcode","stock_location_type_name","num","price","difference_price","difference_amount","currency"};		
		
		
		for (Map<String, Object> map : outCostDifferenceMapList) {
			String orderType = (String) map.get("order_type");
			String tableType = (String) map.get("table_type");
			String orderName="";
			if ("销售已上架".equals(tableType)) {
				orderName= DERP_REPORT.getLabelByKey(DERP_REPORT.financeSaleShelf_orderTypeList, orderType);
			}
			if ("出库残损".equals(tableType)) {
				orderName= DERP_REPORT.getLabelByKey(DERP_REPORT.financeSaleDamaged_orderTypeList, orderType);
			}
			if ("盘盈盘亏".equals(tableType)) {
				orderName= DERP_REPORT.getLabelByKey(DERP_REPORT.financeTakesStockResults_typeList, orderType);
			}
			if ("库位调整".equals(tableType)) {
				orderName= DERP_ORDER.getLabelByKey(DERP_ORDER.locationAdjustmentOrder_orderTypeList, orderType);
			}
			 
			map.put("order_type", orderName);
		}
		
		Map<String, Object> map2=new HashMap<>();
		map2.put("sheetName", sheetName2);
		map2.put("columns", columns2);
		map2.put("keys", keys2);
		map2.put("list", outCostDifferenceMapList);
		excelMapList.add(map2);

		
		// 生成Sd财务经销存汇总和详情数据
		SXSSFWorkbook wb = DownloadExcelUtil.createCostDifferenceTask(excelMapList);
		inCostDifferenceMapList=null;
		outCostDifferenceMapList=null;


		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		//写出文件
		String fileName = "事业部财务美赞成本差异"+buNameSheetName+month+".xlsx";
		FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
		wb.write(fileOut);
		fileOut.close();
		wb=null;
		System.out.println("事业部财务美赞成本差异商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

	}

	
	
	/**
	 * 获取美赞负金额单价
	 * @param goodsId
	 * @return
	 * @throws SQLException
	 */
	private BigDecimal getFgPrice(Long buId,Long goodsId,Long merchantId,String month) throws SQLException {
		BigDecimal fgPrice=new BigDecimal(0);
		Map<String, Object> invenMappingParams = new HashMap<String, Object>() ;
		invenMappingParams.put("goodsId", goodsId);
		InventoryLocationMappingMongo locationMappingMongo = inventoryLocationMappingMongoDao.findOne(invenMappingParams);
		//按N20， N19排序 获取库存映射商品
		InventoryLocationMappingMongo inventoryLocationMappingMongo=null;
		if (locationMappingMongo!=null&&locationMappingMongo.getOriginalGoodsId()!=null) {
			Map<String, Object> orgGoodsInvenParams = new HashMap<String, Object>();
			orgGoodsInvenParams.put("originalGoodsId", locationMappingMongo.getOriginalGoodsId());
			orgGoodsInvenParams.put("type", "1");
			List<InventoryLocationMappingMongo> invernMappingList = inventoryLocationMappingMongoDao.findAll(orgGoodsInvenParams);
			/**按N20， N19排序*/
			invernMappingList = invernMappingList.stream()
					.sorted(Comparator.comparing(InventoryLocationMappingMongo :: getGoodsNo).reversed())
					.collect(Collectors.toList()) ;invernMappingList = invernMappingList.stream()
					.sorted(Comparator.comparing(InventoryLocationMappingMongo :: getGoodsNo).reversed())
					.collect(Collectors.toList()) ;
			inventoryLocationMappingMongo = invernMappingList.get(0);
			Long normaGoodsId = inventoryLocationMappingMongo.getGoodsId();
			MerchandiseInfoModel normaMerchandiseInfo = merchandiseInfoDao.searchById(normaGoodsId);
			String normaCommbarcode="";
			if (normaMerchandiseInfo!=null){
				normaCommbarcode=normaMerchandiseInfo.getCommbarcode();
			}
			// 查询根据加权单价表
			WeightedPriceModel weightedPriceModel=new WeightedPriceModel();
			weightedPriceModel.setMerchantId(merchantId);
			weightedPriceModel.setCommbarcode(normaCommbarcode);
			weightedPriceModel.setBuId(buId);
			weightedPriceModel.setEffectiveMonth(month);
			if (StringUtils.isNotBlank(normaCommbarcode)) {
				weightedPriceModel = weightedPriceDao.searchByModel(weightedPriceModel);
			}
			if (weightedPriceModel!=null)fgPrice=weightedPriceModel.getPrice();
			if (fgPrice==null)fgPrice=new BigDecimal(0);
		}

		return fgPrice;
	}

	public void createExcelMoveItem2(String buNameSheetName,Map<String, Object> itemMoveMap,int k,String month,String path,
									 Integer startMoveIndex2,Integer pageMoveSize,String sheetName18,String[] columns18,String[] keys18,
									 int sheetMovePageLenth){
		try {
			//查询商家、仓库、月份入库明细
			List<Map<String,Object>> moveItemList2 = new ArrayList<Map<String,Object>>();
			itemMoveMap.put("startIndex", startMoveIndex2);
			for(int i=0;i<sheetMovePageLenth;i++){
				List<Map<String,Object>> tempList = buFinanceMoveDetailsDao.exportFinanceMoveDetailsList(itemMoveMap);
				if(tempList==null||tempList.size()<=0) break;
				moveItemList2.addAll(tempList);
				tempList=null;
				startMoveIndex2 += pageMoveSize;
				itemMoveMap.put("startIndex", startMoveIndex2);
				System.out.println("10万+入startIndex="+startMoveIndex2);
			}
			System.out.println("开始写第"+k+"个文件");
			//生成第二个文件
			SXSSFWorkbook wb2 = ExcelUtilXlsx.createSXSSExcel(sheetName18, columns18, keys18, moveItemList2);
			moveItemList2=null;
			//写出文件
			String fileName2 = "本期事业部移库明细"+buNameSheetName+month+"("+k+").xlsx";
			FileOutputStream fileOut2=new FileOutputStream(path+"/"+fileName2);
			wb2.write(fileOut2);
			fileOut2.close();
			wb2=null;
			System.out.println("第"+k+"个文件创建结束");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void createExcelInItem2(String buNameSheetName,Map<String, Object> itemMap,int k,String month,String path,
								   Integer startIndex,Integer pageSize,String sheetName5,String[] columns5,String[] keys5,
								   int sheetPageLenth,List<String> orderCodeList){
		try {
			//查询商家、仓库、月份入库明细
			List<Map<String,Object>> inItemList2 = new ArrayList<Map<String,Object>>();
			itemMap.put("startIndex", startIndex);
			for(int i=0;i<sheetPageLenth;i++){
				List<Map<String,Object>> tempList = buFinanceSaleShelfDao.exportBuFinanceSaleShelfMapList(itemMap);
				if(tempList==null||tempList.size()<=0) break;
				for (Map<String, Object> map : tempList) {
					String shopTypeCode = (String) map.get("shop_type_code");
					if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
						shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"一件代发";
					}
					if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
						shopTypeCode=DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);//"POP";
					}
					map.put("shop_type_code", shopTypeCode);
					String orderCode = (String) map.get("order_code");
					if (orderCodeList.contains(orderCode)) {
						map.put("payment", 0);//实付总金额
						map.put("taxes", 0);//税费
						map.put("discount", 0);//优化金额
						map.put("way_frt_fee", 0);//运费
						map.put("sale_com", 0);//运费
					}else {
						orderCodeList.add(orderCode);
					}

				}
				inItemList2.addAll(tempList);
				tempList=null;
				startIndex += pageSize;
				itemMap.put("startIndex", startIndex);
				System.out.println("10万+入startIndex="+startIndex);
			}
			System.out.println("开始写第"+k+"个文件");
			//生成第二个文件
			SXSSFWorkbook wb2 = ExcelUtilXlsx.createSXSSExcel(sheetName5, columns5, keys5, inItemList2);
			inItemList2=null;
			//写出文件
			String fileName2 = "销售已上架明细表"+buNameSheetName+month+"("+k+").xlsx";
			FileOutputStream fileOut2=new FileOutputStream(path+"/"+fileName2);
			wb2.write(fileOut2);
			fileOut2.close();
			wb2=null;
			System.out.println("第"+k+"个文件创建结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 移除前100000条数据
		if (orderCodeList.size()>=200000) {
			orderCodeList = orderCodeList.subList(100000,orderCodeList.size());
		}
	}

	/**
	 * 关帐分页
	 */
	@Override
	public BuFinanceInventorySummaryDTO getListDesc(BuFinanceInventorySummaryDTO model,User user) throws SQLException {
		
		if (model.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			model.setBuList(buList);
		}
		return buFinanceInventorySummaryDao.getListDescByPage(model);
	}

	@Override
	public Map<String, Object> closeReport(String month ,Long buId, User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantId" , user.getMerchantId());
		paramMap.put("buId", buId);
		paramMap.put("status", DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029);
		paramMap.put("month", month);
		Integer count = buFinanceInventorySummaryDao.countBeforeMonthList(paramMap);
		if (count > 0) {
			resultMap.put("code", "01");
			resultMap.put("message", "请对历史月份报表进行关账完毕!");
			return resultMap;
		}
		BusinessUnitModel buModel = businessUnitDao.searchById(buId);
		BuFinanceInventorySummaryModel model = new BuFinanceInventorySummaryModel();
		model.setMerchantId(user.getMerchantId());
		model.setMerchantName(user.getMerchantName());
		model.setMonth(month);
		model.setBuId(buId);//事业部id
		model.setBuName(buModel.getName());// 事业部名称
		model.setStatus(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
		model.setCloseDate(TimeUtils.getNow());
		int num =  buFinanceInventorySummaryDao.closeReport(model);
		// 报表操作日志
		OperateReportLogModel log =new OperateReportLogModel();
		String relCode=user.getMerchantId()+","+buId+","+month;
		log.setModule("1");
		log.setRelCode(relCode);
		log.setOperateId(user.getId());
		log.setOperater(user.getName());
		//log.setoperaterDepot
		log.setOperateAction("关账");
		log.setOperateDate(TimeUtils.getNow());
		log.setOperateResult("成功");
		log.setOperateRemark("关账日志");
		log.setCreateDate(TimeUtils.getNow());

		operateReportLogDao.save(log);		
		if (num == 0) {
			resultMap.put("code", "01");
			resultMap.put("message", "关帐失败!");
			return resultMap;
		}
		resultMap.put("code", "00");
		resultMap.put("message", "关帐成功!");
		return resultMap;
	}
	
	/**
	 * 修改为未关账
	 */
	@Override
	public Map<String, Object> updateNotClose(String month ,Long buId, User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantId" , user.getMerchantId());
		paramMap.put("buId", buId);
		paramMap.put("status", DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
		paramMap.put("month", month);
		Integer count = buFinanceInventorySummaryDao.countAftrerMonthList(paramMap);//
		if (count > 0) {
			resultMap.put("state", "01");
			resultMap.put("message", "存在大于当前月份关账的数据");
			return resultMap;
		}
		BusinessUnitModel buModel = businessUnitDao.searchById(buId);
		BuFinanceInventorySummaryModel model = new BuFinanceInventorySummaryModel();
		model.setMerchantId(user.getMerchantId());
		model.setMerchantName(user.getMerchantName());
		model.setMonth(month);
		model.setBuId(buId);//事业部id
		model.setBuName(buModel.getName());// 事业部名称
		model.setStatus(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029);
		int num =  buFinanceInventorySummaryDao.updateNotClose(model);
		// 报表操作日志
		OperateReportLogModel log =new OperateReportLogModel();
		String relCode=user.getMerchantId()+","+buId+","+month;
		log.setModule("1");
		log.setRelCode(relCode);
		log.setOperateId(user.getId());
		log.setOperater(user.getName());
		//log.setoperaterDepot
		log.setOperateAction("反关账");
		log.setOperateDate(TimeUtils.getNow());
		log.setOperateResult("成功");
		log.setOperateRemark("反关账日志");
		log.setCreateDate(TimeUtils.getNow());
		operateReportLogDao.save(log);			
		
		if (num == 0) {
			resultMap.put("code", "01");
			resultMap.put("message", "反关帐失败!");
			return resultMap;
		}
		resultMap.put("code", "00");
		resultMap.put("message", "反关帐成功!");
		return resultMap;
    }


    /**
     * 生成 商家、月份的excel 事业部财务总账
     */
    @Override
    public String createAllAccountExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> jsonMap = jsonData;
        Integer merchantId = (Integer) jsonMap.get("merchant_id");
        String month = (String) jsonMap.get("own_month");
        Integer buIdInt = (Integer) jsonMap.get("buId");
        List<Long> buList=null;
        String userId="";
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}
        
        String buNameSheetName = "";
        if (buIdInt != null) {
            BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buIdInt));
            buNameSheetName = "(" + businessUnitModel.getName()+")";
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+buIdInt+"/"+month+userId;
		}else {
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+month+userId;
		}
		
		System.out.println("事业部财务经总账 商家Id="+merchantId+",事业部:"+buNameSheetName+",月份="+month+"---生成excel文件开始----------");
	    /*辅助核算1
	    1、当科目编码为：60010104、60010105时，辅助核算1 取对应事业部信息，显示格式为：{事业部编码}：部门，例：E070000:部门。请参考附件对应字段显示。
	    2、当科目编码为：11220103、11220203时，辅助核算1取对应数据行客商信息，显示格式为：{客商主数据编码}：客商，例：1000000432:客商。请参考附件对应字段显示。
	    3、其他科目时，该字段为空；
	    辅助核算2
	    1、当科目编码为：60010104、60010105时，辅助核算2取对应客商信息，显示格式为：{客商主数据编码}：客商，例：1000000432:客商。请参考附件对应字段显示。
	    2、其他科目时，该字段为空；
	    辅助核算3
	    1、当科目编码为：60010104、60010105时，辅助核算3为固定值，固定值显示格式为：009:销售模式，请参考附件对应字段显示。
	    2、其他科目时，该字段为空；
	    辅助核算4
	    1、当科目编码为：60010104、60010105时，辅助核算4为固定值，固定值显示格式为：006:渠道，请参考附件对应字段显示。
	    2、其他科目时，该字段为空；
	    辅助核算5
	    1、当科目编码为：60010104、60010105时，辅助核算5为固定值，固定值显示格式为：~:品牌，请参考附件对应字段显示。
	    2、其他科目时，该字段为空；
	    辅助核算6
	    1、当科目编码为：60010104、60010105时，辅助核算6为固定值，固定值显示格式为：~:店铺名，请参考附件对应字段显示。
	    2、其他科目时，该字段为空；*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("month",month);
		if (buIdInt!=null) paramMap.put("buId", Long.valueOf(buIdInt));
		paramMap.put("buList", buList);
		// 获取商家信息
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));
		String topidealCode= merchantInfo.getTopidealCode();

		//销售收入
		String sheetName1 = "销售收入";
		String sheetName1Param1="\""+"null_$head,main_m_pk_accountingbook,main_m_pk_vouchertype,main_m_num,main_m_attachment,"
				+ "main_pk_prepared,main_m_prepareddate,m_e"
				+ "xplanation,m_accsubjcode,m_pk_currtype,"
				+ "m_debitamount,m_localdebitamount,m_groupdebitamount,m_globaldebitamount,unitname,"
				+ "m_price,m_debitquantity,m_creditquantity,m_creditamount,m_localcreditamount,m_groupcreditamount,"
				+ "m_globalcreditamount,m_checkno,m_checkdate,verifyno,verifydate,m_bankaccount,billtype,m_checkstyle,"
				+ "m_excrate2,excrate3,excrate4,ass_1,ass_2,ass_3,ass_4,ass_5,ass_6,ass_7,ass_8,ass_9"+"\"";
		String sheetName1Param2="\""+"cashflow,m_flag,cashflowcurr,m_money,m_moneymain,m_moneygroup,m_moneyglobal,cashflowinnercorp,"
				+ "cashflowName,cashflowCode"+"\"";
		//获取当前公司主体取对应核算账簿值
		Map<String, Object>accountMap=new HashMap<>();
		accountMap.put("0000134", "200100-003");//卓烨贸易
		accountMap.put("1000000204", "200500-003");//健太
		accountMap.put("1000000594", "200600-003");//嘉宝贸易
		accountMap.put("0000138", "200700-003");//香港宝信
		accountMap.put("1000000626", "201000-003");//润佰贸易
		accountMap.put("1000004669", "201100-003");//香港元森泰
		accountMap.put("1000000645", "201200-003");//广旺贸易

		//获取当前公司主体取对应核算账簿值
		Map<String, Object>keyWordMap=new HashMap<>();
		keyWordMap.put("0000134", "200100");//卓烨贸易
		keyWordMap.put("1000000204", "200500");//健太
		keyWordMap.put("1000000594", "200600");//嘉宝贸易
		keyWordMap.put("0000138", "200700");//香港宝信
		keyWordMap.put("1000000626", "201000");//润佰贸易
		keyWordMap.put("1000004669", "201100");//香港元森泰
		keyWordMap.put("1000000645", "201200");//广旺贸易
		ArrayList<String> columnsList1 = new ArrayList<String>(){{add(sheetName1Param1);add("* 核算账簿");
			add("* 凭证类别编码");add("* 凭证号");add("附单据数");add("* 制单人编码");add("* 制单日期");add("* 摘要");add("* 科目编码");add("* 币种");
			add("* 原币借方金额");add("* 本币借方金额");add("集团本币借方金额");
			add("全局本币借方金额");add("公司主键");add("单价");add("借方数量");add("贷方数量");add("* 原币贷方金额");add("* 本币贷方金额");add("集团本币贷方金额");
			add("全局本币贷方金额");add("结算号");add("结算日期");add("核销号");add("业务日期");add("银行账户");add("票据类型");add("结算方式");
			add("组织本币汇率");add("集团本币汇率");add("全局本币汇率");add("辅助核算1");add("辅助核算2");add("辅助核算3");
		}};

		ArrayList<String> keyList1 = new ArrayList<String>(){{add("sheetName1Param1");add("* 核算账簿");
			add("* 凭证类别编码");add("* 凭证号");add("附单据数");add("* 制单人编码");add("* 制单日期");add("* 摘要");add("* 科目编码");add("* 币种");
			add("* 原币借方金额");
			add("* 本币借方金额");add("集团本币借方金额");
			add("全局本币借方金额");add("公司主键");add("单价");add("借方数量");add("贷方数量");
			add("* 原币贷方金额");add("* 本币贷方金额");add("集团本币贷方金额");
			add("全局本币贷方金额");add("结算号");add("结算日期");add("核销号");add("业务日期");
			add("银行账户");add("票据类型");add("结算方式");add("组织本币汇率");
			add("集团本币汇率");add("全局本币汇率");add("辅助核算1");
			add("辅助核算2");
			add("辅助核算3");
		}};
		int sheetName1ParamNum=0;
		// 获取每月最后一天时间
		Timestamp monthEndDateTimestamp = TimeUtils.getMonthEndDate(Timestamp.valueOf(month+"-01 00:00:00"));
		String monthEndDate=TimeUtils.format(monthEndDateTimestamp, "yyyy-MM-dd");

		List<Map<String,Object>>saleList=new ArrayList<>();// 汇总所有的数据
		// 注：该sheet基于现有事业部财务进销存中的销售已上架明细表、本期销毁明细表、盘盈盘亏明细表，各明细表取值维度为：
		//1、销售已上架明细表：
		//（1）当渠道类型为To B且运营类型为空 时以“事业部+月份+客商+母品牌+币种”汇总金额（金额取明细表中“销售金额字段”）；
		//（2）当渠道类型为To B且运营类型为一件代发时，以“事业部+月份+平台名称+母品牌+币种”汇总金额（金额取明细表中“订单总金额字段”）；
		//（3）当渠道类型为To C时，以“事业部+月份+平台名称+母品牌+币种”汇总金额（金额取明细表中“订单总金额字段”）；
		//2、盘盈盘亏明细表：
		//（1）盘盈类型：以“事业部+月份+母品牌+盘盈”汇总金额；金额=盘盈数量*商品本期加权单价；
		//（2）盘亏类型：以“事业部+月份+母品牌+盘亏”汇总金额；金额=盘亏数量*商品本期加权单价；
		// 获取总账导出 销售上架 渠道类型为To B且运营类型为空
		List<Map<String,Object>> accountShelfToBOld=buFinanceSaleShelfDao.getAllAccountShelfToB(paramMap);
		for (Map<String, Object> map : accountShelfToBOld) {
			Long buId = (Long) map.get("bu_id");
			Long customerId = (Long) map.get("customer_id");
			String superiorParentPrand = (String) map.get("superior_parent_brand");
			String superiorParentBrandCode = (String) map.get("superior_parent_brand_code");
			BigDecimal saleAmount = (BigDecimal) map.get("sale_amount");
			String saleCurrency = (String) map.get("sale_currency");
			if (saleAmount==null)saleAmount=new BigDecimal(0);
			BigDecimal tgtsSaleAmount=new BigDecimal(0);//本币借方金额

			ExchangeRateModel rateModel=new ExchangeRateModel();
			rateModel.setOrigCurrencyCode(saleCurrency);
			rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
			rateModel.setEffectiveDate(monthEndDateTimestamp);
			Double rate=0.0;
			if (StringUtils.isNotBlank(saleCurrency)) {
				rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
				if (rateModel!=null)rate=rateModel.getRate();
			}
			if (rate==null) rate=0.0;
			if (DERP.CURRENCYCODE_HKD.equals(saleCurrency)) {
				rate=1.0;
			}
			tgtsSaleAmount=saleAmount.multiply(new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
			saleAmount=saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			String customerName="";
			String customerType="";
			String mainId="";//客商编码
			if (customerId!=null) {
				CustomerInfoModel customerInfoModel = customerInfoDao.searchById(customerId);
				if (customerInfoModel!=null){
					customerName = customerInfoModel.getName();
					customerType=customerInfoModel.getType();
					mainId=customerInfoModel.getMainId();
				}
			}
			String buName="";
			String buCode="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null){
					buName=businessUnit.getName();
					buCode=businessUnit.getCode();
				}
			}
			// 获取辅助核算 1~3
			map=getAssistAccount("60019816",map,buCode,mainId,superiorParentBrandCode);
			map.put("buName", buName);
			map.put("* 摘要", month+customerName+superiorParentPrand+"销售收入");
			map.put("sheetName1Param1", sheetName1ParamNum);
			map.put("* 核算账簿", accountMap.get(topidealCode));
			map.put("* 凭证类别编码", "01");
			map.put("* 制单日期", monthEndDate);
			map.put("* 科目编码", "60019816");
			String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency);
			map.put("* 币种", saleCurrencyName);
			map.put("* 原币贷方金额", saleAmount);
			map.put("* 本币贷方金额", tgtsSaleAmount);
			map.put("组织本币汇率", rate);
			map.put("公司主键", keyWordMap.get(topidealCode));
			saleList.add(map);
			// 内部公司复制一条
			if (DERP_SYS.CUSTOMERINFO_TYPE_1.equals(customerType)) {
				Map<String, Object>newMap=new HashMap<>();
				newMap.putAll(map);
				newMap.put("sheetName1Param1", sheetName1ParamNum);
				newMap.put("customerType", customerType);
				newMap.put("* 原币借方金额", saleAmount);
				newMap.put("* 本币借方金额", tgtsSaleAmount);
				newMap.put("* 原币贷方金额", "");
				newMap.put("* 本币贷方金额", "");
				newMap.put("* 科目编码", "11220203");
				newMap=getAssistAccount("11220203",newMap,buCode,mainId,superiorParentBrandCode);
				saleList.add(newMap);
			}
			//外部公司
			if (DERP_SYS.CUSTOMERINFO_TYPE_2.equals(customerType)) {
				Map<String, Object>newMap=new HashMap<>();
				newMap.putAll(map);
				newMap.put("sheetName1Param1", sheetName1ParamNum);
				newMap.put("* 原币借方金额", saleAmount);
				newMap.put("* 本币借方金额", tgtsSaleAmount);
				newMap.put("* 原币贷方金额", "");
				newMap.put("* 本币贷方金额", "");
				newMap.put("customerType", customerType);
				newMap.put("* 科目编码", "11220103");
				newMap=getAssistAccount("11220103",newMap,buCode,mainId,superiorParentBrandCode);
				saleList.add(newMap);
			}


		}
		// 获取总账导出 销售上架 渠道类型为To B且运营类型为一件代发
		List<Map<String,Object>> accountShelfToB002=buFinanceSaleShelfDao.getAllAccountShelfToB002(paramMap);
		for (Map<String, Object> map : accountShelfToB002) {

			Long buId = (Long) map.get("bu_id");
			String storePlatformCode = (String) map.get("store_platform_code");
			Long customerId = (Long) map.get("customer_id");
			String superiorParentPrand = (String) map.get("superior_parent_brand");
			String superiorParentBrandCode = (String) map.get("superior_parent_brand_code");
			BigDecimal saleAmount = (BigDecimal) map.get("sale_amount");
			String saleCurrency = (String) map.get("sale_currency");
			if (saleAmount==null)saleAmount=new BigDecimal(0);
			BigDecimal tgtsSaleAmount=new BigDecimal(0);//本币借方金额

			ExchangeRateModel rateModel=new ExchangeRateModel();
			rateModel.setOrigCurrencyCode(saleCurrency);
			rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
			rateModel.setEffectiveDate(monthEndDateTimestamp);
			Double rate=0.0;
			if (StringUtils.isNotBlank(saleCurrency)) {
				rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
				if (rateModel!=null)rate=rateModel.getRate();
			}
			if (rate==null) rate=0.0;
			if (DERP.CURRENCYCODE_HKD.equals(saleCurrency)) {
				rate=1.0;
			}
			tgtsSaleAmount=saleAmount.multiply(new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
			saleAmount=saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			String customerName="";
			String customerType="";
			String mainId="";//客商编码
			if (customerId!=null) {
				CustomerInfoModel customerInfoModel = customerInfoDao.searchById(customerId);
				if (customerInfoModel!=null){
					customerName = customerInfoModel.getName();
					customerType=customerInfoModel.getType();
					mainId=customerInfoModel.getMainId();
				}
			}

			String buName="";
			String buCode="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null){
					buName=businessUnit.getName();
					buCode=businessUnit.getCode();
				}

			}

			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
			map.put("sheetName1Param1", sheetName1ParamNum);
			map.put("* 摘要", month+storePlatformName+superiorParentPrand+"销售收入");

			map.put("* 原币贷方金额", saleAmount);
			map.put("* 本币贷方金额", tgtsSaleAmount);
			map.put("组织本币汇率", rate);
			map.put("buName", buName);
			map.put("* 核算账簿", accountMap.get(topidealCode));
			map.put("* 凭证类别编码", "01");
			map.put("* 制单日期", monthEndDate);
			map.put("* 科目编码", "60019816");
			map=getAssistAccount("60019816",map,buCode,mainId,superiorParentBrandCode);
			String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency);
			map.put("* 币种", saleCurrencyName);
			map.put("公司主键", keyWordMap.get(topidealCode));
			saleList.add(map);
			Map<String, Object>newMap=new HashMap<>();
			newMap.putAll(map);
			newMap.put("sheetName1Param1", sheetName1ParamNum);
			newMap.put("* 原币借方金额", saleAmount);
			newMap.put("* 本币借方金额", tgtsSaleAmount);
			newMap.put("* 原币贷方金额", "");
			newMap.put("* 本币贷方金额", "");
			newMap.put("* 科目编码", "11220203");
			newMap=getAssistAccount("11220203",newMap,buCode,mainId,superiorParentBrandCode);
			saleList.add(newMap);

		}
		// 获取总账导出 销售上架 渠道类型为To C
		List<Map<String,Object>> accountShelfToC=buFinanceSaleShelfDao.getAllAccountShelfToC(paramMap);
		for (Map<String, Object> map : accountShelfToC) {

			Long buId = (Long) map.get("bu_id");
			String storePlatformCode = (String) map.get("store_platform_code");
			Long customerId = (Long) map.get("customer_id");
			String superiorParentPrand = (String) map.get("superior_parent_brand");
			String superiorParentBrandCode = (String) map.get("superior_parent_brand_code");
			BigDecimal saleAmount = (BigDecimal) map.get("sale_amount");
			String saleCurrency = (String) map.get("sale_currency");
			if (saleAmount==null)saleAmount=new BigDecimal(0);
			BigDecimal tgtsSaleAmount=new BigDecimal(0);//本币借方金额

			ExchangeRateModel rateModel=new ExchangeRateModel();
			rateModel.setOrigCurrencyCode(saleCurrency);
			rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
			rateModel.setEffectiveDate(monthEndDateTimestamp);
			Double rate=0.0;
			if (StringUtils.isNotBlank(saleCurrency)) {
				rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
				if (rateModel!=null)rate=rateModel.getRate();
			}
			if (rate==null) rate=0.0;
			if (DERP.CURRENCYCODE_HKD.equals(saleCurrency)) {
				rate=1.0;
			}
			tgtsSaleAmount=saleAmount.multiply(new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
			saleAmount=saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			String customerName="";
			String customerType="";
			String mainId="";//客商编码
			if (customerId!=null) {
				CustomerInfoModel customerInfoModel = customerInfoDao.searchById(customerId);
				if (customerInfoModel!=null){
					customerName = customerInfoModel.getName();
					customerType=customerInfoModel.getType();
					mainId=customerInfoModel.getMainId();
				}
			}
			String buName="";
			String buCode="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null){
					buName=businessUnit.getName();
					buCode=businessUnit.getCode();
				}

			}

			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);

			map.put("buName", buName);
			map.put("* 摘要", month+storePlatformName+superiorParentPrand+"销售收入");


			map.put("sheetName1Param1", sheetName1ParamNum);
			map.put("* 核算账簿", accountMap.get(topidealCode));
			map.put("* 凭证类别编码", "01");
			map.put("* 制单日期", monthEndDate);
			map.put("* 科目编码", "60019817");
			String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency);
			map.put("* 币种", saleCurrencyName);
			map.put("* 原币贷方金额", saleAmount);
			map.put("* 本币贷方金额", tgtsSaleAmount);
			map.put("组织本币汇率", rate);
			map.put("公司主键", keyWordMap.get(topidealCode));
			map=getAssistAccount("60019817",map,buCode,mainId,superiorParentBrandCode);
			saleList.add(map);
			//ToC都是外部客户 默认复制一条
			Map<String, Object>newMap=new HashMap<>();
			newMap.putAll(map);
			newMap.put("sheetName1Param1", sheetName1ParamNum);
			newMap.put("* 原币借方金额", saleAmount);
			newMap.put("* 本币借方金额", tgtsSaleAmount);
			newMap.put("* 原币贷方金额", "");
			newMap.put("* 本币贷方金额", "");
			newMap.put("* 科目编码", "11220103");
			newMap=getAssistAccount("11220103",newMap,buCode,mainId,superiorParentBrandCode);
			saleList.add(newMap);

		}

		//获取总账导出 盘盈盘亏明细表
		List<Map<String,Object>> stockGroupByType=buFinanceTakesStockResultsDao.getAllAccountGroupByType(paramMap);

		for (Map<String, Object> map : stockGroupByType) {

			Long buId = (Long) map.get("bu_id");
			String type = (String) map.get("type");
			String superiorParentPrand = (String) map.get("superior_parent_brand");
			String superiorParentBrandCode = (String) map.get("superior_parent_brand_code");
			BigDecimal saleAmount = (BigDecimal) map.get("amount");
			if (saleAmount==null)saleAmount=new BigDecimal(0);
			saleAmount=saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	    	/*BigDecimal tgtsSaleAmount=new BigDecimal(0);//本币借方金额
	    	ExchangeRateModel rateModel=new ExchangeRateModel();
	    	rateModel.setOrigCurrencyCode(saleCurrency);
	    	rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
	    	rateModel.setEffectiveDate(monthEndDate);
	    	Double rate=0.0;
	    	if (saleCurrency!=null) {
	    		rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
	    		if (rateModel!=null)rate=rateModel.getRate();
			}
	    	if (rate==null) rate=0.0;
	    	tgtsSaleAmount=saleAmount.multiply(new BigDecimal(rate.toString()));
	    	*/

			String buName="";
			String buCode="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null){
					buName=businessUnit.getName();
					buCode=businessUnit.getCode();
				}

			}

			map.put("buName", buName);
			if ("1".equals(type)) map.put("* 摘要", month+superiorParentPrand+"盘盈");// 盘盈
			if ("2".equals(type)) map.put("* 摘要", month+superiorParentPrand+"盘亏");// 盘亏

			map.put("sheetName1Param1", sheetName1ParamNum);
			map.put("* 核算账簿", accountMap.get(topidealCode));
			map.put("* 凭证类别编码", "01");
			map.put("* 制单日期", monthEndDate);
			if ("1".equals(type))map.put("* 科目编码", "14050103");
			if ("2".equals(type))map.put("* 科目编码", "19010102");
			if ("1".equals(type))map=getAssistAccount("14050103",map,buCode,"",superiorParentBrandCode);
			if ("2".equals(type))map=getAssistAccount("19010102",map,buCode,"",superiorParentBrandCode);
			//String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency);
			map.put("* 币种", "港币");
			map.put("* 原币贷方金额", "");
			map.put("* 本币贷方金额", "");
			map.put("组织本币汇率", 1);
			map.put("* 原币借方金额", saleAmount);
			map.put("* 本币借方金额", saleAmount);
			map.put("公司主键", keyWordMap.get(topidealCode));
			saleList.add(map);
			Map<String, Object>newMap=new HashMap<>();
			newMap.putAll(map);
			newMap.put("sheetName1Param1", sheetName1ParamNum);
			newMap.put("* 原币贷方金额", saleAmount);
			newMap.put("* 本币贷方金额", saleAmount);
			newMap.put("* 原币借方金额", "");
			newMap.put("* 本币借方金额", "");
			if ("1".equals(type)) newMap.put("* 科目编码", "19010101");// 盘盈
			if ("2".equals(type)) newMap.put("* 科目编码", "14050103");// 盘亏
			if ("1".equals(type))newMap=getAssistAccount("19010101",newMap,buCode,"",superiorParentBrandCode);
			if ("2".equals(type))newMap=getAssistAccount("14050103",newMap,buCode,"",superiorParentBrandCode);

			saleList.add(newMap);

		}
		//采购入库
		String sheetName2 = "采购入库";

		// 表体
		String sheetName2Item1="\"bodys,invoiceno,scomment,objtype,supplier,pk_deptid,pk_subjcode,subjcode,pk_psndoc,pk_currtype,"
				+ "price,taxprice,quantity_cr,money_cr,local_money_cr,rate,taxcodeid,taxrate,notax_cr,local_notax_cr,local_tax_cr,"
				+ "busidate,def30,taxtype,buysellflag,project,def12,def11,def10,def8,def6,def5,def4,def3,def2,def1,def13,material,"
				+ "def17,def16,pk_payterm,def18\"";
		ArrayList<String> columnsListItem2 = new ArrayList<String>(){
			{add(sheetName2Item1);add("发票号");add("摘要");add("* 往来对象");add("* 供应商");add("* 部门");add("* 收支项目");add("* 科目");add("* 业务员");
				add("* 币种");add("单价");add("含税单价");add("贷方数量");
				add("* 贷方原币金额");add("组织本币金额");add("* 组织本币汇率");add("* 税码");add("* 税率");
				add("贷方原币无税金额");add("组织本币无税金额");add("税额");add("* 起算日期");add("备注");
				add("* 扣税类别");add("* 购销类型");add("项目");add("店铺名");add("合同号");add("渠道");add("仓库类型");add("交易类别");add("销售模式");
			}
		};

		ArrayList<String> keyListItem2 = new ArrayList<String>(){{add("index");add("发票号");add("摘要");add("* 往来对象");add("* 供应商");add("* 部门");add("* 收支项目");add("* 科目");add("* 业务员");
			add("* 币种");add("单价");add("含税单价");add("贷方数量");
			add("* 贷方原币金额");add("组织本币金额");add("* 组织本币汇率");add("* 税码");add("* 税率");
			add("贷方原币无税金额");add("组织本币无税金额");add("税额");add("* 起算日期");add("备注");
			add("* 扣税类别");add("* 购销类型");add("项目");add("店铺名");add("合同号");add("渠道");add("仓库类型");add("交易类别");add("销售模式");
		}};

		//表头
		List<Map<String, Object>> orderList=new ArrayList<>();

		int index=0;
		List<Map<String, Object>> orderItemList=new ArrayList<>();
		//以“事业部+月份+供应商+币种”汇总金额（金额取明细表中“采购金额字段”）； 采购入库明细表：
		List<Map<String, Object>> allAccountFinPurWarehouse = buFinanceWarehouseDetailsDao.getAllAccountFinPurWarehouse(paramMap);
		Map<String, Object>orderMap=new HashMap<>();
		for (Map<String, Object> map : allAccountFinPurWarehouse) {
			Long buId = (Long) map.get("bu_id");
			Long supplierId = (Long) map.get("supplier_id");
			String orderCurrency = (String) map.get("order_currency");
			String poNo = (String) map.get("po_no");
			BigDecimal orderAmount = (BigDecimal) map.get("order_amount");
			String customerName="";
			String customerType="";
			if (supplierId!=null) {
				CustomerInfoModel customerInfoModel = customerInfoDao.searchById(supplierId);
				if (customerInfoModel!=null){
					customerName = customerInfoModel.getName();
					customerType=customerInfoModel.getType();
				}
			}
			String buName="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null) buName=businessUnit.getName();
			}
			ExchangeRateModel rateModel=new ExchangeRateModel();
			rateModel.setOrigCurrencyCode(orderCurrency);
			rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
			rateModel.setEffectiveDate(monthEndDateTimestamp);
			Double rate=0.0;
			if (StringUtils.isNotBlank(orderCurrency)) {
				rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
				if (rateModel!=null)rate=rateModel.getRate();
			}
			if (rate==null) rate=0.0;
			if (DERP.CURRENCYCODE_HKD.equals(orderCurrency)) {
				rate=1.0;
			}
			BigDecimal tgtsOrderAmount=orderAmount.multiply(new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
			orderAmount=orderAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
			map.put("index", index);
			map.put("摘要", customerName+poNo);
			map.put("* 往来对象", "供应商");
			map.put("* 供应商", customerName);
			map.put("* 部门", buName);
			map.put("* 收支项目", "(CN)品牌经销");
			if (DERP_SYS.CUSTOMERINFO_TYPE_1.equals(customerType)) {
				map.put("* 科目", "14050203");
			}
			if (DERP_SYS.CUSTOMERINFO_TYPE_2.equals(customerType)) {
				map.put("* 科目", "14050103");
			}
			String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, orderCurrency);
			map.put("* 币种", saleCurrencyName);
			map.put("* 贷方原币金额", orderAmount);
			map.put("组织本币金额", tgtsOrderAmount);
			map.put("* 组织本币汇率", rate);
			map.put("* 税码", "CN03");
			map.put("* 税率", "0");
			map.put("* 起算日期", monthEndDate);
			// 存储序号
			String orderKey=buId+","+supplierId+","+orderCurrency;
			if (!orderMap.containsKey(orderKey)) {
				Map<String, Object> orderKeyMap=new HashMap<>();
				orderKeyMap.put("index", index);
				orderKeyMap.put("* 应付财务组织", merchantInfo.getEnglishName());
				orderKeyMap.put("* 单据日期", monthEndDate);
				orderKeyMap.put("* 往来对象", "供应商");
				orderKeyMap.put("* 供应商", customerName);
				orderKeyMap.put("* 币种", saleCurrencyName);
				orderKeyMap.put("* 应付类型code", "D1");
				orderKeyMap.put("收支项目", "(CN)品牌经销");
				orderKeyMap.put("部门", buName);
				orderList.add(orderKeyMap);
				orderMap.put(orderKey, orderKey);
				index++;
			}
			orderItemList.add(map);
		}
		//（1）以“事业部+月份+供应商+币种”汇总金额（金额取明细表中“采购金额字段”）； 本期采购在途明细表：
		List<Map<String, Object>> allAccountFinNoshelf = buFinanceAddPurchaseNotshelfDetailsDao.getAllAccountFinNoshelf(paramMap);
		for (Map<String, Object> map : allAccountFinNoshelf) {
			Long buId = (Long) map.get("bu_id");
			Long supplierId = (Long) map.get("supplier_id");
			String orderCurrency = (String) map.get("order_currency");
			String poNo = (String) map.get("po_no");
			BigDecimal orderAmount = (BigDecimal) map.get("order_amount");


			String customerName="";
			String customerType="";
			if (supplierId!=null) {
				CustomerInfoModel customerInfoModel = customerInfoDao.searchById(supplierId);
				if (customerInfoModel!=null){
					customerName = customerInfoModel.getName();
					customerType=customerInfoModel.getType();
				}
			}
			String buName="";
			if (buId!=null) {
				BusinessUnitModel businessUnit = businessUnitDao.searchById(buId);
				if (businessUnit!=null) buName=businessUnit.getName();
			}
			ExchangeRateModel rateModel=new ExchangeRateModel();
			rateModel.setOrigCurrencyCode(orderCurrency);
			rateModel.setTgtCurrencyCode(DERP.CURRENCYCODE_HKD);
			rateModel.setEffectiveDate(monthEndDateTimestamp);
			Double rate=0.0;
			if (StringUtils.isNotBlank(orderCurrency)) {
				rateModel = exchangeRateDao.getByDateOrigCurrency(rateModel);
				if (rateModel!=null)rate=rateModel.getRate();
			}
			if (rate==null) rate=0.0;
			if (DERP.CURRENCYCODE_HKD.equals(orderCurrency)) {
				rate=1.0;
			}
			BigDecimal tgtsOrderAmount=orderAmount.multiply(new BigDecimal(rate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
			orderAmount=orderAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

			map.put("index", index);
			map.put("摘要", customerName+poNo);
			map.put("* 往来对象", "供应商");
			map.put("* 供应商", customerName);
			map.put("* 部门", buName);
			map.put("* 收支项目", "(CN)品牌经销");
			map.put("* 科目", "14020000");
			String saleCurrencyName=DERP.getLabelByKey(DERP.currencyCodeList, orderCurrency);
			map.put("* 币种", saleCurrencyName);
			map.put("* 贷方原币金额", orderAmount);
			map.put("组织本币金额", tgtsOrderAmount);
			map.put("组织本币汇率", rate);
			map.put("* 税码", "CN03");
			map.put("* 税率", "0");
			map.put("* 起算日期", monthEndDate);
			// 存储序号
			String orderKey=buId+","+supplierId+","+orderCurrency;
			if (!orderMap.containsKey(orderKey)) {
				Map<String, Object> orderKeyMap=new HashMap<>();
				orderKeyMap.put("index", index);
				orderKeyMap.put("* 应付财务组织", merchantInfo.getEnglishName());
				orderKeyMap.put("* 单据日期", monthEndDate);
				orderKeyMap.put("* 往来对象", "供应商");
				orderKeyMap.put("* 供应商", customerName);
				orderKeyMap.put("* 币种", saleCurrencyName);
				orderKeyMap.put("* 应付类型code", "D1");
				orderKeyMap.put("收支项目", "(CN)品牌经销");
				orderKeyMap.put("部门", buName);
				orderList.add(orderKeyMap);
				orderMap.put(orderKey, orderKey);
				index++;
			}
			orderItemList.add(map);
		}
		// 表头
		String sheetName2Order1="\"payablebill_$head,billno,pk_org,billdate,busidate,objtype,supplier,pk_psndoc,"
				+ "pk_deptid,pk_busitype,pk_currtype,pk_tradetype,pk_subjcode,recaccount,def29,def28\"";
		ArrayList<String> columnsListOrder2 = new ArrayList<String>(){{add(sheetName2Order1);add("* 单据号");add("* 应付财务组织");
			add("* 单据日期");add("起算日期");add("* 往来对象");add("* 供应商");add("业务员");add("部门");add("业务流程");
			add("* 币种");add("* 应付类型code");add("收支项目");add("收款银行账户");add("账单号");add("来源系统");
		}};
		ArrayList<String> keyListOrder2 = new ArrayList<String>(){{add("index");add("* 单据号");add("* 应付财务组织");
			add("* 单据日期");add("起算日期");add("* 往来对象");add("* 供应商");add("业务员");add("部门");add("业务流程");
			add("* 币种");add("* 应付类型code");add("收支项目");add("收款银行账户");add("账单号");add("来源系统");
		}};
		// 生成Sd财务经销存汇总和详情数据
		SXSSFWorkbook wb = DownloadExcelUtil.createBuAllAccount(sheetName1,columnsList1,keyList1, saleList,
				sheetName2,columnsListOrder2,keyListOrder2,orderList,
				columnsListItem2,keyListItem2,orderItemList
		);
		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		//写出文件
		String fileName = "事业部财务进销存总账"+buNameSheetName+month+".xlsx";
		FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
		wb.write(fileOut);
		fileOut.close();
		wb=null;
		System.out.println("事业部财务经总账商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

	}


	/**
	 * 获取辅助核算1~9
	 * @param code 科目编码
	 * @param map
	 * @return
	 */
	private Map<String, Object> getAssistAccount(String code, Map<String, Object> map,String buCode,String mainId,String superiorParentBrandCode) {
		map.put("辅助核算1", "");
		map.put("辅助核算2", "");
		map.put("辅助核算3", "");
		
		//辅助核算1
		if ("60019816".equals(code)||"60019817".equals(code)||"14050203".equals(code)) {
			map.put("辅助核算1", buCode+":部门");
		}
		if ("11220103".equals(code)||"11220203".equals(code)) {
			map.put("辅助核算1", mainId+":客商");
		}
		//辅助核算2
		if ("60019816".equals(code)||"60019817".equals(code)) {
			map.put("辅助核算2", mainId+":客商");
		}
		if ("11220103".equals(code)||"11220203".equals(code)) {
			map.put("辅助核算2", buCode+":部门");
		}
		//辅助核算3
		if ("60019816".equals(code)||"60019817".equals(code)) {
			//map.put("辅助核算3", superiorParentBrandCode+"~:品牌");
			map.put("辅助核算3", "~:品牌");
		}
		if ("11220103".equals(code)||"11220203".equals(code)) {
			map.put("辅助核算3", "~:项目");
		}
		return map;
	}

	/**
	 * 累计汇总表分页
	 */
	@Override
	public BuFinanceInventorySummaryDTO getListAddByPage(User user,BuFinanceInventorySummaryDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return buFinanceInventorySummaryDao.getListAddByPage(dto);
    }

    /**
     * 累计汇总导出
     */
    @Override
    public List<BuFinanceInventorySummaryDTO> getListAddExport(User user,BuFinanceInventorySummaryDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
        return buFinanceInventorySummaryDao.getListAddExport(dto);
    }

    /**
     * 暂估应收pdf出
     */
    @Override
    public String createTempEstimatePdf(FileTaskMongo task, String basePath) throws Exception {
        System.out.println("暂估应收开始1");
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> jsonMap = jsonData;
        Integer merchantId = (Integer) jsonMap.get("merchant_id");
        String month = (String) jsonMap.get("own_month");
        Integer buId = (Integer) jsonMap.get("buId");
        List<Long> buList=null;
        String userId="";
        if (task.getUserId()!=null) {
        	userId="/"+task.getUserId();
        	buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}
        
        if (buId != null) {
            BusinessUnitModel businessUnitModel = businessUnitDao.searchById(Long.valueOf(buId));
            basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + buId+"/"+month+userId;
		}else {
			basePath = basePath+"/"+task.getTaskType()+"/"+merchantId+"/"+month+userId;			
		}

		System.out.println("事业部财务经暂估应收 商家Id="+merchantId+",月份="+month+"---生成excel文件开始----------");
		// 查询商家
		MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(merchantId));
		System.out.println("暂估应收开始2");
		Map<String, Object>parmMap= new HashMap<>();
		parmMap.put("merchantId", Long.valueOf(merchantId));
		parmMap.put("month", month);
		if (buId!=null)parmMap.put("buId", Long.valueOf(buId));
		parmMap.put("buList", buList);
		List<Long>buIdMapList=new ArrayList<>();

		Map <Long, List<Map<String, Object>>>pdfToBMap=new HashMap<>();
		//事业部财务经销存暂估应收PDF导出To B
		List<Map<String, Object>> tempEstimatePdfToB = buFinanceSaleShelfDao.getTempEstimatePdfToB(parmMap);
		for (Map<String, Object> map : tempEstimatePdfToB) {
			Long buIdMap = (Long) map.get("bu_id");
			BigDecimal num = (BigDecimal) map.get("num");
			if (num!=null) {
				String str = String.format("%,d", num.intValue());
				map.put("num", str);
			}
			BigDecimal saleAmount = (BigDecimal) map.get("sale_amount");
			if (saleAmount!=null) {
				DecimalFormat df4 = new DecimalFormat("#,##0.000");
				saleAmount=saleAmount.setScale(3,BigDecimal.ROUND_HALF_UP);
				String saleAmountStr = df4.format(saleAmount);
				map.put("sale_amount", saleAmountStr);
			}
			map.put("NC科目", "60010104\\主营业务收入\\商品销售\\经销业务TO B");
			if (!buIdMapList.contains(buIdMap))buIdMapList.add(buIdMap);
			List<Map<String, Object>> list = pdfToBMap.get(buIdMap);
			if (list==null||list.size()==0) {
				list=new ArrayList<>();
				list.add(map);
			}else {
				list.add(map);
			}


			pdfToBMap.put(buIdMap, list);
		}
		//事业部财务经销存暂估应收PDF导出To C
		Map <Long, List<Map<String, Object>>>pdfToCMap=new HashMap<>();
		List<Map<String, Object>> tempEstimatePdfToC = buFinanceSaleShelfDao.getTempEstimatePdfToC(parmMap);
		for (Map<String, Object> map : tempEstimatePdfToC) {
			Long buIdMap = (Long) map.get("bu_id");
			BigDecimal num = (BigDecimal) map.get("num");
			if (num!=null) {
				String str = String.format("%,d", num.intValue());
				map.put("num", str);
			}
			BigDecimal saleAmount = (BigDecimal) map.get("sale_amount");
			if (saleAmount!=null) {
				DecimalFormat df4 = new DecimalFormat("#,##0.000");
				saleAmount=saleAmount.setScale(3,BigDecimal.ROUND_HALF_UP);
				String saleAmountStr = df4.format(saleAmount);
				map.put("sale_amount", saleAmountStr);
			}
			map.put("NC科目", "60010105\\主营业务收入\\商品销售\\经销业务TO C");
			if (!buIdMapList.contains(buIdMap))buIdMapList.add(buIdMap);
			List<Map<String, Object>> list = pdfToCMap.get(buIdMap);
			if (list==null||list.size()==0) {
				list=new ArrayList<>();
				list.add(map);
			}else {
				list.add(map);
			}
			pdfToCMap.put(buIdMap, list);
		}
		/*ArrayList<String> columnsList = new ArrayList<String>(){
			{add("NC科目");add("客商名称");add("母品牌");add("数量");add("金额");add(" 币种");			}
		};*/
		ArrayList<String> keyList = new ArrayList<String>(){{add("NC科目");add("customer_name");add("superior_parent_brand");add("num");add("sale_amount");add("sale_currency");
		}};
		String merchantNameParm = merchantInfo.getName();
		merchantNameParm="公司："+merchantNameParm;
		String creatDate="";
		if (Timestamp.valueOf(month+"-01 00:00:00").getTime()<Timestamp.valueOf(TimeUtils.format(new Date(), "yyyy-MM")+"-01 00:00:00").getTime()) {
			creatDate=TimeUtils.getMonthLastDay(Timestamp.valueOf(month+"-01 00:00:00"));
		}else {
			creatDate=TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
		}

		String yearMonth = TimeUtils.format(Timestamp.valueOf(month+"-01 00:00:00"), "yyyy年MM月");
		//删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		//创建目录
		new File(basePath).mkdirs();
		System.out.println("暂估应收开始3"+basePath);
		//循环生成pdf
		for (Long buIdMap : buIdMapList) {
			BusinessUnitModel businessUnit = businessUnitDao.searchById(buIdMap);
			String buName = businessUnit.getName();
			String oneCellvale=yearMonth+buName+"暂估应收";
			List<Map<String, Object>> PdfToBList = pdfToBMap.get(buIdMap);
			if (PdfToBList==null||PdfToBList.size()==0)PdfToBList=new ArrayList<>();
			List<Map<String, Object>> PdfToCList = pdfToCMap.get(buIdMap);
			if (PdfToCList==null||PdfToCList.size()==0)PdfToCList=new ArrayList<>();
			// 生成财务经销存暂估应收导出数据
			//SXSSFWorkbook wb = DownloadExcelUtil.createBuTempEstimate(columnsList,keyList,PdfToBList,PdfToCList,merchantNameParm,creatDate,oneCellvale);
			//写出文件
			String fileName = oneCellvale+".pdf";
			String pdfPath=basePath+"/"+fileName;
			
			PdfUtils.getPdfFile(pdfPath,keyList,PdfToBList,PdfToCList,merchantNameParm,creatDate,oneCellvale);

			//FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
			//FileOutputStream fileOut=new FileOutputStream("D:/"+fileName);
			//wb.write(fileOut);
			//fileOut.close();
			//wb=null;
		}

		System.out.println("事业部财务经暂估应收商家Id="+merchantId+",月份="+month+"---生成excel文件结束----------");
		return basePath;

	}

	/**
	 * 获取是否关账
	 */
	@Override
	public String getSummaryStatus(Map<String, Object> map) throws Exception {
		return buFinanceInventorySummaryDao.getSummaryStatus(map);
	}



}
