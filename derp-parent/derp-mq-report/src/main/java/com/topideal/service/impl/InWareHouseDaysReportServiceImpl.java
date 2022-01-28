package com.topideal.service.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.reporting.*;
import com.topideal.dao.system.*;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.InWarehouseTempModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.service.InWareHouseDaysReportService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InWareHouseDaysReportServiceImpl implements InWareHouseDaysReportService {
	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	//在库天数统计dao
	@Autowired
	private InWarehouseDetailsDao inWarehouseDetailsDao ;
	@Autowired
	private BusinessUnitDao businessUnitDao ;
	@Autowired
	private BuInventorySummaryDao buInventorySummaryDao ;
	@Autowired
	private BuBusinessAddTransferNoshelfDetailsDao buBusinessAddTransferNoshelfDetailsDao ;
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private ExchangeRateDao exchangeRateDao ;
	@Autowired
	private InWarehouseTempDao inWarehouseTempDao ;
	@Autowired
	private WeightedPriceDao weightedPriceDao ;
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	
	/* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(InWareHouseDaysReportServiceImpl.class);
    
    private static final String ZY_TOPIDEAL_CODE = "0000134" ;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201501090, model = DERP_LOG_POINT.POINT_13201501090_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception{
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		String months = (String) jsonMap.get("month");//月份
		Integer buId = (Integer) jsonMap.get("buId");

		/**计算要刷新的月份*/
		if(StringUtils.isEmpty(months)){
			//若没有指定月份则取当前时间前一天日期近二个月月份,定时器刷新未关账的，本月、上月
			months = TimeUtils.getLastTwoMonthsByNow();
		}
		/**查询所有事业部,若指定了事业部则只查本事业部*/
		BusinessUnitModel buModel = new BusinessUnitModel() ;
		if(buId!= null) {
			buModel.setId(Long.valueOf(buId));
		}
		//循环事业部、月份生成报表
		List<BusinessUnitModel> buList = businessUnitDao.list(buModel);
		String[] montharr = months.split(",");
		/**循环月份，事业部*/
		for(String month : montharr){
			for(BusinessUnitModel buTempModel : buList) {
				//过滤卓普信事业部
				if("OD020000".equals(buModel.getCode())) continue;

				saveGenerateReport(buTempModel , month);
			}
		}

	}

	/**
	 * 根据事业部生成报表
	 */
	private void saveGenerateReport(BusinessUnitModel buModel, String month) throws Exception {

		/**计算统计截止日期: 默认为当前日期，若报表月份<当前月份则日期=报表月份最后一天(平均汇率) (在库天数用下月1号)*/
		Timestamp statisticsDate = TimeUtils.getNow();
		Date endDate = TimeUtils.getNow();//计算在库天数用
		String nowMonth = TimeUtils.formatMonth(new Date());//当前月份
		if(month.compareTo(nowMonth)<0) {
			String lastDayOfMonth = TimeUtils.getLastDayOfMonth(month);//月份最后一天
			statisticsDate = TimeUtils.parseDay(lastDayOfMonth);

			String nextMonthFirstDay = TimeUtils.getNextMonthFirstDay(month);//取下月1日0点
			endDate = TimeUtils.parseDay(nextMonthFirstDay);
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("month", month);
		paramMap.put("buId", buModel.getId());

		/**清空本月份、事业部数据*/
		inWarehouseDetailsDao.delDepotMonthReport(paramMap) ;

        /**统计事业部、标准条码库存量*/
		Map<String , Integer> buInventorySumMapAll = sumInvertoryMap(buModel, month);
		if(buInventorySumMapAll==null||buInventorySumMapAll.size()<=0){
			logger.info("1-事业部商品库存数量为0，结束");
			return ;
		}

		Map<String , Object> inWarehouseNumMap = new LinkedHashMap<String , Object>() ; //存储入库量 key=入库单号_标准条码 value=
		//统计采购入库量、在入库临时表
        sumInWarehouseNumSumMap(buModel, month, inWarehouseNumMap);
		logger.info("3-入库数量集合："+(inWarehouseNumMap!=null?inWarehouseNumMap.size():null));

		//生成匹配报表明细
		saveMacthInwarehouseDetails(buModel, month , buInventorySumMapAll, inWarehouseNumMap,statisticsDate,endDate);

		//生产未匹配报表明细
		saveUnmacthInWarehouseDetails(buModel, month , buInventorySumMapAll,statisticsDate);

	}
	/**
	 * 统计事业部+标准条码本月在所有公司主体保税仓、海外仓的期末正常品库存量(业务进销存汇总表、业务进销存-累计调拨在途明细表)
	 */
	private Map<String, Integer> sumInvertoryMap(BusinessUnitModel buModel, String month) throws Exception {


		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("month", month) ;
		queryMap.put("buId", buModel.getId()) ;

		/**1、事业部+标准条码 查询事业部业务进销存，统计商品在所有公司主体下保税仓和海外仓的期末正常品库存量*/
		List<Map<String, Object>> buInventorySumMapList = buInventorySummaryDao.getInWarehouseSumAccount(queryMap) ;
		/**2、事业部+标准条码 查询事业部业务进销存，统计商品在所有公司主体下保税仓和海外仓的累计调拨在途量*/
		List<Map<String, Object>> buInventorySumTransferMapList = buBusinessAddTransferNoshelfDetailsDao.getInWarehouseSumTransferAccount(queryMap) ;

		//防止空指针
		if(buInventorySumMapList==null) buInventorySumMapList = new ArrayList<>();
		if(buInventorySumTransferMapList==null) buInventorySumTransferMapList = new ArrayList<>();

		/**合并累计调拨在途到期末正常量list*/
		for (Map<String, Object> map : buInventorySumTransferMapList) {
			Map<String, Object> mapTemp = new HashMap<>();
			mapTemp.put("bu_id",map.get("bu_id"));//事业部id
			mapTemp.put("bu_name",map.get("bu_name"));//事业部名称
			mapTemp.put("commbarcode",map.get("commbarcode"));//标准条码
			mapTemp.put("unit",map.get("tallying_unit"));//理货单位
			mapTemp.put("month_end_normal_num",map.get("num"));//累计在途量
			buInventorySumMapList.add(mapTemp);
		}

		/**箱托转换为件，并合并相同条码的库存量*/
		Map<String, Integer> buInventorySumMapAll = new HashMap<>();//存储转换后的库存量
		String errorMsg = "";//异常消息
		for(Map<String, Object> map : buInventorySumMapList) {
			String commbarcode = (String) map.get("commbarcode");//标准条码
			String unit = (String) map.get("unit");//理货单位
			BigDecimal normalNumBD = (BigDecimal) map.get("month_end_normal_num");//库存量
			Integer numInt = normalNumBD.intValue();//库存量

			//理货单位不为空需要箱托转换
			if(StringUtils.isNotBlank(unit)){
				MerchandiseInfoModel queryModel = new MerchandiseInfoModel();
				queryModel.setCommbarcode(commbarcode);
				List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(queryModel);
				if(merchandiseList==null||merchandiseList.size()<=0) {
					errorMsg += "标准条码："+commbarcode+"未找到商品信息";
					logger.info(errorMsg);
					continue ;
				}
				MerchandiseInfoModel merchandise = merchandiseList.get(0);
				Integer tempNum = changeUnit(unit, normalNumBD, merchandise.getBoxToUnit(), merchandise.getTorrToUnit());
				if(tempNum == null) {
					errorMsg += ",标准条码："+commbarcode+"箱拖转换失败";
					logger.info("标准条码：" + commbarcode + " 箱拖转换失败");
					continue ;
				}

				numInt = tempNum;
			}
			Integer InventorySum = (Integer)buInventorySumMapAll.get(commbarcode);
			if(InventorySum==null) InventorySum = 0;
			buInventorySumMapAll.put(commbarcode,InventorySum+numInt);//累加相同标准条码的库存量
		}

		/**箱托转完成后若异常信息不为空则抛异常*/
		if(StringUtils.isNotBlank(errorMsg)){
			throw new RuntimeException(errorMsg);
		}

		return buInventorySumMapAll;
	}

	/**统计采购入库量
     * */
	private void sumInWarehouseNumSumMap(BusinessUnitModel buModel, String month, Map<String, Object> inWarehouseNumMap) throws Exception {

		//查询事业部、月份的采购入库单按入库单号、标准条码、理货单位分组统计
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("buId", buModel.getId()) ;
		queryMap.put("month", month) ;
		List<Map<String, Object>> warehouseDetails = purchaseWarehouseDao.getInWarehouseDaysDetail(queryMap);

		//查询本事业部的临时入库表
		List<InWarehouseTempModel> tempList = inWarehouseTempDao.getInWarehouseTempDetail(queryMap) ;

		//入库量进行箱托转换为件
        String errorMsg = "";
        String errorUnit = "";//单位转换失败的标准条码
		for (Map<String, Object> warehouseMap : warehouseDetails) {
			String code = (String)warehouseMap.get("code");//入库单号
			String commbarcode = (String)warehouseMap.get("commbarcode");
			String unit = (String)warehouseMap.get("tallying_unit");
			BigDecimal numBD = (BigDecimal)warehouseMap.get("num");//入库量

            // 根据标准条码查询商品表获取箱拖转换信息，多个只取第一个
			MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
			queryModel.setCommbarcode(commbarcode);
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(queryModel);
			if(merchandiseList==null||merchandiseList.size()<=0) {
                errorMsg+=","+commbarcode;
                logger.info("标准条码：" + commbarcode + " 查询商品表未找到对应商品信息");
				continue ;
			}

			MerchandiseInfoModel merchandiseInfoModel = merchandiseList.get(0);
			Integer tempNum = changeUnit(unit, numBD, merchandiseInfoModel.getBoxToUnit(), merchandiseInfoModel.getTorrToUnit());
			if(tempNum==null){
                errorUnit+=","+commbarcode;
                logger.info("标准条码：" + commbarcode + "箱托转换失败");
                continue ;
            }

			//商品的数量合并到一起以入库单号、标准条码为key
			Map<String , Object> contentMap = (Map<String , Object>)inWarehouseNumMap.get(code + "#" + commbarcode)  ;
			if(contentMap == null) contentMap = new HashMap<String , Object>() ;
			Integer inWarehouseNum = (Integer)contentMap.get("inWarehouseNum") ;//入库量

			if(inWarehouseNum == null) inWarehouseNum = 0 ;
            inWarehouseNum += tempNum;

			contentMap.put("inWarehouseNum", inWarehouseNum) ;
			contentMap.put("warehouseMap", warehouseMap) ;
			inWarehouseNumMap.put(code + "#" + commbarcode , contentMap);
		}

		//异常消息不为空则抛异常提示
		if(StringUtils.isNotBlank(errorMsg)||StringUtils.isNotBlank(errorUnit)){
		    if(StringUtils.isNotBlank(errorMsg)) errorMsg = "标准条码:"+errorMsg+"商品不存在;";
		    if(StringUtils.isNotBlank(errorUnit)) errorMsg += "标准条码:"+errorUnit+"箱托转换失败";
            throw new RuntimeException(errorMsg);
        }

        //临时表入库量合并
		for (InWarehouseTempModel inWarehouseTempModel : tempList) {
			String commbarcode = inWarehouseTempModel.getCommbarcode();
			//临时表入库数
			Integer num = inWarehouseTempModel.getWarehouseNum() == null ? 0 : inWarehouseTempModel.getWarehouseNum();

			//商品的数量合并到一起以入库单号、标准条码为key
			Map<String , Object> contentMap = (Map<String , Object>)inWarehouseNumMap.get(inWarehouseTempModel.getCode() + "#" + commbarcode)  ;
			if(contentMap == null) contentMap = new HashMap<String , Object>() ;
			Integer inWarehouseNum = (Integer)contentMap.get("inWarehouseNum") ;
			if(inWarehouseNum == null) inWarehouseNum = 0 ;

            inWarehouseNum += num ;

			contentMap.put("inWarehouseNum", inWarehouseNum) ;
			contentMap.put("tempModel", inWarehouseTempModel) ;
			inWarehouseNumMap.put(inWarehouseTempModel.getCode() + "#" + commbarcode , contentMap);
		}

	}


	/**
	 * 保存匹配的在库明细
	 */
	private void saveMacthInwarehouseDetails(BusinessUnitModel buModel, String month, Map<String, Integer> buInventorySumMapAll,
											 Map<String, Object> inWarehouseNumMap,Timestamp statisticsDate,Date endDate) throws Exception {
		logger.info("生成匹配报表明细开始");
		//循环采购入库量map生成在库明细
		for (String orderNo_commbarcode : inWarehouseNumMap.keySet()) {
			String orderNo = orderNo_commbarcode.split("#")[0] ;
			String commbarcode = orderNo_commbarcode.split("#")[1] ;
            Map<String , Object> contentMap = (Map)inWarehouseNumMap.get(orderNo_commbarcode);
            Integer warehouseNum = (Integer)contentMap.get("inWarehouseNum") ;//入库量
            Map<String, Object> warehouseMap = (Map<String, Object>)contentMap.get("warehouseMap");//入库信息
            InWarehouseTempModel tempModel = (InWarehouseTempModel)contentMap.get("tempModel") ;//临时表信息

            Integer buInventoryNum = buInventorySumMapAll.get(commbarcode);//库存量
			if(buInventoryNum == null || buInventoryNum.intValue() == 0) continue ;

			Integer benInWarehouseNum = 0 ;//分配数量
			//入库量<=库存量，本次分配量=入库量
			if(warehouseNum.intValue() <= Math.abs(buInventoryNum.intValue())) {
				benInWarehouseNum = warehouseNum;
			}else {
				benInWarehouseNum = buInventoryNum;
			}
			buInventoryNum -= benInWarehouseNum;//剩余库存量
			buInventorySumMapAll.put(commbarcode , buInventoryNum);

			/**查询属于这个标准条码的所有商品,获取商品id List、和获取第一个条码*/
			MerchandiseInfoModel merchandiseModel = new MerchandiseInfoModel();
			merchandiseModel.setCommbarcode(commbarcode);
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(merchandiseModel);
			if(merchandiseList==null||merchandiseList.size()<=0){
				throw new RuntimeException("标准条码商品未找到"+commbarcode);
			}
			List<Long> goodsIdList = merchandiseList.stream().map(e -> e.getId()).collect(Collectors.toList());
			String barcode = merchandiseList.get(0).getBarcode();
			Long merchantId = null;
			String merchantName = null;
			Long depotId = null;
			String depotName = null;
			Timestamp warehouseDate = null;//入库时间
			BigDecimal price = new BigDecimal(0);//单价
			if(warehouseMap != null) {
				PurchaseWarehouseModel queryWareModel = new PurchaseWarehouseModel() ;
				queryWareModel.setCode(orderNo);
				queryWareModel = purchaseWarehouseDao.searchByModel(queryWareModel) ;

				merchantId = queryWareModel.getMerchantId();
				merchantName = queryWareModel.getMerchantName() ;
				warehouseDate = queryWareModel.getInboundDate() ;
				depotId = queryWareModel.getDepotId();
				depotName = queryWareModel.getDepotName() ;

				Map<String, Object> queryPurchaseMap = new HashMap<String, Object>() ;
				queryPurchaseMap.put("warehouseCode", orderNo) ;
				String purchaseIds = purchaseWarehouseDao.getPurchaseOrderIdByWarehouse(queryPurchaseMap) ;

				//查询对应采购订单币种
				String[] idArr = purchaseIds.split(",");
				List<Long> purchaseIdList = new ArrayList<Long>() ;
				Map<String, String> currencyMap = new HashMap<String, String>() ;
				for (String purchaseOrderId : idArr) {
					PurchaseOrderModel purchaseModel = purchaseOrderDao.searchById(Long.valueOf(purchaseOrderId));
					purchaseIdList.add(Long.valueOf(purchaseOrderId)) ;
					currencyMap.put(purchaseOrderId, purchaseModel.getCurrency()) ;
				}

				queryPurchaseMap.clear();
				queryPurchaseMap.put("ids", purchaseIdList) ;
				queryPurchaseMap.put("goodsId", goodsIdList) ;
				List<PurchaseOrderItemModel> purchaseItemList = purchaseOrderItemDao.getInWarehouserItem(queryPurchaseMap) ;
				for (PurchaseOrderItemModel purchaseOrderItemModel : purchaseItemList) {
					if(purchaseOrderItemModel.getPrice() != null) {
						BigDecimal tempPrice = purchaseOrderItemModel.getPrice() ;
						Long purchaseOrderId = purchaseOrderItemModel.getPurchaseOrderId();
						String currency = currencyMap.get(String.valueOf(purchaseOrderId));

						if(!DERP.CURRENCYCODE_HKD.equals(currency)) {
							Double avgRate = exchangeAvgRate(currency,DERP.CURRENCYCODE_HKD,statisticsDate);
							tempPrice = tempPrice.multiply(new BigDecimal(avgRate)).setScale(3, BigDecimal.ROUND_HALF_UP) ;
						}
						price = price.add(tempPrice);
					}
				}
				//计算平均单价
				if(purchaseItemList!=null&&purchaseItemList.size()>0) {
					price = price.divide(new BigDecimal(purchaseItemList.size()), 3, BigDecimal.ROUND_HALF_UP);
				}

			}else if(tempModel != null) {
				merchantId = tempModel.getMerchantId();
				merchantName = tempModel.getMerchantName() ;
				warehouseDate = tempModel.getDivergenceDate() ;
				depotId = tempModel.getDepotId() ;
				depotName = tempModel.getDepotName() ;

				String tempCurrency = tempModel.getCurrency();
				BigDecimal tempPrice = tempModel.getPrice() ;
				//转换为港币单价
				if(!DERP.CURRENCYCODE_HKD.equals(tempCurrency)) {
					Double avgRate = exchangeAvgRate(tempCurrency,DERP.CURRENCYCODE_HKD,statisticsDate);
					price = tempPrice.multiply(new BigDecimal(avgRate)).setScale(3, BigDecimal.ROUND_HALF_UP) ;
				}
			}

			Integer inWarehouseDays = TimeUtils.daysBetween(warehouseDate, endDate);
			String inWarehouseRange = macthInWarehouseRange(inWarehouseDays);

			//在库金额=在库数量*标准成本单价
			BigDecimal inWarehouseAmount = price.multiply(new BigDecimal(benInWarehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);

			//查询加权单价
			WeightedPriceModel queryWeightedPriceModel = new WeightedPriceModel() ;
			queryWeightedPriceModel.setBuId(buModel.getId());
			queryWeightedPriceModel.setMerchantId(merchantId);
			queryWeightedPriceModel.setEffectiveMonth(month);
			queryWeightedPriceModel.setCommbarcode(commbarcode);
			queryWeightedPriceModel.setCurrency(DERP.CURRENCYCODE_HKD);
			queryWeightedPriceModel = weightedPriceDao.searchByModel(queryWeightedPriceModel) ;

			BigDecimal weightedPrice = null ;
			BigDecimal weightedAmount = null ;
			if(queryWeightedPriceModel != null) {
				weightedPrice = queryWeightedPriceModel.getPrice() ;
				weightedAmount = weightedPrice.multiply(new BigDecimal(benInWarehouseNum)).setScale(2, BigDecimal.ROUND_HALF_UP) ;
			}

			CommbarcodeModel queryModel = new CommbarcodeModel() ;
			queryModel.setCommbarcode(commbarcode);
			queryModel = commbarcodeDao.searchByModel(queryModel) ;

			InWarehouseDetailsModel inWarehouseDetailsModel = new InWarehouseDetailsModel();
			inWarehouseDetailsModel.setBuId(buModel.getId());
			inWarehouseDetailsModel.setBuName(buModel.getName());
			inWarehouseDetailsModel.setBarcode(barcode);
			inWarehouseDetailsModel.setMerchantId(merchantId);          							//商家ID
			inWarehouseDetailsModel.setMerchantName(merchantName);       							//商家名称
			inWarehouseDetailsModel.setMonth(month);                           						//月份
			inWarehouseDetailsModel.setGoodsName(queryModel.getCommGoodsName());                   	//商品名称
			inWarehouseDetailsModel.setBrandId(queryModel.getCommBrandParentId());         			//标准品牌ID
			inWarehouseDetailsModel.setBrandName(queryModel.getCommBrandParentName());				//标准品牌名称
			inWarehouseDetailsModel.setCommbarcode(commbarcode);               						//标准条码
			inWarehouseDetailsModel.setTypeId(queryModel.getCommTypeId());           				//商品二级分类id
			inWarehouseDetailsModel.setTypeName(queryModel.getCommTypeName());                     	//商品二级分类名称
			inWarehouseDetailsModel.setInWarehouseDays(inWarehouseDays);       						//在库天数
			inWarehouseDetailsModel.setWarehouseNo(orderNo);                						//归属入库单号
			inWarehouseDetailsModel.setWarehouseDate(warehouseDate);           						//入库时间
			inWarehouseDetailsModel.setWarehouseNum(warehouseNum);             						//入库量
			inWarehouseDetailsModel.setDepotId(depotId);                 //入库仓库id
			inWarehouseDetailsModel.setDepotName(depotName);             //入库仓库名称
			inWarehouseDetailsModel.setStatus(DERP_REPORT.INWAREHOUSEDETAILS_STATUS_01);                           //状态
			inWarehouseDetailsModel.setCreateDate(TimeUtils.getNow());         						//创建时间
			inWarehouseDetailsModel.setModifyDate(TimeUtils.getNow());         						//修改时间
			inWarehouseDetailsModel.setStatisticsDate(statisticsDate);								//统计截止时间
			inWarehouseDetailsModel.setInWarehouseNum(benInWarehouseNum);
			inWarehouseDetailsModel.setInWarehousePrice(price);
			inWarehouseDetailsModel.setInWarehouseAmount(inWarehouseAmount);   //在库金额
			inWarehouseDetailsModel.setCurrency(DERP.CURRENCYCODE_HKD);
			inWarehouseDetailsModel.setWeightedPrice(weightedPrice);
			inWarehouseDetailsModel.setWeightedAmount(weightedAmount);
			inWarehouseDetailsModel.setInWarehouseRange(inWarehouseRange);     //在库天数范围

			inWarehouseDetailsDao.save(inWarehouseDetailsModel);
			logger.info("匹配-在库天数明细保存成功,商家="+inWarehouseDetailsModel.getMerchantName()
					+"事业部="+inWarehouseDetailsModel.getBuName()+"月份="+inWarehouseDetailsModel.getMonth());

		}

		//inWarehouseDetailsDao.deleteNullInWarehouseNumDetails() ;
	}

	/**
	 * 保存未匹配的在库明细
	 */
	private void saveUnmacthInWarehouseDetails(BusinessUnitModel buModel, String month,
											   Map<String, Integer> buInventorySumMapAll,Timestamp statisticsDate) throws Exception {
		logger.info("保存未匹配明细开始..");

		for (String commbarcode : buInventorySumMapAll.keySet()) {

			Integer inventoryNum = buInventorySumMapAll.get(commbarcode);//剩余未匹配库存量
			if(inventoryNum == null || inventoryNum.intValue() == 0) continue ;

			/**查询属于这个标准条码的所有商品,获取第一个条码*/
			MerchandiseInfoModel merchandiseModel = new MerchandiseInfoModel();
			merchandiseModel.setCommbarcode(commbarcode);
			List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(merchandiseModel);
			String barcode = "";
			if(merchandiseList!=null&&merchandiseList.size()>0){
				barcode = merchandiseList.get(0).getBarcode();
			}

			CommbarcodeModel queryModel = new CommbarcodeModel();
			queryModel.setCommbarcode(commbarcode);
			queryModel = commbarcodeDao.searchByModel(queryModel) ;
			if(queryModel==null) queryModel = new CommbarcodeModel();

			InWarehouseDetailsModel inWarehouseDetailsModel = new InWarehouseDetailsModel();
			inWarehouseDetailsModel.setMonth(month);                           //月份
			inWarehouseDetailsModel.setGoodsName(queryModel.getCommGoodsName());                   //商品名称
			inWarehouseDetailsModel.setBrandId(queryModel.getCommBrandParentId());         //标准品牌ID
			inWarehouseDetailsModel.setBrandName(queryModel.getCommBrandParentName());				   //标准品牌名称
			inWarehouseDetailsModel.setCommbarcode(commbarcode);               //标准条码
			inWarehouseDetailsModel.setTypeId(queryModel.getCommTypeId());           //商品二级分类id
			inWarehouseDetailsModel.setTypeName(queryModel.getCommTypeName());                     //商品二级分类名称
			inWarehouseDetailsModel.setInWarehouseNum(inventoryNum);       //未匹配在库数量
			inWarehouseDetailsModel.setStatus(DERP_REPORT.INWAREHOUSEDETAILS_STATUS_00);                           //状态
			inWarehouseDetailsModel.setCreateDate(TimeUtils.getNow());         //创建时间
			inWarehouseDetailsModel.setModifyDate(TimeUtils.getNow());         //修改时间
			inWarehouseDetailsModel.setStatisticsDate(statisticsDate);
			inWarehouseDetailsModel.setCurrency(DERP.CURRENCYCODE_HKD);
			inWarehouseDetailsModel.setBuId(buModel.getId());
			inWarehouseDetailsModel.setBuName(buModel.getName());
			inWarehouseDetailsModel.setBarcode(barcode);
			inWarehouseDetailsModel.setInWarehouseRange("未匹配");              //在库天数范围

			inWarehouseDetailsDao.save(inWarehouseDetailsModel);
			logger.info("未匹配-保存成功,商家="+inWarehouseDetailsModel.getMerchantName()
					+",事业部="+inWarehouseDetailsModel.getBuName()+",标准条码="+inWarehouseDetailsModel.getCommbarcode());

		}

	}

	/**
	 * 匹配在库天数范围
	 * @param inWarehouseDays
	 * @return
	 */
	private String macthInWarehouseRange(Integer inWarehouseDays) {
		String inwarehouserange = "" ;
		if(0 <= inWarehouseDays && inWarehouseDays <= 30) {
			inwarehouserange = "0~30天" ;
		}else if( inWarehouseDays <= 60) {
			inwarehouserange = "30~60天" ;
		}else if( inWarehouseDays <= 90) {
			inwarehouserange = "60~90天" ;
		}else if( inWarehouseDays <= 120) {
			inwarehouserange = "90~120天" ;
		}else if( inWarehouseDays <= 150) {
			inwarehouserange = "120~150天" ;
		}else if( inWarehouseDays <= 180) {
			inwarehouserange = "150~180天" ;
		}else if( inWarehouseDays > 180) {
			inwarehouserange = "180天以上" ;
		}

		return inwarehouserange ;
	}

	/**
	 * 箱托转换
	 * @param unit
	 * @param num
	 * @param boxToUnit
	 * @param torrToUnit
	 */
	private Integer changeUnit(String unit,BigDecimal num,Integer boxToUnit,Integer torrToUnit) {
		Integer numInt= 0;
		if(num==null) return numInt;

		//若是单据的理货单位则转换未库存的理货单位
		if(StringUtils.isNotBlank(unit)){
			if(DERP.ORDER_TALLYINGUNIT_00.equals(unit)) {
				unit = DERP.INVENTORY_UNIT_0;
			}else if(DERP.ORDER_TALLYINGUNIT_01.equals(unit)) {
				unit = DERP.INVENTORY_UNIT_1;
			}else if(DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
				unit = DERP.INVENTORY_UNIT_2;
			}
		}

		//转换单位为件后返回
		if(StringUtils.isEmpty(unit)||unit.equals(DERP.INVENTORY_UNIT_2)){
			numInt=num.intValue();
		}else if(unit.equals(DERP.INVENTORY_UNIT_1)){
			if(boxToUnit == null||boxToUnit.intValue() <= 0){
				return null;
			}
			numInt = num.intValue() * boxToUnit.intValue();
		}else if(unit.equals(DERP.INVENTORY_UNIT_0)){
			if(torrToUnit == null || torrToUnit.intValue() <= 0){
				return null;
			}
			numInt = num.intValue() * torrToUnit.intValue();
		}
		return numInt;
	}

	/**按常规品-赠品排序
	 * */
	private void sortList(List<String> kwCommbarcodeList){
		//按常规品-赠品排序
		kwCommbarcodeList.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if(o1.startsWith("N") &&
						!o2.startsWith("N")) {
					return -1 ;
				}
				if(!o1.startsWith("N") &&
						o2.startsWith("N")) {
					return 1 ;
				}
				if((o1.startsWith("N") &&
						o2.startsWith("N"))) {
					return o2.compareTo(o1) ;
				}
				return 0;
			}
		});
	}

	/**获取币种兑换平均汇率
	 * @param origCurrencyCode 原币种编码
	 * @param TgtCurrencyCode 目标币种编码
	 * */
	private Double exchangeAvgRate(String origCurrencyCode,String TgtCurrencyCode,Timestamp statisticsDate) throws Exception{
		Double avgRate = null;
		if(origCurrencyCode.equals(TgtCurrencyCode)){
			avgRate = 1.00;
			return avgRate;
		}
		ExchangeRateModel exchangeRateModel = new ExchangeRateModel() ;
		exchangeRateModel.setOrigCurrencyCode(origCurrencyCode);
		exchangeRateModel.setTgtCurrencyCode(TgtCurrencyCode);
		exchangeRateModel.setEffectiveDate(statisticsDate);
		List<ExchangeRateModel> rateList = exchangeRateDao.getRecentRateList(exchangeRateModel);
		if(rateList==null || rateList.size()<=0) return avgRate;
		for (ExchangeRateModel rateModel : rateList) {
			if(rateModel.getAvgRate() != null) {
				avgRate = rateModel.getAvgRate();
				break ;
			}
		}
        if(avgRate == null){
			throw new RuntimeException("平均汇率未找到,原币种"+origCurrencyCode+"目标币种"+TgtCurrencyCode+"生效日期"+statisticsDate);
		}
		return avgRate;
	}
}
