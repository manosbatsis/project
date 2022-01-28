package com.topideal.report.service.reporting.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.dao.reporting.*;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.reporting.*;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.VipPoBillVerificationService;
import com.topideal.report.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.*;

@Service
public class VipPoBillVerificationServiceImpl implements VipPoBillVerificationService{

	@Autowired
	private VipPoBillVerificationDao vipPoBillVerificationDao ;
	
	@Autowired
	private CrawlerBillDao crawlerBillDao;
	
	@Autowired
	private DepotInfoDao depotInfoDao ;
	
	@Autowired
	private VipShelfDetailsDao vipShelfDetailsDao;
	
	@Autowired
	private VipOutDepotDetailsDao vipOutDepotDetailsDao;
	
	@Autowired
	private VipSaleReturnOdepotDetailsDao vipSaleReturnOdepotDetailsDao ;
	
	@Autowired
	private VipAdjustmentInventoryDetailsDao vipAdjustmentInventoryDetailsDao ;
	
	@Autowired
	private VipTakesStockResultsDetailsDao vipTakesStockResultsDetailsDao ;
	
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	
	@Autowired
	private VipTransferDepotDetailsDao VipTransferDepotDetailsDao ;

	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	
	/**
	 * 根据特定条件获取分页信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VipPoBillVerificationDTO listVipPoBillVeriList(VipPoBillVerificationDTO model, User user) {

		if (model.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				model.setList(new ArrayList<>());
				model.setTotal(0);
				return model;
			}
			model.setBuList(buList);
		}
		
		List<VipPoBillVerificationDTO> vipPoBillVeriList = vipPoBillVerificationDao.listVipPoBillVeriList(model) ;
		
		Integer total = vipPoBillVerificationDao.getVipPoBillVeriListCount(model) ;
		
		
		model.setList(vipPoBillVeriList);
		model.setTotal(total);
		
		return model;
	}

	
	/**
	 * 获取实体
	 * @throws SQLException 
	 */
	@Override
	public VipPoBillVerificationModel searchByModel(VipPoBillVerificationModel vipPoBillVerificationModel) throws SQLException {
		return vipPoBillVerificationDao.searchByModel(vipPoBillVerificationModel);
	}


	
	/**
	 * 获取账单客户名
	 */
	@Override
	public String getCustomerName(VipPoBillVerificationModel vipPoBillVerificationModel) throws SQLException {
		
		String customerName = crawlerBillDao.getCustomerNameByPO(vipPoBillVerificationModel.getPo());
		
		return customerName;
	}


	@Override
	public Long getVipDepotId() throws SQLException {
		DepotInfoModel depotInfoModel = new DepotInfoModel() ;
		depotInfoModel.setDepotCode("VIP001");
		depotInfoModel = depotInfoDao.searchByModel(depotInfoModel) ;
		
		return depotInfoModel.getId();
	}


	
	/**
	 * 获取上架明细
	 */
	@Override
	public List<VipShelfDetailsDTO> getShelfDetails(VipPoBillVerificationDTO vipPoBillVerificationDTO)
			throws SQLException {
		
		VipShelfDetailsDTO dto = new VipShelfDetailsDTO() ;
		dto.setPoNo(vipPoBillVerificationDTO.getPo());
		dto.setCommbarcode(vipPoBillVerificationDTO.getCommbarcode());
		dto.setMerchantId(vipPoBillVerificationDTO.getMerchantId());
		dto.setBuId(vipPoBillVerificationDTO.getBuId());
		dto.setCustomerId(vipPoBillVerificationDTO.getCustomerId());
		dto.setBuList(vipPoBillVerificationDTO.getBuList());

		List<VipShelfDetailsDTO> list = vipShelfDetailsDao.listDTO(dto);
		
		return list;
	}

	/**
	 * 销售出库明细
	 */
	@Override
	public List<VipOutDepotDetailsDTO> getSaleOutDetails(VipPoBillVerificationDTO vipPoBillVerificationDTO)
			throws SQLException {
		
		VipOutDepotDetailsDTO dto = new VipOutDepotDetailsDTO();
		dto.setPoNo(vipPoBillVerificationDTO.getPo());
		dto.setMerchantId(vipPoBillVerificationDTO.getMerchantId());
		dto.setDepotId(vipPoBillVerificationDTO.getDepotId());
		dto.setCommbarcode(vipPoBillVerificationDTO.getCommbarcode());
		dto.setBuId(vipPoBillVerificationDTO.getBuId());
		dto.setCustomerId(vipPoBillVerificationDTO.getCustomerId());
		dto.setBuList(vipPoBillVerificationDTO.getBuList());

		List<VipOutDepotDetailsDTO> list = vipOutDepotDetailsDao.listDTO(dto);
		for (VipOutDepotDetailsDTO vipOutDepotDetails : list) {
			String billType = crawlerBillDao.getBillTypeByBillCode(vipOutDepotDetails.getVipBillCode()) ;
			if(DERP_ORDER.CRAWLER_BILLTYPE_00.equals(billType)
					|| DERP_ORDER.CRAWLER_BILLTYPE_01.equals(billType)) {
				billType = DERP_ORDER.getLabelByKey(DERP_ORDER.crawler_billTypeList, billType) ;
				vipOutDepotDetails.setBillType(billType);
			}
		}

		return list;
	}

	/**
	 * 销售退货明细
	 */
	@Override
	public List<VipSaleReturnOdepotDetailsDTO> getSaleReturnOdepotDetails(
			VipPoBillVerificationDTO vipPoBillVerificationDTO) throws SQLException {
		
		VipSaleReturnOdepotDetailsDTO dto = new VipSaleReturnOdepotDetailsDTO();
		dto.setPoNo(vipPoBillVerificationDTO.getPo());
		dto.setCommbarcode(vipPoBillVerificationDTO.getCommbarcode());
		dto.setMerchantId(vipPoBillVerificationDTO.getMerchantId());
		dto.setBuId(vipPoBillVerificationDTO.getBuId());
		dto.setCustomerId(vipPoBillVerificationDTO.getCustomerId());
		dto.setBuList(vipPoBillVerificationDTO.getBuList());
		
		List<VipSaleReturnOdepotDetailsDTO> list = vipSaleReturnOdepotDetailsDao.listDTO(dto);
		
		for (VipSaleReturnOdepotDetailsDTO tempModel : list) {
			String saleReturnOdepotType = tempModel.getSaleReturnOdepotType(); 
			saleReturnOdepotType = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnOrder_returnTypeList, saleReturnOdepotType) ;
			tempModel.setSaleReturnOdepotType(saleReturnOdepotType);
		}
		
		return list;
	}


	
	/**
	 * 获取国检抽样明细
	 * @throws SQLException 
	 */
	@Override
	public List<VipAdjustmentInventoryDetailsDTO> getNationalInspectionDetails(
			VipPoBillVerificationDTO vipPoBillVerification) throws SQLException {
		
		VipAdjustmentInventoryDetailsDTO dto = new VipAdjustmentInventoryDetailsDTO();
		dto.setPoNo(vipPoBillVerification.getPo());
		dto.setMerchantId(vipPoBillVerification.getMerchantId());
		dto.setCommbarcode(vipPoBillVerification.getCommbarcode());
		dto.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5);
		dto.setBuId(vipPoBillVerification.getBuId());
		dto.setCustomerId(vipPoBillVerification.getCustomerId());
		dto.setBuList(vipPoBillVerification.getBuList());

		List<VipAdjustmentInventoryDetailsDTO> list = vipAdjustmentInventoryDetailsDao.listDTO(dto);
		for (VipAdjustmentInventoryDetailsDTO tempModel : list) {
			String adjustmentInventoryType = tempModel.getAdjustmentInventoryType(); 
			if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_0.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调减" ;
			}else if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_1.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调增" ;
			}
			
			tempModel.setAdjustmentInventoryType(adjustmentInventoryType);
		}
		
		return list;
	}
	
	/**
	 * 获取唯品红冲明细
	 * @throws SQLException 
	 */
	@Override
	public List<VipAdjustmentInventoryDetailsDTO> getVipHcDetails(
			VipPoBillVerificationDTO vipPoBillVerification) throws SQLException {

		VipAdjustmentInventoryDetailsDTO dto = new VipAdjustmentInventoryDetailsDTO();
		dto.setPoNo(vipPoBillVerification.getPo());
		dto.setMerchantId(vipPoBillVerification.getMerchantId());
		dto.setCommbarcode(vipPoBillVerification.getCommbarcode());
		dto.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4);
		dto.setBuId(vipPoBillVerification.getBuId());
		dto.setCustomerId(vipPoBillVerification.getCustomerId());
		dto.setBuList(vipPoBillVerification.getBuList());

		List<VipAdjustmentInventoryDetailsDTO> list = vipAdjustmentInventoryDetailsDao.listDTO(dto);
		
		for (VipAdjustmentInventoryDetailsDTO tempModel : list) {
			String adjustmentInventoryType = tempModel.getAdjustmentInventoryType(); 
			if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_0.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调减" ;
			}else if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_1.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调增" ;
			}
			
			tempModel.setAdjustmentInventoryType(adjustmentInventoryType);
		}
		
		return list;
	}


	@Override
	public List<VipPoBillVerificationModel> searchByIds(String idsStr) {
		
		String[] ids = idsStr.split(",") ;
		
		return vipPoBillVerificationDao.searchByIds(Arrays.asList(ids));
	}


	
	/**
	 * 根据标准条码获取商品名称
	 * @param merchantId 
	 * @throws SQLException 
	 */
	@Override
	public String getGoodsNameByCommbarcode(String commbarcode, Long merchantId) throws SQLException {
		
		MerchandiseInfoModel model = new MerchandiseInfoModel() ;
		model.setCommbarcode(commbarcode);
		model.setMerchantId(merchantId);
		model.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		List<MerchandiseInfoModel> list = merchandiseInfoDao.list(model);
		
		String goodsName = "" ;
		
		if(!list.isEmpty()) {
			goodsName = list.get(0).getName() ;
		}
		
		return goodsName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VipPoBillVerificationDTO listVipPoBillVeriPoList(VipPoBillVerificationDTO model) {

		List<VipPoBillVerificationDTO> vipPoBillVeriList = vipPoBillVerificationDao.listVipPoBillVeriPoList(model) ; 
		
		Integer total = vipPoBillVerificationDao.getVipPoBillVeriPoListCount(model) ;
		
		
		model.setList(vipPoBillVeriList);
		model.setTotal(total);
		
		return model;
	}


	@Override
	public Integer countUnsettledAccount(VipPoBillVerificationModel model) throws Exception {
		model.setStatus(DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_0);
		return vipPoBillVerificationDao.countUnsettledAccount(model);
	}


	@Override
	public Integer modifyStatus(VipPoBillVerificationModel model) throws Exception{
		
		model.setStatus(DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_1);
		model.setOverDate(TimeUtils.getNow());
		
		return vipPoBillVerificationDao.modifyStatus(model) ;
	}


	@Override
	public List<VipPoBillVerificationModel> getListByPo(Map<String , Object> map) {
		
		String pos = String.valueOf(map.get("pos")) ;
		
		String[] poArr = pos.split(",");
		
		List<String> list = Arrays.asList(poArr);
		
		map.put("pos", list) ;
		
		return vipPoBillVerificationDao.getListByPo(map);
	}


	@Override
	public List<VipAdjustmentInventoryDetailsDTO> getVipScrapDetails(
			VipPoBillVerificationDTO vipPoBillVerification) throws SQLException {
		VipAdjustmentInventoryDetailsDTO dto = new VipAdjustmentInventoryDetailsDTO();
		dto.setPoNo(vipPoBillVerification.getPo());
		dto.setMerchantId(vipPoBillVerification.getMerchantId());
		dto.setCommbarcode(vipPoBillVerification.getCommbarcode());
		dto.setModel(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6);
		dto.setBuId(vipPoBillVerification.getBuId());
		dto.setCustomerId(vipPoBillVerification.getCustomerId());
		dto.setBuList(vipPoBillVerification.getBuList());

		List<VipAdjustmentInventoryDetailsDTO> list = vipAdjustmentInventoryDetailsDao.listDTO(dto);
		
		for (VipAdjustmentInventoryDetailsDTO tempModel : list) {
			String adjustmentInventoryType = tempModel.getAdjustmentInventoryType(); 
			if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_0.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调减" ;
			}else if(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_1.equals(adjustmentInventoryType)) {
				adjustmentInventoryType = "调增" ;
			}
			
			tempModel.setAdjustmentInventoryType(adjustmentInventoryType);
		}
		
		return list;
	}


	@Override
	public List<VipTakesStockResultsDetailsDTO> getVipTakesStockResultsDetails(
			VipPoBillVerificationDTO vipPoBillVerification) throws SQLException {

		VipTakesStockResultsDetailsDTO dto = new VipTakesStockResultsDetailsDTO();
		dto.setPoNo(vipPoBillVerification.getPo());
		dto.setMerchantId(vipPoBillVerification.getMerchantId());
		dto.setCommbarcode(vipPoBillVerification.getCommbarcode());
		dto.setBuId(vipPoBillVerification.getBuId());
		dto.setCustomerId(vipPoBillVerification.getCustomerId());
		dto.setBuList(vipPoBillVerification.getBuList());

		List<VipTakesStockResultsDetailsDTO> list = vipTakesStockResultsDetailsDao.listDTO(dto);
	
		for (VipTakesStockResultsDetailsDTO vipTakesStockResultsDetails : list) {
			String type = vipTakesStockResultsDetails.getTakesStockResultType() ;
		
			if(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1.equals(type)) {
				vipTakesStockResultsDetails.setTakesStockResultType("调增");
			}else if(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2.equals(type)) {
				vipTakesStockResultsDetails.setTakesStockResultType("调减");
			}
		}
		
		return list ;
	}


    @Override
    public String createExcel(FileTaskMongo task, String basePath) throws Exception {

        long before = System.currentTimeMillis();

        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        VipPoBillVerificationDTO dto = (VipPoBillVerificationDTO) JSONObject.toBean(jsonData, VipPoBillVerificationDTO.class);


		List<Long> buList=null;
		String userId="";
		if (task.getUserId()!=null) {
			userId="/"+task.getUserId();
			buList = userBuRelMongoDao.getBuListByUser(task.getUserId());
		}

		basePath = basePath + "/" + task.getTaskType() + "/" + task.getMerchantId() + userId;


		if (dto.getBuId() == null) {
			dto.setBuList(buList);
		}

		dto.setPageSize(0);
        // 商品总表响应结果集
		List<VipPoBillVerificationDTO> mainResult = vipPoBillVerificationDao.listVipPoBillVeriList(dto) ;
		
		//上架明细显示条目
		List<VipShelfDetailsDTO> sheetShelfDetails = this.getShelfDetails(dto);
		
		//销售出库明细
		List<VipOutDepotDetailsDTO> sheetOutDepotDetails = this.getSaleOutDetails(dto);
		
		//商品销售退出库明细
		List<VipSaleReturnOdepotDetailsDTO> sheetSaleReturnOdepotDetails = this.getSaleReturnOdepotDetails(dto);
		 
		//国检抽样明细
		List<VipAdjustmentInventoryDetailsDTO> sheetNationSampleDetails = this.getNationalInspectionDetails(dto);
		
		//唯品红冲明细
		List<VipAdjustmentInventoryDetailsDTO> sheetVipHCDetails = this.getVipHcDetails(dto);
		
		//唯品报废明细
		List<VipAdjustmentInventoryDetailsDTO> sheetVipScrapDetails = this.getVipScrapDetails(dto);
		
		//唯品盘点明细
		List<VipTakesStockResultsDetailsDTO> sheetVipStockResultsDetails = this.getVipTakesStockResultsDetails(dto) ;
		
		//唯品调拨明细
		List<VipTransferDepotDetailsDTO> sheetVipTransferDetails = this.getVipTransferDetails (dto) ;
		
		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		//唯品by po核销总表
		String mainSheetName = "唯品by po核销总表" ;
		String[] mainKey = { "merchantName" , "buName", "po" ,"customerName", "poDate" , "commbarcode", "goodsName", "brandParent", "superiorParentBrand", "currency", "salePrice", "unsettledAccount", "inventoryAmount",
				"shelfTotalAccount" ,"shelfTotalAmount", "shelfDamagedAccount", "firstShelfDate" , "billTotalAccount" , "billRecentDate" , "outDepotTotalAccont" ,
				"nationalInspectionSampleAccount" , "saleReturnAccount" , "vipHcAccount" , "inventorySurplusAccount" , "inventoryDeficientAccount" ,"scrapAccount" , "transferInAccount", "transferOutAccount", "days" , "modifyDate"} ;
		String[] mainColumns = { "商家", "事业部", "PO号","客户名称", "PO时间", "标准条码", "商品名称", "标准品牌", "母品牌", "销售币种", "销售单价", "库存量" ,"库存金额",
				"上架总量", "上架总金额","上架残损量", "首次上架时间", "账单总量", "最近账单时间" ,"销售出库总量" ,"国检抽样" ,"销售退数量" ,"唯品红冲数量" ,"盘盈数量" , "盘亏数量" ,"唯品报废数量" , "调拨入库", "调拨出库", "天数" ,"更新时间"};
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKey, mainResult);
		sheetList.add(mainSheet) ;
		
		//商品上架明细
		String shelfSheetName = "商品上架明细" ;
		String[] shelfColumns = { "商家", "PO单号", "PO单时间", "标准条码" ,"商品名称", "商品货号", "销售订单号", "销售数量", "销售单价", "上架数量" , "残损数量" , "少货数量" , "上架时间"};
		String[] shelfKeys = {"merchantName" , "poNo" , "poDate" , "commbarcode" , "goodsName" , "goodsNo" , "orderNo" , "num" , "salePrice", "shelfNum" , "damagedNum" , "lackNum" , "shelfDate"} ;
		ExportExcelSheet shelfSheet = ExcelUtilXlsx.createSheet(shelfSheetName, shelfColumns, shelfKeys, sheetShelfDetails);
		sheetList.add(shelfSheet) ;
		
		//商品销售出库明细
		String odepotSheetName = "商品销售出库明细" ;
		String[] odepotColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "销售出库单号", "唯品账单号", "账单类型", "销售订单号" , "商品货号" , "出库数量" , "出库时间"};
		String[] odepotKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName" , "saleOutOrder" ,"vipBillCode" , "billType" , "saleOrder" , "goodsNo" , "outDepotNum" , "outDepotDate"} ;
		ExportExcelSheet odepotSheet = ExcelUtilXlsx.createSheet(odepotSheetName, odepotColumns, odepotKeys, sheetOutDepotDetails );
		sheetList.add(odepotSheet) ;
		
		//商品销售退出库明细
		String returnSheetName = "商品销售退出库明细" ;
		String[] returnColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "销售退订单号", "销售退出库单号", "退货类型", "商品货号" , "退货数量" , "退货时间"};
		String[] returnKeys = {"merchantName" , "poNo", "commbarcode" , "goodsName" , "saleReturnOrder" ,"saleReturnOdepotOrder" , "saleReturnOdepotType" , "goodsNo" , "saleReturnOdepotNum" , "saleReturnOdepotDate"} ;
		ExportExcelSheet returnSheet = ExcelUtilXlsx.createSheet(returnSheetName, returnColumns, returnKeys, sheetSaleReturnOdepotDetails);
		sheetList.add(returnSheet) ;
		
		//商品国检抽样明细
		String nationalSheetName = "商品国检抽样明细" ;
		String[] nationalColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "库存调整单号", "商品货号", "调整类型", "调整数量" , "归属月份" , "调整时间"};
		String[] nationalKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName"  , "adjustmentInventoryOrder" , "goodsNo" , "adjustmentInventoryType" , "adjustmentInventoryNum" , "adjustmentInventoryMonths" , "adjustmentInventoryDate"} ;
		ExportExcelSheet nationalSheet = ExcelUtilXlsx.createSheet(nationalSheetName, nationalColumns, nationalKeys, sheetNationSampleDetails);
		sheetList.add(nationalSheet) ;
		
		//唯品红冲明细明细
		String vipHCSheetName = "唯品红冲明细" ;
		String[] vipHCColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "库存调整单号", "唯品账单号" ,"商品货号", "调整类型", "调整数量" , "归属月份" , "调整时间"};
		String[] vipHCKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName"  , "adjustmentInventoryOrder" , "sourceCode" ,"goodsNo" , "adjustmentInventoryType" , "adjustmentInventoryNum" , "adjustmentInventoryMonths" , "adjustmentInventoryDate"} ;
		ExportExcelSheet vipHCSheet = ExcelUtilXlsx.createSheet(vipHCSheetName, vipHCColumns, vipHCKeys, sheetVipHCDetails);
		sheetList.add(vipHCSheet) ;
		
		//唯品报废明细
		String vipScrapSheetName = "唯品报废明细" ;
		String[] vipScrapColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "库存调整单号", "唯品账单号" ,"商品货号", "调整类型", "调整数量" , "归属月份" , "调整时间"};
		String[] vipScrapKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName"  , "adjustmentInventoryOrder" , "sourceCode" ,"goodsNo" , "adjustmentInventoryType" , "adjustmentInventoryNum" , "adjustmentInventoryMonths" , "adjustmentInventoryDate"} ;
		ExportExcelSheet vipScrapSheet = ExcelUtilXlsx.createSheet(vipScrapSheetName, vipScrapColumns, vipScrapKeys, sheetVipScrapDetails);
		sheetList.add(vipScrapSheet) ;
		
		//唯品盘点明细
		String vipStockResultsSheetName = "唯品盘点明细" ;
		String[] vipStockResultsColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "盘点结果单号", "唯品账单号" ,"商品货号", "调整类型", "调整数量" , "接收时间"};
		String[] vipStockResultsKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName"  , "takesStockResultOrder" , "sourceCode" ,"goodsNo" , "takesStockResultType" , "takesStockResultNum" , "receiveTime"} ;
		ExportExcelSheet vipStockResultsSheet = ExcelUtilXlsx.createSheet(vipStockResultsSheetName, vipStockResultsColumns, vipStockResultsKeys, sheetVipStockResultsDetails);
		sheetList.add(vipStockResultsSheet) ;
		
		//唯品调拨明细
		String vipTransferSheetName = "唯品调拨明细" ;
		String[] vipTransferColumns = { "商家", "PO单号", "标准条码" ,"商品名称", "调拨出/入库单号", "调拨类型" ,"商品货号", "调拨数量" , "调拨时间"};
		String[] vipTransferKeys = {"merchantName" , "poNo" , "commbarcode" , "goodsName"  , "transferDepotOrder" , "transferType" ,"goodsNo" , "transferNum" , "transferTime" } ;
		ExportExcelSheet vipTransferSheet = ExcelUtilXlsx.createSheet(vipTransferSheetName, vipTransferColumns, vipTransferKeys, sheetVipTransferDetails);
		sheetList.add(vipTransferSheet) ;
		
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
		
		//删除目录下的子文件
	   	 DownloadExcelUtil.deleteFile(basePath);
	   	//创建目录
	   	new File(basePath).mkdirs();
	   	//写出文件
    	String fileName = dto.getMerchantName() + "唯品PO核销明细表 .xlsx";
    	FileOutputStream fileOut=new FileOutputStream(basePath+"/"+fileName);
    	wb.write(fileOut);
        fileOut.close();
        
        long end = System.currentTimeMillis();
        
        System.out.println("耗时：" + (end - before));
		
        return basePath;
	}


	/**
	 * 生成唯品调拨明细
	 * @param vipPoBillVerification
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<VipTransferDepotDetailsDTO> getVipTransferDetails(VipPoBillVerificationDTO vipPoBillVerification) throws SQLException {
		VipTransferDepotDetailsDTO dto = new VipTransferDepotDetailsDTO();
		dto.setPoNo(vipPoBillVerification.getPo());
		dto.setMerchantId(vipPoBillVerification.getMerchantId());
		dto.setCommbarcode(vipPoBillVerification.getCommbarcode());
		dto.setBuId(vipPoBillVerification.getBuId());
		dto.setCustomerId(vipPoBillVerification.getCustomerId());
		dto.setBuList(vipPoBillVerification.getBuList());
		
		List<VipTransferDepotDetailsDTO> list = VipTransferDepotDetailsDao.listDTO(dto);
	
		for (VipTransferDepotDetailsDTO vipTransferDepotDetails : list) {
			String type = vipTransferDepotDetails.getTransferType() ;
		
			type = DERP_REPORT.getLabelByKey(DERP_REPORT.vipTransDetails_transferTypeList, type) ;

			vipTransferDepotDetails.setTransferType(type);
		}
		
		return list ;
	}


	@Override
	public Map<String, Object> getDataTime(Long merchantId) {
		return vipPoBillVerificationDao.getDataTime(merchantId);
	}

}
