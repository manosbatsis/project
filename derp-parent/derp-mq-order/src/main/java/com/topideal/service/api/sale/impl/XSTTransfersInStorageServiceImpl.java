/*
package com.topideal.service.api.sale.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnIdepotItemDao;
import com.topideal.dao.sale.SaleReturnOdepotDao;
import com.topideal.dao.sale.SaleReturnOdepotItemDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.entity.dto.common.ZPJsonGoodsDTO;
import com.topideal.entity.dto.common.ZPOrderItemDTO;
import com.topideal.entity.dto.common.ZPTempItemDTO;
import com.topideal.entity.vo.sale.SaleReturnIdepotItemModel;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotItemModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.api.v2_5.SaleTransfersInStorageGoodsListJson;
import com.topideal.json.api.v2_5.SaleTransfersInStorageRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.InventoryLocationMappingMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.api.sale.XSTTransfersInStorageService;
import com.topideal.tools.ZPUtils;

import net.sf.json.JSONObject;
*/
/**
 * 调拨入库
 *//*

@Service
public class XSTTransfersInStorageServiceImpl implements XSTTransfersInStorageService{
	
	*/
/*打印日志*//*

	private static final Logger LOGGER = LoggerFactory.getLogger(XSTTransfersInStorageServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;// 销售退货入库单表体	
	@Autowired 
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private SaleReturnOdepotDao saleReturnOdepotDao;// 销售退货出库单 
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;// 仓库商家关联表 mongo
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;// 销售退货出库商品
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;// 销售退货出库单表体
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao;

	// 调拨入库
	@Override
	@SystemServiceLog(point="12203120800",model="调拨入库",keyword="orderCode")
	public boolean saveTransfersInStorage(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleTransfersInStorageGoodsListJson.class);
		// JSON对象转实体
		SaleTransfersInStorageRootJson rootJson = (SaleTransfersInStorageRootJson) JSONObject.toBean(jsonData, SaleTransfersInStorageRootJson.class, classMap);
		String orderCode = rootJson.getOrderCode();// 订单号
		//获取是否菜鸟字段(1-是，0-否)
		String isRookie = rootJson.getIsRookie();
		String merchantIdStr = rootJson.getMerchantId();// 商家ID
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setMerchantId(Long.valueOf(merchantIdStr));
		if ("0".equals(isRookie)) {// 销售退货订单 (非菜鸟)
			saleReturnOrderModel.setCode(orderCode);
			saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		}else if("1".equals(isRookie)){// 销售退货订单 (菜鸟)
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
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleReturnOrderModel.getInDepotId());// 根据仓库id
		DepotInfoMongo indepotMongo = depotInfoMongoDao.findOne(depotInfo_params);
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(indepotMongo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(indepotMongo.getBatchValidation())) {
			for (SaleTransfersInStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(indepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(indepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
				}
				
			}
		}		

		String inExternalCode = rootJson.getCode();//调拨单号 外部单号		
		String remark = rootJson.getRemark();//备注
		String transfer = rootJson.getTransferDate();
		Timestamp transferDate=null;//发货时间
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
				goodsMap.put(model.getInGoodsId(), model);
			}
			kwGoodsMap.put(model.getInGoodsId(),model);
		}
		//检查报文里的商品在订单中是否存在
		for(SaleTransfersInStorageGoodsListJson goodsJson : rootJson.getGoodsList()){
			SaleReturnOrderItemModel orderItem = goodsMap.get(goodsJson.getGoodsId());
			if(orderItem == null) {
				LOGGER.error("销售退货单中未找到商品,订单编号:" + orderCode+",原货号:"+goodsJson.getGoodsNo());
				throw new RuntimeException("销售退货单中未找到商品,订单编号:" + orderCode+",原货号:"+goodsJson.getGoodsNo());
			}
		}
				
		//检查销售退货入库单是否已存在
		SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
		if ("0".equals(isRookie)) {// 销售退货入订单 (非菜鸟)
			saleReturnIdepotModel.setOrderCode(orderCode);//销售退货编号			
			saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);	
		}else if("1".equals(isRookie)){// 销售退货入订单 (菜鸟)
			saleReturnIdepotModel.setLbxNo(orderCode);//销售退货编号			
			saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
		}
		if (saleReturnIdepotModel!=null) {
			LOGGER.error("销售退货入库单已经存在orderCode:"+orderCode);
			throw new RuntimeException("销售退货入库单已经存在orderCode: "+orderCode);
		}
		// 新增 销售退货入库
    	SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();
    	sReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());//销售退货ID
    	sReturnIdepotModel.setMerchantId(saleReturnOrderModel.getMerchantId());//商家ID
    	sReturnIdepotModel.setMerchantName(saleReturnOrderModel.getMerchantName());//商家名称
    	sReturnIdepotModel.setContractNo(saleReturnOrderModel.getContractNo());//合同号
    	sReturnIdepotModel.setInDepotId(saleReturnOrderModel.getInDepotId());//退入仓库id
    	sReturnIdepotModel.setInDepotName(saleReturnOrderModel.getInDepotName());//退入仓库名称
    	sReturnIdepotModel.setOutDepotId(saleReturnOrderModel.getOutDepotId());//退出仓库id
    	sReturnIdepotModel.setOutDepotName(saleReturnOrderModel.getOutDepotName());//退出仓库名称      	
        sReturnIdepotModel.setReturnInDate(transferDate);//退货入库时间
    	sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_028);//'状态   DRC("011","待入仓"),YRC("012","已入仓"), "028","入库中"       	
//    	sReturnIdepotModel.setCode(CodeGeneratorUtil.getNo("XSTR"));//销售退货入库单号 来下接口回推
    	sReturnIdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTR));//销售退货入库单号 来下接口回推
    	sReturnIdepotModel.setOrderCode(saleReturnOrderModel.getCode());//销售退货编号
    	sReturnIdepotModel.setMerchantReturnNo(saleReturnOrderModel.getMerchantReturnNo());//企业退运单号
    	sReturnIdepotModel.setInspectNo(saleReturnOrderModel.getInspectNo());//申报地国检编码
    	sReturnIdepotModel.setCustomsNo(saleReturnOrderModel.getCustomsNo());//申报地海关编码
    	sReturnIdepotModel.setModel(saleReturnOrderModel.getModel());//业务场景 账册内货权转移/账册内货权转移调仓
    	sReturnIdepotModel.setServeTypes(saleReturnOrderModel.getServeTypes());//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
    	sReturnIdepotModel.setCustomerId(saleReturnOrderModel.getCustomerId());//客户id
    	sReturnIdepotModel.setCustomerName(saleReturnOrderModel.getCustomerName());//客户名称
    	sReturnIdepotModel.setRemark(remark);// 备注
    	sReturnIdepotModel.setLbxNo(saleReturnOrderModel.getLbxNo());//lBX单号
    	sReturnIdepotModel.setInExternalCode(inExternalCode);// 销售退入外部单号
    	sReturnIdepotModel.setBuId(saleReturnOrderModel.getBuId()); // 事业部
    	sReturnIdepotModel.setBuName(saleReturnOrderModel.getBuName());

		Integer inTotalNum = 0;
		List<SaleReturnIdepotItemModel> inItemList = new ArrayList<SaleReturnIdepotItemModel>();//存储拼装好的退货入库单表体
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();

		//根据原货号查询表体是否存在，若存在则根据赠品方案摊分，若不存在则走原来流程
		List<SaleTransfersInStorageGoodsListJson> goodsJsonList = new ArrayList<>();//存储走原流程报文商品
		List<ZPJsonGoodsDTO> zpJsonGoodsList = new ArrayList<>();//拼装好的走分摊流程的报文商品
		Map<String,List<ZPOrderItemDTO>> zpOrderItemListMap = new HashMap<>();//存储拼装好的单据商品 key=原货号 value=单据商品list
		for(SaleTransfersInStorageGoodsListJson goods : rootJson.getGoodsList()) {
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
					InventoryLocationMappingMongo locationMappingMongo = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(saleReturnOrderModel.getTopidealCode(), item.getInGoodsNo());
					if(locationMappingMongo==null){
						LOGGER.error("库位映射表未找到配置,订单编号" + orderCode+"原货号"+item.getInOriginalGoodsId()+"库位货号"+item.getInGoodsNo());
						throw new RuntimeException("库位映射表未找到配置,订单编号" + orderCode+"原货号"+item.getInOriginalGoodsId()+"库位货号"+item.getInGoodsNo());
					}
					ZPOrderItemDTO zpOrderItem = new ZPOrderItemDTO();
					zpOrderItem.setGoodsId(item.getInGoodsId());
					zpOrderItem.setGoodsNo(item.getInGoodsNo());
					zpOrderItem.setOriginalGoodsId(item.getInOriginalGoodsId());
					zpOrderItem.setOriginalGoodsNo(item.getInOriginalGoodsNo());
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
		if(allZPTempList!=null&&allZPTempList.size()>0) {
			for(ZPTempItemDTO tempItem : allZPTempList) {
				//获取库位商品
				Map<String, Object> kwMerchandiseParam = new HashMap<>();
				kwMerchandiseParam.put("merchandiseId", tempItem.getGoodsId());//库位货号
				MerchandiseInfoMogo kwMerchandiseInfo = merchandiseInfoMogoDao.findOne(kwMerchandiseParam);

				SaleReturnIdepotItemModel indepotItemModel =new SaleReturnIdepotItemModel();
				indepotItemModel.setOriginalGoodsId(tempItem.getOriginalGoodsId());//原货号
				indepotItemModel.setOriginalGoodsNo(tempItem.getOriginalGoodsNo());
				indepotItemModel.setInGoodsId(tempItem.getGoodsId());//退入商品id
				indepotItemModel.setInGoodsNo(tempItem.getGoodsNo());//退入商品货号
				indepotItemModel.setInGoodsCode(kwMerchandiseInfo.getGoodsCode());//退入商品编码
				indepotItemModel.setInGoodsName(kwMerchandiseInfo.getName());//退入商品名称
				indepotItemModel.setBarcode(kwMerchandiseInfo.getBarcode());//条码
				indepotItemModel.setCommbarcode(kwMerchandiseInfo.getCommbarcode()); // 标准条码
				indepotItemModel.setReturnNum(tempItem.getNormalNum());//正常品数量
				indepotItemModel.setWornNum(tempItem.getWornNum());//坏货数量
				indepotItemModel.setReturnBatchNo(tempItem.getBatchNo());//退货批次
				String productionDate = tempItem.getProductionDate();//生产日期
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length()==10) {
						indepotItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
					}else {
						indepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
					}
				}
				String overdueDateStr = tempItem.getOverdueDate();//到期日期
				Date overdueDate=null;
				if (StringUtils.isNotBlank(overdueDateStr)) {
					if (overdueDateStr.length()==10) {
						overdueDate=Timestamp.valueOf(overdueDateStr+" 00:00:00");//到期日期
					}else {
						overdueDate=Timestamp.valueOf(overdueDateStr);//到期日期
					}
				}
				indepotItemModel.setOverdueDate(overdueDate);
				inTotalNum+=tempItem.getNormalNum()+tempItem.getWornNum();
				inItemList.add(indepotItemModel);

				//拼装加库存报文
				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(overdueDate!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(overdueDate);//判断是否过期是否过期（0是 1否）
				}
				//查询原商品
				Map<String, Object> originalMerchandiseParam = new HashMap<>();
				originalMerchandiseParam.put("merchandiseId", indepotItemModel.getOriginalGoodsId());
				MerchandiseInfoMogo originalMerchandise = merchandiseInfoMogoDao.findOne(originalMerchandiseParam);

				// 好品
				if(indepotItemModel.getReturnNum() != null && indepotItemModel.getReturnNum().intValue() > 0) {
					InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsListJson.setLocationGoodsId(String.valueOf(indepotItemModel.getInGoodsId()));//库位商品
					subtractGoodsListJson.setLocationGoodsNo(indepotItemModel.getInGoodsNo());
					subtractGoodsListJson.setGoodsId(String.valueOf(indepotItemModel.getOriginalGoodsId()));//原商品
					subtractGoodsListJson.setGoodsNo(indepotItemModel.getOriginalGoodsNo());
					subtractGoodsListJson.setGoodsName(originalMerchandise.getName());
					subtractGoodsListJson.setBarcode(originalMerchandise.getBarcode());
					subtractGoodsListJson.setNum(indepotItemModel.getReturnNum());
					subtractGoodsListJson.setType("0");//商品分类 （0 好品 1坏品 ）
					subtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
					subtractGoodsListJson.setBatchNo(indepotItemModel.getReturnBatchNo());
					subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(indepotItemModel.getProductionDate()));
					subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(indepotItemModel.getOverdueDate()));
					subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsListJsonList.add(subtractGoodsListJson);
				}
				// 坏品
				if (indepotItemModel.getWornNum() != null && indepotItemModel.getWornNum().intValue() > 0){
					InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsListJson.setLocationGoodsId(String.valueOf(indepotItemModel.getInGoodsId()));//库位商品
					subtractGoodsListJson.setLocationGoodsNo(indepotItemModel.getInGoodsNo());
					subtractGoodsListJson.setGoodsId(String.valueOf(indepotItemModel.getOriginalGoodsId()));//原商品
					subtractGoodsListJson.setGoodsNo(indepotItemModel.getOriginalGoodsNo());
					subtractGoodsListJson.setGoodsName(originalMerchandise.getName());
					subtractGoodsListJson.setBarcode(originalMerchandise.getBarcode());
					subtractGoodsListJson.setNum(indepotItemModel.getWornNum());
					subtractGoodsListJson.setType("1");//商品分类 （0 好品 1坏品 ）
					subtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
					subtractGoodsListJson.setBatchNo(indepotItemModel.getReturnBatchNo());
					subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(indepotItemModel.getProductionDate()));
					subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(indepotItemModel.getOverdueDate()));
					subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsListJsonList.add(subtractGoodsListJson);
				}
			}
		}

    	//走原来流程报文商品
    	for(SaleTransfersInStorageGoodsListJson goodsJson : goodsJsonList) {
			//新增销售入库出库单表体
			SaleReturnIdepotItemModel IndepotItemModel = new SaleReturnIdepotItemModel();
			IndepotItemModel.setInGoodsId(goodsJson.getGoodsId());//退入商品id
			IndepotItemModel.setInGoodsCode(goodsJson.getGoodsCode());//退入商品编码
			IndepotItemModel.setInGoodsName(goodsJson.getGoodsName());//退入商品名称
			IndepotItemModel.setInGoodsNo(goodsJson.getGoodsNo());//退入商品货号
			IndepotItemModel.setBarcode(goodsJson.getBarcode());//条码
			IndepotItemModel.setCommbarcode(goodsJson.getCommbarcode()); // 标准条码
			IndepotItemModel.setReturnNum(goodsJson.getSalesNum());//正常品数量
			IndepotItemModel.setWornNum(goodsJson.getWornNum());//坏货数量
			IndepotItemModel.setReturnBatchNo(goodsJson.getBatchNo());//退货批次
			String productionDate = goodsJson.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length() == 10) {
					IndepotItemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));//生产日期
				} else {
					IndepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
				}
			}
			String overdueDate = goodsJson.getOverdueDate();//到期日期
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length() == 10) {
					IndepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));//到期日期
				} else {
					IndepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//到期日期
				}
			}
			Integer salesNum = goodsJson.getSalesNum()==null?0:goodsJson.getSalesNum();
			Integer wornNum = goodsJson.getWornNum()==null?0:goodsJson.getWornNum();
			inTotalNum += salesNum+wornNum;
			inItemList.add(IndepotItemModel);

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(IndepotItemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(IndepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			// 好品
			if(IndepotItemModel.getReturnNum() != null && IndepotItemModel.getReturnNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				subtractGoodsListJson.setGoodsId(String.valueOf(IndepotItemModel.getInGoodsId()));
				subtractGoodsListJson.setGoodsName(IndepotItemModel.getInGoodsName());
				subtractGoodsListJson.setGoodsNo(IndepotItemModel.getInGoodsNo());
				subtractGoodsListJson.setBarcode(IndepotItemModel.getBarcode());
				subtractGoodsListJson.setType("0");////商品分类 （0 好品 1坏品 ）
				subtractGoodsListJson.setNum(IndepotItemModel.getReturnNum());
				subtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
				subtractGoodsListJson.setBatchNo(IndepotItemModel.getReturnBatchNo());
				subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(IndepotItemModel.getProductionDate()));
				subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(IndepotItemModel.getOverdueDate()));
				subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
				subtractGoodsListJsonList.add(subtractGoodsListJson);
			}
			// 坏品
			if(IndepotItemModel.getWornNum() != null && IndepotItemModel.getWornNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				subtractGoodsListJson.setGoodsId(String.valueOf(IndepotItemModel.getInGoodsId()));
				subtractGoodsListJson.setGoodsName(IndepotItemModel.getInGoodsName());
				subtractGoodsListJson.setGoodsNo(IndepotItemModel.getInGoodsNo());
				subtractGoodsListJson.setBarcode(IndepotItemModel.getBarcode());
				subtractGoodsListJson.setType("1");
				subtractGoodsListJson.setNum(IndepotItemModel.getWornNum());
				subtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
				subtractGoodsListJson.setBatchNo(IndepotItemModel.getReturnBatchNo());
				subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(IndepotItemModel.getProductionDate()));
				subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(IndepotItemModel.getOverdueDate()));
				subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
				subtractGoodsListJsonList.add(subtractGoodsListJson);
			}
		}
    	// 新增销售退货入库
    	sReturnIdepotModel.setReturnInNum(inTotalNum);
    	saleReturnIdepotDao.save(sReturnIdepotModel);
    	for(SaleReturnIdepotItemModel sReturnIdepotItemModel:inItemList){
    		sReturnIdepotItemModel.setSreturnIdepotId(sReturnIdepotModel.getId());//销售退货入库ID
        	saleReturnIdepotItemDao.save(sReturnIdepotItemModel);
    	}

    	// 增加库存
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now = sdf.format(new Date());
		InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
		inventoryRoot.setMerchantId(saleReturnOrderModel.getMerchantId().toString());
		inventoryRoot.setMerchantName(saleReturnOrderModel.getMerchantName());
		inventoryRoot.setTopidealCode(saleReturnOrderModel.getTopidealCode());
		inventoryRoot.setDepotId(indepotMongo.getDepotId().toString());
		inventoryRoot.setDepotName(indepotMongo.getName());
		inventoryRoot.setDepotCode(indepotMongo.getCode());
		inventoryRoot.setDepotType(indepotMongo.getType());
		inventoryRoot.setIsTopBooks(indepotMongo.getIsTopBooks());
		inventoryRoot.setOrderNo(sReturnIdepotModel.getCode());
		inventoryRoot.setBusinessNo(saleReturnOrderModel.getCode());
		inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004);
		inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_005);
		inventoryRoot.setSourceDate(now);
		inventoryRoot.setDivergenceDate(transferStr);// 出入库日期
		// 事业部
		inventoryRoot.setBuId(String.valueOf(saleReturnOrderModel.getBuId()));
		inventoryRoot.setBuName(saleReturnOrderModel.getBuName());
		// 获取当前年月
		inventoryRoot.setOwnMonth(yearsMonths);// 归属月份
		inventoryRoot.setGoodsList(subtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		inventoryRoot.setBackTags(MQPushBackOrderEnum.TRANSFER_IN_STIRAGE_PUSH_BACK_2.getTags());//回调标签
		inventoryRoot.setBackTopic(MQPushBackOrderEnum.TRANSFER_IN_STIRAGE_PUSH_BACK_2.getTopic());//回调主题		
		customParam.put("code", saleReturnOrderModel.getCode());// 销售退货订单内部单号
		inventoryRoot.setCustomParam(customParam);////自定义回调参数		
		
	
		//-----------------------------------判断是否走已入定出(销售退货出库表 )-----------------------
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
    	boolean isSubtractInventoryFlag = false;	// 标识是否减库存
		DepotMerchantRelMongo depotMerchantRelInfo = null;
		if(null!=saleReturnOrderModel.getOutDepotId()){
			// 查询该商家销售退出仓仓库的相关信息
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", saleReturnOrderModel.getMerchantId());
			depotMerchantRelParam.put("depotId", saleReturnOrderModel.getOutDepotId());
			depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (depotMerchantRelInfo == null || "".equals(depotMerchantRelInfo)) {
				LOGGER.error("未查到该商家下退出仓库信息,销售退货订单编号" + orderCode);
				throw new RuntimeException("未查到该商家下退出仓库信息,销售退货订单编号" + orderCode);
			}
		}
	
		*/
/**
		 * 销售退货类型为代销、购销均适用逻辑：
		 *	1、如果入库仓库为香港仓时，不走以入定出；
		 *	2、出库仓库在对应商家仓库管理下的“以入定出”标识为“是”的,则正常已入定出生成退货出库单，扣减退出仓的出库量；
		 *	3、有出库仓要考虑走以入定出，无出库仓就不用以入定出
		 *//*

		if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleReturnOrderModel.getReturnType()) ||
			DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleReturnOrderModel.getReturnType())){
			if(!DERP_SYS.DEPOTINFO_TYPE_C.equals(indepotMongo.getType()) &&
					depotMerchantRelInfo!=null && DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(depotMerchantRelInfo.getIsInDependOut())){

				//判断退出仓库是否批次效期强校验
				Map<String, Object> depotInfoMap = new HashMap<String, Object>();
				depotInfoMap.put("depotId", saleReturnOrderModel.getOutDepotId());// 根据仓库id
				DepotInfoMongo outdepotMongo = depotInfoMongoDao.findOne(depotInfoMap);
				// 批次效期强校验：1-是 0-否
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outdepotMongo.getBatchValidation()) ) {
					for (SaleTransfersInStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
						String batchNo = goodsListJson.getBatchNo();
						String productionDate = goodsListJson.getProductionDate();
						String overdueDate = goodsListJson.getOverdueDate();
						if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
							LOGGER.error(outdepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
							throw new RuntimeException(outdepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
						}
					}
				}
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(indepotMongo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(indepotMongo.getBatchValidation())) {
					for (SaleTransfersInStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
						String batchNo = goodsListJson.getBatchNo();
						String productionDate = goodsListJson.getProductionDate();
						String overdueDate = goodsListJson.getOverdueDate();
						if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
							LOGGER.error(indepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
							throw new RuntimeException(indepotMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
						}
					}
				}
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
					LOGGER.error("销售退货出库表已经存在,订单号:" + orderCode);
					throw new RuntimeException("销售退货出库表已经存在,订单号:" + orderCode);
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
	//			sReturnOdepotModel.setCode(CodeGeneratorUtil.getNo("XSTC"));//销售退货出库单号
				sReturnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTC));//销售退货出库单号
				sReturnOdepotModel.setOrderCode(saleReturnOrderModel.getCode());//销售退货编号
				sReturnOdepotModel.setMerchantReturnNo(saleReturnOrderModel.getMerchantReturnNo());//企业退运单号
				sReturnOdepotModel.setInspectNo(saleReturnOrderModel.getInspectNo());//申报地国检编码
				sReturnOdepotModel.setCustomsNo(saleReturnOrderModel.getCustomsNo());//申报地海关编码
				// model和serveTypes这两个字段先不设值
				//sReturnOdepotModel.setModel(null);//业务场景 账册内货权转移/账册内货权转移调仓
				//sReturnOdepotModel.setServeTypes(null);//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
				sReturnOdepotModel.setCustomerId(saleReturnOrderModel.getCustomerId());//客户id
				sReturnOdepotModel.setCustomerName(saleReturnOrderModel.getCustomerName());//客户名称
				sReturnOdepotModel.setRemark(rootJson.getRemark());//备注
				sReturnOdepotModel.setLbxNo(saleReturnOrderModel.getLbxNo());
				sReturnOdepotModel.setOutExternalCode(inExternalCode);// 销售出库外部单号
				sReturnOdepotModel.setBuId(saleReturnOrderModel.getBuId());// 事业部
				sReturnOdepotModel.setBuName(saleReturnOrderModel.getBuName());
				sReturnOdepotModel.setReturnOutNum(sReturnIdepotModel.getReturnInNum());

				//复制退货入库单表体生成退货出库单
				List<SaleReturnOdepotItemModel> outItemlist = new ArrayList<SaleReturnOdepotItemModel>();//存储拼装好的出库单表体
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				for(SaleReturnIdepotItemModel indepotItem : inItemList){
					//获取销售退货单退入商品获取退出商品
					SaleReturnOrderItemModel orderItemModel = kwGoodsMap.get(indepotItem.getInGoodsId());
					//查询退出商品
					Map<String, Object> outMerchandiseParam = new HashMap<>();
					outMerchandiseParam.put("merchandiseId", orderItemModel.getOutGoodsId());
					MerchandiseInfoMogo outMerchandiseInfo = merchandiseInfoMogoDao.findOne(outMerchandiseParam);
					MerchandiseInfoMogo originalMerchandiseInfo = null;
					if(orderItemModel.getOutOriginalGoodsId()!=null){
						Map<String, Object> originalMerchandiseParam = new HashMap<>();
						originalMerchandiseParam.put("merchandiseId", orderItemModel.getOutOriginalGoodsId());
						originalMerchandiseInfo = merchandiseInfoMogoDao.findOne(originalMerchandiseParam);
					}

					//拼装销售退货出库表体
					SaleReturnOdepotItemModel outdepotItemModel =new SaleReturnOdepotItemModel();
					outdepotItemModel.setOriginalGoodsId(orderItemModel.getOutOriginalGoodsId());//原货号
					outdepotItemModel.setOriginalGoodsNo(orderItemModel.getOutOriginalGoodsNo());
					outdepotItemModel.setOutGoodsId(orderItemModel.getOutGoodsId());//退出商品id
					outdepotItemModel.setOutGoodsCode(orderItemModel.getOutGoodsCode());//退出商品编码
					outdepotItemModel.setOutGoodsName(orderItemModel.getOutGoodsName());//退出商品名称
					outdepotItemModel.setOutGoodsNo(orderItemModel.getOutGoodsNo());//退出商品货号
					outdepotItemModel.setPoNo(orderItemModel.getPoNo());	// PO单号
					outdepotItemModel.setReturnNum(indepotItem.getReturnNum());//退货正常品数量
					outdepotItemModel.setWornNum(indepotItem.getWornNum());//坏货数量
					outdepotItemModel.setReturnBatchNo(indepotItem.getReturnBatchNo());//退货批次
					outdepotItemModel.setBarcode(outMerchandiseInfo.getBarcode());
					outdepotItemModel.setCommbarcode(outMerchandiseInfo.getCommbarcode());	// 标准条码
					outdepotItemModel.setProductionDate(indepotItem.getProductionDate());//生产日期
					outdepotItemModel.setOverdueDate(indepotItem.getOverdueDate());//失效日期
					outItemlist.add(outdepotItemModel);

					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(outdepotItemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(outdepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					//好品
					if (outdepotItemModel.getReturnNum() != null && outdepotItemModel.getReturnNum().intValue() > 0) {
						InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
						if(outdepotItemModel.getOriginalGoodsId()!=null){
							subtractGoodsListJson.setLocationGoodsId(String.valueOf(outdepotItemModel.getOutGoodsId()));//库位商品
							subtractGoodsListJson.setLocationGoodsNo(outdepotItemModel.getOutGoodsNo());
							subtractGoodsListJson.setGoodsId(String.valueOf(outdepotItemModel.getOriginalGoodsId()));//原商品
							subtractGoodsListJson.setGoodsNo(outdepotItemModel.getOriginalGoodsNo());
							subtractGoodsListJson.setGoodsName(originalMerchandiseInfo.getName());
							subtractGoodsListJson.setBarcode(originalMerchandiseInfo.getBarcode());
							subtractGoodsListJson.setCommbarcode(originalMerchandiseInfo.getCommbarcode());
						}else {
							subtractGoodsListJson.setGoodsId(String.valueOf(outdepotItemModel.getOutGoodsId()));
							subtractGoodsListJson.setGoodsNo(outdepotItemModel.getOutGoodsNo());
							subtractGoodsListJson.setGoodsName(outdepotItemModel.getOutGoodsName());
							subtractGoodsListJson.setBarcode(outdepotItemModel.getBarcode());
							subtractGoodsListJson.setCommbarcode(outdepotItemModel.getCommbarcode());
						}
						subtractGoodsListJson.setType("0");
						subtractGoodsListJson.setNum(outdepotItemModel.getReturnNum());
						subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
						subtractGoodsListJson.setBatchNo(outdepotItemModel.getReturnBatchNo());
						subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(outdepotItemModel.getProductionDate()));
						subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(outdepotItemModel.getOverdueDate()));
						subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
						invetAddOrSubtractGoodsListJsonList.add(subtractGoodsListJson);
					}
					// 坏品
					if (outdepotItemModel.getWornNum() != null && outdepotItemModel.getWornNum().intValue() > 0) {
						InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
						if(outdepotItemModel.getOriginalGoodsId()!=null){
							subtractGoodsListJson.setLocationGoodsId(String.valueOf(outdepotItemModel.getOutGoodsId()));//库位商品
							subtractGoodsListJson.setLocationGoodsNo(outdepotItemModel.getOutGoodsNo());
							subtractGoodsListJson.setGoodsId(String.valueOf(outdepotItemModel.getOriginalGoodsId()));//原商品
							subtractGoodsListJson.setGoodsNo(outdepotItemModel.getOriginalGoodsNo());
							subtractGoodsListJson.setGoodsName(originalMerchandiseInfo.getName());
							subtractGoodsListJson.setBarcode(originalMerchandiseInfo.getBarcode());
							subtractGoodsListJson.setCommbarcode(originalMerchandiseInfo.getCommbarcode());
						}else {
							subtractGoodsListJson.setGoodsId(String.valueOf(outdepotItemModel.getOutGoodsId()));
							subtractGoodsListJson.setGoodsNo(outdepotItemModel.getOutGoodsNo());
							subtractGoodsListJson.setGoodsName(outdepotItemModel.getOutGoodsName());
							subtractGoodsListJson.setBarcode(outdepotItemModel.getBarcode());
							subtractGoodsListJson.setCommbarcode(outdepotItemModel.getCommbarcode());
						}
						subtractGoodsListJson.setType("1");//商品分类 （0 好品 1坏品 ）
						subtractGoodsListJson.setNum(outdepotItemModel.getWornNum());
						subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
						subtractGoodsListJson.setBatchNo(outdepotItemModel.getReturnBatchNo());
						subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(outdepotItemModel.getProductionDate()));
						subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(outdepotItemModel.getOverdueDate()));
						subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0 是 1 否）
						invetAddOrSubtractGoodsListJsonList.add(subtractGoodsListJson);
					}
				}

				//新增销售退货出库
				saleReturnOdepotDao.save(sReturnOdepotModel);
				for(SaleReturnOdepotItemModel sReturnOdepotItemModel:outItemlist){
					sReturnOdepotItemModel.setSreturnOdepotId(sReturnOdepotModel.getId());//销售退货出库ID
					// 新增销售退货出库表体
					saleReturnOdepotItemDao.save(sReturnOdepotItemModel);
				}

				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				//扣减销售出库库存量
				String now1 = sdf2.format(new Date());
				invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(sReturnOdepotModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(sReturnOdepotModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(saleReturnOrderModel.getTopidealCode());

				invetAddOrSubtractRootJson.setDepotId(outdepotMongo.getDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(outdepotMongo.getName());
				invetAddOrSubtractRootJson.setDepotCode(outdepotMongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(outdepotMongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(outdepotMongo.getIsTopBooks());
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
				invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

				//库存回推数据
				Map<String, Object> customParam2=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2.getTopic());//回调主题
				customParam2.put("code", saleReturnOrderModel.getCode());//销售退货内部单号
				invetAddOrSubtractRootJson.setCustomParam(customParam2);////自定义回调参数
				isSubtractInventoryFlag = true;	// 减库存
		    }
	    }

		//修改销售退货订单状态
		SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
		//判断是否存在出库单
		SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
		saleReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());
		List<SaleReturnOdepotModel> saleReturnOdepotList = saleReturnOdepotDao.list(saleReturnOdepotModel);
		if(saleReturnOdepotList != null && saleReturnOdepotList.size()>0){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);	// "007","已完结"
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_012);	// "012","已入仓"
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);
		// 把加减库存放到最下方防止已入定出出现异常接口报错
		rocketMQProducer.send(JSONObject.fromObject(inventoryRoot).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		if(isSubtractInventoryFlag){
			rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}
		return true;
	}
}
*/
