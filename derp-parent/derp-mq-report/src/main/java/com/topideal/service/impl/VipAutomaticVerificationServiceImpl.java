package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.automatic.VipAutomaticVerificationDao;
import com.topideal.dao.order.BillOutinDepotItemDao;
import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.dao.order.CrawlerOutRelDao;
import com.topideal.dao.order.MerchandiseContrastDao;
import com.topideal.dao.order.MerchandiseContrastItemDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.reporting.VipDifferenceAnalysisDao;
import com.topideal.dao.reporting.VipNondifferenceCheckDao;
import com.topideal.dao.storage.AdjustmentInventoryItemDao;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.automatic.VipAutomaticVerificationModel;
import com.topideal.entity.vo.order.BillOutinDepotItemModel;
import com.topideal.entity.vo.order.CrawlerBillModel;
import com.topideal.entity.vo.order.CrawlerOutRelModel;
import com.topideal.entity.vo.order.MerchandiseContrastItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastModel;
import com.topideal.entity.vo.reporting.VipDifferenceAnalysisModel;
import com.topideal.entity.vo.reporting.VipNondifferenceCheckModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.service.VipAutomaticVerificationService;

import net.sf.json.JSONObject;

@Service
public class VipAutomaticVerificationServiceImpl implements VipAutomaticVerificationService{
	
	/* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(VipAutomaticVerificationServiceImpl.class);

	@Autowired
	CrawlerBillDao crawlerBillDao;
	//仓库DAO
	@Autowired
	private DepotInfoDao depotInfoDao ;
	//爬虫商品对照DAO
	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao ;
	//销售出库DAO
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao ;
	//库存调整明细DAO
	@Autowired
	private AdjustmentInventoryItemDao adjustmentInventoryItemDao ;
	//盘点结果Dao
	@Autowired
	private TakesStockResultsDao takesStockResultsDao ;
	@Autowired
	private VipAutomaticVerificationDao vipAutomaticVerificationDao;
	@Autowired
	private VipDifferenceAnalysisDao vipDifferenceAnalysisDao ;
	@Autowired
	private VipNondifferenceCheckDao vipNondifferenceCheckDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	@Autowired
	private CrawlerOutRelDao crawlerOutRelDao ;
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao ;
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502010, model = DERP_LOG_POINT.POINT_13201502010_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags) throws SQLException {
		JSONObject jsonData = JSONObject.fromObject(json);
		Long merchantId = null ;
		String poNo = null ;
		String crawlerNo = null ;
		
		if(jsonData.get("poNo") != null) {
			poNo = jsonData.getString("poNo");
		}
		
		if(jsonData.get("merchantId") != null) {
			merchantId = jsonData.getLong("merchantId");
		}
		
		if(jsonData.get("crawlerNo") != null) {
			crawlerNo = jsonData.getString("crawlerNo");
		}
		
		//获取唯品代销仓id
		DepotInfoModel depotInfoModel = new DepotInfoModel() ;
		depotInfoModel.setDepotCode("VIP001");
		depotInfoModel = depotInfoDao.searchByModel(depotInfoModel) ;
		
		/***
		 * 1.1 根据商家、po号、账单号查询状态为未获取的爬虫账单,生成账单数据唯品自动校验账单对象
		 */
		List<VipAutomaticVerificationModel> vipAutoVeriList = saveVipAutoVeriBill(merchantId, poNo, crawlerNo) ;
	
		/***
		 * 1.2 根据自动校验账单对象，汇总统计系统量，并保存校验对象
		 */
		saveVipAutoVeriAccountSystem(vipAutoVeriList , depotInfoModel.getId()) ;
		
		/**
		 * 2.1 查询未对平记录，生成差异分析报告
		 */
		saveDifferenceAnalysis(vipAutoVeriList, depotInfoModel.getId()) ;
		
		/**
		 * 2.2查询已对平记录，生成无差异记录
		 */
		saveNondifferenceCheck(vipAutoVeriList) ;
		
	}

	/**
	 * 查询已对平记录，生成无差异记录
	 * @param merchantId
	 * @param poNo
	 * @param crawlerNo
	 * @throws SQLException 
	 */
	private void saveNondifferenceCheck(List<VipAutomaticVerificationModel> vipAutoVeriList) throws SQLException {
		
		for (VipAutomaticVerificationModel tempModel : vipAutoVeriList) {
			
			Map<String , Object> delMap = new HashMap<String , Object>() ;
			delMap.put("poNo", tempModel.getPoNo()) ;
			delMap.put("crawlerNo", tempModel.getCrawlerNo()) ;
			delMap.put("merchantId", tempModel.getMerchantId()) ;
			vipNondifferenceCheckDao.delByMap(delMap) ;
			
			if(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_0.equals(tempModel.getVerificationResult())) {
				continue ;
			}
			
			
			MerchandiseContrastModel queryModel = new MerchandiseContrastModel() ;
			queryModel.setCrawlerGoodsNo(tempModel.getCrawlerGoodsNo());
			queryModel.setMerchantId(tempModel.getMerchantId());
			queryModel.setStatus(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0);
			queryModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);
			queryModel = merchandiseContrastDao.searchByModel(queryModel) ;
			
			if(queryModel == null) {
				logger.error("该sku:" + tempModel.getCrawlerGoodsNo() + ",在爬虫商品对照表中找不到映射关系");
				continue ;
			}
			
			MerchandiseContrastItemModel queryItemModel = new MerchandiseContrastItemModel() ;
			queryItemModel.setContrastId(queryModel.getId());
			
			List<MerchandiseContrastItemModel> itemList = merchandiseContrastItemDao.list(queryItemModel);
			
			for (MerchandiseContrastItemModel itemModel : itemList) {
				String goodsNo = itemModel.getGoodsNo() ;
				Long goodsId = null ;
				
				MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel() ;
				merchandiseInfoModel.setMerchantId(tempModel.getMerchantId());
				merchandiseInfoModel.setGoodsNo(goodsNo);
				merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel) ;
				
				if(merchandiseInfoModel != null) {
					goodsId = merchandiseInfoModel.getId() ;
				}
				
				//根据爬虫账单号获取爬虫ID
				String billCode = tempModel.getCrawlerNo();
				CrawlerBillModel crawlerBillModel = new CrawlerBillModel() ;
				crawlerBillModel.setBillCode(billCode);
				crawlerBillModel.setPoNo(tempModel.getPoNo());
				crawlerBillModel.setGoodsNo(tempModel.getCrawlerGoodsNo());
				crawlerBillModel.setMerchantId(tempModel.getMerchantId());
				
				List<CrawlerBillModel> crawlerList = crawlerBillDao.list(crawlerBillModel);
				for (CrawlerBillModel tempCrawler : crawlerList) {
					CrawlerOutRelModel crawlerOutRelModel = new CrawlerOutRelModel() ;
					crawlerOutRelModel.setCrawlerId(tempCrawler.getId());
					crawlerOutRelModel.setMerchantId(tempModel.getMerchantId());
					crawlerOutRelModel.setGoodsNo(goodsNo);
					crawlerOutRelModel.setGoodsId(goodsId);
					crawlerOutRelModel.setType(DERP.CRAWLER_TYPE_2);
					
					crawlerOutRelModel = crawlerOutRelDao.searchByModel(crawlerOutRelModel) ;
					
					if(crawlerOutRelModel == null) {
						logger.info("爬虫账单号：" + tempCrawler.getBillCode() + "在爬虫账单核销表找不到关联关系");
						continue ;
					}
					
					VipNondifferenceCheckModel vipNondifferenceCheckModel = new VipNondifferenceCheckModel() ;
					vipNondifferenceCheckModel.setCrawlerGoodsNo(tempModel.getCrawlerGoodsNo());
					vipNondifferenceCheckModel.setCrawlerType(tempCrawler.getBillType());
					vipNondifferenceCheckModel.setCrawlerNo(tempModel.getCrawlerNo());
					vipNondifferenceCheckModel.setGoodsNo(goodsNo);
					vipNondifferenceCheckModel.setGoodsId(goodsId);
					vipNondifferenceCheckModel.setMerchantId(tempModel.getMerchantId());
					vipNondifferenceCheckModel.setMerchantName(tempModel.getMerchantName());
					vipNondifferenceCheckModel.setPlatform(tempModel.getPlatform());
					vipNondifferenceCheckModel.setMonth(tempModel.getMonth());
					vipNondifferenceCheckModel.setOrderCode(crawlerOutRelModel.getOrderCode());
					vipNondifferenceCheckModel.setOrderId(crawlerOutRelModel.getOrderId());
					vipNondifferenceCheckModel.setPoNo(tempModel.getPoNo());
					vipNondifferenceCheckModel.setAccount(crawlerOutRelModel.getNum());
					vipNondifferenceCheckModel.setCreateDate(TimeUtils.getNow());
					
					String billType = tempCrawler.getBillType() ;
					//若不是销售明细和库存买断，按原来账单类型存入
					if(!DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_00.equals(billType)
							&& !DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_01.equals(billType)) {
						vipNondifferenceCheckModel.setOrderType(billType);
					}else {
						if(crawlerOutRelModel.getOrderCode() != null ) {
							if(crawlerOutRelModel.getOrderCode().startsWith(DERP.UNIQUEID_PREFIX_WPHC)) {
								vipNondifferenceCheckModel.setOrderType(DERP_REPORT.VIP_AUTO_VERI_ORDER_TYPE_06);
							}else {
								vipNondifferenceCheckModel.setOrderType(DERP_REPORT.VIP_AUTO_VERI_ORDER_TYPE_00);
							}
						}
					}
					
					vipNondifferenceCheckDao.save(vipNondifferenceCheckModel) ;
				}
			}
			
		}
	}

	/**
	 * 查询未对平记录，生成差异分析报告
	 * @param merchantId
	 * @param poNo
	 * @param crawlerNo
	 * @throws SQLException 
	 */
	private void saveDifferenceAnalysis(List<VipAutomaticVerificationModel> vipAutoVeriList, Long depotId) throws SQLException {
		
		for (VipAutomaticVerificationModel tempModel : vipAutoVeriList) {
			
			Map<String , Object> delMap = new HashMap<String , Object>() ;
			delMap.put("poNo", tempModel.getPoNo()) ;
			delMap.put("crawlerNo", tempModel.getCrawlerNo()) ;
			delMap.put("merchantId", tempModel.getMerchantId()) ;
			vipDifferenceAnalysisDao.delByMap(delMap) ;
			
			if(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_1.equals(tempModel.getVerificationResult())) {
				continue ;
			}
			
			VipDifferenceAnalysisModel analysisModel = new VipDifferenceAnalysisModel() ;
			analysisModel.setCrawlerGoodsNo(tempModel.getCrawlerGoodsNo());
			analysisModel.setCrawlerNo(tempModel.getCrawlerNo());
			analysisModel.setMerchantId(tempModel.getMerchantId());
			analysisModel.setMerchantName(tempModel.getMerchantName());
			analysisModel.setPoNo(tempModel.getPoNo());
			analysisModel.setMonth(tempModel.getMonth());
			analysisModel.setPlatform(tempModel.getPlatform());
			
			//生成销售明细差异
			if(tempModel.getSalesOutDifference() != 0) {
				
				analysisModel.setDifferenceAccount(tempModel.getSalesOutDifference());
				analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_00);
				analysisModel.setCreateDate(TimeUtils.getNow());
				
				analysisModel.setId(null);
				vipDifferenceAnalysisDao.save(analysisModel) ;
			}
			
			//唯品红冲差异
			if(tempModel.getHcDifference() != 0) {
				
				analysisModel.setDifferenceAccount(tempModel.getHcDifference());
				analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_06);
				analysisModel.setCreateDate(TimeUtils.getNow());
				
				analysisModel.setId(null);
				vipDifferenceAnalysisDao.save(analysisModel) ;
			}
			
			/*//库存买断差异
			if(tempModel.getTakesStockDifference() != 0) {
				analysisModel.setDifferenceAccount(tempModel.getTakesStockDifference());
				analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_01);
				analysisModel.setCreateDate(TimeUtils.getNow());
				
				analysisModel.setId(null);
				vipDifferenceAnalysisDao.save(analysisModel) ;
			}*/
			
			//调整（调增）差异
			if(tempModel.getAdjustmentIncreaseDifferent() != 0) {
				analysisModel.setDifferenceAccount(tempModel.getAdjustmentIncreaseDifferent());
				analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_04);
				analysisModel.setCreateDate(TimeUtils.getNow());
				
				analysisModel.setId(null);
				vipDifferenceAnalysisDao.save(analysisModel) ;
			}
			
			//调整（调减）差异 ，分别取出国检抽样、报废、盘亏3种差异
			if(tempModel.getAdjustmentDecreaseDifferent() != 0) {
				
				//爬虫表查询数量集合
				Map<String, Object> queryCrawlerMap = new HashMap<String, Object>() ;
				queryCrawlerMap.put("goodsNo", tempModel.getCrawlerGoodsNo()) ;
				queryCrawlerMap.put("billCode", tempModel.getCrawlerNo()) ;
				queryCrawlerMap.put("poNo", tempModel.getPoNo()) ;
				queryCrawlerMap.put("merchantId", tempModel.getMerchantId()) ;
				queryCrawlerMap.put("crawlerGoodsNo", tempModel.getCrawlerGoodsNo()) ;
				
				//盘亏账单数量
				queryCrawlerMap.put("billType", DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02) ;
				Integer pkAccount = crawlerBillDao.getDecreaseNum(queryCrawlerMap) ;
				if(pkAccount == null) {
					pkAccount = 0 ;
				}
				
				//国检抽样账单数量
				queryCrawlerMap.put("billType", DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_05) ;
				Integer gjAccount = crawlerBillDao.getDecreaseNum(queryCrawlerMap) ;
				if(gjAccount == null) {
					gjAccount = 0 ;
				}
				
				//唯品报废账单数量
				queryCrawlerMap.put("billType", DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_03) ;
				Integer bfAccount = crawlerBillDao.getDecreaseNum(queryCrawlerMap) ;
				if(bfAccount == null) {
					bfAccount = 0 ;
				}
				
				MerchandiseContrastModel merchandiseContrastModel = new MerchandiseContrastModel() ;
				merchandiseContrastModel.setCrawlerGoodsNo(tempModel.getCrawlerGoodsNo());
				merchandiseContrastModel.setMerchantId(tempModel.getMerchantId());
				merchandiseContrastModel.setStatus(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0);
				merchandiseContrastModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);
				merchandiseContrastModel = merchandiseContrastDao.searchByModel(merchandiseContrastModel) ;
				
				if(merchandiseContrastModel == null) {
					if(pkAccount != 0) {
						analysisModel.setDifferenceAccount(pkAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
					
					if(gjAccount != 0) {
						analysisModel.setDifferenceAccount(gjAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_05);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
					
					if(bfAccount != 0) {
						analysisModel.setDifferenceAccount(bfAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_03);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
					
				}else {
					MerchandiseContrastItemModel queryItemModel = new MerchandiseContrastItemModel() ;
					queryItemModel.setContrastId(merchandiseContrastModel.getId());
					
					List<MerchandiseContrastItemModel> itemList = merchandiseContrastItemDao.list(queryItemModel);
					
					String goodsNos = itemList.stream().map(MerchandiseContrastItemModel::getGoodsNo).collect(Collectors.joining(","));
					
					Map<String, Object> queryMap = new HashMap<String, Object>() ;
					queryMap.put("goodsNos", goodsNos) ;
					queryMap.put("vipsBillNo", tempModel.getCrawlerNo()) ;
					queryMap.put("poNo", tempModel.getPoNo()) ;
					queryMap.put("merchantId", tempModel.getMerchantId()) ;
					queryMap.put("depotId", depotId) ;
					
					//盘亏系统数量
					queryMap.put("type", DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2) ; 
					Integer pKSystemAccount = takesStockResultsDao.getStockSystemAccount(queryMap) ;
					queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PKC) ;
					BillOutinDepotItemModel systemPKItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
					if(pKSystemAccount == null) {
						pKSystemAccount = 0 ;
					}
					
					if(systemPKItem != null) {
						pKSystemAccount += systemPKItem.getNum() ;
						Integer pkContrastNum = systemPKItem.getContrastNum();
						pkAccount = pkAccount * pkContrastNum ;
					}
					
					if(pkAccount  - pKSystemAccount != 0) {
						
						analysisModel.setDifferenceAccount(pkAccount - pKSystemAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
					
					//国检抽样系统数量
					queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5) ;
					Integer systemGjAccount = adjustmentInventoryItemDao.getInventorySystemNumByModel(queryMap) ;
					queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_GJC) ;
					BillOutinDepotItemModel systemGJItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
					if(systemGjAccount == null) {
						systemGjAccount = 0 ;
					}
					
					if(systemGJItem != null) {
						systemGjAccount += systemGJItem.getNum() ;
						Integer gjContrastNum = systemGJItem.getContrastNum() ;
						gjAccount = gjAccount * gjContrastNum ;
					}
					
					if(gjAccount - systemGjAccount != 0) {
						
						analysisModel.setDifferenceAccount(gjAccount  - systemGjAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_05);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
					
					//唯品报废系统数量
					queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6) ;
					Integer systemBfAccount = adjustmentInventoryItemDao.getInventorySystemNumByModel(queryMap) ;
					queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_BFC) ;
					BillOutinDepotItemModel systemBFItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
					if(systemBfAccount == null) {
						systemBfAccount = 0 ;
					}
					
					if(systemBFItem != null) {
						systemBfAccount = systemBFItem.getNum() ;
						Integer bfContrastNum = systemBFItem.getContrastNum();
						bfAccount = bfAccount * bfContrastNum ;
					}
					
					if(bfAccount - systemBfAccount != 0) {
						analysisModel.setDifferenceAccount(bfAccount - systemBfAccount);
						analysisModel.setBillType(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_03);
						analysisModel.setCreateDate(TimeUtils.getNow());
						
						analysisModel.setId(null);
						vipDifferenceAnalysisDao.save(analysisModel) ;
					}
				}
			}
		}
	}

	/**
	 * 根据自动校验账单对象，汇总统计系统量
	 * @param vipAutoVeriList
	 * @return
	 * @throws SQLException 
	 */
	private void saveVipAutoVeriAccountSystem(
			List<VipAutomaticVerificationModel> vipAutoVeriList , Long depotId) throws SQLException {
		
		for (VipAutomaticVerificationModel tempModel : vipAutoVeriList) {
			//sku
			String crawlerGoodsNo = tempModel.getCrawlerGoodsNo();
			//爬虫账单号
			String crawlerNo = tempModel.getCrawlerNo();
			//po号
			String poNo = tempModel.getPoNo();
			Long merchantId = tempModel.getMerchantId();
			
			MerchandiseContrastModel merchandiseContrastModel = new MerchandiseContrastModel() ;
			merchandiseContrastModel.setCrawlerGoodsNo(crawlerGoodsNo);
			merchandiseContrastModel.setMerchantId(merchantId);
			merchandiseContrastModel.setStatus(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0);
			merchandiseContrastModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);
			merchandiseContrastModel = merchandiseContrastDao.searchByModel(merchandiseContrastModel) ;
			
			//在怕从商品对照表找不到货号时，直接生成无系统单据自动校验对象
			if(merchandiseContrastModel == null) {
				//销售出库
				Integer billSalesAccount = tempModel.getBillSalesAccount() ;
				if(billSalesAccount == null ) {
					billSalesAccount = 0 ;
					tempModel.setBillSalesAccount(billSalesAccount);
				}
				
				tempModel.setSystemSalesOutAccount(0);
				tempModel.setSalesOutDifference(billSalesAccount);
				
				//唯品红冲
				Integer billHcAccount = tempModel.getBillHcAccount() ;
				if(billHcAccount == null) {
					billHcAccount = 0 ;
					tempModel.setBillHcAccount(billHcAccount);
				}
				
				tempModel.setSystemHcAccount(0);
				tempModel.setHcDifference(billHcAccount);
				
				//调增
				Integer billAdjustmentIncreaseAccount = tempModel.getBillAdjustmentIncreaseAccount() ;
				if(billAdjustmentIncreaseAccount == null) {
					billAdjustmentIncreaseAccount = 0 ;
					tempModel.setBillAdjustmentIncreaseAccount(billAdjustmentIncreaseAccount);
				}
				
				tempModel.setSystemAdjustmentIncreaseAccount(0);
				tempModel.setAdjustmentIncreaseDifferent(billAdjustmentIncreaseAccount);
				
				//调减
				Integer billAdjustmentDecreaseAccount = tempModel.getBillAdjustmentDecreaseAccount() ;
				if(billAdjustmentDecreaseAccount == null) {
					billAdjustmentDecreaseAccount = 0 ;
					tempModel.setBillAdjustmentDecreaseAccount(billAdjustmentDecreaseAccount);
				}
				
				tempModel.setSystemAdjustmentDecreaseAccount(0);
				tempModel.setAdjustmentDecreaseDifferent(billAdjustmentDecreaseAccount);
				
				tempModel.setVerificationResult(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_0); 
				
			}else {
				MerchandiseContrastItemModel queryItemModel = new MerchandiseContrastItemModel() ;
				queryItemModel.setContrastId(merchandiseContrastModel.getId());
				
				List<MerchandiseContrastItemModel> itemList = merchandiseContrastItemDao.list(queryItemModel);
				
				String goodsNos = itemList.stream().map(MerchandiseContrastItemModel::getGoodsNo).collect(Collectors.joining(","));
				
				Map<String, Object> queryMap = new HashMap<String, Object>() ;
				queryMap.put("goodsNos", goodsNos) ;
				queryMap.put("vipsBillNo", crawlerNo) ;
				queryMap.put("poNo", poNo) ;
				queryMap.put("merchantId", merchantId) ;
				queryMap.put("depotId", depotId) ;
				queryMap.put("platformSku", crawlerGoodsNo) ;
				
				//系统销售出库总量(合并库存买断)
				Integer systemSalesOutAccount = saleOutDepotItemDao.getVipAutoVeriSystemNum(queryMap) ;
				
				queryMap.put("billSource", DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_2) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_XSC) ;
				BillOutinDepotItemModel systemSalesOutItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				
				if(systemSalesOutAccount == null) {
					systemSalesOutAccount = 0 ;
				}
				
				//销售出库差异：账单销售总量-系统销售出库总量 
				Integer billSalesAccount = tempModel.getBillSalesAccount();
				if(billSalesAccount == null) {
					billSalesAccount = 0 ;
					tempModel.setBillSalesAccount(billSalesAccount);
				}
				
				if(systemSalesOutItem != null) {
					systemSalesOutAccount += systemSalesOutItem.getNum() ;
					Integer saleContrastNum = systemSalesOutItem.getContrastNum() ;
					billSalesAccount = billSalesAccount * saleContrastNum ;
				}
				
				Integer	salesOutDifference = billSalesAccount - systemSalesOutAccount ;
				tempModel.setSystemSalesOutAccount(systemSalesOutAccount);
				tempModel.setSalesOutDifference(salesOutDifference);
				
				
				//系统红冲总量
				queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4) ;
				Integer systemHcAccount = adjustmentInventoryItemDao.getInventorySystemNumByModel(queryMap) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_KTR) ;
				BillOutinDepotItemModel systemHcItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				
				if(systemHcAccount == null) {
					systemHcAccount = 0;
				}
				
				Integer billHcAccount = tempModel.getBillHcAccount();
				if(billHcAccount == null) {
					billHcAccount = 0 ;
					tempModel.setBillHcAccount(billHcAccount);
				}
				
				if(systemHcItem != null) {
					systemHcAccount += systemHcItem.getNum() ;
					Integer hcContrastNum = systemHcItem.getContrastNum() ;
					billHcAccount = billHcAccount * hcContrastNum ;
				}
				
				//红冲差异 ：账单红冲总量-系统红冲总量   
				Integer	hcDifference = billHcAccount - systemHcAccount ;
				
				tempModel.setSystemHcAccount(systemHcAccount);
				tempModel.setHcDifference(hcDifference);
				
				//系统库存调整（调减） 合计其他明细类型中的国检抽样、报废、盘亏数量
				//账单调减数量
				Integer billAdjustmentDecreaseAccount = tempModel.getBillAdjustmentDecreaseAccount();
				Integer billAdjustmentDecreaseTotalAccount = 0 ;
				if(billAdjustmentDecreaseAccount == null) {
					billAdjustmentDecreaseAccount = 0 ;
					tempModel.setBillAdjustmentDecreaseAccount(billAdjustmentDecreaseAccount);
				}
				
				queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5) ;
				Integer systemGjAccount = adjustmentInventoryItemDao.getInventorySystemNumByModel(queryMap) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_GJC) ;
				BillOutinDepotItemModel systemGjItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				
				if(systemGjAccount == null) {
					systemGjAccount = 0 ;
				}
				
				if(systemGjItem != null) {
					systemGjAccount += systemGjItem.getNum() ;
					Integer gjContrastNum = systemGjItem.getContrastNum() ;
					billAdjustmentDecreaseTotalAccount = billAdjustmentDecreaseAccount * gjContrastNum ;
				}
				
				queryMap.put("model", DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6) ;
				Integer systemBfAccount = adjustmentInventoryItemDao.getInventorySystemNumByModel(queryMap) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_BFC) ;
				BillOutinDepotItemModel systemBFItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				if(systemBfAccount == null) {
					systemBfAccount = 0 ;
				}
				
				if(systemBFItem != null) {
					systemBfAccount += systemBFItem.getNum() ;
					Integer bfContrastNum = systemBFItem.getContrastNum() ;
					billAdjustmentDecreaseTotalAccount = billAdjustmentDecreaseAccount * bfContrastNum ;
				}
				
				//盘亏数量
				queryMap.put("type", DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2) ; 
				Integer pKAccount = takesStockResultsDao.getStockSystemAccount(queryMap) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PKC) ;
				BillOutinDepotItemModel systemPKItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				if(pKAccount == null) {
					pKAccount = 0 ;
				}
				
				if(systemPKItem != null) {
					pKAccount += systemPKItem.getNum() ;
					Integer pkContrastNum = systemPKItem.getContrastNum() ;
					billAdjustmentDecreaseTotalAccount = billAdjustmentDecreaseAccount * pkContrastNum ;
				}
				
				Integer	systemAdjustmentDecreaseAccount = systemGjAccount + systemBfAccount + pKAccount ;
				tempModel.setSystemAdjustmentDecreaseAccount(systemAdjustmentDecreaseAccount) ;
				
				//调减差异
				Integer adjustmentDecreaseDifferent = billAdjustmentDecreaseTotalAccount - systemAdjustmentDecreaseAccount ;
				tempModel.setAdjustmentDecreaseDifferent(adjustmentDecreaseDifferent);
				
				//盘盈数量
				queryMap.put("type", DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1) ; 
				Integer systemAdjustmentIncreaseAccount = takesStockResultsDao.getStockSystemAccount(queryMap) ;
				queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_PYR) ;
				BillOutinDepotItemModel systemAdjustmentIncreaseItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
				
				if(systemAdjustmentIncreaseAccount == null) {
					systemAdjustmentIncreaseAccount = 0 ;
				}
				
				//账单调增数量
				Integer billAdjustmentIncreaseAccount = tempModel.getBillAdjustmentIncreaseAccount();
				if(billAdjustmentIncreaseAccount == null) {
					billAdjustmentIncreaseAccount = 0 ;
					tempModel.setBillAdjustmentIncreaseAccount(billAdjustmentIncreaseAccount);
				}
				
				if(systemAdjustmentIncreaseItem != null) {
					systemAdjustmentIncreaseAccount += systemAdjustmentIncreaseItem.getNum();
					Integer increaseContrastNum  = systemAdjustmentIncreaseItem.getContrastNum() ;
					billAdjustmentIncreaseAccount = billAdjustmentIncreaseAccount * increaseContrastNum ;
				}
				
				//盘盈差异
				Integer adjustmentIncreaseDifferent = billAdjustmentIncreaseAccount - systemAdjustmentIncreaseAccount ;
				tempModel.setSystemAdjustmentIncreaseAccount(systemAdjustmentIncreaseAccount);
				tempModel.setAdjustmentIncreaseDifferent(adjustmentIncreaseDifferent);
				
				if(salesOutDifference == 0 &&
						hcDifference == 0 &&
						adjustmentDecreaseDifferent == 0 &&
						adjustmentIncreaseDifferent == 0) {
					//已对平
					tempModel.setVerificationResult(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_1); 
				}else {
					//未对平
					tempModel.setVerificationResult(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_0); 
				}
			}
			
			//根据PO + SKU + 账单号 + 商家查询是否存在该记录
			VipAutomaticVerificationModel queryModel = new VipAutomaticVerificationModel() ;
			queryModel.setPoNo(tempModel.getPoNo());
			queryModel.setCrawlerGoodsNo(tempModel.getCrawlerGoodsNo());
			queryModel.setCrawlerNo(tempModel.getCrawlerNo());
			queryModel.setMerchantId(tempModel.getMerchantId());
			
			queryModel = vipAutomaticVerificationDao.searchByModel(queryModel) ;
			
			if(queryModel != null) {
				tempModel.setId(queryModel.getId());
				tempModel.setModifyDate(TimeUtils.getNow());
				
				vipAutomaticVerificationDao.modify(tempModel) ;
			}else {
				tempModel.setCreateDate(TimeUtils.getNow());
				vipAutomaticVerificationDao.save(tempModel) ;
			}
		}
		
	}

	/**
	 * 根据商家、po号、账单号查询状态为未获取的爬虫账单,生成账单数据唯品自动校验账单对象
	 * @param merchantId
	 * @param poNo
	 * @param crawlerNo
	 * @return
	 * @throws SQLException 
	 */
	private List<VipAutomaticVerificationModel> saveVipAutoVeriBill(Long merchantId, String poNo, String crawlerNo) throws SQLException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
		
		List<VipAutomaticVerificationModel> list = new ArrayList<VipAutomaticVerificationModel>() ;
		
		//以商家+账单+PO+SKU为key , 储存自动校验model映射对象
		Map<String, VipAutomaticVerificationModel> autoVeriMap = new HashMap<String, VipAutomaticVerificationModel>() ;
		
		//查询未对平账单
		VipAutomaticVerificationModel vipAutomaticVerificationModel = new VipAutomaticVerificationModel() ;
		vipAutomaticVerificationModel.setPoNo(poNo);
		vipAutomaticVerificationModel.setCrawlerNo(crawlerNo);
		vipAutomaticVerificationModel.setMerchantId(merchantId);
		vipAutomaticVerificationModel.setVerificationResult(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_0);
		
		List<VipAutomaticVerificationModel> unLeavingList = vipAutomaticVerificationDao.list(vipAutomaticVerificationModel);
		
		for (VipAutomaticVerificationModel unLeavingModel : unLeavingList) {
			Long merchantIdTemp = unLeavingModel.getMerchantId() ;
			String poNoTemp = unLeavingModel.getPoNo() ;
			String billCodeTemp = unLeavingModel.getCrawlerNo() ;
			String goodNoTemp = unLeavingModel.getCrawlerGoodsNo() ;
			
			String key = merchantIdTemp+ "_" + poNoTemp + "_" + billCodeTemp + "_" + goodNoTemp ;
			
			VipAutomaticVerificationModel tempModel = autoVeriMap.get(key);
			
			if(tempModel == null) {
				tempModel = unLeavingModel ;
				autoVeriMap.put(key, tempModel) ;
			}
			
		}
		
		//查询未校验状态爬虫账单汇总po号、账单号、爬虫货号
		Map<String, Object> params = new HashMap<String, Object>() ;
		params.put("merchantId", merchantId); 
		params.put("poNo", poNo) ;
		params.put("crawlerNo", crawlerNo) ;
		
		List<Map<String, Object>> crawlerUnVeriList = crawlerBillDao.getUnVeriList(params) ;
		
		for (Map<String, Object> map : crawlerUnVeriList) {
			Long merchantIdTemp = judgeIsNullOrNotReturnObj(map.get("merchant_id"), Long.class) ;
			String poNoTemp = judgeIsNullOrNotReturnObj(map.get("po_no"), String.class) ;
			String billCodeTemp = judgeIsNullOrNotReturnObj(map.get("bill_code"), String.class) ;
			String goodNoTemp = judgeIsNullOrNotReturnObj(map.get("goods_no"), String.class) ;
			
			String key = merchantIdTemp+ "_" + poNoTemp + "_" + billCodeTemp + "_" + goodNoTemp ;
			
			VipAutomaticVerificationModel tempModel = autoVeriMap.get(key);
			
			if(tempModel == null) {
				tempModel = new VipAutomaticVerificationModel() ;
				tempModel.setMerchantId(merchantIdTemp);
				tempModel.setMerchantName(judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class));
				tempModel.setPoNo(poNoTemp);
				tempModel.setCrawlerNo(billCodeTemp);
				tempModel.setCrawlerGoodsNo(goodNoTemp);
				
				String month = null ;
				
				Timestamp billDate = judgeIsNullOrNotReturnObj(map.get("bill_date"), Timestamp.class);
				Date date = new Date(billDate.getTime()) ;
				month = sdf.format(date) ;
				tempModel.setMonth(month);
				
				//默认设置为：唯品会
				tempModel.setPlatform(DERP.getLabelByKey(DERP.crawler_typeList, DERP.CRAWLER_TYPE_2));
				
				autoVeriMap.put(key, tempModel) ;
			}
			
			//设置账单验证状态为已验证
			Map<String, Object> updateMap = new HashMap<String, Object>() ;
			updateMap.put("poNo", poNoTemp) ;
			updateMap.put("merchantId", merchantIdTemp) ;
			updateMap.put("billCode", billCodeTemp) ;
			updateMap.put("crawlerGoodsNo", goodNoTemp) ;
			crawlerBillDao.updateVipVeriState(updateMap) ;
		}
		
		
		Map<String, Object> queryParams = new HashMap<String, Object>() ;
		for (String key : autoVeriMap.keySet()) {
			VipAutomaticVerificationModel tempModel = autoVeriMap.get(key);
			
			queryParams.put("merchantId", tempModel.getMerchantId()); 
			queryParams.put("poNo", tempModel.getPoNo()) ;
			queryParams.put("crawlerNo", tempModel.getCrawlerNo()) ;
			queryParams.put("crawlerGoodsNo", tempModel.getCrawlerGoodsNo()) ;
			
			List<Map<String, Object>> crawlerList = crawlerBillDao.getVipAutoVeriList(queryParams);
			
			for (Map<String, Object> map : crawlerList) {
				//汇总数量
				BigDecimal accountBD = judgeIsNullOrNotReturnObj(map.get("total_sales_qty"), BigDecimal.class) ;
				Integer account = accountBD == null ? 0 : accountBD.intValue() ;
				
				String vipVeriBillType = judgeIsNullOrNotReturnObj(map.get("vip_veri_bill_type"), String.class);
				//00-销售明细 , 设置账单销售总量
				if(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_00.equals(vipVeriBillType)) {
					tempModel.setBillSalesAccount(account);
				}
				/*//01-库存买断，设置账单买断总量 
				else if (DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_01.equals(vipVeriBillType)) {
					tempModel.setBillTakesStockAccount(account);
				}*/
				//02-库存盘亏，设置账单其他总量（调减）
				else if (DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02.equals(vipVeriBillType)) {
					tempModel.setBillAdjustmentDecreaseAccount(account);
				}
				//04-库存盘盈，设置账单其他总量（调增）
				else if (DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_04.equals(vipVeriBillType)) {
					tempModel.setBillAdjustmentIncreaseAccount(account);
				}
				//06-唯品红冲，设置账单红冲总量
				else if (DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_06.equals(vipVeriBillType)) {
					tempModel.setBillHcAccount(account);
				}
			}
			
			list.add(autoVeriMap.get(key)) ;
		}
		
		return list ;
		
	}
	
	@SuppressWarnings({ "unchecked" })
   	private <T>T judgeIsNullOrNotReturnObj(Object obj , Class<T> clazz){
       	if(obj == null) {
       		return null ;
       	}
       	
       	return (T)obj ;
    }

}
