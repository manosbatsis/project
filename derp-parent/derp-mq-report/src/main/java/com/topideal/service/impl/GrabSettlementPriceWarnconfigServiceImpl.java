package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseWarehouseBatchDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.order.PurchaseWarehouseItemDao;
import com.topideal.dao.order.WarehouseOrderRelDao;
import com.topideal.dao.reporting.SettlementPriceDao;
import com.topideal.dao.reporting.SettlementPriceWarnconfigDao;
import com.topideal.dao.reporting.SkuPriceWarnDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.entity.dto.PurchaseWarehouseDTO;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.entity.vo.order.PurchaseWarehouseBatchModel;
import com.topideal.entity.vo.order.PurchaseWarehouseItemModel;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;
import com.topideal.entity.vo.reporting.SkuPriceWarnModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.GrabSettlementPriceWarnconfigService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 标准成本单价波动预警
 */
@Service
public class GrabSettlementPriceWarnconfigServiceImpl implements GrabSettlementPriceWarnconfigService {
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;// 采购订单
	@Autowired
	private SettlementPriceWarnconfigDao settlementPriceWarnconfigDao;
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
	@Autowired
	private DepotInfoDao depotInfoDao;
	@Autowired
	private SettlementPriceDao settlementPriceDao;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private SkuPriceWarnDao skuPriceWarnDao;
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabSettlementPriceWarnconfigServiceImpl.class);

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201306800, model = DERP_LOG_POINT.POINT_13201306800_Label)
	public boolean saveGrabDerpPurchaseIdepotOrder(String json, String keys, String topics, String tags)
			throws Exception {
		LOGGER.info("抓取经分销采购入库单入库仓库为保税仓、海外仓的入库数据json:" + json);
		Date startTimeDate=null;
		Date endTimeDate=null;
		Calendar now = Calendar.getInstance(); 
		int day = now.get(Calendar.DAY_OF_MONTH);
		// 若当前日期是1号则统计上月整月数据
		if(day==1){
			// 分别获取获取上月的开始时间、结束时间
			startTimeDate = TimeUtils.getBeginDayOfLastMonth();
			endTimeDate = TimeUtils.getEndDayOfLastMonth();
		}else if(TimeUtils.getWeekOfDate(TimeUtils.getNow()).equals("星期一")){	// 若当前日期非1号，判断是否周一，周一则统计前7天。
			// 分别获取上周开始时间、结束时间
			startTimeDate = TimeUtils.getBeginDayOfLastWeek();
			endTimeDate = TimeUtils.getEndDayOfLastWeek();
		}else{ // 都不是则结束,不发邮件
			LOGGER.info("若当前日期非1号，非周一，不发标准成本单价波动预警邮件");
			return true;
		}
		 Timestamp startTime = new Timestamp(startTimeDate.getTime());
		 Timestamp endTime = new Timestamp(endTimeDate.getTime());
		 // 邮件模板展示
		 String startTimeStr = TimeUtils.formatDay(startTime);
		 String endTimeStr = TimeUtils.formatDay(endTime);
		
		// 查询启用状态下配置信息
		SettlementPriceWarnconfigModel model = new SettlementPriceWarnconfigModel();
		model.setStatus(DERP_REPORT.SETTLEMENTPRICEWARNCONFIG_STATUS_1);
		List<SettlementPriceWarnconfigModel> findAllList = settlementPriceWarnconfigDao.list(model);
		if (findAllList == null || findAllList.size() == 0) {
			LOGGER.info("标准成本单价预警配置表 ,没有启用的标准成本单价预警配置信息");
			return true;
		}
		for (SettlementPriceWarnconfigModel settlementPriceWarnconfigModel : findAllList) {
			// 业务参数=========================
			Long merchantId = settlementPriceWarnconfigModel.getMerchantId();// 公司id
			String merchantName = settlementPriceWarnconfigModel.getMerchantName();// 公司
			String buName = settlementPriceWarnconfigModel.getBuName(); // 事业部
			Long buId = settlementPriceWarnconfigModel.getBuId(); // 事业部id
			BigDecimal waveRange = settlementPriceWarnconfigModel.getWaveRange(); // 波动范围
			String receiverEmail = settlementPriceWarnconfigModel.getReceiverEmail(); // 多个用;逗号隔开
			String receiverNameList = settlementPriceWarnconfigModel.getReceiverName();// 多个用;逗号隔开
			String emailTitle = settlementPriceWarnconfigModel.getEmailTitle();

			JSONObject jsonObject = new JSONObject();// 推送内容
			jsonObject.put("mailCode", SmurfsAPICodeEnum.EMAIL_M020.getCode());
			JSONObject paramJson = new JSONObject();// 存储所有数据的结果
			JSONObject data = new JSONObject();// 存储数据结果集
			JSONArray attachmentArray = new JSONArray();// 存储附件结果集JSONArray

			// 以采购入库单创建时间(创建时间为上个月/上周),仅要入库仓库为保税仓、海外仓的入库数据为准，触发邮件预警逻辑校验，如下：
			PurchaseWarehouseDTO purchaseWarehouseDTO  = new PurchaseWarehouseDTO();
			purchaseWarehouseDTO.setMerchantId(merchantId); // 查询选择的公司
			purchaseWarehouseDTO.setBuId(buId); // 查询选择的事业部
			purchaseWarehouseDTO.setCreateStartDate(startTime);	// 创建时间开始
			purchaseWarehouseDTO.setCreateEndDate(endTime);	// 创建时间结束
			List<PurchaseWarehouseDTO> purchaseWarehouseList = purchaseWarehouseDao.getByTimeOrder(purchaseWarehouseDTO);
			if (purchaseWarehouseList.size() == 0) {
				LOGGER.info("标准成本单价波动预警======> 采购入库单创建时间在(上个月/上周)、商家:"+merchantName+"事业部:"+buName+",没有查询到采购入库单");
				continue;
			}

			List<PurchaseWarehouseDTO> yesterdayOrderList = new ArrayList<>(); // 保存入库仓库为保税仓、海外仓的入库数据
			for (int i = 0; i < purchaseWarehouseList.size(); i++) {
				PurchaseWarehouseDTO purchaseWarehouse  = purchaseWarehouseList.get(i);
				// 获取仓库信息
				DepotInfoModel depot = depotInfoDao.searchById(purchaseWarehouse.getDepotId());
				// 仅要入库仓库为保税仓、海外仓的入库数据
				if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depot.getType())
						|| DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
					yesterdayOrderList.add(purchaseWarehouse);
				}
			}
			if (yesterdayOrderList == null || yesterdayOrderList.size() == 0) {
				LOGGER.info("标准成本单价波动预警======>以采购入库单创建时间在(上个月/上周)、商家:"+merchantName+"事业部:"+buName+",仅要入库仓库为保税仓、海外仓的入库数据为准,没有查询到采购入库单");
				continue;
			}
			int countA = 0;
			int countB = 0;

			JSONObject attachmentObejct = new JSONObject();// 存储附件结果集
			attachmentObejct.put("code", SmurfsAPICodeEnum.EMAIL_M021.getCode());
			attachmentObejct.put("fileName", "单价预警SKU导出模板.xlsx");
			JSONArray sheetJsonArray = new JSONArray();

			JSONObject sheetJSONObject0 = new JSONObject();
			sheetJSONObject0.put("columns",
					"公司,事业部,商品条码,商品名称,采购单价,采购币种,折算汇率价,标准成本单价,标准成本单价币种," + "波动率,供应商,ERP采购单号,PO号,采购入库单号,批次,生产日期,失效日期,入库时间");
			sheetJSONObject0.put("sheentIndex", 0);

			JSONObject sheetJSONObject1 = new JSONObject();
			sheetJSONObject1.put("columns", "公司,事业部,商品条码,商品名称,采购单价,采购币种,供应商,ERP采购单号,PO号,采购入库单号,批次,生产日期,失效日期,入库时间");
			sheetJSONObject1.put("sheentIndex", 1);

			JSONArray dataListJsonArray0 = new JSONArray();
			JSONArray dataListJsonArray1 = new JSONArray();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();


			// 遍历入库仓库为保税仓、海外仓的入库数据
			for (int k = 0; k < yesterdayOrderList.size(); k++) {
				// 采购入库单
				PurchaseWarehouseDTO pwm = yesterdayOrderList.get(k);
				// 获取采购入库商品
				PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
				itemModel.setWarehouseId(pwm.getId());
				List<PurchaseWarehouseItemModel> itemList = purchaseWarehouseItemDao.list(itemModel);

				// 获取对应采购订单ID（采购入库关联采购订单表）
				WarehouseOrderRelModel wor = new WarehouseOrderRelModel();
				wor.setWarehouseId(pwm.getId());
				List<WarehouseOrderRelModel> warehouseOrderRelList = warehouseOrderRelDao.list(wor);
				if (warehouseOrderRelList == null || warehouseOrderRelList.size() == 0) {
					LOGGER.error("标准成本单价波动预警======>采购入库关联采购订单表(t_warehouse_order_rel)里没有对应数据,采购入库单id：" + pwm.getId());
					continue;
				}
				List<PurchaseOrderModel> purchaseOrderList = new ArrayList<>();
				for (int i = 0; i < warehouseOrderRelList.size(); i++) {
					// 获取对应采购订单信息
					PurchaseOrderModel pom = new PurchaseOrderModel();
					pom.setId(warehouseOrderRelList.get(i).getPurchaseOrderId());
					PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchByModel(pom);
					purchaseOrderList.add(purchaseOrderModel);
				}
				for (int j = 0; j < purchaseOrderList.size(); j++) {
					PurchaseOrderModel purchaseOrderModel = purchaseOrderList.get(j);
					// 遍历采购入库商品
					for (int i = 0; i < itemList.size(); i++) {
						JSONObject dataJSONObject0 = new JSONObject();
						JSONObject dataJSONObject1 = new JSONObject();

						PurchaseWarehouseItemModel item = itemList.get(i);

						// 采购入库商品批次表
						PurchaseWarehouseBatchModel batchModel = new PurchaseWarehouseBatchModel();
						batchModel.setItemId(item.getId());
						batchModel.setGoodsId(item.getGoodsId());
						List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.list(batchModel);

						
						/**
						 * 判断采购入库单中对应的采购币种是否与标准单价维护币种一致，若一致则 拿 采购单价与【标准成本单价*（1
						 * +/- 波动范围）】比较判断是否超范围，对超范围的SKU发送单价预警邮件；
						 */
						// 拿采购入库单商品的货号去采购订单表体找商品单价
						if (item.getPurchaseItemId()==null) {
							LOGGER.error("标准成本单价波动预警======>入库单表体采购订单表体id为空,货号"+item.getGoodsNo());
							continue;
						}
						PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
						purchaseOrderItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
						purchaseOrderItemModel.setGoodsNo(item.getGoodsNo());
						purchaseOrderItemModel.setId(item.getPurchaseItemId());
						purchaseOrderItemModel = purchaseOrderItemDao.searchByModel(purchaseOrderItemModel);
						
						if(purchaseOrderItemModel==null){
							LOGGER.error("标准成本单价波动预警======>根据采购订单id：" + purchaseOrderModel.getId() + "+商品货号:"
									+ item.getGoodsNo() + "没有查询到采购商品");
							continue;
						}

						// 根据采购入库单中已入库的 “事业部+商品条码”查询标准成本单价维护表，若查不到维护的成本单价金额时邮件预警
						Map<String, Object> map =new HashMap<String, Object>();
						map.put("buId", pwm.getBuId());
						map.put("barcode", item.getBarcode());
						map.put("status", DERP_REPORT.SETTLEMENTPRICE_STATUS_032);// 已生效
						map.put("effectiveDate",TimeUtils.format(pwm.getInboundDate(), "yyyy-MM"));	// 进仓生效日期
						List<SettlementPriceDTO> settlementPriceList = settlementPriceDao
								.getListByEffectiveDate(map);
						// 新品维护预警
						if (settlementPriceList == null || settlementPriceList.size() == 0) {
							countA = countA+1;
							LOGGER.info("标准成本单价波动预警======>根据采购入库单中已入库的 “事业部：" + pwm.getBuId() + "+商品条码:"
									+ item.getBarcode() + "”查询标准成本单价维护表,查不到维护的成本单价金额时邮件预警");
							JSONObject jsonModel = new JSONObject();
							jsonModel.put("merchantName", merchantName);
							jsonModel.put("buName", buName);
							jsonModel.put("barcode", item.getBarcode());
							jsonModel.put("goodsName", item.getGoodsName());
							BigDecimal purchaseItemBd = purchaseOrderItemModel.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
							jsonModel.put("purchasePrice", purchaseOrderModel.getCurrency()+purchaseItemBd);
							jsonModel.put("supplier", purchaseOrderModel.getSupplierName());
							jsonModel.put("purchaseCode", purchaseOrderModel.getCode());
							jsonModel.put("poNo", purchaseOrderModel.getPoNo());
							jsonArray.add(jsonModel);
							data.put("settlementPriceList", jsonArray); // 新品维护预警
							
							// 注入数据-SKU单价预警表
							SkuPriceWarnModel skuPriceWarnModel = new SkuPriceWarnModel();
							skuPriceWarnModel.setMerchantId(merchantId);
							skuPriceWarnModel.setMerchantName(merchantName);
							skuPriceWarnModel.setBuId(buId);
							skuPriceWarnModel.setBuName(buName);
							skuPriceWarnModel.setSku(item.getBarcode());	// 条形码
							skuPriceWarnModel.setPurchaseCurrency(purchaseOrderModel.getCurrency());
							skuPriceWarnModel.setPurchasePrice(purchaseItemBd);
							skuPriceWarnModel.setWarnType(DERP_REPORT.SKUPRICEWARN_WARNTYPE_1);// 预警类型 1-新品预警维护
							skuPriceWarnModel.setMonth(TimeUtils.formatMonth(TimeUtils.getNow()));// 归属月份
							skuPriceWarnModel.setCreateDate(TimeUtils.getNow());
							skuPriceWarnModel.setModifyDate(TimeUtils.getNow());
							skuPriceWarnDao.save(skuPriceWarnModel);

							if(batchList.size()>0){
								for (int t = 0; t < batchList.size(); t++) {
									// sheet内容1
									PurchaseWarehouseBatchModel batch = batchList.get(t);
									dataJSONObject1.put("公司", merchantName);
									dataJSONObject1.put("事业部", buName);
									dataJSONObject1.put("商品条码", item.getBarcode());
									dataJSONObject1.put("商品名称", item.getGoodsName());
									dataJSONObject1.put("采购单价", purchaseOrderItemModel.getPrice());
									dataJSONObject1.put("采购币种", purchaseOrderModel.getCurrency());
									dataJSONObject1.put("供应商", purchaseOrderModel.getSupplierName());
									dataJSONObject1.put("ERP采购单号", purchaseOrderModel.getCode());
									dataJSONObject1.put("PO号", purchaseOrderModel.getPoNo()==null?" ":purchaseOrderModel.getPoNo());
									dataJSONObject1.put("采购入库单号", pwm.getCode());
									dataJSONObject1.put("批次", batch.getBatchNo()==null?" ":batch.getBatchNo());
									String productionDateStr = null;
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									if(batch.getProductionDate()!=null){
										productionDateStr = sdf.format(batch.getProductionDate());
									}
									String overdueDateStr = null;
									if(batch.getOverdueDate()!=null){
										overdueDateStr = sdf.format(batch.getOverdueDate());
									}
									dataJSONObject1.put("生产日期", productionDateStr==null?" ":productionDateStr);
									dataJSONObject1.put("失效日期", overdueDateStr==null?" ":overdueDateStr);
									String inboundDateStr =null;
									if(pwm.getInboundDate()!=null){
										inboundDateStr = TimeUtils.formatDay(pwm.getInboundDate());
									}
									dataJSONObject1.put("入库时间", inboundDateStr);	// 采购入库单的入库时间
									dataListJsonArray1.add(dataJSONObject1);
								}
							}
							
							continue;
						}
						SettlementPriceDTO spd = settlementPriceList.get(0);// 成本单价

						// 转换为小数
						BigDecimal waveRangeBd = waveRange.divide(new BigDecimal("100"));
						// 标准成本单价*(1-波动范围)
						BigDecimal waveRangeSub = new BigDecimal("1").subtract(waveRangeBd);
						BigDecimal waveRangeSubPrice = spd.getPrice().multiply(waveRangeSub);
						// 标准成本单价*(1+波动范围)
						BigDecimal waveRangeAdd = new BigDecimal("1").add(waveRangeBd);
						BigDecimal waveRangeAddPrice = spd.getPrice().multiply(waveRangeAdd);
						// 附件 sheet0
						JSONObject jsonModel = new JSONObject();
						jsonModel.put("columns", "公司,事业部,商品条码,商品名称,采购单价,采购币种,折算汇率,标准成本单价,标准成本单价币种,"
								+ "波动率,供应商,ERP采购单号,采购入库单号,批次,生产日期,失效日期");
						jsonModel.put("sheentIndex", "0");

						// 单价波动预警	
						JSONObject jsonModel2 = new JSONObject();
						jsonModel2.put("merchantName", merchantName);
						jsonModel2.put("buName", buName);
						jsonModel2.put("barcode", item.getBarcode()==null?" ":item.getBarcode());
						jsonModel2.put("goodsName", item.getGoodsName());
						BigDecimal purchasePriceBd = purchaseOrderItemModel.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
						jsonModel2.put("purchasePrice", purchaseOrderModel.getCurrency()+purchasePriceBd);
						BigDecimal spdPriceBd = spd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
						jsonModel2.put("settlementPrice", spd.getCurrency()+spdPriceBd);
						jsonModel2.put("supplier", purchaseOrderModel.getSupplierName());
						jsonModel2.put("purchaseCode", purchaseOrderModel.getCode());
						jsonModel2.put("poNo", purchaseOrderModel.getPoNo());
						int isPriceWaveRange = 0;
						// 注入数据-SKU单价预警表（ 2-波动预警）
						SkuPriceWarnModel skuPriceWarnModel2 = new SkuPriceWarnModel();

						// 若币种一致,比较判断是否超范围
						if (spd.getCurrency().equals(purchaseOrderModel.getCurrency())) {
							if (purchaseOrderItemModel.getPrice().compareTo(waveRangeSubPrice) < 1
									|| purchaseOrderItemModel.getPrice().compareTo(waveRangeAddPrice) > -1) {
								countB = countB+1;
								isPriceWaveRange = isPriceWaveRange+1;
								BigDecimal purchasePBd = purchaseOrderItemModel.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal  num1=purchaseOrderItemModel.getPrice().subtract(spd.getPrice());
								BigDecimal  num2= new BigDecimal("0");
								BigDecimal zeroNum = new BigDecimal("0.00000");// 为0
								BigDecimal spdPrice = new BigDecimal("0");// 存成本单价
								// 若成本单价为0，默认成本单价为0.1
								if(spd.getPrice().equals(zeroNum)){
									spdPrice = new BigDecimal("0.1");
								}else{
									spdPrice = spd.getPrice();
								}
								// 若为负数
								if(num1.compareTo(BigDecimal.ZERO)==-1){
									num1 = num1.abs().setScale(2, BigDecimal.ROUND_HALF_UP);// 采购单价-成本单价
									num2 = num1.divide(spdPrice,2,BigDecimal.ROUND_HALF_UP);
									num2 = num2.divide(new BigDecimal("-1"),2, BigDecimal.ROUND_HALF_UP);
								}else{// 若为正数
									num2=num1.divide(spdPrice,2, BigDecimal.ROUND_HALF_UP);
								}
								num2 = num2.multiply(new BigDecimal("100"));
								String waveStr = num2.stripTrailingZeros().toPlainString();
								jsonModel2.put("floatRate", waveStr+"%");
								skuPriceWarnModel2.setWaveRate(num2);	// SKU单价预警表-浮动率
								skuPriceWarnModel2.setPurchaseCurrency(purchaseOrderModel.getCurrency());// 采购币种
								skuPriceWarnModel2.setPurchasePrice(purchasePBd);
								skuPriceWarnModel2.setSettlementCurrency(spd.getCurrency());	// 成本单价
								skuPriceWarnModel2.setSettlementPrice(spd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
								skuPriceWarnModel2.setAfterCurrency(purchaseOrderModel.getCurrency());// 折算币种
								skuPriceWarnModel2.setAfterPrice(purchasePBd);	// 折算汇率价
								jsonModel2.put("afterPrice", purchaseOrderModel.getCurrency()+purchasePBd); // 本位币换算
								if(batchList.size()>0){
									for (int t = 0; t < batchList.size(); t++) {
										PurchaseWarehouseBatchModel batch = batchList.get(t);
										// sheet内容0(单价波动SKU)
										dataJSONObject0.put("公司", merchantName);
										dataJSONObject0.put("事业部", buName);
										dataJSONObject0.put("商品条码", item.getBarcode()==null?" ":item.getBarcode());
										dataJSONObject0.put("商品名称", item.getGoodsName());
										dataJSONObject0.put("采购单价", purchaseOrderItemModel.getPrice());
										dataJSONObject0.put("采购币种", purchaseOrderModel.getCurrency());
										dataJSONObject0.put("标准成本单价", spd.getPrice());
										dataJSONObject0.put("标准成本单价币种", spd.getCurrency());
										dataJSONObject0.put("波动率", waveStr+"%");
										dataJSONObject0.put("供应商", purchaseOrderModel.getSupplierName());
										dataJSONObject0.put("ERP采购单号", purchaseOrderModel.getCode());
										dataJSONObject0.put("PO号", purchaseOrderModel.getPoNo()==null?" ":purchaseOrderModel.getPoNo());
										dataJSONObject0.put("采购入库单号", pwm.getCode());
										String productionDateStr = null;
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										if(batch.getProductionDate()!=null){
											productionDateStr = sdf.format(batch.getProductionDate());
										}
										String overdueDateStr = null;
										if(batch.getOverdueDate()!=null){
											overdueDateStr = sdf.format(batch.getOverdueDate());
										}
										dataJSONObject0.put("批次", batch.getBatchNo()==null?" ":batch.getBatchNo());
										dataJSONObject0.put("生产日期", productionDateStr==null?" ":productionDateStr);
										dataJSONObject0.put("失效日期", overdueDateStr==null?" ":overdueDateStr);
										String inboundDateStr =null;
										if(pwm.getInboundDate()!=null){
											inboundDateStr = TimeUtils.formatDay(pwm.getInboundDate());
										}
										dataJSONObject0.put("入库时间", inboundDateStr);
										dataJSONObject0.put("折算汇率价", purchasePBd);
										dataListJsonArray0.add(dataJSONObject0);
									}
								}
							}
						}
						/**
						 * 若不一致则查询 汇率配置表，找到采购币种与本位币种的换算比例，拿采购单价转换成本位币之后的金额与
						 * 【标准成本单价*（1 +/- 波动范围）】比较判断是否超范围，对超范围的SKU发送单价预警邮件
						 */
						else {
							ExchangeRateModel erm = new ExchangeRateModel();
							erm.setOrigCurrencyCode(purchaseOrderModel.getCurrency());
							erm.setTgtCurrencyCode(spd.getCurrency());
							erm.setEffectiveDate(pwm.getCreateDate());// 采购入库单的创建日期
							List<ExchangeRateModel> exchangeRateList = exchangeRateDao.getRecentRateList(erm);
							if (exchangeRateList == null || exchangeRateList.size() == 0) {
								LOGGER.error("标准成本单价波动预警======>原币种编码:"+purchaseOrderModel.getCurrency()+"目标币种编码:"+spd.getCurrency()+"生效年月:"+pwm.getCreateDate()+"没有查询到对应汇率管理表信息,结束执行");
								throw new RuntimeException("标准成本单价波动预警======>原币种编码:"+purchaseOrderModel.getCurrency()+"目标币种编码:"+spd.getCurrency()+"生效年月:"+pwm.getCreateDate()+"没有查询到对应汇率管理表信息,结束执行");
							}
							// 汇率管理
							ExchangeRateModel exchangeRateModel = exchangeRateList.get(0);
							// 采购单价转换成本位币之后的金额
							BigDecimal afterPrice = purchaseOrderItemModel.getPrice()
									.multiply(new BigDecimal(exchangeRateModel.getRate()));

							if (afterPrice.compareTo(waveRangeSubPrice) < 1
									|| afterPrice.compareTo(waveRangeAddPrice) > -1) {
								countB = countB+1;
								isPriceWaveRange = isPriceWaveRange+1;
								BigDecimal afterPriceBd = afterPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal  num1=afterPrice.subtract(spd.getPrice());
								BigDecimal  num2= new BigDecimal("0");
								BigDecimal zeroNum = new BigDecimal("0.00000");// 为0
								BigDecimal spdPrice = new BigDecimal("0");// 存成本单价
								// 若成本单价为0，默认成本单价为0.1
								if(spd.getPrice().equals(zeroNum)){
									spdPrice = new BigDecimal("0.1");
								}else{
									spdPrice = spd.getPrice();
								}
								if(num1.compareTo(BigDecimal.ZERO)==-1){
									BigDecimal num= num1.abs().setScale(2, BigDecimal.ROUND_HALF_UP);
									num2 = num.divide(spdPrice,2, BigDecimal.ROUND_UP);
									num2= num2.divide(new BigDecimal(-1),2,BigDecimal.ROUND_UP);
								}else{
									num2=num1.divide(spdPrice,2, BigDecimal.ROUND_HALF_UP);
								}
								num2 = num2.multiply(new BigDecimal("100"));
								String waveStr = num2.stripTrailingZeros().toPlainString();
								jsonModel2.put("floatRate", waveStr+"%");
								skuPriceWarnModel2.setWaveRate(num2);	// SKU单价预警表-浮动率
								skuPriceWarnModel2.setPurchaseCurrency(purchaseOrderModel.getCurrency());	// 采购单价
								BigDecimal purchasePBd = purchaseOrderItemModel.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
								skuPriceWarnModel2.setPurchasePrice(purchasePBd);
								skuPriceWarnModel2.setSettlementCurrency(spd.getCurrency());	//成本单价币种
								skuPriceWarnModel2.setSettlementPrice(spd.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));// 成本单价
								skuPriceWarnModel2.setAfterCurrency(spd.getCurrency());//折算币种
								skuPriceWarnModel2.setAfterPrice(afterPriceBd);
								
								jsonModel2.put("afterPrice", spd.getCurrency()+afterPriceBd); // 本位币换算
								if(batchList.size()>0){
									for (int t = 0; t < batchList.size(); t++) {
										PurchaseWarehouseBatchModel batch = batchList.get(t);
										// sheet内容0(单价波动SKU)
										dataJSONObject0.put("公司", merchantName);
										dataJSONObject0.put("事业部", buName);
										dataJSONObject0.put("商品条码", item.getBarcode()==null?" ":item.getBarcode());
										dataJSONObject0.put("商品名称", item.getGoodsName());
										dataJSONObject0.put("采购单价", purchaseOrderItemModel.getPrice());
										dataJSONObject0.put("采购币种", purchaseOrderModel.getCurrency());
										dataJSONObject0.put("标准成本单价", spd.getPrice());
										dataJSONObject0.put("标准成本单价币种", spd.getCurrency());
										dataJSONObject0.put("波动率", waveStr+"%");
										dataJSONObject0.put("供应商", purchaseOrderModel.getSupplierName());
										dataJSONObject0.put("ERP采购单号", purchaseOrderModel.getCode());
										dataJSONObject0.put("PO号", purchaseOrderModel.getPoNo()==null?" ":purchaseOrderModel.getPoNo());
										dataJSONObject0.put("采购入库单号", pwm.getCode());
										String productionDateStr = null;
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										if(batch.getProductionDate()!=null){
											productionDateStr = sdf.format(batch.getProductionDate());
										}
										String overdueDateStr = null;
										if(batch.getOverdueDate()!=null){
											overdueDateStr = sdf.format(batch.getOverdueDate());
										}
										dataJSONObject0.put("批次", batch.getBatchNo()==null?" ":batch.getBatchNo());
										dataJSONObject0.put("生产日期", productionDateStr==null?" ":productionDateStr);
										dataJSONObject0.put("失效日期", overdueDateStr==null?" ":overdueDateStr);
										String inboundDateStr =null;
										if(pwm.getInboundDate()!=null){
											inboundDateStr = TimeUtils.formatDay(pwm.getInboundDate());
										}
										dataJSONObject0.put("入库时间", inboundDateStr);
										dataJSONObject0.put("折算汇率价", afterPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
										dataListJsonArray0.add(dataJSONObject0);
									}
								}
							}
						}
						// 附件
						if (isPriceWaveRange > 0) {
							// 注入数据-SKU单价预警表（ 2-波动预警）
							skuPriceWarnModel2.setMerchantId(merchantId);
							skuPriceWarnModel2.setMerchantName(merchantName);
							skuPriceWarnModel2.setBuId(buId);
							skuPriceWarnModel2.setBuName(buName);
							skuPriceWarnModel2.setSku(item.getBarcode());
							skuPriceWarnModel2.setWarnType(DERP_REPORT.SKUPRICEWARN_WARNTYPE_2);// 预警类型  2-波动预警
							skuPriceWarnModel2.setMonth(TimeUtils.formatMonth(TimeUtils.getNow()));// 归属月份
							skuPriceWarnModel2.setCreateDate(TimeUtils.getNow());
							skuPriceWarnModel2.setModifyDate(TimeUtils.getNow());
							skuPriceWarnDao.save(skuPriceWarnModel2);
							jsonArray2.add(jsonModel2);
							data.put("priceWaveRangeList", jsonArray2); // 单价波动预警
						}
					}
				}
			}
			
			if (dataListJsonArray0.size()==0) {
				JSONObject dataJSONObject0 = new JSONObject();
				dataJSONObject0.put("公司", " ");
				dataJSONObject0.put("事业部", " ");
				dataJSONObject0.put("商品条码", " ");
				dataJSONObject0.put("商品名称", " ");
				dataJSONObject0.put("采购单价", " ");
				dataJSONObject0.put("采购币种", " ");
				dataJSONObject0.put("标准成本单价", " ");
				dataJSONObject0.put("标准成本单价币种", " ");
				dataJSONObject0.put("波动率", " ");
				dataJSONObject0.put("供应商", " ");
				dataJSONObject0.put("ERP采购单号", " ");
				dataJSONObject0.put("PO号", "  ");
				dataJSONObject0.put("采购入库单号"," ");
				dataJSONObject0.put("批次", " ");
				dataJSONObject0.put("生产日期", " ");
				dataJSONObject0.put("失效日期", " ");
				dataJSONObject0.put("入库时间", " ");
				dataJSONObject0.put("折算汇率价", " ");
				dataListJsonArray0.add(dataJSONObject0);
			}
			if (dataListJsonArray1.size()==0) {
				JSONObject dataJSONObject1 = new JSONObject();
				dataJSONObject1.put("公司", " ");
				dataJSONObject1.put("事业部", " ");
				dataJSONObject1.put("商品条码", " ");
				dataJSONObject1.put("商品名称"," ");
				dataJSONObject1.put("采购单价", " ");
				dataJSONObject1.put("供应商", " ");
				dataJSONObject1.put("ERP采购单号", " ");
				dataJSONObject1.put("PO号", "  ");
				dataJSONObject1.put("采购入库单号", " ");
				dataJSONObject1.put("批次", " ");
				dataJSONObject1.put("生产日期", " ");
				dataJSONObject1.put("失效日期", " ");
				dataJSONObject1.put("入库时间", " ");
				dataListJsonArray1.add(dataJSONObject1);
			}
			if (data != null) {
				if(data.get("settlementPriceList")==null){
					JSONArray jsonArray0 = new JSONArray();
					JSONObject jsonModel = new JSONObject();
					jsonModel.put("merchantName", " ");
					jsonModel.put("buName", " ");
					jsonModel.put("barcode", " ");
					jsonModel.put("goodsName"," ");
					jsonModel.put("purchasePrice", " ");
					jsonModel.put("supplier", " ");
					jsonModel.put("purchaseCode", " ");
					jsonModel.put("poNo", " ");
					jsonArray0.add(jsonModel);
					data.put("settlementPriceList", jsonArray0); // 新品维护预警
				}
				if(data.get("priceWaveRangeList")==null){
					// 单价波动预警
					JSONArray jsonArray1 = new JSONArray();
					JSONObject jsonModel2 = new JSONObject();
					jsonModel2.put("merchantName", " ");
					jsonModel2.put("buName", " ");
					jsonModel2.put("barcode", " ");
					jsonModel2.put("goodsName", " ");
					jsonModel2.put("purchasePrice", " ");
					jsonModel2.put("afterPrice", " ");
					jsonModel2.put("settlementPrice", " ");
					jsonModel2.put("floatRate", " ");
					jsonModel2.put("poNo", " ");
					jsonModel2.put("supplier", " ");
					jsonModel2.put("purchaseCode", " ");			
					jsonArray1.add(jsonModel2);
					data.put("priceWaveRangeList", jsonArray1); // 单价波动预警
				}
				paramJson.put("dataMap", data);
			}
			sheetJSONObject0.put("dataList", dataListJsonArray0);
			sheetJSONObject1.put("dataList", dataListJsonArray1);
			sheetJsonArray.add(sheetJSONObject0);
			sheetJsonArray.add(sheetJSONObject1);
			attachmentObejct.put("sheetJson", sheetJsonArray);
			if (countA > 0 || countB > 0) {
				
				paramJson.put("receiverName", receiverNameList); // 用户
				paramJson.put("countA", countA);// 新品维护预警个数
				paramJson.put("countB", countB); // 单价波动预警个数
				paramJson.put("emailTitle", emailTitle); // 邮件主题
				
				paramJson.put("startTimeStr", startTimeStr); // 查询的入库单创建开始时间
				paramJson.put("endTimeStr", endTimeStr); // 查询的入库单创建结束时间

				jsonObject.put("recipients", receiverEmail);
				jsonObject.put("paramJson", paramJson);
				attachmentArray.add(attachmentObejct);
				jsonObject.put("attachmentJson", attachmentArray);
				System.out.println("------jsonObject:" + jsonObject);
				System.out.println("------paramJson:" + paramJson);
				System.out.println("------attachmentArray:" + attachmentArray);
				// 调用外部接口发送邮件(带附件)
				String resultMsg = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_EMAIL_ATTACHMENT);
				System.out.println(resultMsg);
			}
		}
		LOGGER.info("-----------------标准成本单价波动预警-发送邮件结束----------------------");
		return true;
	}

}
