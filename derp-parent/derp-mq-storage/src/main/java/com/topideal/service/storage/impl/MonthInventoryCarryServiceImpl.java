package com.topideal.service.storage.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.dao.AdjustmentInventoryItemDao;
import com.topideal.entity.vo.AdjustmentInventoryItemModel;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.json.storage.j01.AdjustmentInventoryGoodsListJson;
import com.topideal.json.storage.j01.AdjustmentInventoryRootJson;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.storage.MonthInventoryCarryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月库存结转
 * 
 * @author 杨创 2018/7/16
 */
@Service
public class MonthInventoryCarryServiceImpl implements MonthInventoryCarryService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MonthInventoryCarryServiceImpl.class);
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;// 库存调整单
	@Autowired
	private AdjustmentInventoryItemDao AdjustmentInventoryItemDao;// 库存调整单商品	
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品mongodb

	@Override
	@SystemServiceLog(point = "13201201600", model = "库存->月库存结转接口-已废弃",keyword="sourceCode")
	public boolean saveMonthInventoryCarryInfo(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串 转成
		JSONObject jsonData = JSONObject.fromObject(json);
		LOGGER.info("月结库存"+json);
		
		// 将json转成实体
		Map classMap= new HashMap<>();
		classMap.put("goodsList", AdjustmentInventoryGoodsListJson.class);		
		AdjustmentInventoryRootJson rootJson = (AdjustmentInventoryRootJson)JSONObject.toBean(jsonData, AdjustmentInventoryRootJson.class, classMap);
		
		List<AdjustmentInventoryGoodsListJson> goodsList = rootJson.getGoodsList();	// 商品信息
		String merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();// 商家名称
		String topidealCode = rootJson.getTopidealCode();// 卓志编码
		String depotId = rootJson.getDepotId();// 仓库id
		String depotName = rootJson.getDepotName();// 仓库名称
		String model = rootJson.getModel();// 1销毁 2 月结损益 3 其他入
		String sourceCode = rootJson.getSourceCode();//来源订单号
		String adjustmentTime = rootJson.getAdjustmentTime();// 调整时间
		String months = rootJson.getMonths();// 月份		
		String sourceTime = rootJson.getSourceTime();// 单据所属日期

		// 根据来源单号查询库存调整单
		AdjustmentInventoryModel adjustmentInventoryModel = new AdjustmentInventoryModel();
		adjustmentInventoryModel.setSourceCode((String) jsonData.get("sourceCode"));// 单据来源号
		adjustmentInventoryModel = adjustmentInventoryDao.searchByModel(adjustmentInventoryModel);
		if (adjustmentInventoryModel != null) {
			LOGGER.error("月库存结转接口,库存调整单接口已经存在,单据来源号:sourceCode:" + sourceCode);
			throw new RuntimeException("月库存结转接口,库存调整单接口已经存在,单据来源号:sourceCode:" + sourceCode);
		}
		// 新增库存调整单 号
		AdjustmentInventoryModel adModel = new AdjustmentInventoryModel();
//		adModel.setCode(CodeGeneratorUtil.getNo("KCTZ"));// 自生成		
		adModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KCTZ));// 自生成
		adModel.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020);//DTZ("020","待调整"),// 状态 库存调整单状态 020待调整 019
		adModel.setModel(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2);// 业务类别 1销毁 2 月结损益 3 其他入
		adModel.setSourceCode(sourceCode);// 来源单据号
		adModel.setMerchantId(Long.valueOf(merchantId));// 商家id
		adModel.setMerchantName(merchantName);// 商家名称
		adModel.setDepotId(Long.valueOf(depotId));// 盘点仓库id
		adModel.setDepotName(depotName);// 盘点仓库名称
		adModel.setAdjustmentTime(Timestamp.valueOf(adjustmentTime));// 调整时间
		adModel.setMonths(months);// 月份
		adModel.setSourceTime(Timestamp.valueOf(sourceTime));//单据所属日期
		adModel.setSource(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_01); //JKHC("01", "接口回传"),
		// 新增库存调整
		adjustmentInventoryDao.save(adModel);
		
		for (AdjustmentInventoryGoodsListJson adjustmentGoods : goodsList) {
			
			
			String goodsIdStr = adjustmentGoods.getGoodsId();// 商品ID
			Long goodsId=Long.valueOf(goodsIdStr);
			Map<String, Object>goodsParams = new HashMap<>();
			goodsParams.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(goodsParams);
			if (merchandiseInfoMogo==null) {
				LOGGER.error("从mongdb未查询到商品:商品id:" + goodsId);
				throw new  RuntimeException("从mongdb未查询到商品:商品id:" + goodsId);		
			}
			String goodsName = adjustmentGoods.getGoodsName();// 商品名称
			String goodsNo = adjustmentGoods.getGoodsNo();// 商品货号
			String goodsCode = adjustmentGoods.getGoodsCode();// 商品编码
			String batchNo = adjustmentGoods.getBatchNo();//原始批次号
			String barcode = merchandiseInfoMogo.getBarcode();//商品条形码(mongdb中的商品条形码)
			String commbarcode = merchandiseInfoMogo.getCommbarcode(); //标准条码
			String type = adjustmentGoods.getType();// 调整类型 //1调增 0调减 2 其他
			String stockType = adjustmentGoods.getStockType();//商品分类 0好品 1坏品
			String productionDate = adjustmentGoods.getProductionDate();//生产日期
			String overdueDate = adjustmentGoods.getOverdueDate();//失效日期
			Integer adjustTotal = adjustmentGoods.getAdjustTotal();// 数量
			String unit = adjustmentGoods.getUnit();//字符串 0 托盘 1箱  2 件
		
			AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
			itemModel.setTAdjustmentInventoryId(adModel.getId());// 库存调整id
			itemModel.setGoodsId(Long.valueOf(goodsId));// 商品id
			itemModel.setGoodsCode(goodsCode);// 商品编码
			itemModel.setGoodsNo(goodsNo);// 商品货号
			itemModel.setGoodsName(goodsName);// 商品名称
			itemModel.setBarcode(barcode);// 商品条形码
			itemModel.setCommbarcode(commbarcode); //标准条码
			itemModel.setType(type);// 调整类型 调增  
			itemModel.setOldBatchNo(batchNo);// 原始批次号
			itemModel.setIsDamage(stockType);// 是否坏品// 0:好品;1:坏品;						
			/*if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {
					Timestamp productionDateTime = TimeUtils.parse(productionDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");			
					itemModel.setProductionDate(productionDateTime);//生成日期 
				}else {
					Timestamp productionDateTime = TimeUtils.parse(productionDate, "yyyy-MM-dd HH:mm:ss");			
					itemModel.setProductionDate(productionDateTime);//生成日期 
				}
				
			}*/
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date productionDateTime = sdf.parse(productionDate);
					itemModel.setProductionDate(productionDateTime);//生成日期
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date productionDateTime = sdf.parse(productionDate);
					itemModel.setProductionDate(productionDateTime);//生成日期
				}
			}
			/*if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {
					Timestamp overdueDateTime = TimeUtils.parse(overdueDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
					itemModel.setOverdueDate(overdueDateTime);//失效日期 
				}else {
					Timestamp overdueDateTime = TimeUtils.parse(overdueDate, "yyyy-MM-dd HH:mm:ss");
					itemModel.setOverdueDate(overdueDateTime);//失效日期 
				}
				
			}	*/
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date overdueDateTime = sdf.parse(overdueDate);
					itemModel.setOverdueDate(overdueDateTime);//生成日期
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date overdueDateTime = sdf.parse(overdueDate);
					itemModel.setOverdueDate(overdueDateTime);//生成日期
				}
			}
			itemModel.setAdjustTotal(adjustTotal);// 总调整数量
			if (DERP.INVENTORY_UNIT_0.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_00);
			}else if (DERP.INVENTORY_UNIT_1.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_01);
			}else if (DERP.INVENTORY_UNIT_2.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_02);
			}
			
			// 新增库存调整单商品
			AdjustmentInventoryItemDao.save(itemModel);
						
		}
		
		return true;
	}

}
