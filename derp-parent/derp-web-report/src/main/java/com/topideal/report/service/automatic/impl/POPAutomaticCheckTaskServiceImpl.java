package com.topideal.report.service.automatic.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excelReader.ExcelReader;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.dao.order.WayBillItemDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.POPAutomaticJDDTO;
import com.topideal.entity.dto.POPAutomaticTM2DTO;
import com.topideal.entity.dto.POPAutomaticTMDTO;
import com.topideal.entity.dto.POPAutomaticZHYCDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.entity.vo.order.OrderItemModel;
import com.topideal.entity.vo.order.OrderModel;
import com.topideal.entity.vo.order.WayBillItemModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.report.service.automatic.POPAutomaticCheckTaskService;
import com.topideal.report.tools.DownloadExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * POP核对任务
 * 
 * @author wy
 */
@Service
public class POPAutomaticCheckTaskServiceImpl implements POPAutomaticCheckTaskService {
	// 仓库Mongo
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	// 运单表体
	@Autowired
	private WayBillItemDao wayBillItemDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;

	@Override
	public String createExcel(AutomaticCheckTaskModel model, String basePath) throws Exception {

		// 获取源文件
		List<Map<String, String>> sheetList = ExcelUtilXlsx.parseSheetOne(model.getSourcePath());
		String fileName = null;

		// 平台为天猫（天猫出仓仓库为天运二期仓）
		if (DERP.STOREPLATFORM_1000000310.equals(model.getStorePlatformCode())
						&& "ERPWMS_360_0104".equals(model.getOutDepotCode())) {
			
			fileName = veriTM2CheckTask(model, sheetList, basePath);
			
		}
		// 出仓仓库为综合一仓时
		else if ("ERPWMS_360_0402".equals(model.getOutDepotCode())) { 
			
			fileName = veriZHYCCheckTask(model, sheetList, basePath);
			
		}
		// 平台为天猫且出仓仓库为非天运二期仓 和 京东平台
		else if (DERP.STOREPLATFORM_100044998.equals(model.getStorePlatformCode()) ||	
				(DERP.STOREPLATFORM_1000000310.equals(model.getStorePlatformCode())
				&& !"ERPWMS_360_0104".equals(model.getOutDepotCode()))) { 
			
			fileName = veriJDAndTMCheckTask(model, sheetList, basePath);
			
		}

		return fileName;
	}

	/**
	 * 平台为天猫（天猫出仓仓库为天运二期仓）自动校验
	 * 
	 * @param data
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	private String veriTM2CheckTask(AutomaticCheckTaskModel model, List<Map<String, String>> sheetList, String basePath)
			throws Exception {

		String mainSheetName = null;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String fileName = null;
		List<POPAutomaticTM2DTO> tm2DtoList = new ArrayList<POPAutomaticTM2DTO>();

		Long merchantId = model.getMerchantId();
		String outDepotCode = model.getOutDepotCode();
		Timestamp checkStartDate = model.getCheckStartDate();
		Timestamp checkEndDate = model.getCheckEndDate();
		
		Map<String, POPAutomaticTM2DTO> tmMap = new HashMap<String, POPAutomaticTM2DTO>() ;
		
		/**按必填字段合并数量*/
		for (int j = 1; j <= sheetList.size(); j++) {
			Map<String, String> map = sheetList.get(j - 1);
			POPAutomaticTM2DTO tm2Dto = excelMapExchangeTM2Dto(map);
			
			/**
			 * 当出仓仓库为综合一仓时, 取导入表格中字段为“商品货号”、“交易数量”、“原始批次号”、“平台订单号”、“发货时间”、
			 * 五个必填字段；若缺少必填字段，导入失败并提示
			 */
			String goodsNo = tm2Dto.getGoodsNo();
			if (StringUtils.isBlank(goodsNo)) {
				setExceptionMsg(tm2Dto, tm2DtoList, "是", "货品id要必填", null);
				continue;
			}

			Integer goodsNum = tm2Dto.getGoodsNum();
			if (goodsNum == null) {
				setExceptionMsg(tm2Dto, tm2DtoList, "是", "商品数量要必填", null);
				continue;
			}

			String tradeOrderNo = tm2Dto.getTradeOrderNo();
			if (StringUtils.isBlank(tradeOrderNo)) {
				setExceptionMsg(tm2Dto, tm2DtoList, "是", "交易订单号要必填", null);
				continue;
			}

			Timestamp deliverDate = tm2Dto.getDeliverDate();
			if (deliverDate == null) {
				setExceptionMsg(tm2Dto, tm2DtoList, "是", "发货时间要必填", null);
				continue;
			}
			
			String key = goodsNo + "_" + tradeOrderNo + "_" + TimeUtils.formatFullTime(deliverDate) ;
			
			POPAutomaticTM2DTO tempDto = tmMap.get(key);
			
			if(tempDto == null) {
				tempDto = tm2Dto ;
			}else {
				tempDto.setGoodsNum(tm2Dto.getGoodsNum() + tempDto.getGoodsNum());
			}
			
			tmMap.put(key, tempDto) ;
		}
		
		for (POPAutomaticTM2DTO dto : tmMap.values()) {
			// 根据导入的平台订单号获取该电商订单
			OrderModel orderModelParam = new OrderModel();
			orderModelParam.setExternalCode(dto.getTradeOrderNo());
			orderModelParam.setMerchantId(merchantId);
			List<OrderModel> orderList = orderDao.list(orderModelParam);
			
			// 过滤已删除的订单
			orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
					.collect(Collectors.toList()) ;

			/** 系统找不到该外部交易单号时，“是否异常”标识为“是”、差异原因提示：系统查无此外部交易单号；*/
			if (orderList.isEmpty()) {
				setExceptionMsg(dto, tm2DtoList, "是", "系统查无此外部交易单号", null);
				continue;
				
			}

			if (orderList.size() > 1) {
				setExceptionMsg(dto, tm2DtoList, "是", "系统根据该外部交易单号找到了多条电商订单", null);
				continue;
			}
			
			OrderModel orderModel = orderList.get(0);

			// 获取导入的仓库信息
			Map<String, Object> depotMap = new HashMap<String, Object>();
			depotMap.put("depotId", orderModel.getDepotId());
			DepotInfoMongo depotOrder = depotInfoMongoDao.findOne(depotMap);

			if (!outDepotCode.equals(depotOrder.getDepotCode())) {
				setExceptionMsg(dto, tm2DtoList, "是", "订单仓库与选择校验仓库不一致", null);
				continue;
			}
			
			if(TimeUtils.daysBetween(orderModel.getDeliverDate(), checkStartDate) > 0
					|| TimeUtils.daysBetween(checkEndDate, orderModel.getDeliverDate()) > 0) {
				
				setExceptionMsg(dto, tm2DtoList, "是", "订单发货时间不在核对范围内", null);
				continue;
				
			}
			
			Map<String, Object> sumItemMap = new HashMap<String, Object>() ;
			sumItemMap.put("orderId", orderModel.getId()) ;
			sumItemMap.put("goodsNo", dto.getGoodsNo()) ;
			
			OrderItemModel sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
			
			if(sumItem == null) {
				
				OrderItemModel queryModel = new OrderItemModel() ;
				queryModel.setOrderId(orderModel.getId());
				List<OrderItemModel> orderItemList = orderItemDao.list(queryModel);
				
				StringBuffer sb = new StringBuffer() ;
				
				for (OrderItemModel orderItemModel : orderItemList) {
					if(sb.length() > 0) {
						sb.append(",") ;
					}
					
					sb.append(orderItemModel.getSkuId()) ;
				}
				
				setExceptionMsg(dto, tm2DtoList, "是", "电商订单表体不存在导入商品信息", sb.toString());
				continue;
			}
			
			if(sumItem.getNum().compareTo(dto.getGoodsNum()) != 0) {
				setExceptionMsg(dto, tm2DtoList, "是", "电商订单表体销售量与导入商品数量不一致", sumItem.getSkuId());
				continue;
			}
			
			if (!DERP_ORDER.ORDER_STATUS_4.equals(orderModel.getStatus())) {
				String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, orderModel.getStatus());
				setExceptionMsg(dto, tm2DtoList, "是", "单据状态不是已发货，而是：" + statusLabel, sumItem.getSkuId());
				continue;
			}
			
			setExceptionMsg(dto, tm2DtoList, "否", null, sumItem.getSkuId());
		}
		
		mainSheetName = "天猫（天猫出仓仓库为天运二期仓）核对结果表";
		String[] mainKey = { "tradeOrderNo", "goodsNo", "skuId", "goodsNum", "deliverDate", "isException", "excetionResult",
				"shopName", "depotName", "makingTime", "payTime", "status", "logisticsNo", "depotOrderNo",
				"shippingCompany", "waybillNo", "buyerName", "receiverName", "province", "city", "area", "street",
				"phone", "orderPrice", "wayFrtFee", "goodsCode", "goodsId", "merchantsCode", "goodsName", "taxFee",
				"goodsTaxFee" };
		
		String[] mainColumns = { "交易订单号", "货品id", "映射SKU_ID","商品数量", "发货时间", "是否异常", "异常原因", 
				"店铺名称", "仓库名称", "下单时间", "支付时间",
				"订单状态", "物流订单号", "仓库订单号", "配送公司", "配送运单号", "买家昵称", 
				"收件人", "省", "市", "区", "街道", "手机", "订单金额", "运费",
				"货品编码", "商品id", "商家编码", "商品名称", "税费", "商品包税金额" };
		
		wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, tm2DtoList);
		// 写出文件
		fileName = basePath + "/SJXY/" + model.getTaskCode() + "/天猫（天猫出仓仓库为天运二期仓）核对结果表 .xlsx";

		basePath = basePath += "/SJXY/" + model.getTaskCode();
		// 删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		// 创建目录
		new File(basePath).mkdirs();
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();

		return fileName;
	}

	/**
	 * 出仓仓库为综合一仓时 自动校验
	 * 
	 * @param data
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	private String veriZHYCCheckTask(AutomaticCheckTaskModel model, List<Map<String, String>> sheetList, String basePath)
			throws Exception {

		String mainSheetName = null;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String fileName = null;

		List<POPAutomaticZHYCDTO> zhycDtoList = new ArrayList<POPAutomaticZHYCDTO>();

		Long merchantId = model.getMerchantId();
		String outDepotCode = model.getOutDepotCode();
		Timestamp checkStartDate = model.getCheckStartDate();
		Timestamp checkEndDate = model.getCheckEndDate();
		
		Map<String, POPAutomaticZHYCDTO> zhycMap = new HashMap<>() ;
		
		/**按必填字段合并数量*/
		for (int j = 1; j <= sheetList.size(); j++) {
			Map<String, String> map = sheetList.get(j - 1);
			POPAutomaticZHYCDTO zhycDto = excelMapExchangeZHYCDto(map);
			
			/**
			 * 当出仓仓库为综合一仓时, 取导入表格中字段为“商品货号”、“交易数量”、“原始批次号”、“平台订单号”、“发货时间”、
			 * 五个必填字段；若缺少必填字段，导入失败并提示
			 */
			String goodsNo = zhycDto.getGoodsNo();
			if (StringUtils.isBlank(goodsNo)) {
				setExceptionMsg(zhycDto, zhycDtoList, "是", "商品货号要必填");
				continue;
			}

			Integer goodsNum = zhycDto.getNum();
			if (goodsNum == null) {
				setExceptionMsg(zhycDto, zhycDtoList, "是", "交易数量要必填");
				continue;
			}

			String originalBatchNo = zhycDto.getOriginalBatchNo();
			if (StringUtils.isBlank(originalBatchNo)) {
				setExceptionMsg(zhycDto, zhycDtoList, "是", "原始批次号要必填");
				continue;
			}

			String externalCode = zhycDto.getExternalCode();
			if (StringUtils.isBlank(externalCode)) {
				setExceptionMsg(zhycDto, zhycDtoList, "是", "平台订单号要必填");
				continue;
			}

			Timestamp deliverDate = zhycDto.getDeliverDate();
			if (deliverDate == null) {
				setExceptionMsg(zhycDto, zhycDtoList, "是", "发货时间要必填");
				continue;
			}
			
			String key = goodsNo + "_" + originalBatchNo + "_" + externalCode + "_"
					+ TimeUtils.formatFullTime(deliverDate) ;
			
			POPAutomaticZHYCDTO tempDto = zhycMap.get(key);
			
			if(tempDto == null) {
				tempDto = zhycDto ;
			}else {
				tempDto.setNum(tempDto.getNum() + zhycDto.getNum());
			}
			
			zhycMap.put(key, tempDto) ;
		}
		
		for (POPAutomaticZHYCDTO dto : zhycMap.values()) {
			
			// 根据导入的平台订单号获取该电商订单
			OrderModel orderModelParam = new OrderModel();
			orderModelParam.setExternalCode(dto.getExternalCode());
			orderModelParam.setMerchantId(merchantId);
			List<OrderModel> orderList = orderDao.list(orderModelParam);
			
			// 过滤已删除的订单
			orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
					.collect(Collectors.toList()) ;

			/** 系统找不到该外部交易单号时，“是否异常”标识为“是”、差异原因提示：系统查无此外部交易单号；*/
			if (orderList.isEmpty()) {
				setExceptionMsg(dto, zhycDtoList, "是", "系统查无此外部交易单号");
				continue;
				
			}

			if (orderList.size() > 1) {
				setExceptionMsg(dto, zhycDtoList, "是", "系统根据该外部交易单号找到了多条电商订单");
				continue;
			}
			
			OrderModel orderModel = orderList.get(0);

			// 获取导入的仓库信息
			Map<String, Object> depotMap = new HashMap<String, Object>();
			depotMap.put("depotId", orderModel.getDepotId());
			DepotInfoMongo depotOrder = depotInfoMongoDao.findOne(depotMap);

			if (!outDepotCode.equals(depotOrder.getDepotCode())) {
				setExceptionMsg(dto, zhycDtoList, "是", "订单仓库与选择校验仓库不一致");
				continue;
			}
			
			if(TimeUtils.daysBetween(orderModel.getDeliverDate(), checkStartDate) > 0
					|| TimeUtils.daysBetween(checkEndDate, orderModel.getDeliverDate()) > 0) {
				
				setExceptionMsg(dto, zhycDtoList, "是", "订单发货时间不在核对范围内");
				continue;
				
			}
			
			Map<String, Object> sumItemMap = new HashMap<String, Object>() ;
			sumItemMap.put("orderId", orderModel.getId()) ;
			sumItemMap.put("goodsNo", dto.getGoodsNo()) ;
			
			OrderItemModel sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
			
			if(sumItem == null) {
				setExceptionMsg(dto, zhycDtoList, "是", "电商订单表体不存在导入商品信息");
				continue;
			}
			
			if(sumItem.getNum().compareTo(dto.getNum()) != 0) {
				setExceptionMsg(dto, zhycDtoList, "是", "电商订单表体销售量与导入商品数量不一致");
				continue;
			}
			
			sumItemMap.put("batchNo", dto.getOriginalBatchNo()) ;
			WayBillItemModel wayItem = wayBillItemDao.getWayBillItemByOrder(sumItemMap) ;
			
			if(wayItem == null) {
				setExceptionMsg(dto, zhycDtoList, "是", "原始批次号不一致");
				continue;
			}
			
			if(wayItem.getNum().compareTo(dto.getNum()) != 0) {
				setExceptionMsg(dto, zhycDtoList, "是", "电商订单运单表体原始批次号销售量与导入商品数量不一致");
				continue;
			}
			
			if (!DERP_ORDER.ORDER_STATUS_4.equals(orderModel.getStatus())) {
				String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, orderModel.getStatus());
				setExceptionMsg(dto, zhycDtoList, "是", "单据状态不是已发货，而是：" + statusLabel);
				continue;
			}
			
			setExceptionMsg(dto, zhycDtoList, "否", null);
		}

		mainSheetName = "综合1仓核对结果表";
		String[] mainKey = { "goodsNo", "num", "originalBatchNo", "externalCode", "deliverDate", "isException",
				"excetionResult", "merchantName", "depotName", "goodsOPGNo", "goodsName", "tradeType", "unit",
				"businessNo", "productionDate", "overdueDate", "tradeCreateDate", "deliverNo", "externalSystemNo" };
		String[] mainColumns = { "商品货号", "交易数量", "原始批次号", "平台订单号", "发货时间", "是否异常", "异常原因", "商家名称", "仓库名称", "商品OPG号",
				"商品名称", "交易类型", "单位", "业务单据号", "生产日期", "失效日期", "交易创建日期", "发货单号", "外部系统单号" };
		wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, zhycDtoList);
		// 写出文件
		fileName = basePath + "/SJXY/" + model.getTaskCode() + "/综合1仓核对结果表 .xlsx";

		basePath = basePath += "/SJXY/" + model.getTaskCode();
		// 删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		// 创建目录
		new File(basePath).mkdirs();
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();

		return fileName;
	}

	/**
	 * 平台为天猫且出仓仓库为非天运二期仓 和 京东平台
	 * 
	 * @param data
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	private String veriJDAndTMCheckTask(AutomaticCheckTaskModel model, List<Map<String, String>> sheetList, String basePath)
			throws Exception {
	
		String mainSheetName = null;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		String fileName = null;
		
		List<POPAutomaticTMDTO> tmDtoList = new ArrayList<POPAutomaticTMDTO>();
		List<POPAutomaticJDDTO> jdDtoList = new ArrayList<POPAutomaticJDDTO>();
		
		Long merchantId = model.getMerchantId();
		String outDepotCode = model.getOutDepotCode();
		Timestamp checkStartDate = model.getCheckStartDate();
		Timestamp checkEndDate = model.getCheckEndDate();
		String storePlatformCode = model.getStorePlatformCode();
		
		//查询是否卓烨
		MerchantInfoModel merchant = merchantInfoDao.searchById(merchantId);
		boolean isZyMerchant = false ;
		
		if("0000134".equals(merchant.getTopidealCode())) {
			isZyMerchant = true ;
		}
		
		// 当平台为天猫（出仓仓库非天运二期仓）时
		if (DERP.STOREPLATFORM_1000000310.equals(storePlatformCode) && !"ERPWMS_360_0104".equals(outDepotCode)) {
			
			Map<String, POPAutomaticTMDTO> tmMap = new HashMap<>() ;
			
			for (int j = 1; j <= sheetList.size(); j++) {
				Map<String, String> map = sheetList.get(j - 1);
				POPAutomaticTMDTO tmDto = excelMapExchangeTMDto(map);
				
				String goodsCode = tmDto.getGoodsCode();
				if (StringUtils.isBlank(goodsCode)) {
					setExceptionMsg(tmDto, tmDtoList, "是", "货品编码要必填", null);
					continue;
				}
				
				String externalCode = tmDto.getExternalCode();
				if (StringUtils.isBlank(externalCode)) {
					setExceptionMsg(tmDto, tmDtoList, "是", "交易订单号要必填", null);
					continue;
				}
				
				Integer outInNum = tmDto.getOutInNum();
				if(outInNum == null) {
					setExceptionMsg(tmDto, tmDtoList, "是", "商品数量要必填", null);
					continue;
				}
				
				Timestamp deliverDate = tmDto.getOutInDepotTime();
				if (deliverDate == null) {
					setExceptionMsg(tmDto, tmDtoList, "是", "发货时间要必填", null);
					continue;
				}
				
				String key = goodsCode + "_" + externalCode + "_" + TimeUtils.formatFullTime(deliverDate) ;
				
				POPAutomaticTMDTO tempDto = tmMap.get(key);
				
				if(tempDto == null) {
					tempDto = tmDto ;
				}else {
					tempDto.setOutInNum(tempDto.getOutInNum() + tmDto.getOutInNum());
				}
				
				tmMap.put(key, tempDto) ;
			}
			
			for (POPAutomaticTMDTO dto : tmMap.values()) {
				// 根据导入的平台订单号获取该电商订单
				OrderModel orderModelParam = new OrderModel();
				orderModelParam.setExternalCode(dto.getExternalCode());
				orderModelParam.setMerchantId(merchantId);
				List<OrderModel> orderList = orderDao.list(orderModelParam);
				
				// 过滤已删除的订单
				orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
						.collect(Collectors.toList()) ;
	
				/** 系统找不到该外部交易单号时，“是否异常”标识为“是”、差异原因提示：系统查无此外部交易单号；*/
				if (orderList.isEmpty()) {
					setExceptionMsg(dto, tmDtoList, "是", "系统查无此外部交易单号", null);
					continue;
					
				}
	
				if (orderList.size() > 1) {
					setExceptionMsg(dto, tmDtoList, "是", "系统根据该外部交易单号找到了多条电商订单", null);
					continue;
				}
				
				OrderModel orderModel = orderList.get(0);
	
				// 获取导入的仓库信息
				Map<String, Object> depotMap = new HashMap<String, Object>();
				depotMap.put("depotId", orderModel.getDepotId());
				DepotInfoMongo depotOrder = depotInfoMongoDao.findOne(depotMap);
	
				if (!outDepotCode.equals(depotOrder.getDepotCode())) {
					setExceptionMsg(dto, tmDtoList, "是", "订单仓库与选择校验仓库不一致", null);
					continue;
				}
				
				if(TimeUtils.daysBetween(orderModel.getDeliverDate(), checkStartDate) > 0
						|| TimeUtils.daysBetween(checkEndDate, orderModel.getDeliverDate()) > 0) {
					
					setExceptionMsg(dto, tmDtoList, "是", "订单发货时间不在核对范围内", null);
					continue;
					
				}
				
				/**
				 * 导入的订单为卓烨商家，需根据导入的货品编码查询是否以原条形码存在于库位映射表中，
				 * 并找到导入的货品编码对应关联的库位条形码，
				 * 只要系统订单商品条码与导入的货品编码查找到的库位条形码中存在至少一个库位条形码对碰得上即可
				 */
				OrderItemModel sumItem = null ;
				
				Map<String, Object> sumItemMap = new HashMap<String, Object>() ;
				sumItemMap.put("orderId", orderModel.getId()) ;
				
				if(isZyMerchant) {
					
					MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
					queryModel.setBarcode(dto.getBarcode());
					queryModel.setMerchantId(merchantId);
					
					List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(queryModel) ;
					
					if(merchandiseList.isEmpty()) {
						setExceptionMsg(dto, tmDtoList, "是", "根据条形码：" + dto.getBarcode() + "查询商品不存在", null);
						continue ;
					}
					
					for (MerchandiseInfoModel merchandise : merchandiseList) {
						/**先以原货号ID查询电商订单表体*/
						Map<String, Object> queryMap = new HashMap<>() ;
						queryMap.put("originalGoodsNo", merchandise.getId()) ;
						
						sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
						
						/**若为空，以库位货号ID查询电商订单表体*/
						if(sumItem == null) {
							queryMap.remove("originalGoodsNo") ;
							
							queryMap.put("goodsNo", merchandise.getGoodsNo()) ;
							
							sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
						}
						
						if(sumItem != null) {
							break ;
						}
					}
					
				}else {
					sumItemMap.put("barcode", dto.getGoodsCode()) ;
					
					sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
				}
				
				if(sumItem == null) {
					
					OrderItemModel queryModel = new OrderItemModel() ;
					queryModel.setOrderId(orderModel.getId());
					List<OrderItemModel> orderItemList = orderItemDao.list(queryModel);
					
					StringBuffer sb = new StringBuffer() ;
					
					for (OrderItemModel orderItemModel : orderItemList) {
						if(sb.length() > 0) {
							sb.append(",") ;
						}
						
						sb.append(orderItemModel.getSkuId()) ;
					}
					
					setExceptionMsg(dto, tmDtoList, "是", "电商订单表体不存在导入商品信息", sb.toString());
					
					continue;
				}
				
				if(sumItem.getNum().compareTo(dto.getOutInNum()) != 0) {
					setExceptionMsg(dto, tmDtoList, "是", "电商订单表体销售量与导入商品数量不一致", sumItem.getSkuId());
					continue;
				}
				
				if (!DERP_ORDER.ORDER_STATUS_4.equals(orderModel.getStatus())) {
					String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, orderModel.getStatus());
					setExceptionMsg(dto, tmDtoList, "是", "单据状态不是已发货，而是：" + statusLabel, sumItem.getSkuId());
					continue;
				}
				
				setExceptionMsg(dto, tmDtoList, "否", null, sumItem.getSkuId());
			}
			
			mainSheetName = "平台天猫(天猫出仓仓库非天运二期仓)核对结果表" ;
			String[] mainKey = { "externalCode" , "goodsCode" , "skuId" ,"outInNum" ,"outInDepotTime" ,"isException", "excetionResult" , "code" , "lpCode" , "barcode", "goodsName" , "depotName" ,  
										"orderType" , "inventoryType" , "balanceAmount", "erpNo"} ;
			String[] mainColumns = { "交易订单号","货品编码", "skuId", "商品数量" ,"发货时间", "是否异常","异常原因","单号", "LP单号", "商品条形码","货品名称", "仓库名称",  "单据类型" ,"库存类型" ,"结存数量" ,
										"ERP订单号"};
			wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, tmDtoList) ;
			
			fileName = basePath + "/SJXY/" + model.getTaskCode() + "/平台天猫(天猫出仓仓库非天运二期仓)核对结果表 .xlsx";
			
		}else if(DERP.STOREPLATFORM_100044998.equals(storePlatformCode)) {
			
			Map<String, POPAutomaticJDDTO> jdMap = new HashMap<>() ;
			
			for (int j = 1; j <= sheetList.size(); j++) {
				Map<String, String> map = sheetList.get(j - 1);
				POPAutomaticJDDTO jdDto = excelMapExchangeJDDto(map);
				
				String barcode = jdDto.getBarcode();
				if (StringUtils.isBlank(barcode)) {
					setExceptionMsg(jdDto, jdDtoList, "是", "条形码要必填", null);
					continue;
				}
				
				String saleOrderCode = jdDto.getSaleOrderCode();
				if (StringUtils.isBlank(saleOrderCode)) {
					setExceptionMsg(jdDto, jdDtoList, "是", "销售平台订单号要必填", null);
					continue;
				}
				
				Integer outDepotNum = jdDto.getOutDepotNum();
				if(outDepotNum == null) {
					setExceptionMsg(jdDto, jdDtoList, "是", "实际出库数量要必填", null);
					continue;
				}
				
				String key = barcode + "_" + saleOrderCode  ;
				
				POPAutomaticJDDTO tempDto = jdMap.get(key);
				
				if(tempDto == null) {
					tempDto = jdDto ;
				}else {
					tempDto.setOutDepotNum(tempDto.getOutDepotNum() + jdDto.getOutDepotNum());
				}
				
				jdMap.put(key, tempDto) ;
			}
			
			for (POPAutomaticJDDTO dto : jdMap.values()) {
				// 根据导入的平台订单号获取该电商订单
				OrderModel orderModelParam = new OrderModel();
				orderModelParam.setExternalCode(dto.getSaleOrderCode());
				orderModelParam.setMerchantId(merchantId);
				List<OrderModel> orderList = orderDao.list(orderModelParam);
				
				// 过滤已删除的订单
				orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
						.collect(Collectors.toList()) ;
	
				/** 系统找不到该外部交易单号时，“是否异常”标识为“是”、差异原因提示：系统查无此外部交易单号；*/
				if (orderList.isEmpty()) {
					setExceptionMsg(dto, jdDtoList, "是", "系统查无此外部交易单号", null);
					continue;
					
				}
	
				if (orderList.size() > 1) {
					setExceptionMsg(dto, jdDtoList, "是", "系统根据该外部交易单号找到了多条电商订单", null);
					continue;
				}
				
				OrderModel orderModel = orderList.get(0);
	
				// 获取导入的仓库信息
				Map<String, Object> depotMap = new HashMap<String, Object>();
				depotMap.put("depotId", orderModel.getDepotId());
				DepotInfoMongo depotOrder = depotInfoMongoDao.findOne(depotMap);
	
				if (!outDepotCode.equals(depotOrder.getDepotCode())) {
					setExceptionMsg(dto, jdDtoList, "是", "订单仓库与选择校验仓库不一致", null);
					continue;
				}
				
				/**
				 * 导入的订单为卓烨商家，需根据导入的货品编码查询是否以原条形码存在于库位映射表中，
				 * 并找到导入的货品编码对应关联的库位条形码，
				 * 只要系统订单商品条码与导入的货品编码查找到的库位条形码中存在至少一个库位条形码对碰得上即可
				 */
				OrderItemModel sumItem = null ;
				
				Map<String, Object> sumItemMap = new HashMap<String, Object>() ;
				sumItemMap.put("orderId", orderModel.getId()) ;
				
				if(isZyMerchant) {
					
					MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
					queryModel.setBarcode(dto.getBarcode());
					queryModel.setMerchantId(merchantId);
					
					List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(queryModel) ;
					
					if(merchandiseList.isEmpty()) {
						setExceptionMsg(dto, jdDtoList, "是", "根据条形码：" + dto.getBarcode() + "查询商品不存在", null);
						continue ;
					}
					
					for (MerchandiseInfoModel merchandise : merchandiseList) {
						/**先以原货号ID查询电商订单表体*/
						Map<String, Object> queryMap = new HashMap<>() ;
						queryMap.put("originalGoodsNo", merchandise.getId()) ;
						
						sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
						
						/**若为空，以库位货号ID查询电商订单表体*/
						if(sumItem == null) {
							queryMap.remove("originalGoodsNo") ;
							
							queryMap.put("goodsNo", merchandise.getGoodsNo()) ;
							
							sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
						}
						
						if(sumItem != null) {
							break ;
						}
					}
					
				}else {
					sumItemMap.put("barcode", dto.getBarcode()) ;
					
					sumItem = orderItemDao.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
				}
				
				if(sumItem == null) {
					
					OrderItemModel queryModel = new OrderItemModel() ;
					queryModel.setOrderId(orderModel.getId());
					List<OrderItemModel> orderItemList = orderItemDao.list(queryModel);
					
					StringBuffer sb = new StringBuffer() ;
					
					for (OrderItemModel orderItemModel : orderItemList) {
						if(sb.length() > 0) {
							sb.append(",") ;
						}
						
						sb.append(orderItemModel.getSkuId()) ;
					}
					
					setExceptionMsg(dto, jdDtoList, "是", "电商订单表体不存在导入商品信息", sb.toString());
					
					continue;
				}
				
				if(sumItem.getNum().compareTo(dto.getOutDepotNum()) != 0) {
					setExceptionMsg(dto, jdDtoList, "是", "电商订单表体销售量与导入商品数量不一致", sumItem.getSkuId());
					continue;
				}
				
				if (!DERP_ORDER.ORDER_STATUS_4.equals(orderModel.getStatus())) {
					String statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_statusList, orderModel.getStatus());
					setExceptionMsg(dto, jdDtoList, "是", "单据状态不是已发货，而是：" + statusLabel, sumItem.getSkuId());
					continue;
				}
				
				setExceptionMsg(dto, jdDtoList, "否", null, sumItem.getSkuId());
			}
			
			mainSheetName = "平台京东核对结果表" ;
			String[] mainKey = { "saleOrderCode" , "barcode" , "skuId" ,"outDepotNum" ,"isException", "excetionResult" } ;
			String[] mainColumns = { "销售平台订单号","条形码", "skuId", "实际出库数量" , "是否异常","异常原因"};
			wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, mainColumns, mainKey, jdDtoList) ;
			
			fileName = basePath + "/SJXY/" + model.getTaskCode() + "/京东平台核对结果表 .xlsx";
		}
		
		basePath = basePath += "/SJXY/" + model.getTaskCode();
		// 删除目录下的子文件
		DownloadExcelUtil.deleteFile(basePath);
		// 创建目录
		new File(basePath).mkdirs();
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();
	
		return fileName ;
	
	}

	/**
	 * 综合一仓设置错误信息
	 * 
	 * @param zhycDto
	 * @param zhycDtoList
	 * @param isException
	 * @param exceptionMsg
	 */
	private void setExceptionMsg(POPAutomaticZHYCDTO zhycDto, List<POPAutomaticZHYCDTO> zhycDtoList, String isException,
			String exceptionMsg) {
		
		zhycDto.setIsException(isException);
		zhycDto.setExcetionResult(exceptionMsg);
		
		zhycDtoList.add(zhycDto);
	}

	/**
	 * 京东平台设置错误信息
	 * @param tm2Dto
	 * @param tm2DtoList
	 * @param isException
	 * @param exceptionMsg
	 * @param amountDifference
	 * @param skuId
	 */
	private void setExceptionMsg(POPAutomaticJDDTO jdDto, List<POPAutomaticJDDTO> jdDtoList, String isException, String exceptionMsg,
			String skuId) {
		
		jdDto.setIsException(isException);
		jdDto.setExcetionResult(exceptionMsg);
		jdDto.setSkuId(skuId);
		
		jdDtoList.add(jdDto);
	}
	
	/**
	 * 平台为天猫（天猫出仓仓库为天运二期仓）设置错误信息
	 * @param tm2Dto
	 * @param tm2DtoList
	 * @param isException
	 * @param exceptionMsg
	 * @param amountDifference
	 * @param skuId
	 */
	private void setExceptionMsg(POPAutomaticTM2DTO tm2Dto, List<POPAutomaticTM2DTO> tm2DtoList, String isException, String exceptionMsg,
			String skuId) {
		
		tm2Dto.setIsException(isException);
		tm2Dto.setExcetionResult(exceptionMsg);
		tm2Dto.setSkuId(skuId);
		
		tm2DtoList.add(tm2Dto);
	}
	
	private void setExceptionMsg(POPAutomaticTMDTO tmDto, List<POPAutomaticTMDTO> tmDtoList, 
			String isException, String exceptionMsg, String skuId) {
		
			tmDto.setIsException(isException);
			tmDto.setExcetionResult(exceptionMsg);
			tmDto.setSkuId(skuId);
			
			tmDtoList.add(tmDto);
	}

	/**
	 * excel 行转成京东DTO
	 * 
	 * @param map
	 * @return
	 */
	private POPAutomaticJDDTO excelMapExchangeJDDto(Map<String, String> map) {
		POPAutomaticJDDTO dto = new POPAutomaticJDDTO();
		dto.setSaleOrderCode(map.get("销售平台订单号"));
		dto.setBarcode(map.get("条形码"));

		String numStr = map.get("实际出库数量");
		if (StringUtils.isNotBlank(numStr)) {
			dto.setOutDepotNum(Integer.valueOf(numStr));
		}
		
		return dto;
	}

	/**
	 * excel 行转成天猫出仓仓库为天运二期仓DTO
	 * 
	 * @param map
	 * @return
	 */
	private POPAutomaticTM2DTO excelMapExchangeTM2Dto(Map<String, String> map) {
		POPAutomaticTM2DTO dto = new POPAutomaticTM2DTO();
		dto.setShopName(map.get("店铺名称"));
		dto.setDepotName(map.get("仓库名称"));
		dto.setStatus(map.get("订单状态"));
		dto.setLogisticsNo(map.get("物流订单号"));
		dto.setTradeOrderNo(map.get("交易订单号"));
		dto.setDepotOrderNo(map.get("仓库订单号"));
		dto.setShippingCompany(map.get("配送公司"));
		dto.setShippingCompany(map.get("配送运单号"));
		dto.setBuyerName(map.get("买家昵称"));
		dto.setReceiverName(map.get("收件人"));
		dto.setProvince(map.get("省"));
		dto.setCity(map.get("市"));
		dto.setArea(map.get("区"));
		dto.setStreet(map.get("街道"));
		dto.setPhone(map.get("手机"));
		dto.setGoodsNo(map.get("货品id"));
		dto.setGoodsCode(map.get("货品编码"));
		dto.setGoodsId(map.get("商品id"));
		dto.setMerchantsCode(map.get("商家编码"));
		dto.setGoodsName(map.get("商品名称"));

		String numStr = map.get("商品数量");
		if (StringUtils.isNotBlank(numStr)) {
			dto.setGoodsNum(Integer.valueOf(numStr));
		}

		String makeTimeStr = map.get("下单时间");
		Timestamp makingTime = null;
		if (StringUtils.isNotBlank(makeTimeStr)) {
			makingTime = TimeUtils.parseFullTime(makeTimeStr);
		}
		dto.setMakingTime(makingTime);

		String payTimeStr = map.get("支付时间");
		Timestamp payTime = null;
		if (StringUtils.isNotBlank(payTimeStr)) {
			payTime = TimeUtils.parseFullTime(payTimeStr);
		}
		dto.setPayTime(payTime);

		String deliverDateStr = map.get("发货时间");
		Timestamp deliverDate = null;
		if (StringUtils.isNotBlank(deliverDateStr)) {
			deliverDate = TimeUtils.parseFullTime(deliverDateStr);
		}
		dto.setDeliverDate(deliverDate);

		dto.setDeliverDate(deliverDate);
		String orderPriceStr = map.get("订单金额");
		if (StringUtils.isNotBlank(orderPriceStr)) {
			dto.setOrderPrice(new BigDecimal(orderPriceStr));
		}
		String wayFrtFeeStr = map.get("运费");
		if (StringUtils.isNotBlank(wayFrtFeeStr)) {
			dto.setWayFrtFee(new BigDecimal(wayFrtFeeStr));
		}
		String taxFeeStr = map.get("税费");
		if (StringUtils.isNotBlank(taxFeeStr)) {
			dto.setTaxFee(new BigDecimal(taxFeeStr));
		}
		String goodsTaxFeeStr = map.get("商品包税金额");
		if (StringUtils.isNotBlank(goodsTaxFeeStr)) {
			dto.setGoodsTaxFee(new BigDecimal(goodsTaxFeeStr));
		}
		return dto;
	}

	/**
	 * excel 行转成出仓仓库为综合一仓DTO
	 * 
	 * @param map
	 * @return
	 */
	private POPAutomaticZHYCDTO excelMapExchangeZHYCDto(Map<String, String> map) {
		POPAutomaticZHYCDTO dto = new POPAutomaticZHYCDTO();
		dto.setGoodsName(map.get("商品名称"));
		dto.setDepotName(map.get("仓库名称"));
		dto.setGoodsNo(map.get("商品货号"));
		dto.setGoodsOPGNo(map.get("商品OPG号"));
		dto.setGoodsName(map.get("商品名称"));
		dto.setTradeType(map.get("交易类型"));
		dto.setUnit(map.get("单位"));
		dto.setBusinessNo(map.get("业务单据号"));
		dto.setOriginalBatchNo(map.get("原始批次号"));
		dto.setExternalCode(map.get("平台订单号"));
		dto.setDeliverNo(map.get("发货单号"));
		dto.setExternalSystemNo(map.get("外部系统单号"));

		String numStr = map.get("交易数量");
		if (StringUtils.isNotBlank(numStr)) {
			dto.setNum(Integer.valueOf(numStr));
		}
		String tradeCreateDateStr = map.get("交易创建时间");
		Timestamp tradeCreateDateTime = null;
		if (StringUtils.isNotBlank(tradeCreateDateStr)) {
			tradeCreateDateTime = TimeUtils.parseFullTime(tradeCreateDateStr);
		}
		dto.setTradeCreateDate(tradeCreateDateTime);

		String deliverDateStr = map.get("发货时间");
		Timestamp deliverDate = null;
		if (StringUtils.isNotBlank(deliverDateStr)) {
			deliverDate = TimeUtils.parseFullTime(deliverDateStr);
		}
		dto.setDeliverDate(deliverDate);

		String productionDateStr = map.get("生产日期");
		Timestamp productionDate = null;
		if (StringUtils.isNotBlank(productionDateStr)) {
			productionDate = TimeUtils.parseDay(productionDateStr);
		}
		dto.setProductionDate(productionDate);

		String overdueDateStr = map.get("失效日期");
		Timestamp overdueDate = null;
		if (StringUtils.isNotBlank(overdueDateStr)) {
			overdueDate = TimeUtils.parseDay(overdueDateStr);
		}
		dto.setOverdueDate(overdueDate);
		return dto;
	}

	/**
	 * excel 行转成天猫（天猫出仓仓库不为天运二期仓）DTO
	 * 
	 * @param map
	 * @return
	 */
	private POPAutomaticTMDTO excelMapExchangeTMDto(Map<String, String> map) {
		POPAutomaticTMDTO dto = new POPAutomaticTMDTO();
		dto.setCode(map.get("单号"));
		dto.setLpCode(map.get("LP单号"));
		dto.setBarcode(map.get("商品条形码"));
		dto.setGoodsCode(map.get("货品编码"));
		dto.setGoodsName(map.get("货品名称"));
		dto.setDepotName(map.get("仓库名称"));
		dto.setOrderType(map.get("单据类型"));
		dto.setInventoryType(map.get("库存类型"));
		dto.setErpNo(map.get("ERP订单号"));
		dto.setExternalCode(map.get("交易订单号"));

		String outInDepotTimeStr = map.get("发货时间");
		Timestamp outInDepotTime = null;
		if (StringUtils.isNotBlank(outInDepotTimeStr)) {
			outInDepotTime = TimeUtils.parseFullTime(outInDepotTimeStr);
		}
		dto.setOutInDepotTime(outInDepotTime);

		String outInNum = map.get("商品数量");
		if (StringUtils.isNotBlank(outInNum)) {
			Integer outInNumInt = Integer.valueOf(outInNum);
			dto.setOutInNum(Math.abs(outInNumInt));
		}
		String balanceAmount = map.get("结存数量");
		if (StringUtils.isNotBlank(balanceAmount)) {
			dto.setBalanceAmount(Integer.valueOf(balanceAmount));
		}
		return dto;
	}

}
