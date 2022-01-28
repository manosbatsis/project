package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOrderItemDao;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.dao.sale.MaterialOutDepotItemDao;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.json.api.v5_8.ESOutInventoryGoodsListJson;
import com.topideal.json.api.v5_8.ESOutInventoryRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.LLDOutInventoryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库明细回推实现类 —— 领料单 生成出库单
 */
@Service
public class LLDOutInventoryServiceImpl implements LLDOutInventoryService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LLDOutInventoryServiceImpl.class);
	// 领料订单
	@Autowired
	private MaterialOrderDao materialOrderDao;
	// 领料单表体
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao;
	// 领料出库单
	@Autowired
	private MaterialOutDepotDao materialOutDepotDao;
	// 领料出库订单商品
	@Autowired
	private MaterialOutDepotItemDao materialOutDepotItemDao;

	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	//订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120003, model = DERP_LOG_POINT.POINT_12203120003_Label,keyword="orderCode")
	public boolean saveOutDepotInfo(String json,String keys,String topics,String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		
		Map classMap = new HashMap();
		classMap.put("goodsList",ESOutInventoryGoodsListJson.class);
		// JSON对象转实体
		ESOutInventoryRootJson rootJson = (ESOutInventoryRootJson) JSONObject.toBean(jsonData, ESOutInventoryRootJson.class, classMap);
				
		String orderCode = rootJson.getOrderCode();// 订单号或者LBX号
		//String deliver = rootJson.getDeliverDate();//必填
		Long merchantId = rootJson.getMerchantId();// 商家ID		

		// 领料 
		MaterialOrderModel materialOrderModel = new MaterialOrderModel();
		materialOrderModel.setMerchantId(merchantId);
		materialOrderModel.setCode(orderCode);
		materialOrderModel = materialOrderDao.searchByModel(materialOrderModel);
		if (materialOrderModel == null) {
			LOGGER.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}
		if (DERP_ORDER.MATERIALORDER_STATE_001.equals(materialOrderModel.getState()) || DERP_ORDER.MATERIALORDER_STATE_018.equals(materialOrderModel.getState())) {
			LOGGER.error("订单状态为“待审核/已出库”,订单编号" + orderCode);
			throw new RuntimeException("订单状态为“待审核/已出库”,订单编号" + orderCode);
		}
		if(null == materialOrderModel.getBuId()){
			LOGGER.error("领料订单编号" + materialOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("领料订单编号" + materialOrderModel.getCode()+"事业部的值为空");
		}
		// 根据仓库id查询 仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", materialOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo==null ) {
			LOGGER.error("根据领料订单的仓库id没有查询到仓库,订单号:" + orderCode);
			throw new RuntimeException("根据领料订单的仓库id没有查询到仓库,订单号:" + orderCode);
		}
		if (DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_04.equals(mongo.getISVDepotType())) {
			LOGGER.error("出库仓ISV仓库类型为海外仓不走该流程" + orderCode);
			throw new RuntimeException("出库仓ISV仓库类型为海外仓不走该流程" + orderCode);
		}
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (ESOutInventoryGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
				}				
			}
		}
		
		//领料单编码查询领料出库单
		MaterialOutDepotModel MaterialOutDepotModel = new MaterialOutDepotModel();
		MaterialOutDepotModel.setMaterialOrderCode(orderCode);// 领料出库清单编码
		MaterialOutDepotModel = materialOutDepotDao.searchByModel(MaterialOutDepotModel);
		if (MaterialOutDepotModel != null) {
			LOGGER.error("领料出库清单已经存在,订单号" + orderCode);
			throw new RuntimeException("领料出库清单已经存在,订单号" + orderCode);
		}
		// 查询领料单商品
		MaterialOrderItemModel tempMaterialOrderItemModel = new MaterialOrderItemModel();
		tempMaterialOrderItemModel.setOrderId(materialOrderModel.getId());
		List<MaterialOrderItemModel> MaterialOrderItemList = materialOrderItemDao.list(tempMaterialOrderItemModel);
		Map<Long, MaterialOrderItemModel> kwGoodsMap = new HashMap<>();//库位商品 key=库位商品id value=单据商品
		for(MaterialOrderItemModel model:MaterialOrderItemList) {
			kwGoodsMap.put(model.getGoodsId(),model);
		}

		// 新增向领料出库
		MaterialOutDepotModel mOutDepotModel = new MaterialOutDepotModel();
		mOutDepotModel.setMaterialOrderId(materialOrderModel.getId());// 领料订单id
		mOutDepotModel.setMerchantId(materialOrderModel.getMerchantId());// 商家ID
		mOutDepotModel.setPoNo(materialOrderModel.getPoNo());// PO号
		mOutDepotModel.setOutDepotId(materialOrderModel.getOutDepotId());// 调出仓库id
		mOutDepotModel.setOutDepotName(materialOrderModel.getOutDepotName()); // 调出仓库名称
		mOutDepotModel.setCustomerId(materialOrderModel.getCustomerId());// 客户id(供应商)
		mOutDepotModel.setCustomerName(materialOrderModel.getCustomerName());// 客户名称
		//mOutDepotModel.setDeliverDate(deliverDate);// 发货时间
		mOutDepotModel.setStatus(DERP_ORDER.MATERIALOUTDEPOT_STATUS_017);// '状态 017,待出库 ,018,已出库,027:出库中
		mOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LLCK));// 出库清单编号自生成
		mOutDepotModel.setMerchantName(materialOrderModel.getMerchantName());// 商家名称
		mOutDepotModel.setMaterialOrderCode(materialOrderModel.getCode());// 领料单编号
		mOutDepotModel.setOutExternalCode(rootJson.getExternalCode());// 外部单号
		mOutDepotModel.setCurrency(materialOrderModel.getCurrency());//币种
		mOutDepotModel.setOrderSource("2");// 1手工导入 2.接口回传
		mOutDepotModel.setBuId(materialOrderModel.getBuId());// 事业部
		mOutDepotModel.setBuName(materialOrderModel.getBuName());
		// 新增
		materialOutDepotDao.save(mOutDepotModel);

		for(ESOutInventoryGoodsListJson goods : rootJson.getGoodsList()) {
			// 获取领料订单商品信息
			MaterialOrderItemModel MaterialOrderItemModel = kwGoodsMap.get(goods.getGoodsId());

			// 新增领料出库表体
			MaterialOutDepotItemModel itemModel = new MaterialOutDepotItemModel();
			itemModel.setMaterialOutDepotId(mOutDepotModel.getId());// 领料出库ID
			itemModel.setGoodsId(goods.getGoodsId());// 商品id
			itemModel.setGoodsCode(goods.getGoodsCode());// 商品编码
			itemModel.setGoodsName(goods.getGoodsName());// 商品名称
			itemModel.setGoodsNo(goods.getGoodsNo());// 商品货号
			if ("0".equals(goods.getStockType())) {//0：好品  ,1：坏品
				itemModel.setTransferNum(goods.getNum());//好品数量
			} else {
				itemModel.setWornNum(goods.getNum());// 坏品数量
			}
			itemModel.setTransferBatchNo(goods.getBatchNo());// 调拨批次
			itemModel.setBarcode(goods.getBarcode());// 货品条形码
			itemModel.setCommbarcode(goods.getCommbarcode());    // 标准条码
			if (MaterialOrderItemModel != null) {
				itemModel.setMaterialNum(MaterialOrderItemModel.getNum());
			}
			String productionDate = goods.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(TimeUtils.StringToDate(productionDate));//生产日期
			}
			String overdueDate = goods.getOverdueDate();//生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(TimeUtils.StringToDate(overdueDate));//失效日期
			}
			// 新增领料出库表体
			materialOutDepotItemDao.save(itemModel);            
		}
		
		// 修改领料出库单
		MaterialOrderModel sModel = new MaterialOrderModel();
		sModel.setState(DERP_ORDER.MATERIALORDER_STATE_017);
		sModel.setId(materialOrderModel.getId());
		materialOrderDao.modify(sModel);
		return true;
	}
	
}
