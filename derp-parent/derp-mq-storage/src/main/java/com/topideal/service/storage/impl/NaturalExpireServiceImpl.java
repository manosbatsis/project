package com.topideal.service.storage.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.json.storage.j03.AdjustmentTypeGoodsListJson;
import com.topideal.json.storage.j03.AdjustmentTypeRootJson;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.storage.NaturalExpireService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自然过期
 * 
 * @author 杨创 2019/10/22
 */
@Service
public class NaturalExpireServiceImpl implements NaturalExpireService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NaturalExpireServiceImpl.class);
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;//类型调整单
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;//类型调整单商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品mongodb

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201205500, model = DERP_LOG_POINT.POINT_13201205500_Label,keyword="sourceCode")
	public boolean savenaturalExpire(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串 转成
		JSONObject jsonData = JSONObject.fromObject(json);
		LOGGER.info("自然过期:"+json);
		
		// 将json转成实体
		Map classMap= new HashMap<>();
		classMap.put("goodsList", AdjustmentTypeGoodsListJson.class);		
		AdjustmentTypeRootJson rootJson = (AdjustmentTypeRootJson)JSONObject.toBean(jsonData, AdjustmentTypeRootJson.class, classMap);
		
		List<AdjustmentTypeGoodsListJson> goodsList = rootJson.getGoodsList();	// 商品信息
		String merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();// 商家名称
		String topidealCode = rootJson.getTopidealCode();// 卓志编码
		String depotId = rootJson.getDepotId();// 仓库id
		String depotName = rootJson.getDepotName();// 仓库名称
		String model = rootJson.getModel();// 
		String sourceCode = rootJson.getSourceCode();//来源订单号
		String adjustmentTime = rootJson.getAdjustmentTime();// 调整时间
		String months = rootJson.getMonths();// 月份		
		String sourceTime = rootJson.getSourceTime();// 单据所属日期

		// 根据来源单号查询库存调整单
		AdjustmentTypeModel adjustmentTypeModel = new AdjustmentTypeModel();
		adjustmentTypeModel.setSourceCode((String) jsonData.get("sourceCode"));// 单据来源号
		adjustmentTypeModel = adjustmentTypeDao.searchByModel(adjustmentTypeModel);
		if (adjustmentTypeModel != null) {
			LOGGER.error("自然过期接口,类型调整单接口已经存在,单据来源号:sourceCode:" + sourceCode);
			throw new RuntimeException("自然过期接口,类型调整单接口已经存在,单据来源号:sourceCode:" + sourceCode);
		}
		// 新增类型调整单 号
		AdjustmentTypeModel adModel = new AdjustmentTypeModel();
		
		adModel.setCode(sourceCode);
		adModel.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_019);//YTZ("019","已调整"),
		adModel.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_5);// 业务类别 1.好坏品互转 2.货号变更 3效期调整,4大货理货 ,5.自然过期'
		//adModel.setAdjustmentRemark();
		adModel.setMerchantId(Long.valueOf(merchantId));
		adModel.setMerchantName(merchantName);
		adModel.setDepotId(Long.valueOf(depotId));
		adModel.setDepotName(depotName);
		
		adModel.setSourceCode(sourceCode);
		adModel.setAdjustmentTypeName("自然过期");
		adModel.setCodeTime(Timestamp.valueOf(adjustmentTime));	
		adModel.setPushTime(Timestamp.valueOf(adjustmentTime));
		adModel.setAdjustmentTime(Timestamp.valueOf(adjustmentTime));

		adModel.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01);
		adModel.setCreateUsername("接口回传");
		// 新增库存调整
		adjustmentTypeDao.save(adModel);
		
		for (AdjustmentTypeGoodsListJson adjustmentGoods : goodsList) {

			String goodsIdStr = adjustmentGoods.getGoodsId();// 商品ID
			Long goodsId=Long.valueOf(goodsIdStr);
			Map<String, Object>goodsParams = new HashMap<>();
			goodsParams.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(goodsParams);
			if (merchandiseInfoMogo==null) {
				LOGGER.error("从mongdb未查询到商品:商品id:" + goodsId);
				throw new  RuntimeException("从mongdb未查询到商品:商品id:" + goodsId);		
			}

			String batchNo = adjustmentGoods.getBatchNo();//原始批次号
			String stockType = adjustmentGoods.getStockType();//商品分类 0好品 1坏品
			String productionDate = adjustmentGoods.getProductionDate();//生产日期
			String overdueDate = adjustmentGoods.getOverdueDate();//失效日期
			Integer adjustTotal = adjustmentGoods.getAdjustTotal();// 数量
			String unit = adjustmentGoods.getUnit();//字符串 0 托盘 1箱  2 件
		
			AdjustmentTypeItemModel itemModel = new AdjustmentTypeItemModel();
			
			
			

			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			itemModel.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
			itemModel.settAdjustmentTypeId(adModel.getId());
			itemModel.setGoodsName(merchandiseInfoMogo.getName());
			itemModel.setBarcode(merchandiseInfoMogo.getBarcode());
			itemModel.setOldBatchNo(batchNo);
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(Timestamp.valueOf(productionDate+ " 00:00:00"));
			}
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(Timestamp.valueOf(overdueDate+ " 00:00:00"));
			}
			itemModel.setAdjustTotal(adjustTotal);
			
			if (DERP.INVENTORY_UNIT_0.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_00);
			}else if (DERP.INVENTORY_UNIT_1.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_01);
			}else if (DERP.INVENTORY_UNIT_2.equals(unit)) {
				itemModel.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_02);
			}

			itemModel.setIsDamage(stockType);  //是否坏品 0 好品 1坏
		
			// 新增类型调整单商品
			adjustmentTypeItemDao.save(itemModel);
						
		}
		
		return true;
	}

}
