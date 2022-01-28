/*
package com.topideal.service.api.sale.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ZPJsonGoodsDTO;
import com.topideal.entity.dto.common.ZPOrderItemDTO;
import com.topideal.entity.dto.common.ZPTempItemDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.InventoryLocationMappingMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.tools.ZPUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.json.api.v2_7.SaleReturnTransfersOutStorageGoodsListJson;
import com.topideal.json.api.v2_7.SaleReturnTransfersOutStorageRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.XSTTransfersOutStorageService;

import net.sf.json.JSONObject;
*/
/**
 * 调拨出库接口
 *//*

@Service
public class XSTTransfersOutStorageServiceImpl implements XSTTransfersOutStorageService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersOutStorageServiceImpl.class);
	
	@Autowired 
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired 
	private SaleReturnOdepotDao saleReturnOdepotDao;//销售退货出库	
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;// 销售退货出库商品	
	@Autowired 
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	@Autowired
	private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

	@Override
	@SystemServiceLog(point="12203120901",model="调拨出库回推接口",keyword="orderCode")
	public boolean saveTransfersOutStorageInfo(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleReturnTransfersOutStorageGoodsListJson.class);
		// JSON对象转实体
		SaleReturnTransfersOutStorageRootJson rootJson = (SaleReturnTransfersOutStorageRootJson) JSONObject.toBean(jsonData, SaleReturnTransfersOutStorageRootJson.class, classMap);
		
		String orderCode = rootJson.getOrderCode();// 订单号		
		//获取是否菜鸟字段(1-是，0-否)
		String isRookie = rootJson.getIsRookie();
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setMerchantId(rootJson.getMerchantId());
		if ("0".equals(isRookie)) {// 销售退货订单 (非菜鸟)
			saleReturnOrderModel.setCode(orderCode);
			saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		}else if("1".equals(isRookie)){// 来自销售退货订单 (菜鸟)
			saleReturnOrderModel.setLbxNo(orderCode);
			saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		}else{
			LOGGER.error("isRookie有误,isRookie:" + isRookie);
			throw new RuntimeException("isRookie有误,isRookie:" + isRookie);
		}
		
		if (saleReturnOrderModel == null) {
			LOGGER.error("没有查询到对应的订单,订单号:" + orderCode);
			throw new RuntimeException("没有查询到对应的订单,订单号:" + orderCode);
		}
		if (DERP_ORDER.SALERETURNORDER_STATUS_001.equals(saleReturnOrderModel.getStatus())) {
			LOGGER.error("订单状态为“待审核”,订单编号" + saleReturnOrderModel.getCode());
			throw new RuntimeException("订单状态为“待审核”,订单编号" + saleReturnOrderModel.getCode());
		}
		if(null == saleReturnOrderModel.getBuId()){
			LOGGER.error("销售退货订单,订单编号" + saleReturnOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售退货订单,订单编号" + saleReturnOrderModel.getCode()+"事业部的值为空");
		}

		// 查询销售退商品
		SaleReturnOrderItemModel saleReturnOrderItemModel = new SaleReturnOrderItemModel();
		saleReturnOrderItemModel.setOrderId(saleReturnOrderModel.getId());
		List<SaleReturnOrderItemModel> itemList = saleReturnOrderItemDao.list(saleReturnOrderItemModel);
		Map<Long, SaleReturnOrderItemModel> goodsMap = new HashMap<>();//原商品 key=原商品id value=单据商品
		Map<Long, SaleReturnOrderItemModel> kwGoodsMap = new HashMap<>();//库位商品 key=库位商品id value=单据商品 以入定出用
		for(SaleReturnOrderItemModel model:itemList) {
			if(model.getOutOriginalGoodsId()!=null){
				goodsMap.put(model.getOutOriginalGoodsId(), model);
			}else{
				goodsMap.put(model.getOutGoodsId(), model);
			}
			kwGoodsMap.put(model.getOutGoodsId(),model);
		}
		//检查报文里的商品在订单中是否存在
		for(SaleReturnTransfersOutStorageGoodsListJson goods : rootJson.getGoodsList()) {
			SaleReturnOrderItemModel orderItem = goodsMap.get(goods.getGoodsId());
			if(orderItem == null) {
				LOGGER.error("销售退货单中未找到商品,订单编号:" + orderCode+",原货号:"+goods.getGoodsNo());
				throw new RuntimeException("销售退货单中未找到商品,订单编号:" + orderCode+",原货号:"+goods.getGoodsNo());
			}
		}

		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleReturnOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo outDepotMongo = depotInfoMongoDao.findOne(depotInfo_params);
		// 批次效期强校验：1-是 0-否
		if ("1".equals(outDepotMongo.getBatchValidation())) {
			for (SaleReturnTransfersOutStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(outDepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(outDepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
				}
				
			}
		}

		String outExternalCode = rootJson.getCode();// 调拨单号(入库单和出库单)
		String yModel = rootJson.getModel();//业务场景 账册内货权转移/账册内货权转移调仓 必填			
		String serveTypes = rootJson.getServeTypes();//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务'必填,
		String transfer = rootJson.getTransferDate();
		Timestamp transferDate=null;//调拨时间
		if (StringUtils.isNotBlank(transfer)) {			
			if (transfer.length()==10) {
				transferDate=Timestamp.valueOf(transfer+" 00:00:00");	//调拨时间
			}else {
				transferDate=Timestamp.valueOf(transfer);//调拨时间
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(transferDate);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(transferDate);//归属月份

		// 查询销售退货出库表
		SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
		if ("0".equals(isRookie)) {// 销售订单 (非菜鸟)
			saleReturnOdepotModel.setOrderCode(orderCode);// 销售退货的订单号 订单号		
			saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
		}else{// 来自销售 (菜鸟)		
			saleReturnOdepotModel.setLbxNo(orderCode);//销售退货出库表lbx单号			
			saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
		}
		if(saleReturnOdepotModel != null){
			LOGGER.error("销售退货出库单已经存在,订单号:" + orderCode);
			throw new RuntimeException("销售退货出库单已经存在,订单号:" + orderCode);
		}
			
		// 新增销售退货出库表 saleReturnOrderModel
		SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
		sReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());//销售退货ID
		sReturnOdepotModel.setMerchantId(saleReturnOrderModel.getMerchantId());//商家ID
		sReturnOdepotModel.setMerchantName(saleReturnOrderModel.getMerchantName());//商家名称
		sReturnOdepotModel.setContractNo(saleReturnOrderModel.getContractNo());//合同号
		sReturnOdepotModel.setInDepotId(saleReturnOrderModel.getInDepotId());//退入仓库id
		sReturnOdepotModel.setInDepotName(saleReturnOrderModel.getInDepotName());//退入仓库名称
		sReturnOdepotModel.setOutDepotId(saleReturnOrderModel.getOutDepotId());//退出仓库id
		sReturnOdepotModel.setOutDepotName(saleReturnOrderModel.getOutDepotName());//退出仓库名称		
		sReturnOdepotModel.setReturnOutDate(transferDate);//退货出库时间
		sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);//"011","待入仓",012","已入仓""027","出库中"
//		sReturnOdepotModel.setCode(CodeGeneratorUtil.getNo("XSTC"));//销售退货出库单号
		sReturnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTC));//销售退货出库单号
		sReturnOdepotModel.setOrderCode(saleReturnOrderModel.getCode());//销售退货编号
		sReturnOdepotModel.setMerchantReturnNo(saleReturnOrderModel.getMerchantReturnNo());//企业退运单号
		sReturnOdepotModel.setInspectNo(saleReturnOrderModel.getInspectNo());//申报地国检编码			
		sReturnOdepotModel.setCustomsNo(saleReturnOrderModel.getCustomsNo());//申报地海关编码
		sReturnOdepotModel.setModel(yModel);//业务场景 账册内货权转移/账册内货权转移调仓 必填			
		sReturnOdepotModel.setServeTypes(serveTypes);//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务'必填,
		sReturnOdepotModel.setCustomerId(saleReturnOrderModel.getCustomerId());//客户id
		sReturnOdepotModel.setCustomerName(saleReturnOrderModel.getCustomerName());//客户名称
		sReturnOdepotModel.setRemark(rootJson.getRemark());//备注
		sReturnOdepotModel.setLbxNo(saleReturnOrderModel.getLbxNo());
		sReturnOdepotModel.setOutExternalCode(outExternalCode);// 销售出库外部单号
		sReturnOdepotModel.setBuId(saleReturnOrderModel.getBuId());// 事业部
		sReturnOdepotModel.setBuName(saleReturnOrderModel.getBuName());

		List<SaleReturnOdepotItemModel> outItemlist = new ArrayList<SaleReturnOdepotItemModel>();//存储拼装好的销售退出库单表体
		Integer returnOutNum = 0;


		//根据原货号查询表体是否存在，若存在则根据赠品方案摊分，若不存在则走原来流程
		List<SaleReturnTransfersOutStorageGoodsListJson> goodsJsonList = new ArrayList<>();//存储走原流程报文商品
		List<ZPJsonGoodsDTO> zpJsonGoodsList = new ArrayList<>();//拼装好的走分摊流程的报文商品
		Map<String,List<ZPOrderItemDTO>> zpOrderItemListMap = new HashMap<>();//存储拼装好的单据商品 key=原货号 value=单据商品list
		for(SaleReturnTransfersOutStorageGoodsListJson goods : rootJson.getGoodsList()) {
			//根据原货号查询销售退订单表体是否有对应商品信息
			SaleReturnOrderItemModel existItem = new SaleReturnOrderItemModel();
			existItem.setInOriginalGoodsId(goods.getGoodsId());
			existItem.setOrderId(saleReturnOrderModel.getId());
			List<SaleReturnOrderItemModel> existItems = saleReturnOrderItemDao.list(existItem);
			if(existItems!=null&&existItems.size()>0){
				//拼装走分摊流程的报文商品实体
				ZPJsonGoodsDTO  zpJsonGoods = new ZPJsonGoodsDTO();
				zpJsonGoods.setGoodsId(goods.getGoodsId());
				zpJsonGoods.setGoodsNo(goods.getGoodsNo());
				zpJsonGoods.setNormalNum(goods.getSalesNum());
				zpJsonGoods.setWornNum(goods.getWornNum());
				zpJsonGoods.setBatchNo(goods.getBatchNo());
				zpJsonGoods.setProductionDate(goods.getProductionDate());
				zpJsonGoods.setOverdueDate(goods.getOverdueDate());
				zpJsonGoodsList.add(zpJsonGoods);
				//拼装单据商品list
				List<ZPOrderItemDTO> zpOrderItemList = new ArrayList<>();
				for(SaleReturnOrderItemModel item: existItems){
					//获取单据商品在库位映射表的类型
					InventoryLocationMappingMongo locationMappingMongo = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(saleReturnOrderModel.getTopidealCode(), item.getOutGoodsNo());
					if(locationMappingMongo==null){
						LOGGER.error("库位映射表未找到配置,订单编号" + orderCode+"原货号"+item.getOutOriginalGoodsId()+"库位货号"+item.getOutGoodsNo());
						throw new RuntimeException("库位映射表未找到配置,订单编号" + orderCode+"原货号"+item.getOutOriginalGoodsId()+"库位货号"+item.getOutGoodsNo());
					}
					ZPOrderItemDTO zpOrderItem = new ZPOrderItemDTO();
					zpOrderItem.setGoodsId(item.getOutGoodsId());
					zpOrderItem.setGoodsNo(item.getOutGoodsNo());
					zpOrderItem.setOriginalGoodsId(item.getOutOriginalGoodsId());
					zpOrderItem.setOriginalGoodsNo(item.getOutOriginalGoodsNo());
					zpOrderItem.setNormalNum(item.getReturnNum());//好品数量
					zpOrderItem.setWornNum(item.getBadGoodsNum());//坏品数量
					zpOrderItem.setType(locationMappingMongo.getType());//库位映射类型
					zpOrderItemList.add(zpOrderItem);
				}
				zpOrderItemListMap.put(goods.getGoodsNo(),zpOrderItemList);
			}else{
				goodsJsonList.add(goods);//走原流程商品
			}
		}

		//分摊
		List<ZPTempItemDTO> allZPTempList = new ArrayList<>();//存储分摊后的实体
		if(zpJsonGoodsList!=null&&zpJsonGoodsList.size()>0){
			//拆分好品、过期品、坏品排序先分好品》过期品》坏品
			zpJsonGoodsList = ZPUtils.splitByTypeAndSort(zpJsonGoodsList);
			for(ZPJsonGoodsDTO jsonGooods : zpJsonGoodsList){
				List<ZPOrderItemDTO> zpOrderItemList = zpOrderItemListMap.get(jsonGooods.getGoodsNo());
				List<ZPTempItemDTO> tempList =ZPUtils.splitOrderItemNum(jsonGooods,zpOrderItemList);
				allZPTempList.addAll(tempList);
			}
			//合并分摊明细
			allZPTempList = ZPUtils.mergItem(allZPTempList);
		}

		//分摊实体生成销售入库表体
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		if(allZPTempList!=null&&allZPTempList.size()>0) {
			for(ZPTempItemDTO tempItem : allZPTempList) {
				//获取销售退货单商品
				SaleReturnOrderItemModel orderItemModel = kwGoodsMap.get(tempItem.getGoodsId());
				//查询库位商品
				Map<String, Object> merchandiseParam = new HashMap<>();
				merchandiseParam.put("merchandiseId", tempItem.getGoodsId());
				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseParam);
				//查询原商品
				Map<String, Object> originalMerchandiseParam = new HashMap<>();
				originalMerchandiseParam.put("merchandiseId", tempItem.getOriginalGoodsId());
				MerchandiseInfoMogo originalMerchandise = merchandiseInfoMogoDao.findOne(originalMerchandiseParam);

				//新增销售退货出库表体
				SaleReturnOdepotItemModel saleReturnOdepotItemModel =new SaleReturnOdepotItemModel();
				saleReturnOdepotItemModel.setOriginalGoodsId(tempItem.getOriginalGoodsId());//原货号
				saleReturnOdepotItemModel.setOriginalGoodsNo(tempItem.getOriginalGoodsNo());
				saleReturnOdepotItemModel.setOutGoodsId(tempItem.getGoodsId());//退出商品id
				saleReturnOdepotItemModel.setOutGoodsNo(tempItem.getGoodsNo());//退出商品货号
				saleReturnOdepotItemModel.setOutGoodsCode(merchandiseInfo.getGoodsCode());//退出商品编码
				saleReturnOdepotItemModel.setOutGoodsName(merchandiseInfo.getName());//退出商品名称
				saleReturnOdepotItemModel.setBarcode(merchandiseInfo.getBarcode());
				saleReturnOdepotItemModel.setCommbarcode(merchandiseInfo.getCommbarcode());	// 标准条码
				saleReturnOdepotItemModel.setReturnNum(tempItem.getNormalNum());//退货正常品数量
				saleReturnOdepotItemModel.setWornNum(tempItem.getWornNum());//坏货数量
				saleReturnOdepotItemModel.setReturnBatchNo(tempItem.getBatchNo());//退货批次
				saleReturnOdepotItemModel.setPoNo(orderItemModel.getPoNo());	// PO单号
				String productionDate = tempItem.getProductionDate();//生产日期
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length()==10) {
						saleReturnOdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
					}else {
						saleReturnOdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
					}
				}
				String overdueDate = tempItem.getOverdueDate();//失效日期
				if (StringUtils.isNotBlank(overdueDate)) {
					if (overdueDate.length()==10) {
						saleReturnOdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
					}else {
						saleReturnOdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
					}
				}
				returnOutNum+=tempItem.getNormalNum()+tempItem.getWornNum();
				outItemlist.add(saleReturnOdepotItemModel);

				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(saleReturnOdepotItemModel.getOverdueDate()!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(saleReturnOdepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
				//好品
				if(saleReturnOdepotItemModel.getReturnNum() != null && saleReturnOdepotItemModel.getReturnNum().intValue() > 0) {
					InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsListJson.setLocationGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));//库位商品
					subtractGoodsListJson.setLocationGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
					subtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOriginalGoodsId()));//原商品
					subtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOriginalGoodsNo());
					subtractGoodsListJson.setGoodsName(originalMerchandise.getName());
					subtractGoodsListJson.setBarcode(originalMerchandise.getBarcode());
					subtractGoodsListJson.setType("0");
					subtractGoodsListJson.setNum(saleReturnOdepotItemModel.getReturnNum());
					subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					subtractGoodsListJson.setBatchNo(saleReturnOdepotItemModel.getReturnBatchNo());
					subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getProductionDate()));
					subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getOverdueDate()));
					subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
					subtractGoodsListJsonList.add(subtractGoodsListJson);
				}
				//坏品
				if(saleReturnOdepotItemModel.getWornNum() != null && saleReturnOdepotItemModel.getWornNum().intValue() > 0) {
					InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsListJson.setLocationGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));//库位商品
					subtractGoodsListJson.setLocationGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
					subtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOriginalGoodsId()));//原商品
					subtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOriginalGoodsNo());
					subtractGoodsListJson.setGoodsName(originalMerchandise.getName());
					subtractGoodsListJson.setBarcode(originalMerchandise.getBarcode());
					subtractGoodsListJson.setType("1");
					subtractGoodsListJson.setNum(saleReturnOdepotItemModel.getWornNum());
					subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					subtractGoodsListJson.setBatchNo(saleReturnOdepotItemModel.getReturnBatchNo());
					subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getProductionDate()));
					subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getOverdueDate()));
					subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
					subtractGoodsListJsonList.add(subtractGoodsListJson);
				}
			}
		}

		//原来流程报文
		for(SaleReturnTransfersOutStorageGoodsListJson goodsJson : goodsJsonList) {
			//新增销售退货出库表体
			SaleReturnOdepotItemModel saleReturnOdepotItemModel =new SaleReturnOdepotItemModel();
			saleReturnOdepotItemModel.setSreturnOdepotId(sReturnOdepotModel.getId());//销售退货出库ID
			saleReturnOdepotItemModel.setOutGoodsId(goodsJson.getGoodsId());//退出商品id
			saleReturnOdepotItemModel.setOutGoodsCode(goodsJson.getGoodsCode());//退出商品编码
			saleReturnOdepotItemModel.setOutGoodsName(goodsJson.getGoodsName());//退出商品名称
			saleReturnOdepotItemModel.setOutGoodsNo(goodsJson.getGoodsNo());//退出商品货号
			saleReturnOdepotItemModel.setBarcode(goodsJson.getBarcode());
			saleReturnOdepotItemModel.setReturnNum(goodsJson.getSalesNum());//正常数量
			saleReturnOdepotItemModel.setWornNum(goodsJson.getWornNum());//坏货数量
			saleReturnOdepotItemModel.setReturnBatchNo(goodsJson.getBatchNo());//退货批次
			saleReturnOdepotItemModel.setCommbarcode(goodsJson.getCommbarcode());	// 标准条码
			String productionDate = goodsJson.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {
					saleReturnOdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
				}else {
					saleReturnOdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
				}
			}
			String overdueDate = goodsJson.getOverdueDate();//失效日期
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {
					saleReturnOdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
				}else {
					saleReturnOdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
				}
			}
			Integer salesNum = goodsJson.getSalesNum()==null?0:goodsJson.getSalesNum();
			Integer wornNum = goodsJson.getWornNum()==null?0:goodsJson.getWornNum();
			returnOutNum+=salesNum+wornNum;
			outItemlist.add(saleReturnOdepotItemModel);

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(saleReturnOdepotItemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(saleReturnOdepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			//好品
			if (saleReturnOdepotItemModel.getReturnNum() != null && saleReturnOdepotItemModel.getReturnNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				subtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));
				subtractGoodsListJson.setGoodsName(saleReturnOdepotItemModel.getOutGoodsName());
				subtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
				subtractGoodsListJson.setBarcode(saleReturnOdepotItemModel.getBarcode());
				subtractGoodsListJson.setType("0");
				subtractGoodsListJson.setNum(saleReturnOdepotItemModel.getReturnNum());
				subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsListJson.setBatchNo(saleReturnOdepotItemModel.getReturnBatchNo());
				subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getProductionDate()));
				subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getOverdueDate()));
				subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
				subtractGoodsListJsonList.add(subtractGoodsListJson);
			}
			// 坏品
			if(saleReturnOdepotItemModel.getWornNum() != null && saleReturnOdepotItemModel.getWornNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				subtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));
				subtractGoodsListJson.setGoodsName(saleReturnOdepotItemModel.getOutGoodsName());
				subtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
				subtractGoodsListJson.setBarcode(saleReturnOdepotItemModel.getBarcode());
				subtractGoodsListJson.setType("1");
				subtractGoodsListJson.setNum(saleReturnOdepotItemModel.getWornNum());
				subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsListJson.setBatchNo(saleReturnOdepotItemModel.getReturnBatchNo());
				subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getProductionDate()));
				subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(saleReturnOdepotItemModel.getOverdueDate()));
				subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
				subtractGoodsListJsonList.add(subtractGoodsListJson);
			}
		}
		// 新增销售退货出库
		sReturnOdepotModel.setReturnOutNum(returnOutNum);
		saleReturnOdepotDao.save(sReturnOdepotModel);
		for(SaleReturnOdepotItemModel sReturnOdepotItemModel:outItemlist){
			sReturnOdepotItemModel.setSreturnOdepotId(sReturnOdepotModel.getId());//销售退货出库ID
			// 新增销售退货出库表体
			saleReturnOdepotItemDao.save(sReturnOdepotItemModel);
		}
		//修改销售退货订单状态
    	SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
		//判断是否存在出库单
    	SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
    	saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());
    	List<SaleReturnIdepotModel> saleReturnIdepotList = saleReturnIdepotDao.list(saleReturnIdepotModel);
		if(saleReturnIdepotList != null && saleReturnIdepotList.size()>0){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//已完结
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_016);//已出仓
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//扣减销售出库库存量
		String now1 = sdf.format(new Date());
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(sReturnOdepotModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(sReturnOdepotModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(saleReturnOrderModel.getTopidealCode());
		invetAddOrSubtractRootJson.setDepotId(outDepotMongo.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(outDepotMongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(outDepotMongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(outDepotMongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(outDepotMongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(sReturnOdepotModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(saleReturnOrderModel.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_006);
		invetAddOrSubtractRootJson.setSourceDate(now1);		  
		invetAddOrSubtractRootJson.setDivergenceDate(transferStr);
		// 事业部
		invetAddOrSubtractRootJson.setBuId(String.valueOf(saleReturnOrderModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(saleReturnOrderModel.getBuName());
		// 获取当前年月		
		invetAddOrSubtractRootJson.setOwnMonth(yearsMonths);
		invetAddOrSubtractRootJson.setGoodsList(subtractGoodsListJsonList);
		
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTopic());//回调主题		
		customParam.put("code", saleReturnOrderModel.getCode());//销售退货内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		return true;
	}
}
*/
