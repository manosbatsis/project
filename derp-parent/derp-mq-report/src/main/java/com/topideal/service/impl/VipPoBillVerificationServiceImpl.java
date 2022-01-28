package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.BillOutinDepotItemDao;
import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.dao.order.MerchandiseContrastDao;
import com.topideal.dao.order.MerchandiseContrastItemDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.order.SaleReturnOrderItemDao;
import com.topideal.dao.order.SaleShelfDao;
import com.topideal.dao.order.TransferInDepotItemDao;
import com.topideal.dao.order.TransferOutDepotItemDao;
import com.topideal.dao.reporting.VipAdjustmentInventoryDetailsDao;
import com.topideal.dao.reporting.VipOutDepotDetailsDao;
import com.topideal.dao.reporting.VipPoBillTempDao;
import com.topideal.dao.reporting.VipPoBillVerificationDao;
import com.topideal.dao.reporting.VipSaleReturnOdepotDetailsDao;
import com.topideal.dao.reporting.VipShelfDetailsDao;
import com.topideal.dao.reporting.VipTakesStockResultsDetailsDao;
import com.topideal.dao.reporting.VipTransferDepotDetailsDao;
import com.topideal.dao.storage.AdjustmentInventoryItemDao;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.dao.system.CommbarcodeDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.order.BillOutinDepotItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastModel;
import com.topideal.entity.vo.order.SaleOrderItemModel;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.entity.vo.order.SaleShelfModel;
import com.topideal.entity.vo.reporting.VipAdjustmentInventoryDetailsModel;
import com.topideal.entity.vo.reporting.VipOutDepotDetailsModel;
import com.topideal.entity.vo.reporting.VipPoBillTempModel;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;
import com.topideal.entity.vo.reporting.VipSaleReturnOdepotDetailsModel;
import com.topideal.entity.vo.reporting.VipShelfDetailsModel;
import com.topideal.entity.vo.reporting.VipTakesStockResultsDetailsModel;
import com.topideal.entity.vo.reporting.VipTransferDepotDetailsModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mq.tools.ConncetionUtil;
import com.topideal.service.VipPoBillVerificationService;

import net.sf.json.JSONObject;

@Service
public class VipPoBillVerificationServiceImpl implements VipPoBillVerificationService{
	
	/* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(VipPoBillVerificationServiceImpl.class);
	//爬虫账单DAO
	@Autowired
	private CrawlerBillDao crawlerBillDao ;
	//爬虫商品对照DAO
	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao ;
	//仓库DAO
	@Autowired
	private DepotInfoDao depotInfoDao ;
	//唯品核销表PO DAO
	@Autowired
	private VipPoBillVerificationDao vipPoBillVerificationDao ;
	//销售上架表DAO
	@Autowired
	private SaleShelfDao saleShelfDao ;
	//商品信息DAO
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	//销售出库DAO
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao ;
	//库存调整明细DAO
	@Autowired
	private AdjustmentInventoryItemDao adjustmentInventoryItemDao ;
	//销售退明细
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	//唯品PO商品上架明细DAO
	@Autowired
	private VipShelfDetailsDao vipShelfDetailsDao; 
	//唯品PO出库明细DAO
	@Autowired
	private VipOutDepotDetailsDao vipOutDepotDetailsDao ;
	//唯品PO销售退明细DAO
	@Autowired
	private VipSaleReturnOdepotDetailsDao vipSaleReturnOdepotDetailsDao; 
	//唯品PO库存调整明细DAO
	@Autowired
	private VipAdjustmentInventoryDetailsDao  vipAdjustmentInventoryDetailsDao; 
	//唯品PO盘点明细Dao
	@Autowired
	private VipTakesStockResultsDetailsDao vipTakesStockResultsDetailsDao;
	//盘点结果Dao
	@Autowired
	private TakesStockResultsDao takesStockResultsDao ;
	//调拨入库Dao
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao ;
	//调拨出库Dao
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao ;
	//唯品调拨明细
	@Autowired
	private VipTransferDepotDetailsDao vipTransferDepotDetailsDao ;
	//账单出库单明细
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao ;
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	@Autowired
	private BrandParentMongoDao brandParentMongoDao ;
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao ;
	@Autowired
	private SaleOrderDao saleOrderDao ;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao ;
	@Autowired
	private VipPoBillTempDao vipPoBillTempDao ;

	// 统计上架量仓库 唯品会备查库、妮素备查库、融信备查库
	private static final String[] DEPOT_CODES = {"VIP001", "CNS028", "CRX030"} ;
	// 统计退货量仓库 唯品会暂存区、妮素备查库、融信备查库
	private static final String[] DEPOT_TH_CODES = {"WPTH001", "CNS028", "CRX030"} ;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201501080, model = DERP_LOG_POINT.POINT_13201501080_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		Long merchantId = null ;
		String poNo = null ;
		String depotCode = null ;
		
		if(jsonData.get("poNo") != null) {
			poNo = jsonData.getString("poNo");
		}
		
		if(jsonData.get("merchantId") != null) {
			merchantId = jsonData.getLong("merchantId");
		}
		
		if(jsonData.get("depotCode") != null) {
			depotCode = jsonData.getString("depotCode");
		}
		
		for (int i = 0 ; i < DEPOT_CODES.length; i ++) {
			//判断是否选择仓库
			if(StringUtils.isNotBlank(depotCode)
					&& !depotCode.equals(DEPOT_CODES[i])) {
				continue ;
			}
			
			//查询仓库信息
			DepotInfoModel depotInfoModel = new DepotInfoModel() ;
			depotInfoModel.setDepotCode(DEPOT_CODES[i]);
			depotInfoModel = depotInfoDao.searchByModel(depotInfoModel) ;
			
			/**
			 * 1.1保存po上架总量
			 */
			saveShelfAccount(merchantId , poNo , depotInfoModel);
			
			/**
			 * 2.1查询账单状态未完结的所有po、标准条码、sku。
			 */
			List<VipPoBillVerificationModel> unVeriPoBills = getUnVeriPoBill(merchantId, poNo, depotInfoModel.getId()) ;
			
			/**
			 * 2.3循环账单状态未完结的po、标准条码、sku,统计汇总数量并更新,同时生成明细
			 */
			//获取唯品退货仓ID
			DepotInfoModel vipTHDepotModel = new DepotInfoModel() ;
			vipTHDepotModel.setDepotCode(DEPOT_TH_CODES[i]);
			vipTHDepotModel = depotInfoDao.searchByModel(vipTHDepotModel) ;
			
			if("VIP001".equals(DEPOT_CODES[i])) {
				// 若仓库为唯品仓，源用旧逻辑，保持稳定
				sumAccountAndSaveDetails(unVeriPoBills, vipTHDepotModel) ; 
			}else {
				// 妮素，融信仓，启用新逻辑
				sumNSRXAccountAndSaveDetails(unVeriPoBills, vipTHDepotModel) ;
			}
		}
		
	}
	
	/**
	 * 循环账单状态未完结的po、标准条码、sku,统计汇总数量并更新,同时生成明细
	 * @param unVeriPoBills
	 * @throws SQLException 
	 */
	private void sumNSRXAccountAndSaveDetails(List<VipPoBillVerificationModel> unVeriPoBills, DepotInfoModel vipTHDepotModel) throws SQLException {
		List<VipShelfDetailsModel> insertShelfList = new ArrayList<VipShelfDetailsModel>() ;
		List<VipOutDepotDetailsModel> insertOutDepotList = new ArrayList<VipOutDepotDetailsModel>() ;
		List<VipSaleReturnOdepotDetailsModel> insertSaleReturnOdepotList = new ArrayList<VipSaleReturnOdepotDetailsModel>() ;
		List<VipAdjustmentInventoryDetailsModel> insertAdjustmentInventoryList = new ArrayList<VipAdjustmentInventoryDetailsModel>() ;
		List<VipTakesStockResultsDetailsModel> insertTakesStockResultsList = new ArrayList<VipTakesStockResultsDetailsModel>() ;
		List<VipTransferDepotDetailsModel>  insertTransferDepotList = new ArrayList<VipTransferDepotDetailsModel>() ;
		
		for (VipPoBillVerificationModel vipPoBillVerificationModel : unVeriPoBills) {
			
			//根据po、标准条码、sku,获取销售出库数量
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
			queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
			queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
			queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
			//统计账单出库单---销售出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_XSC) ;
			List<BillOutinDepotItemModel> xscList = billOutinDepotItemDao.getVipPoAccountByType(queryMap);
			
			Integer saleOutAccount = xscList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ;
			
			//销售退数量
			Integer saleReturnIdepotTotal = saleReturnOrderItemDao.getSaleReturnAccount(queryMap) ;
			if(saleReturnIdepotTotal == null) {
				saleReturnIdepotTotal = 0 ;
			}
			
			//统计账单出库单---报废量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_BFC) ;
			List<BillOutinDepotItemModel> bfcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			Integer scrapAccount = bfcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//统计账单出库单---国检出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_GJC) ;
			List<BillOutinDepotItemModel> gjcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			Integer nationAccount = gjcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//唯品红冲
			//统计账单出库单---客退入库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_KTR) ;
			List<BillOutinDepotItemModel> ktrList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			Integer vipHongChongAccount = ktrList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//盘盈数量
			//统计账单出库单---盘盈入库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PYR) ;
			List<BillOutinDepotItemModel> pyrList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			Integer surplusAccount = pyrList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//盘亏数量
			//统计账单出库单---盘亏出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PKC) ;
			List<BillOutinDepotItemModel> pkcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			Integer deficientAccount = pkcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//调拨入库量
			queryMap.put("inDepotId", vipPoBillVerificationModel.getDepotId()) ;
			queryMap.put("outDepotId", vipTHDepotModel.getId()) ;
			Integer transferInAccount = transferInDepotItemDao.getVIPInDepotAccount(queryMap) ;
			if(transferInAccount == null) {
				transferInAccount = 0 ;
			}
			//调拨出库量
			queryMap.put("inDepotId", vipTHDepotModel.getId()) ;
			queryMap.put("outDepotId", vipPoBillVerificationModel.getDepotId()) ;
			Integer transferOutAccount = transferOutDepotItemDao.getVIPOutDepotAccount(queryMap) ;
			if(transferOutAccount == null) {
				transferOutAccount = 0 ;
			}
			
			/**
			 * 未结算量:上架总量－账单总量－国检抽样－销售退数量 + 盘盈数量 - 盘亏数量 - 报废数量 -调拨出量+调拨入量
			 */
			//上架量
			Integer shelfTotalAccount = vipPoBillVerificationModel.getShelfTotalAccount() ;
			if(shelfTotalAccount == null) {
				shelfTotalAccount = 0 ;
			}
			
			Integer billTotalAccount = saleOutAccount - vipHongChongAccount ;
			vipPoBillVerificationModel.setBillTotalAccount(billTotalAccount);
			
			/**获取临时结转量*/
			VipPoBillTempModel tempModel = new VipPoBillTempModel() ;
			tempModel.setPoNo(vipPoBillVerificationModel.getPo());
			tempModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			tempModel.setDepotId(vipPoBillVerificationModel.getDepotId());
			tempModel.setMerchantId(vipPoBillVerificationModel.getMerchantId());
			
			tempModel = vipPoBillTempDao.searchByModel(tempModel) ;
			
			if(tempModel != null) {
				shelfTotalAccount = shelfTotalAccount - tempModel.getOutDepotAccount() + tempModel.getInDepotAccount() ;
			}
			
			
			/**
			 * 未结算量=上架总量－账单总量－国检抽样－销售退数量+盘盈数量-盘亏数量-报废数量-调拨出库+调拨入库
			 */
			Integer unsettledAccount = shelfTotalAccount - billTotalAccount - saleReturnIdepotTotal - nationAccount + surplusAccount - deficientAccount - scrapAccount - transferOutAccount + transferInAccount;
					
			/**
			 * 未核销量：上架总量－销售出库总量－国检抽样－销售退数量 + 唯品红冲 + 盘盈数量 - 盘亏数量 - 报废数量
			 */
			Integer unverificateAccount = shelfTotalAccount - saleOutAccount - nationAccount - saleReturnIdepotTotal + vipHongChongAccount + surplusAccount - deficientAccount - scrapAccount;
			
			/**
			 * 未出库量：账单总量 - 销售出库总量 +  唯品红冲
			 */
			Integer undepotAccount = billTotalAccount - saleOutAccount + vipHongChongAccount ;
			
			/**
			 * 天数：最近账单时间 - 首次上架时间 ， 没有最近账单时间，不做计算
			 */
			Integer days = null ;
			if(vipPoBillVerificationModel.getBillRecentDate() != null) {
				days = TimeUtils.daysBetween(vipPoBillVerificationModel.getFirstShelfDate() , vipPoBillVerificationModel.getBillRecentDate()) ;
			}
			
			/**
			 * 母品牌、标准品牌
			 */
			CommbarcodeModel commbarcode = new CommbarcodeModel() ;
			commbarcode.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			
			commbarcode = commbarcodeDao.searchByModel(commbarcode) ;
			
			if(commbarcode != null) {
				vipPoBillVerificationModel.setBrandParent(commbarcode.getCommBrandParentName());
				vipPoBillVerificationModel.setGoodsName(commbarcode.getCommGoodsName());
				
				Map<String, Object> brandParendMap = new HashMap<>() ;
				brandParendMap.put("brandParentId", commbarcode.getCommBrandParentId()) ;
				
				BrandParentMongo brandParentMongo = brandParentMongoDao.findOne(brandParendMap);
				if(brandParentMongo != null) {
					vipPoBillVerificationModel.setSuperiorParentBrand(brandParentMongo.getSuperiorParentBrand());
				}
				
			}
			
			/**根据PO号，标准条码查询上架表，随机取销售单，获取对应销售单价，统计销售总量*/
			SaleShelfModel queryModel = new SaleShelfModel() ;
			queryModel.setPoNo(vipPoBillVerificationModel.getPo());
			queryModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			
			List<SaleShelfModel> saleList = saleShelfDao.list(queryModel);
			
			if(!saleList.isEmpty()) {
				SaleOrderModel saleOrder = null ;
				
				for (SaleShelfModel saleShelfModel : saleList) {
					SaleOrderModel tempOrder = saleOrderDao.searchById(saleShelfModel.getOrderId());
					
					if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(tempOrder.getBusinessModel())) {
						saleOrder = tempOrder ;
						break ;
					}
					
				}
				
				if(saleOrder != null) {
					vipPoBillVerificationModel.setBuId(saleOrder.getBuId());
					vipPoBillVerificationModel.setBuName(saleOrder.getBuName());
					vipPoBillVerificationModel.setCurrency(saleOrder.getCurrency());
					vipPoBillVerificationModel.setCustomerId(saleOrder.getCustomerId());
					vipPoBillVerificationModel.setCustomerName(saleOrder.getCustomerName());
					
					SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;
					saleOrderItemModel.setOrderId(saleOrder.getId());
					saleOrderItemModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
					
					List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(saleOrderItemModel);
					
					if(!saleItemList.isEmpty()) {
						
						saleOrderItemModel = saleItemList.get(0) ;
						
						BigDecimal inventoryAmount = saleOrderItemModel.getPrice().multiply(new BigDecimal(unsettledAccount));
						
						vipPoBillVerificationModel.setSalePrice(saleOrderItemModel.getPrice());
						vipPoBillVerificationModel.setInventoryAmount(inventoryAmount);
					}
				}
				
			}
			
			vipPoBillVerificationModel.setSaleReturnAccount(saleReturnIdepotTotal);
			vipPoBillVerificationModel.setNationalInspectionSampleAccount(nationAccount);
			vipPoBillVerificationModel.setOutDepotTotalAccont(saleOutAccount);
			vipPoBillVerificationModel.setVipHcAccount(vipHongChongAccount);
			vipPoBillVerificationModel.setScrapAccount(scrapAccount);
			vipPoBillVerificationModel.setTransferInAccount(transferInAccount);
			vipPoBillVerificationModel.setTransferOutAccount(transferOutAccount);
			vipPoBillVerificationModel.setInventorySurplusAccount(surplusAccount);
			vipPoBillVerificationModel.setInventoryDeficientAccount(deficientAccount);
			vipPoBillVerificationModel.setUnsettledAccount(unsettledAccount);
			vipPoBillVerificationModel.setUnverificateAccount(unverificateAccount);
			vipPoBillVerificationModel.setUndepotAccount(undepotAccount);
			vipPoBillVerificationModel.setDays(days);
			vipPoBillVerificationModel.setModifyDate(TimeUtils.getNow());
			
			vipPoBillVerificationDao.modify(vipPoBillVerificationModel) ;
			
			/**
			 * 2.3.4生成明细存储到缓存列表，账单明细量较大，独立生成明细
			 */
			
			//商品上架明细
			saveShelfDetails(vipPoBillVerificationModel, insertShelfList) ; 
			
			//出库明细
			saveOutDepotDetails(vipPoBillVerificationModel, insertOutDepotList) ; 
			
			//销售退明细
			saveReturnOdepotDetails(vipPoBillVerificationModel, insertSaleReturnOdepotList) ; 
			
			//国检抽样,唯品红冲,唯品报废明细
			saveAjustInvenDetails(vipPoBillVerificationModel, insertAdjustmentInventoryList) ; 
			
			//盘盈、盘亏明细
			saveTakesStockResultDetails(vipPoBillVerificationModel, insertTakesStockResultsList) ;
			
			//调拨明细
			saveTransferDetails(vipPoBillVerificationModel, vipTHDepotModel, insertTransferDepotList) ;
		}
	}

	/**
	 * 循环账单状态未完结的po、标准条码、sku,统计汇总数量并更新,同时生成明细
	 * @param unVeriPoBills
	 * @throws SQLException 
	 */
	private void sumAccountAndSaveDetails(List<VipPoBillVerificationModel> unVeriPoBills, DepotInfoModel vipTHDepotModel) throws SQLException {
		
		List<VipShelfDetailsModel> insertShelfList = new ArrayList<VipShelfDetailsModel>() ;
		List<VipOutDepotDetailsModel> insertOutDepotList = new ArrayList<VipOutDepotDetailsModel>() ;
		List<VipSaleReturnOdepotDetailsModel> insertSaleReturnOdepotList = new ArrayList<VipSaleReturnOdepotDetailsModel>() ;
		List<VipAdjustmentInventoryDetailsModel> insertAdjustmentInventoryList = new ArrayList<VipAdjustmentInventoryDetailsModel>() ;
		List<VipTakesStockResultsDetailsModel> insertTakesStockResultsList = new ArrayList<VipTakesStockResultsDetailsModel>() ;
		List<VipTransferDepotDetailsModel>  insertTransferDepotList = new ArrayList<VipTransferDepotDetailsModel>() ;
		
		for (VipPoBillVerificationModel vipPoBillVerificationModel : unVeriPoBills) {
			
			/**
			 * 2.3.1根据标准条码去商品对照表找sku（存在单个/多个）
			 */
			
			MerchandiseInfoModel queryMerchandiseInfoModel = new MerchandiseInfoModel() ;
			queryMerchandiseInfoModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			queryMerchandiseInfoModel.setMerchantId(vipPoBillVerificationModel.getMerchantId());
			
			List<MerchandiseInfoModel> goodsList = merchandiseInfoDao.list(queryMerchandiseInfoModel);
			List<String> goodsNos = goodsList.stream().map(MerchandiseInfoModel::getGoodsNo).collect(Collectors.toList()) ;
			List<String> skuList = new ArrayList<>() ;
			
			if(!goodsNos.isEmpty()) {
				
				Map<String, Object> queryMerchandiseContrastMap = new HashMap<>() ;
				queryMerchandiseContrastMap.put("goodsNos", goodsNos) ;
				queryMerchandiseContrastMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
				queryMerchandiseContrastMap.put("status", DERP_ORDER.MERCHANDISECONTRAST_STATUS_0) ;
				queryMerchandiseContrastMap.put("type", DERP_ORDER.MERCHANDISECONTRAST_TYPE_2) ;
				
				List<MerchandiseContrastModel> merchandiseContrastList = merchandiseContrastDao.getSkusByGoodsNo(queryMerchandiseContrastMap) ;
				
				skuList = merchandiseContrastList.stream()
						.map(MerchandiseContrastModel::getCrawlerGoodsNo).collect(Collectors.toList());
				
			}
			
			/**
			 * 2.3.2根据po、sku（单个/多个）去爬虫账单表账单总量
			 */
			//账单总量
			Integer sumTotalSalesQty = 0;
			//最近账单时间
			Timestamp billRecentDate = null ;
			//po时间
			Timestamp poDate = null ;
			
			//账单结果集
			List<Map<String , Object>> crawlerResultMapList = null ;
			//爬虫货号商品-爬虫量集合
			Map<String, Integer> crawlerGoodsNoQtyMap = new HashMap<>() ;
			
			if(!skuList.isEmpty()) {
				Map<String , Object> crawlerQueryMap = new HashMap<String , Object>() ;
				crawlerQueryMap.put("poNo", vipPoBillVerificationModel.getPo()) ; 
				crawlerQueryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
				crawlerQueryMap.put("skus", skuList) ;
				
				crawlerResultMapList= crawlerBillDao.getBillByPoAndSku(crawlerQueryMap) ; 
			}
			
			if(crawlerResultMapList!=null && !crawlerResultMapList.isEmpty()) {
				
				Map<String , Object> crawlerResultMap = crawlerResultMapList.get(0) ;
				
				for (Map<String , Object> map : crawlerResultMapList) {
					BigDecimal totalSalesQty = judgeIsNullOrNotReturnObj(map.get("totalSalesQty"), BigDecimal.class);
					Integer totalSalesQtyInt = totalSalesQty == null? 0 : totalSalesQty.intValue() ;
					sumTotalSalesQty += totalSalesQtyInt ;
					
					String crawlerGoodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no"), String.class);
					
					MerchandiseContrastModel queryModel = new MerchandiseContrastModel() ;
					queryModel.setCrawlerGoodsNo(crawlerGoodsNo);
					queryModel.setMerchantId(vipPoBillVerificationModel.getMerchantId());
					queryModel.setStatus(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0);
					queryModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);
					
					queryModel = merchandiseContrastDao.searchByModel(queryModel) ;
					
					MerchandiseContrastItemModel queryItemModel = new MerchandiseContrastItemModel() ;
					queryItemModel.setContrastId(queryModel.getId());
					List<MerchandiseContrastItemModel> itemList = merchandiseContrastItemDao.list(queryItemModel);
					
					String tempGoodsNos = itemList.stream().map(MerchandiseContrastItemModel::getGoodsNo).collect(Collectors.joining(","));
					
					crawlerGoodsNoQtyMap.put(tempGoodsNos, totalSalesQtyInt) ;
				}
				
				billRecentDate = judgeIsNullOrNotReturnObj(crawlerResultMap.get("billRecentDate"), Timestamp.class) ; 
				
				poDate = judgeIsNullOrNotReturnObj(crawlerResultMap.get("poTime"), Timestamp.class) ; 
			
				vipPoBillVerificationModel.setBillRecentDate(billRecentDate);
				vipPoBillVerificationModel.setPoDate(poDate);
			}
			
			/**
			 * 2.3.3更新数量
			 */
			//根据po、标准条码、sku,获取销售出库数量
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
			queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
			queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
			queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
			
			//销售出库量
			Integer saleOutAccount = saleOutDepotItemDao.getVipSalesOutNum(queryMap) ;
			if(saleOutAccount == null) {
				saleOutAccount = 0 ;
			}
			//统计账单出库单---销售出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_XSC) ;
			List<BillOutinDepotItemModel> xscList = billOutinDepotItemDao.getVipPoAccountByType(queryMap);
			
			saleOutAccount += xscList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ;
			
			//销售退数量
			Integer saleReturnIdepotTotal = saleReturnOrderItemDao.getSaleReturnAccount(queryMap) ;
			if(saleReturnIdepotTotal == null) {
				saleReturnIdepotTotal = 0 ;
			}
			
			//唯品报废
			queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6) ;
			Integer scrapAccount = adjustmentInventoryItemDao.getInventoryNumByModel(queryMap) ;
			if(scrapAccount == null) {
				scrapAccount = 0;
			}
			//统计账单出库单---报废量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_BFC) ;
			List<BillOutinDepotItemModel> bfcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			scrapAccount += bfcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//国检抽样
			queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5) ;
			Integer nationAccount = adjustmentInventoryItemDao.getInventoryNumByModel(queryMap) ;
			if(nationAccount == null) {
				nationAccount = 0;
			}
			//统计账单出库单---国检出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_GJC) ;
			List<BillOutinDepotItemModel> gjcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			nationAccount += gjcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//唯品红冲
			queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4) ;
			Integer vipHongChongAccount = adjustmentInventoryItemDao.getInventoryNumByModel(queryMap) ;
			if(vipHongChongAccount == null) {
				vipHongChongAccount = 0 ;
			}
			//统计账单出库单---客退入库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_KTR) ;
			List<BillOutinDepotItemModel> ktrList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			vipHongChongAccount += ktrList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//盘盈数量
			queryMap.put("type", DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1) ; //盘盈
			Integer surplusAccount = takesStockResultsDao.getStockAccount(queryMap) ;
			if(surplusAccount == null) {
				surplusAccount = 0 ;
			}
			//统计账单出库单---盘盈入库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PYR) ;
			List<BillOutinDepotItemModel> pyrList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			surplusAccount += pyrList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//盘亏数量
			queryMap.put("type", DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2) ; //盘亏
			Integer deficientAccount = takesStockResultsDao.getStockAccount(queryMap) ;
			if(deficientAccount == null) {
				deficientAccount = 0 ;
			}
			
			//统计账单出库单---盘亏出库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PKC) ;
			List<BillOutinDepotItemModel> pkcList = billOutinDepotItemDao.getVipPoAccountByType(queryMap) ;
			
			deficientAccount += pkcList.stream().mapToInt(BillOutinDepotItemModel::getNum).sum() ; 
			
			//调拨入库量
			queryMap.put("inDepotId", vipPoBillVerificationModel.getDepotId()) ;
			queryMap.put("outDepotId", vipTHDepotModel.getId()) ;
			Integer transferInAccount = transferInDepotItemDao.getVIPInDepotAccount(queryMap) ;
			if(transferInAccount == null) {
				transferInAccount = 0 ;
			}
			
			//调拨出库量
			queryMap.put("inDepotId", vipTHDepotModel.getId()) ;
			queryMap.put("outDepotId", vipPoBillVerificationModel.getDepotId()) ;
			Integer transferOutAccount = transferOutDepotItemDao.getVIPOutDepotAccount(queryMap) ;
			if(transferOutAccount == null) {
				transferOutAccount = 0 ;
			}
			
			//账单总量=销售出库总量 - 红冲量
			sumTotalSalesQty = saleOutAccount - vipHongChongAccount ;
			
			vipPoBillVerificationModel.setBillTotalAccount(sumTotalSalesQty);
			
			/**
			 * 未结算量:上架总量－账单总量－国检抽样－销售退数量 + 盘盈数量 - 盘亏数量 - 报废数量 -调拨出量+调拨入量
			 */
			//上架量
			Integer shelfTotalAccount = vipPoBillVerificationModel.getShelfTotalAccount() ;
			if(shelfTotalAccount == null) {
				shelfTotalAccount = 0 ;
			}
			
			Integer billTotalAccount = vipPoBillVerificationModel.getBillTotalAccount() ;
			if(billTotalAccount == null) {
				billTotalAccount = 0 ;
			}
			
			/**账单总量组合品处理*/
			
			/**
			 * 未结算量=上架总量－账单总量－国检抽样－销售退数量+盘盈数量-盘亏数量-报废数量-调拨出库+调拨入库
			 */
			Integer unsettledAccount = shelfTotalAccount - billTotalAccount - saleReturnIdepotTotal - nationAccount + surplusAccount - deficientAccount - scrapAccount - transferOutAccount + transferInAccount;
					
			/**
			 * 未核销量：上架总量－销售出库总量－国检抽样－销售退数量 + 唯品红冲 + 盘盈数量 - 盘亏数量 - 报废数量
			 */
			Integer unverificateAccount = shelfTotalAccount - saleOutAccount - nationAccount - saleReturnIdepotTotal + vipHongChongAccount + surplusAccount - deficientAccount - scrapAccount;
			
			/**
			 * 未出库量：账单总量 - 销售出库总量 +  唯品红冲
			 */
			Integer undepotAccount = billTotalAccount - saleOutAccount + vipHongChongAccount ;
			
			/**
			 * 天数：最近账单时间 - 首次上架时间 ， 没有最近账单时间，不做计算
			 */
			Integer days = null ;
			if(vipPoBillVerificationModel.getBillRecentDate() != null) {
				days = TimeUtils.daysBetween(vipPoBillVerificationModel.getFirstShelfDate() , vipPoBillVerificationModel.getBillRecentDate()) ;
			}
			
			/**
			 * 母品牌、标准品牌
			 */
			CommbarcodeModel commbarcode = new CommbarcodeModel() ;
			commbarcode.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			
			commbarcode = commbarcodeDao.searchByModel(commbarcode) ;
			
			if(commbarcode != null) {
				vipPoBillVerificationModel.setBrandParent(commbarcode.getCommBrandParentName());
				vipPoBillVerificationModel.setGoodsName(commbarcode.getCommGoodsName());
				
				Map<String, Object> brandParendMap = new HashMap<>() ;
				brandParendMap.put("brandParentId", commbarcode.getCommBrandParentId()) ;
				
				BrandParentMongo brandParentMongo = brandParentMongoDao.findOne(brandParendMap);
				if(brandParentMongo != null) {
					vipPoBillVerificationModel.setSuperiorParentBrand(brandParentMongo.getSuperiorParentBrand());
				}
				
			}
			
			/**根据PO号，标准条码查询上架表，随机取购销-实销实结销售单，获取对应销售单价，统计销售总量*/
			vipPoBillVerificationModel.setSalePrice(new BigDecimal(0));
			vipPoBillVerificationModel.setInventoryAmount(new BigDecimal(0));
			
			SaleShelfModel queryModel = new SaleShelfModel() ;
			queryModel.setPoNo(vipPoBillVerificationModel.getPo());
			queryModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			
			List<SaleShelfModel> saleList = saleShelfDao.list(queryModel);
			
			if(!saleList.isEmpty()) {
				
				SaleOrderModel saleOrder = null ;
				
				for (SaleShelfModel saleShelfModel : saleList) {
					SaleOrderModel tempOrder = saleOrderDao.searchById(saleShelfModel.getOrderId());
					
					if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(tempOrder.getBusinessModel())) {
						saleOrder = tempOrder ;
						break ;
					}
					
				}
				
				if(saleOrder != null) {
					vipPoBillVerificationModel.setBuId(saleOrder.getBuId());
					vipPoBillVerificationModel.setBuName(saleOrder.getBuName());
					vipPoBillVerificationModel.setCurrency(saleOrder.getCurrency());
					vipPoBillVerificationModel.setCustomerId(saleOrder.getCustomerId());
					vipPoBillVerificationModel.setCustomerName(saleOrder.getCustomerName());
					
					SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;
					saleOrderItemModel.setOrderId(saleOrder.getId());
					saleOrderItemModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
					
					List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(saleOrderItemModel);
					
					if(!saleItemList.isEmpty()) {
						
						saleOrderItemModel = saleItemList.get(0) ;
						
						BigDecimal inventoryAmount = saleOrderItemModel.getPrice().multiply(new BigDecimal(unsettledAccount));
						
						vipPoBillVerificationModel.setSalePrice(saleOrderItemModel.getPrice());
						vipPoBillVerificationModel.setInventoryAmount(inventoryAmount);
					}
				}
				
			}
			
			vipPoBillVerificationModel.setSaleReturnAccount(saleReturnIdepotTotal);
			vipPoBillVerificationModel.setNationalInspectionSampleAccount(nationAccount);
			vipPoBillVerificationModel.setOutDepotTotalAccont(saleOutAccount);
			vipPoBillVerificationModel.setVipHcAccount(vipHongChongAccount);
			vipPoBillVerificationModel.setScrapAccount(scrapAccount);
			vipPoBillVerificationModel.setTransferInAccount(transferInAccount);
			vipPoBillVerificationModel.setTransferOutAccount(transferOutAccount);
			vipPoBillVerificationModel.setInventorySurplusAccount(surplusAccount);
			vipPoBillVerificationModel.setInventoryDeficientAccount(deficientAccount);
			vipPoBillVerificationModel.setUnsettledAccount(unsettledAccount);
			vipPoBillVerificationModel.setUnverificateAccount(unverificateAccount);
			vipPoBillVerificationModel.setUndepotAccount(undepotAccount);
			vipPoBillVerificationModel.setDays(days);
			vipPoBillVerificationModel.setModifyDate(TimeUtils.getNow());
			
			vipPoBillVerificationDao.modify(vipPoBillVerificationModel) ;
			
			/**
			 * 2.3.4生成明细存储到缓存列表，账单明细量较大，独立生成明细
			 */
			//商品上架明细
			saveShelfDetails(vipPoBillVerificationModel, insertShelfList) ; 
			
			//出库明细
			saveOutDepotDetails(vipPoBillVerificationModel, insertOutDepotList) ; 
			
			//销售退明细
			saveReturnOdepotDetails(vipPoBillVerificationModel, insertSaleReturnOdepotList) ; 
			
			//国检抽样,唯品红冲,唯品报废明细
			saveAjustInvenDetails(vipPoBillVerificationModel, insertAdjustmentInventoryList) ; 
			
			//盘盈、盘亏明细
			saveTakesStockResultDetails(vipPoBillVerificationModel, insertTakesStockResultsList) ;
			
			//调拨明细
			saveTransferDetails(vipPoBillVerificationModel, vipTHDepotModel, insertTransferDepotList) ;
		}
		
		//批量插入
		if(!insertShelfList.isEmpty()) {
			vipShelfDetailsDao.batchSave(insertShelfList) ;
		}
		
		if(!insertSaleReturnOdepotList.isEmpty()) {
			vipSaleReturnOdepotDetailsDao.batchSave(insertSaleReturnOdepotList) ;
		}
		
		if(!insertOutDepotList.isEmpty()) {
			
			int total = insertOutDepotList.size();
			int limit = 5000 ;
			int num = total / limit ;
			
			
			if(total % limit != 0) {
				num += 1;
			}
			
			int start = 0 ;
			for(int i = 0 ; i < num ; i++) {
				start = limit * i ;
				int end = start + limit ;
				
				if(end > total) {
					end = total ;
				}
				
				List<VipOutDepotDetailsModel> subList = insertOutDepotList.subList(start, end);
				vipOutDepotDetailsDao.batchSave(subList) ;
			}
			
		}
		
		if(!insertAdjustmentInventoryList.isEmpty()) {
			vipAdjustmentInventoryDetailsDao.batchSave(insertAdjustmentInventoryList) ;
		}
		
		if(!insertTakesStockResultsList.isEmpty()) {
			vipTakesStockResultsDetailsDao.batchSave(insertTakesStockResultsList) ;
		}
		
		if(!insertTransferDepotList.isEmpty()) {
			vipTransferDepotDetailsDao.batchSave(insertTransferDepotList) ;
		}
	}

	/**
	 * 调拨明细
	 * @param vipPoBillVerificationModel
	 * @param vipTHDepotModel 
	 * @throws SQLException 
	 */
	private void saveTransferDetails(VipPoBillVerificationModel vipPoBillVerificationModel, DepotInfoModel vipTHDepotModel, List<VipTransferDepotDetailsModel>  insertList) throws SQLException {
		/**
		 * 2.3.2.7.1先清空本商家、仓库、po、标准条码的调拨明细
		 */
		VipTransferDepotDetailsModel model = new VipTransferDepotDetailsModel() ;
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		
		vipTransferDepotDetailsDao.deleteByModel(model) ;
		
		/**
		 * 2.3.2.7.2本商家、仓库、po、标准条码调拨出库明细
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("inDepotId", vipTHDepotModel.getId()) ;
		queryMap.put("outDepotId", vipPoBillVerificationModel.getDepotId()) ;
		
		List<Map<String, Object>> transOutList = transferOutDepotItemDao.getVIPOutDepotDetails(queryMap) ;
		
		for (Map<String, Object> outMap : transOutList) {
			VipTransferDepotDetailsModel transferModel = new VipTransferDepotDetailsModel() ;
			
			transferModel.setMerchantId(judgeIsNullOrNotReturnObj(outMap.get("merchant_id") , Long.class )) ;
			transferModel.setMerchantName(judgeIsNullOrNotReturnObj(outMap.get("merchant_name") , String.class )) ;
			transferModel.setCommbarcode(judgeIsNullOrNotReturnObj(outMap.get("commbarcode") , String.class ));
			transferModel.setGoodsName(judgeIsNullOrNotReturnObj(outMap.get("goods_name") , String.class ));
			transferModel.setGoodsNo(judgeIsNullOrNotReturnObj(outMap.get("goods_no") , String.class ));
			transferModel.setGoodsId(judgeIsNullOrNotReturnObj(outMap.get("goods_id") , Long.class ));
			transferModel.setPoNo(judgeIsNullOrNotReturnObj(outMap.get("po_no") , String.class ));
			transferModel.setTransferOrder(judgeIsNullOrNotReturnObj(outMap.get("transfer_order") , String.class ));
			transferModel.setTransferDepotOrder(judgeIsNullOrNotReturnObj(outMap.get("transfer_depot_order") , String.class ));
			transferModel.setTransferNum(judgeIsNullOrNotReturnObj(outMap.get("transfer_num") , Integer.class ));
			transferModel.setTransferTime(judgeIsNullOrNotReturnObj(outMap.get("transfer_time") , Timestamp.class ));
			transferModel.setCreateDate(judgeIsNullOrNotReturnObj(outMap.get("create_date") , Timestamp.class )); 				
			transferModel.setModifyDate(judgeIsNullOrNotReturnObj(outMap.get("modify_date") , Timestamp.class ));
			transferModel.setTransferType(DERP_REPORT.VIPTRDETAILS_TRANSFER_TYPE_2);
			
			insertList.add(transferModel) ;
		}
		
		/**
		 * 2.3.2.7.3本商家、仓库、po、标准条码调拨入库明细
		 */
		queryMap.put("inDepotId", vipPoBillVerificationModel.getDepotId()) ;
		queryMap.put("outDepotId", vipTHDepotModel.getId()) ;
		
		List<Map<String, Object>> transInList = transferInDepotItemDao.getVIPInDepotDetails(queryMap) ;
		
		for (Map<String, Object> inMap : transInList) {
			VipTransferDepotDetailsModel transferModel = new VipTransferDepotDetailsModel() ;
			
			Long transferNum = judgeIsNullOrNotReturnObj(inMap.get("transfer_num") , Long.class );
			
			transferModel.setMerchantId(judgeIsNullOrNotReturnObj(inMap.get("merchant_id") , Long.class )) ;
			transferModel.setMerchantName(judgeIsNullOrNotReturnObj(inMap.get("merchant_name") , String.class )) ;
			transferModel.setCommbarcode(judgeIsNullOrNotReturnObj(inMap.get("commbarcode") , String.class ));
			transferModel.setGoodsName(judgeIsNullOrNotReturnObj(inMap.get("goods_name") , String.class ));
			transferModel.setGoodsNo(judgeIsNullOrNotReturnObj(inMap.get("goods_no") , String.class ));
			transferModel.setGoodsId(judgeIsNullOrNotReturnObj(inMap.get("goods_id") , Long.class ));
			transferModel.setPoNo(judgeIsNullOrNotReturnObj(inMap.get("po_no") , String.class ));
			transferModel.setTransferOrder(judgeIsNullOrNotReturnObj(inMap.get("transfer_order") , String.class ));
			transferModel.setTransferDepotOrder(judgeIsNullOrNotReturnObj(inMap.get("transfer_depot_order") , String.class ));
			transferModel.setTransferNum(transferNum.intValue());
			transferModel.setTransferTime(judgeIsNullOrNotReturnObj(inMap.get("transfer_time") , Timestamp.class ));
			transferModel.setCreateDate(judgeIsNullOrNotReturnObj(inMap.get("create_date") , Timestamp.class )); 				
			transferModel.setModifyDate(judgeIsNullOrNotReturnObj(inMap.get("modify_date") , Timestamp.class ));
			transferModel.setTransferType(DERP_REPORT.VIPTRDETAILS_TRANSFER_TYPE_1);
			
			insertList.add(transferModel) ;
		}
		
	}

	/**
	 * 盘盈、盘亏明细
	 * @param vipPoBillVerificationModel
	 * @throws SQLException 
	 */
	private void saveTakesStockResultDetails(VipPoBillVerificationModel vipPoBillVerificationModel, List<VipTakesStockResultsDetailsModel> insertList) throws SQLException {
		/**
		 * 2.3.2.6.1先清空本商家、仓库、po、标准条码的盘点结果明细
		 */
		VipTakesStockResultsDetailsModel model = new VipTakesStockResultsDetailsModel() ;
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setDepotId(vipPoBillVerificationModel.getDepotId());
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		
		vipTakesStockResultsDetailsDao.deleteByModel(model) ;
		
		Map<String, VipTakesStockResultsDetailsModel> cacheMap = new HashMap<String, VipTakesStockResultsDetailsModel>() ;
		
		/**
		 * 2.3.2.6.2.1本商家、仓库、po、标准条码盘点结果明细
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
		
		List<Map<String , Object>> mapList = takesStockResultsDao.getVipStockResultsDetails(queryMap) ;
		
		for (Map<String, Object> map : mapList) {
			
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class );
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class ); 
			String type = judgeIsNullOrNotReturnObj(map.get("type") , String.class) ;
			String sourceCode = judgeIsNullOrNotReturnObj(map.get("source_code") , String.class) ;
			
			String key = goodsNo + "_" + code + "_" + type + "_" + sourceCode ;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipTakesStockResultsDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;					//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));				//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 						//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));					//仓库名称
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 							//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 				//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             			//商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));					//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 						//商品货号
				model.setReceiveTime(judgeIsNullOrNotReturnObj(map.get("receive_time") , Timestamp.class ));            //接收时间
				model.setTakesStockResultOrder(judgeIsNullOrNotReturnObj(map.get("code") , String.class ));             //盘点结果单号
				model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("source_code") , String.class));					//来源单号
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				
				if("1".equals(type)) {
					Integer surplusNum = judgeIsNullOrNotReturnObj(map.get("surplus_num") , Integer.class) ;
					model.setTakesStockResultNum(surplusNum);
				}else if("2".equals(type)) {
					Integer deficientNum = judgeIsNullOrNotReturnObj(map.get("deficient_num") , Integer.class) ;
					model.setTakesStockResultNum(deficientNum);
				}
				model.setTakesStockResultType(type);
			}else {
				if("1".equals(type) && model.getTakesStockResultNum() != null) {
					Integer surplusNum = judgeIsNullOrNotReturnObj(map.get("surplus_num") , Integer.class) ;
					model.setTakesStockResultNum(surplusNum + model.getTakesStockResultNum() );
				}else if("2".equals(type) && model.getTakesStockResultNum() != null) {
					Integer deficientNum = judgeIsNullOrNotReturnObj(map.get("deficient_num") , Integer.class) ;
					model.setTakesStockResultNum(deficientNum + model.getTakesStockResultNum());
				}
			}
			
			cacheMap.put(key, model) ;
		
		}
		
		/**
		 * 2.3.2.6.2.2本商家、仓库、po、标准条码盘盈入明细
		 */
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PYR) ;
		List<Map<String, Object>> listNewMap = billOutinDepotItemDao.getVipDetails(queryMap);
		
		/**
		 * 2.3.2.6.2.3本商家、仓库、po、标准条码盘亏出明细
		 */
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PKC) ;
		List<Map<String, Object>> listTempMap = billOutinDepotItemDao.getVipDetails(queryMap);
		
		listNewMap.addAll(listTempMap) ;
		
		for (Map<String, Object> map : listNewMap) {
			
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class );
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class ); 
			String type = judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class) ;
			String sourceCode = judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class) ;
			
			Integer num = judgeIsNullOrNotReturnObj(map.get("num") , Integer.class); 
			
			String key = goodsNo + "_" + code + "_" + type + "_" + sourceCode ;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipTakesStockResultsDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;					//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));				//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 						//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));					//仓库名称
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 							//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 				//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             			//商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));					//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 						//商品货号
				model.setReceiveTime(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));            //接收时间
				model.setTakesStockResultOrder(judgeIsNullOrNotReturnObj(map.get("code") , String.class ));             //盘点结果单号
				model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class));					//来源单号
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				model.setTakesStockResultNum(num);
				
				if(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1.equals(type)) {
					model.setTakesStockResultType(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1);
				}else if(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0.equals(type)) {
					model.setTakesStockResultType(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2);
				}
			}else {
				if(num != null && model.getTakesStockResultNum() != null) {
					model.setTakesStockResultNum(num + model.getTakesStockResultNum());
				}
			}
			
			cacheMap.put(key, model) ;
		
		}
		
		insertList.addAll(cacheMap.values()) ;
	}

	/**
	 * 国检抽样,唯品红冲明细
	 * @param vipPoBillVerificationModel
	 * @throws SQLException 
	 */
	private void saveAjustInvenDetails(VipPoBillVerificationModel vipPoBillVerificationModel, List<VipAdjustmentInventoryDetailsModel> insertList) throws SQLException {
		/**
		 * 2.3.2.5.1先清空本商家、仓库、po、标准条码的国检抽样明细
		 */
		VipAdjustmentInventoryDetailsModel model = new VipAdjustmentInventoryDetailsModel() ;
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setDepotId(vipPoBillVerificationModel.getDepotId());
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		
		vipAdjustmentInventoryDetailsDao.deleteByModel(model) ;
		
		Map<String, VipAdjustmentInventoryDetailsModel> cacheMap = new HashMap<String, VipAdjustmentInventoryDetailsModel>() ;
		
		/**
		 * 2.3.2.5.2.1库存调整单表业务类型为国检抽样，本本商家、仓库、po、标准条码国检抽样明细、唯品报废和唯品红冲明细
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
		
		List<Map<String , Object>> mapList = adjustmentInventoryItemDao.getVipAjuInvenDetails(queryMap) ; 
		
		for (Map<String, Object> map : mapList) {
			
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class );
			String adInvenCode = judgeIsNullOrNotReturnObj(map.get("ad_inven_code") , String.class ) ;
			String type = judgeIsNullOrNotReturnObj(map.get("type") , String.class ) ;
			String sourceCode = judgeIsNullOrNotReturnObj(map.get("source_code") , String.class ) ;
			//业务模式
			String adModel = judgeIsNullOrNotReturnObj(map.get("model") , String.class ) ; 
			//调整数量
			Integer adjustTotal = judgeIsNullOrNotReturnObj(map.get("adjust_total") , Integer.class ) ; 
			
			
			String key = goodsNo + "_" + adInvenCode + "_" + type + "_" + adModel + "_" + sourceCode;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipAdjustmentInventoryDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;					//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));				//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 						//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));					//仓库名称
				model.setAdjustmentInventoryOrder(judgeIsNullOrNotReturnObj(map.get("ad_inven_code") , String.class )); //库存调整单号
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 							//po号
				model.setPoDate(judgeIsNullOrNotReturnObj(map.get("po_date") , Timestamp.class )); 						//PO时间
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 				//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             			//商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));					//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 						//商品货号
				model.setAdjustmentInventoryType(judgeIsNullOrNotReturnObj(map.get("type") , String.class ));			//调整类型
				model.setAdjustmentInventoryMonths(judgeIsNullOrNotReturnObj(map.get("months") , String.class )); 		//归属月份
				model.setAdjustmentInventoryNum(adjustTotal); 															//调整数量
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				model.setAdjustmentInventoryDate(judgeIsNullOrNotReturnObj(map.get("adjustment_time") , Timestamp.class ));			//调整时间
				model.setModel(adModel);
				
				//唯品红冲、唯品报废, 获取唯品账单单号
				if(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4.equals(adModel) 
						|| DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6.equals(adModel)) {
					model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("source_code") , String.class ));   //唯品账单单号
				}
			}else {
				if(adjustTotal != null && model.getAdjustmentInventoryNum() != null) {
					adjustTotal += model.getAdjustmentInventoryNum() ;
					model.setAdjustmentInventoryNum(adjustTotal); 
				}
			}
			
			cacheMap.put(key, model) ;
		}
		
		/**
		 * 2.3.2.5.2.2 账单出库--唯品红冲（客退入库）
		 */
		//账单出库--唯品红冲（客退入库）
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_KTR) ;
		List<Map<String , Object>> listNewMap = billOutinDepotItemDao.getVipDetails(queryMap) ; 
		for (Map<String, Object> map : listNewMap) {
			
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class );
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class ) ; 
			String operateType = judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ) ;
			String billCode = judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ) ;
			
			//调整数量
			Integer adjustTotal = judgeIsNullOrNotReturnObj(map.get("num") , Integer.class ) ; 
			
			String key = goodsNo + "_" + code + "_" + operateType + "_" + DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4 + "_" + billCode;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipAdjustmentInventoryDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 		//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));	//仓库名称
				model.setAdjustmentInventoryOrder(judgeIsNullOrNotReturnObj(map.get("code") , String.class ));	
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 				//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 	//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             //商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));		//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 			//商品货号
				model.setAdjustmentInventoryType(judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ));			//调整类型
				model.setAdjustmentInventoryNum(adjustTotal); 															//调整数量
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				model.setAdjustmentInventoryDate(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));			//调整时间
				
				if(model.getAdjustmentInventoryDate() != null) {
					model.setAdjustmentInventoryMonths(TimeUtils.format(model.getAdjustmentInventoryDate(), "yyyy-MM"));
				}
				
				//业务模式
				model.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4);
				model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ));   //唯品账单单号
			}else {
				if(adjustTotal != null && model.getAdjustmentInventoryNum() != null) {
					adjustTotal += model.getAdjustmentInventoryNum() ;
					model.setAdjustmentInventoryNum(adjustTotal);
				}
			}
			
			cacheMap.put(key, model) ;
		}
		
		/**
		 * 2.3.2.5.2.3 账单出库--国检抽样（国检出库）
		 */
		//账单出库--国检抽样（国检出库）
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_GJC) ;
		listNewMap = billOutinDepotItemDao.getVipDetails(queryMap) ; 
		for (Map<String, Object> map : listNewMap) {
			
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class );
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class ) ; 
			String operateType = judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ) ;
			String billCode = judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ) ;
			
			//调整数量
			Integer adjustTotal = judgeIsNullOrNotReturnObj(map.get("num") , Integer.class ) ; 
			
			String key = goodsNo + "_" + code + "_" + operateType + "_" + DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5 + "_" + billCode;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipAdjustmentInventoryDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 		//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));	//仓库名称
				model.setAdjustmentInventoryOrder(judgeIsNullOrNotReturnObj(map.get("code") , String.class ));	
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 				//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 	//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             //商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));		//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 			//商品货号
				model.setAdjustmentInventoryType(judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ));			//调整类型
				model.setAdjustmentInventoryNum(adjustTotal); 															//调整数量
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				model.setAdjustmentInventoryDate(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));			//调整时间
				
				if(model.getAdjustmentInventoryDate() != null) {
					model.setAdjustmentInventoryMonths(TimeUtils.format(model.getAdjustmentInventoryDate(), "yyyy-MM"));
				}
				
				//业务模式
				model.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5);
				model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ));   //唯品账单单号
			}else {
				if(adjustTotal != null && model.getAdjustmentInventoryNum() != null) {
					adjustTotal += model.getAdjustmentInventoryNum() ;
					model.setAdjustmentInventoryNum(adjustTotal);
				}
			}
			
			cacheMap.put(key, model) ;
		}
		
		/**
		 * 2.3.2.5.2.4 账单出库--报废（报废出库）
		 */
		//账单出库--报废（报废出库）
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_BFC) ;
		listNewMap = billOutinDepotItemDao.getVipDetails(queryMap) ; 
		for (Map<String, Object> map : listNewMap) {
			
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class );
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class ) ; 
			String operateType = judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ) ;
			String billCode = judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ) ;
			
			//调整数量
			Integer adjustTotal = judgeIsNullOrNotReturnObj(map.get("num") , Integer.class ) ; 
			
			String key = goodsNo + "_" + code + "_" + operateType + "_" + DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6 + "_" + billCode;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipAdjustmentInventoryDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 		//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));	//仓库名称
				model.setAdjustmentInventoryOrder(judgeIsNullOrNotReturnObj(map.get("code") , String.class ));	
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 				//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 	//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             //商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));		//商品名称
				model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class )); 			//商品货号
				model.setAdjustmentInventoryType(judgeIsNullOrNotReturnObj(map.get("operate_type") , String.class ));			//调整类型
				model.setAdjustmentInventoryNum(adjustTotal); 															//调整数量
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
				model.setAdjustmentInventoryDate(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));			//调整时间
				
				if(model.getAdjustmentInventoryDate() != null) {
					model.setAdjustmentInventoryMonths(TimeUtils.format(model.getAdjustmentInventoryDate(), "yyyy-MM"));
				}
				
				//业务模式
				model.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6);
				model.setSourceCode(judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ));   //唯品账单单号
			}else {
				if(adjustTotal != null && model.getAdjustmentInventoryNum() != null) {
					adjustTotal += model.getAdjustmentInventoryNum() ;
					model.setAdjustmentInventoryNum(adjustTotal);
				}
			}
			
			cacheMap.put(key, model) ;
		}
		
		insertList.addAll(cacheMap.values()) ;
	}

	/**
	 * 销售退明细
	 * @param vipPoBillVerificationModel
	 * @throws SQLException 
	 */
	private void saveReturnOdepotDetails(VipPoBillVerificationModel vipPoBillVerificationModel, List<VipSaleReturnOdepotDetailsModel> insertList) throws SQLException {
		/**
		 * 2.3.2.4.1清空本商家、仓库、po、标准条码的销售退明细
		 */
		VipSaleReturnOdepotDetailsModel model = new VipSaleReturnOdepotDetailsModel() ;
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setDepotId(vipPoBillVerificationModel.getDepotId());
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		
		vipSaleReturnOdepotDetailsDao.deleteByModel(model) ;
		
		/**
		 * 2.3.2.4.2查询销售退出库表本本商家、仓库、po、标准条码的销售退出库明细并保存
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
		List<Map<String , Object>> mapList = saleReturnOrderItemDao.getVipSaleReturnOdepot(queryMap) ;
	
		for (Map<String, Object> map : mapList) {
			
			if(map == null) {
				continue ;
			}
			
			model = new VipSaleReturnOdepotDetailsModel() ;
			model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;					//商家id
			model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));				//商家名称
			model.setDepotId(judgeIsNullOrNotReturnObj(map.get("out_depot_id") , Long.class )); 					//仓库id
			model.setDepotName(judgeIsNullOrNotReturnObj(map.get("out_depot_name") , String.class ));				//仓库名称
			model.setSaleReturnOrder(judgeIsNullOrNotReturnObj(map.get("sale_return_code") , String.class ));	//销售退订单号
			model.setSaleReturnOdepotOrder(judgeIsNullOrNotReturnObj(map.get("sale_return_odepot_code") , String.class ));	//销售退出库订单号
			model.setSaleReturnOdepotType(judgeIsNullOrNotReturnObj(map.get("return_type") , String.class )); 		//退货类型
			model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 							//po号
			model.setPoDate(judgeIsNullOrNotReturnObj(map.get("po_date") , Timestamp.class ));
			model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode()); 				//标准条码
			model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("out_goods_id") , Long.class ));             			//商品id
			model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("out_goods_name") , String.class ));					//商品名称
			model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("out_goods_no") , String.class )); 						//商品货号
			BigDecimal returnNumBD = judgeIsNullOrNotReturnObj(map.get("return_num") , BigDecimal.class ); 
			model.setSaleReturnOdepotNum(returnNumBD == null ? 0 : returnNumBD.intValue());		//退货数量
			model.setSaleReturnOdepotDate(judgeIsNullOrNotReturnObj(map.get("return_date") , Timestamp.class ));		//退货时间
			model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 				//创建时间
			model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 				//修改时间
		
			insertList.add(model) ;
		}
		
	}

	/**
	 * 出库明细
	 * @param vipPoBillVerificationModel
	 * @throws SQLException 
	 */
	private void saveOutDepotDetails(VipPoBillVerificationModel vipPoBillVerificationModel, List<VipOutDepotDetailsModel> insertList) throws SQLException {
		/**
		 * 2.3.2.3.1先清空本商家、仓库、po、标准条码的出库明细
		 */
		VipOutDepotDetailsModel model = new VipOutDepotDetailsModel() ;
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setDepotId(vipPoBillVerificationModel.getDepotId());
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		
		vipOutDepotDetailsDao.deleteByModel(model) ;
		
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
		queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_XSC) ;
		
		List<Map<String , Object>> listMap = saleOutDepotItemDao.getVipSalesOutDetails(queryMap) ;
		List<Map<String , Object>> listNewMap = billOutinDepotItemDao.getVipDetails(queryMap) ;
		
		Map<String, VipOutDepotDetailsModel> cacheMap = new HashMap<String, VipOutDepotDetailsModel>() ;
		
		for (Map<String, Object> map : listMap) {
			
			String saleOutCode = judgeIsNullOrNotReturnObj(map.get("sale_out_code") , String.class );
			String saleOrderCode = judgeIsNullOrNotReturnObj(map.get("sale_order_code") , String.class );
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class ) ;
			String vipsBillNo = judgeIsNullOrNotReturnObj(map.get("vips_bill_no") , String.class ) ;
			
			//出库数量
			Integer transferNum = judgeIsNullOrNotReturnObj(map.get("transfer_num") , Integer.class ) ;
			
			String key = saleOutCode + "_" + saleOrderCode + "_" + goodsNo + "_" + vipsBillNo ;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipOutDepotDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("out_depot_id") , Long.class )); 		//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("out_depot_name") , String.class ));	//仓库名称
				model.setSaleOrder(saleOrderCode);															//销售订单号
				model.setSaleOutOrder(saleOutCode); 														//销售出库单号
				model.setVipBillCode(vipsBillNo);															//唯品账单号
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 				//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 	//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             //商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));		//商品名称
				model.setGoodsNo(goodsNo); 																	//商品货号
				model.setPoDate(judgeIsNullOrNotReturnObj(map.get("po_date") , Timestamp.class ));
				model.setOutDepotNum(transferNum);
				model.setOutDepotDate(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));	//出库时间
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 		//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 		//修改时间
			}else {
				if(transferNum != null && model.getOutDepotNum() != null) {
					transferNum += model.getOutDepotNum() ;
					model.setOutDepotNum(transferNum);
				}
			}
			
			cacheMap.put(key, model) ;
			
		}
		
		//账单出库明细
		for (Map<String, Object> map : listNewMap) {
			
			String code = judgeIsNullOrNotReturnObj(map.get("code") , String.class );
			String billCode = judgeIsNullOrNotReturnObj(map.get("bill_code") , String.class ) ;
			String goodsNo = judgeIsNullOrNotReturnObj(map.get("goods_no") , String.class ) ;
			
			//出库数量
			Integer transferNum = judgeIsNullOrNotReturnObj(map.get("num") , Integer.class ) ;
			
			String key = code + "_" + billCode + "_" + goodsNo ;
			
			model = cacheMap.get(key) ;
			
			if(model == null) {
				model = new VipOutDepotDetailsModel() ;
				model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
				model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
				model.setDepotId(judgeIsNullOrNotReturnObj(map.get("depot_id") , Long.class )); 			//仓库id
				model.setDepotName(judgeIsNullOrNotReturnObj(map.get("depot_name") , String.class ));		//仓库名称
				model.setSaleOrder(code);																	//销售订单号
				model.setSaleOutOrder(code); 																//销售出库单号
				model.setVipBillCode(billCode);																//唯品账单号
				model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no") , String.class )); 				//po号
				model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode") , String.class )); 	//标准条码
				model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id") , Long.class ));             //商品id
				model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name") , String.class ));		//商品名称
				model.setGoodsNo(goodsNo); 																	//商品货号
				model.setOutDepotNum(transferNum);
				model.setOutDepotDate(judgeIsNullOrNotReturnObj(map.get("deliver_date") , Timestamp.class ));	//出库时间
				model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date") , Timestamp.class )); 		//创建时间
				model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date") , Timestamp.class )); 		//修改时间
			}else {
				if(transferNum != null && model.getOutDepotNum() != null) {
					transferNum += model.getOutDepotNum() ;
					model.setOutDepotNum(transferNum);
				}
			}
			
			cacheMap.put(key, model) ;
		}
		
		insertList.addAll(cacheMap.values()) ;
		
	}

	/**
	 * 生成商品上架明细
	 * @param vipPoBillVerificationModel
	 * @throws SQLException 
	 */
	private void saveShelfDetails(VipPoBillVerificationModel vipPoBillVerificationModel, List<VipShelfDetailsModel> insertList) throws SQLException {
		/**
		 * 2.3.2.1.1先清空本商家、仓库、po、标准条码的上架明细
		 */
		VipShelfDetailsModel model = new VipShelfDetailsModel() ; 
		model.setMerchantId(vipPoBillVerificationModel.getMerchantId());
		model.setPoNo(vipPoBillVerificationModel.getPo());
		model.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
		
		vipShelfDetailsDao.deleteByModel(model) ;
		
		/**
		 * 2.3.2.1.2.1上架表关联销售订单表，查询本商家、仓库、po、标准条码销售类型为代销、上架表订单类型为代销是上架明细并保存。
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("commbarcode", vipPoBillVerificationModel.getCommbarcode()) ;
		queryMap.put("poNo", vipPoBillVerificationModel.getPo()) ;
		queryMap.put("merchantId", vipPoBillVerificationModel.getMerchantId()) ;
		queryMap.put("depotId", vipPoBillVerificationModel.getDepotId()) ;
		List<Map<String , Object>> mapList = saleShelfDao.getVipShelfDetails(queryMap) ;
		List<Map<String , Object>> mapNewList = saleShelfDao.getNewVipShelfDetails(queryMap) ;
		
		mapList.addAll(mapNewList) ;
		
		for (Map<String, Object> map : mapList) {
			model = new VipShelfDetailsModel() ; 
			model.setMerchantId(judgeIsNullOrNotReturnObj(map.get("merchant_id") , Long.class )) ;		//商家id
			model.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));	//商家名称
			model.setCustomerId(judgeIsNullOrNotReturnObj(map.get("customer_id") , Long.class));		//客户id
			model.setCustomerName(judgeIsNullOrNotReturnObj(map.get("customer_name") , String.class));	//客户名称
			model.setPoNo(judgeIsNullOrNotReturnObj(map.get("po_no"), String.class));					//PO号
			model.setOrderNo(judgeIsNullOrNotReturnObj(map.get("code"), String.class));					//销售订单号
			model.setGoodsId(judgeIsNullOrNotReturnObj(map.get("goods_id"), Long.class));				//商品id
			model.setGoodsName(judgeIsNullOrNotReturnObj(map.get("goods_name"), String.class));			//商品名称
			model.setGoodsNo(judgeIsNullOrNotReturnObj(map.get("goods_no"), String.class));				//货号
			model.setCommbarcode(judgeIsNullOrNotReturnObj(map.get("commbarcode"), String.class));		//标准条码
			
			BigDecimal saleNumBD = judgeIsNullOrNotReturnObj(map.get("num"), BigDecimal.class);
			
			model.setNum(saleNumBD == null ? 0 : saleNumBD.intValue());			//销售数量
			
			Integer shelfNum = 0 ;
			
			if(map.get("shelf_num") instanceof Long) {
				Long shelfNumL = judgeIsNullOrNotReturnObj(map.get("shelf_num"), Long.class) ;
				shelfNum = shelfNumL == null ? 0 : shelfNumL.intValue() ;
			}else if(map.get("shelf_num") instanceof Integer) {
				shelfNum = judgeIsNullOrNotReturnObj(map.get("shelf_num"), Integer.class) ;
			}
			
			model.setShelfNum(shelfNum);			//上架数量
			model.setDamagedNum(judgeIsNullOrNotReturnObj(map.get("damaged_num"), Integer.class));		//残损数量
			model.setLackNum(judgeIsNullOrNotReturnObj(map.get("lack_num"), Integer.class));			//少货数量
			model.setShelfDate(judgeIsNullOrNotReturnObj(map.get("shelf_date"), Timestamp.class));		//上架时间
			model.setCreateDate(judgeIsNullOrNotReturnObj(map.get("create_date"), Timestamp.class));	//创建时间
			model.setModifyDate(judgeIsNullOrNotReturnObj(map.get("modify_date"), Timestamp.class));	//修改时间
			
			//销售单价
			SaleOrderModel saleOrder = new SaleOrderModel();
			saleOrder.setCode(judgeIsNullOrNotReturnObj(map.get("code"), String.class));
			saleOrder = saleOrderDao.searchByModel(saleOrder) ;
			
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel() ;
			saleOrderItemModel.setOrderId(saleOrder.getId());
			saleOrderItemModel.setCommbarcode(vipPoBillVerificationModel.getCommbarcode());
			
			List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(saleOrderItemModel);
			
			if(!saleItemList.isEmpty()) {
				
				saleOrderItemModel = saleItemList.get(0) ;
				
				model.setSalePrice(saleOrderItemModel.getPrice());
			}
			
			
			insertList.add(model) ;
		}
		

	}

	/**
	 * 查询账单状态为未完结的所有po、标准条码、sku。
	 * @param depotId 
	 */
	public List<VipPoBillVerificationModel> getUnVeriPoBill(Long merchantId, String poNo, Long depotId) {
		
		VipPoBillVerificationModel model = new VipPoBillVerificationModel() ;
		model.setMerchantId(merchantId);
		model.setPo(poNo);
		model.setDepotId(depotId);
		
		return vipPoBillVerificationDao.getUnVeriPoBill(model);
	}

	/**
	 * 保存上架总量
	 */
	private void saveShelfAccount(Long merchantId, String poNo, DepotInfoModel depot) throws SQLException {
		/**
		 * 1.1 销售订单表关联上架表，查询销售单类型、上架表类型为代销、出仓仓库为唯品代销仓库的上架数据
		 * 		加上按上架入库单统计上架量 （兼容旧版代销）
		 */
		Map<String , Object> shelfAccountQueryMap = new HashMap<String , Object>() ;
		shelfAccountQueryMap.put("merchantId", merchantId) ;
		shelfAccountQueryMap.put("poNo", poNo ) ;
		shelfAccountQueryMap.put("depotId", depot.getId()) ;
		List<Map<String, Object>> shelfAccountMapList = saleShelfDao.getShelfAccountByPo(shelfAccountQueryMap);
		List<Map<String, Object>> shelfAccountNewMapList = saleShelfDao.getNewShelfAccountByPo(shelfAccountQueryMap);
	
		shelfAccountNewMapList.addAll(shelfAccountMapList) ;
		/**
		 * 1.2循环这些上架数据
		 */
		for (Map<String, Object> map : shelfAccountNewMapList) {
			
			if(map.get("commbarcode") == null || StringUtils.isBlank((String)map.get("commbarcode")) || 
					map.get("po_no") == null || StringUtils.isBlank((String)map.get("po_no"))) {
				logger.info("标准条码或PO号为空");
				
				continue ;
				
			}
			
			//上架数量
			BigDecimal shelfNumBD = judgeIsNullOrNotReturnObj(map.get("shelf_num"), BigDecimal.class);
			Integer shelfNum = shelfNumBD == null ? 0 : shelfNumBD.intValue() ;
			
			//上架残损数量
			BigDecimal shelfDamagedNumBD = judgeIsNullOrNotReturnObj(map.get("shelf_damaged_num"), BigDecimal.class);
			Integer shelfDamagedNum = shelfDamagedNumBD == null ? 0 : shelfDamagedNumBD.intValue() ;
			
			/**
			 * 1.2.1判断核销汇总表是否已存在本po、标准条码，无则新增，有则更新
			 */
			VipPoBillVerificationModel vipPoBillVeriModel = new VipPoBillVerificationModel();
			vipPoBillVeriModel.setMerchantId((Long)map.get("merchant_id"));
			vipPoBillVeriModel.setCommbarcode((String)map.get("commbarcode"));
			vipPoBillVeriModel.setPo(String.valueOf(map.get("po_no")));
			vipPoBillVeriModel.setDepotId(depot.getId());
			
			vipPoBillVeriModel = vipPoBillVerificationDao.searchByModel(vipPoBillVeriModel) ;
			
			if(vipPoBillVeriModel == null) {
				
				vipPoBillVeriModel = new VipPoBillVerificationModel();
				vipPoBillVeriModel.setMerchantId((Long)map.get("merchant_id"));
				vipPoBillVeriModel.setMerchantName((String)map.get("merchant_name"));
				vipPoBillVeriModel.setDepotId(depot.getId());
				vipPoBillVeriModel.setDepotName(depot.getName());
				vipPoBillVeriModel.setCommbarcode((String)map.get("commbarcode"));
				vipPoBillVeriModel.setPo(String.valueOf(map.get("po_no")));
				vipPoBillVeriModel.setShelfTotalAccount(shelfNum);
				vipPoBillVeriModel.setShelfDamagedAccount(shelfDamagedNum);
				vipPoBillVeriModel.setFirstShelfDate((Timestamp)map.get("first_shelf_date"));
				vipPoBillVeriModel.setCreateDate(TimeUtils.getNow());
				vipPoBillVeriModel.setModifyDate(TimeUtils.getNow());
				vipPoBillVeriModel.setStatus(DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_0); 											//状态
				
				vipPoBillVerificationDao.save(vipPoBillVeriModel) ;
			}else {
				//判断是否完结，已完结不做处理
				if(!DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_1.equals(vipPoBillVeriModel.getStatus())) {
					
					//汇总上架数量
					Integer shelfTotalAccount = 0;
					//汇总上架残损数量
					Integer shelfDamagedAccount = 0 ;
					
					Map<String , Object> queryMap = new HashMap<String , Object>() ;
					queryMap.put("merchantId", vipPoBillVeriModel.getMerchantId()) ;
					queryMap.put("poNo", vipPoBillVeriModel.getPo()) ;
					queryMap.put("depotId", vipPoBillVeriModel.getDepotId()) ;
					queryMap.put("commbarcode", vipPoBillVeriModel.getCommbarcode()) ;
					
					Map<String, Object> resultMap = saleShelfDao.getShelfAccountByPoAndCommbarcode(queryMap);
					Map<String, Object> resultNewMap = saleShelfDao.getNewShelfAccountByPoAndCommbarcode(queryMap) ;
					
					BigDecimal tempBD = null ;
					BigDecimal tempNewBD = null ;
					
					BigDecimal tempDamagedBD = null ;
					BigDecimal tempNewDamagedBD = null ;
					
					if(resultMap != null) {
						tempBD = judgeIsNullOrNotReturnObj(resultMap.get("shelf_num"), BigDecimal.class);
						tempDamagedBD = judgeIsNullOrNotReturnObj(resultMap.get("shelf_damaged_num"), BigDecimal.class);
					}

					if(resultNewMap != null) {
						tempNewBD = judgeIsNullOrNotReturnObj(resultNewMap.get("shelf_num"), BigDecimal.class);
						tempNewDamagedBD = judgeIsNullOrNotReturnObj(resultNewMap.get("shelf_damaged_num"), BigDecimal.class);
					}
					
					if(tempBD != null) {
						shelfTotalAccount += tempBD.intValue() ;
					}
					
					if(tempNewBD != null) {
						shelfTotalAccount += tempNewBD.intValue() ;
					}
					
					if(tempDamagedBD != null) {
						shelfDamagedAccount += tempDamagedBD.intValue() ;
					}
					
					if(tempNewDamagedBD != null) {
						shelfDamagedAccount += tempNewDamagedBD.intValue() ;
					}
						
					
					//汇总上架数量,上架残损
					vipPoBillVeriModel.setShelfTotalAccount(shelfTotalAccount);
					vipPoBillVeriModel.setShelfDamagedAccount(shelfDamagedAccount);
					vipPoBillVeriModel.setModifyDate(TimeUtils.getNow());
					
					vipPoBillVerificationDao.modifyNecessaryValue(vipPoBillVeriModel) ;
				}
			}
			
			/**
			 * 1.2.2更新上架表核销表获取状态字段为已获取 
			 */
			
			String ids = String.valueOf(map.get("ids")) ;
			
			String[] idArr = ids.split(",");
			
			SaleShelfModel saleShelfModel = null;
			for (String id : idArr) {
				saleShelfModel = new SaleShelfModel() ;
				saleShelfModel.setId(Long.valueOf(id));
				saleShelfModel.setVerifyRelState(DERP_ORDER.SALESHELF_VERIFYRELSTATE_1);
				
				saleShelfDao.modify(saleShelfModel) ;
			}
			
		}
		
	}
	
	

	
	/**理货单位转换
	 *箱/托盘转换为件
	 *boxToUnit 一箱多少件
	 *torrToUnit 一托多少件
	 *unit 理货单位 00-托盘 01-箱 02/空-件
	 * @return
	 */
    public Integer changeUnit(String unit,BigDecimal num,Integer boxToUnit,Integer torrToUnit,String merchantName,String goodsNo) throws RuntimeException{
    	Integer numInt=0;
    	if(num==null) return numInt;
    	
		//转换单位为件后返回
		if(StringUtils.isEmpty(unit)||unit.equals(DERP.ORDER_TALLYINGUNIT_02)){
			numInt=num.intValue();
		}else if(unit.equals(DERP.ORDER_TALLYINGUNIT_01)){
			if(boxToUnit==null||boxToUnit.intValue()<=0){
				throw new RuntimeException(merchantName+",货号："+goodsNo+"商品箱转件数据为空,结束执行");
			}
			numInt=num.intValue()*boxToUnit.intValue();
		}else if(unit.equals(DERP.ORDER_TALLYINGUNIT_00)){
			if(torrToUnit==null||torrToUnit.intValue()<=0){
				throw new RuntimeException(merchantName+",货号："+goodsNo+"商品托转件数据为空,结束执行");
			}
			numInt=num.intValue()*torrToUnit.intValue();
		}
    	return numInt;
    }
    
    @SuppressWarnings({ "unchecked" })
   	private <T>T judgeIsNullOrNotReturnObj(Object obj , Class<T> clazz){
       	if(obj == null) {
       		return null ;
       	}
       	
       	return (T)obj ;
    }


    /**
	 * 同步po未核销量到业务库
	 * */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201501081, model = DERP_LOG_POINT.POINT_13201501081_Label)
	public synchronized void synsOrderData(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		Long merchantId = null ;
		String poNo = null ;
		String depotCode = null ;
		
		if(jsonData.get("poNo") != null) {
			poNo = jsonData.getString("poNo");
		}
		
		if(jsonData.get("merchantId") != null) {
			merchantId = jsonData.getLong("merchantId");
		}
		
		if(jsonData.get("depotCode") != null) {
			depotCode = jsonData.getString("depotCode");
		}
		
		Connection conn = ConncetionUtil.getJDBCConnection(ApolloUtilDB.orderUrl, 
				ApolloUtilDB.orderUserName, ApolloUtilDB.orderPassword, ApolloUtilDB.orderDatabase);
		
		if (conn == null) {
			throw new Exception("jdbc连接失败");
		}
		
		conn.setAutoCommit(false);
		
		StringBuffer delSQLSb = new StringBuffer("delete from t_vip_po_bill_sync where depot_id = ? ") ;
		
		if(merchantId != null) {
			delSQLSb.append(" and merchant_id = ? ") ;
		}
		
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(delSQLSb.toString()) ;
		
		//从1开始，过滤唯品仓
		for (int i = 1 ; i < DEPOT_CODES.length; i ++) {
			//判断是否选择仓库
			if(StringUtils.isNotBlank(depotCode)
					&& !depotCode.equals(DEPOT_CODES[i])) {
				continue ;
			}
			
			//查询仓库
			DepotInfoModel depotInfoModel = new DepotInfoModel() ;
			depotInfoModel.setDepotCode(DEPOT_CODES[i]);
			depotInfoModel = depotInfoDao.searchByModel(depotInfoModel) ;
			
			/**删除业务库*/
			try {
				preparedStatement.setLong(1, depotInfoModel.getId());
				if(merchantId != null) {
					preparedStatement.setLong(2, merchantId);
				}
				
				int delNum = preparedStatement.executeUpdate();
				logger.info("---同步删除成功数：" + delNum);
				
			} catch (Exception e) {
				conn.rollback();
				ConncetionUtil.closed(conn, null, null , null);
				throw new RuntimeException(e) ;
			} 

			//查询状态未完结的po核销明细
			VipPoBillVerificationModel queryModel = new VipPoBillVerificationModel() ;
			queryModel.setMerchantId(merchantId);
			queryModel.setPo(poNo);
			queryModel.setDepotId(depotInfoModel.getId());
			List<VipPoBillVerificationModel> list = vipPoBillVerificationDao.getUnVeriPoBill(queryModel) ;

			try {
				
				String tbFieldSql = "select * from t_vip_po_bill_sync limit 1 ";
				Statement tbFieldsm = conn.createStatement();
				ResultSet tbFieldRs = tbFieldsm.executeQuery(tbFieldSql);
				// 获取对应同步表字段以及类型集合
				Map<String, Object> tbFieldMap = getTbFieldAndType(tbFieldRs);

				tbFieldRs.close();
				tbFieldsm.close();
				
				String selectSql = getSelectFieldStr(tbFieldMap);
				
				PreparedStatement insertPreparedStatement = null;
				
				for (VipPoBillVerificationModel vipPoBillVerificationModel : list) {
					
					if(vipPoBillVerificationModel.getUnsettledAccount() <= 0) {
						continue ;
					}
					
					StringBuffer saveSb = new StringBuffer() ;
					
					saveSb.append("insert into t_vip_po_bill_sync ").append("(").append(selectSql)
					.append(") values(");
					String value = "";
					// 根据字段类型拼接SQL
					for(String field_name : tbFieldMap.keySet()) {
						
						if("depot_id".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getDepotId()) + "," ;
							
						}else if("depot_name".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getDepotName()) + "," ;
							
						}else if("merchant_id".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getMerchantId()) + "," ;
							
						}else if("merchant_name".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getMerchantName()) + "," ;
							
						}else if("bu_id".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getBuId()) + "," ;
							
						}else if("bu_name".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getBuName()) + "," ;
							
						}else if("po_no".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getPo()) + "," ;
							
						}else if("po_no".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getPo()) + "," ;
							
						}else if("commbarcode".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getCommbarcode()) + "," ;
							
						}else if("unsettled_account".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getUnsettledAccount()) + "," ;
							
						}else if("first_shelf_date".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getFirstShelfDate()) + "," ;
							
						}else if("sale_price".equals(field_name)) {
							
							value += createStrjudgeByClz(vipPoBillVerificationModel.getSalePrice()) + "," ;
							
						}else if("create_date".equals(field_name)) {
							
							value += "NOW(), " ;
							
						}else if("modify_date".equals(field_name)) {
							
							value += "NULL, " ;
							
						}
						
					}
					
					value = value.substring(0, value.lastIndexOf(",")) ;
					saveSb.append(value).append(")");
					logger.info("---拼接的修改sql：" + saveSb.toString());
					
					insertPreparedStatement = conn.prepareStatement(saveSb.toString());
					int num = insertPreparedStatement.executeUpdate();
					logger.info("---同步新增成功数：" + num);
					
					insertPreparedStatement.close();
				}
				
				
			} catch (Exception e) {
				conn.rollback();
				ConncetionUtil.closed(conn, null, null , null);
				throw new RuntimeException(e) ;
			}
			
		}
		
		conn.commit();
		ConncetionUtil.closed(conn, preparedStatement, null , null);
	}
	
	/**
	 * 返回数据库表字段名及类型集合
	 * 
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> getTbFieldAndType(ResultSet rs) throws SQLException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		if (!rs.wasNull()) {

			ResultSetMetaData metaData = rs.getMetaData();
			
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				
				String typeName = metaData.getColumnTypeName(i + 1) ;
				if(Types.VARCHAR == metaData.getColumnType(i + 1)) {
					typeName = typeName + "("  + metaData.getColumnDisplaySize(i + 1) + ")" ; 
				}
				
				map.put(metaData.getColumnName(i + 1), metaData.getColumnTypeName(i + 1));
			}

		}

		return map;
	}

	/**
	 * 根据字段名属性集合拼成结果集字段
	 * 
	 * @param map
	 * @return
	 */
	private String getSelectFieldStr(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();

		for (String fieldName : map.keySet()) {
			
			if("id".equals(fieldName)) {
				continue ;
			}
			
			sb.append(" `").append(fieldName).append("`").append(", ");
		}

		String sql = sb.toString();
		sql = sql.substring(0, sql.lastIndexOf(","));

		return sql;
	}
	
	private String createStrjudgeByClz(Object obj) {
		
		String returnStr = "" ;
		
		if(obj instanceof String) {
			
			String temp = String.valueOf(obj) ;
			if(temp.contains("\\")) {
				temp = temp.replace("\\", "\\\\") ;
			}
			
			returnStr += "\'" + temp.replace("'", "") + "\'";
			
		}else if(obj instanceof Integer
				|| obj instanceof Double
				|| obj instanceof Float
				|| obj instanceof Long) {
			
			returnStr = String.valueOf(obj) ;
		}else if(obj instanceof BigDecimal) {
			
			BigDecimal temp = (BigDecimal)obj ;
			returnStr = temp.toPlainString() ;
			
		}else if(obj instanceof Timestamp) {
			
			Timestamp temp = (Timestamp)obj ;
			returnStr = TimeUtils.format(temp, "yyyy-MM-dd HH:mm:ss") ;
			returnStr = "\'" + returnStr.replace("'", "") + "\'";
		}else if(obj == null){

			returnStr = "NULL" ;

		}
		
		return returnStr ;
		
	}
}
