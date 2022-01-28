package com.topideal.service.api.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQGoodsListJson;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.XSLoadingDeliveriesService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 装载交运回推接口实现类
 */
@Service
public class XSLoadingDeliveriesServiceImpl implements XSLoadingDeliveriesService {
	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(XSLoadingDeliveriesServiceImpl.class);
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售出库单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库订单商品
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120501, model = DERP_LOG_POINT.POINT_12203120501_Label,keyword="orderCode")
	public boolean saveLoadingDeliveriesInfo(String json,String keys,String topics,String tags) throws Exception {
		/**
		 * 说明:只接受服务类型为10 的单
		 */
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleLoadingDeliveriesMQGoodsListJson.class);
		// JSON对象转实体
		SaleLoadingDeliveriesMQRootJson rootJson = (SaleLoadingDeliveriesMQRootJson) JSONObject.toBean(jsonData, SaleLoadingDeliveriesMQRootJson.class, classMap);
		List<SaleLoadingDeliveriesMQGoodsListJson> goodsList = rootJson.getGoodsList();
		String orderCode = rootJson.getOrderCode();// 订单号

        try {
            OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
            orderExternalCodeModel.setExternalCode(orderCode);
            orderExternalCodeModel.setOrderSource(3);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
            orderExternalCodeDao.save(orderExternalCodeModel);
        } catch (Exception e) {
            logger.error("销售外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
            throw new RuntimeException("销售外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
        }

		//1.查询预申报单 是否存在
		SaleDeclareOrderModel saleDeclare = new SaleDeclareOrderModel();
		saleDeclare.setCode(orderCode);
		saleDeclare = saleDeclareOrderDao.searchByModel(saleDeclare);
		if(saleDeclare==null){
			logger.error("销售预申报单不存在,单号：" + orderCode );
			throw new RuntimeException("销售预申报单不存在,单号：" + orderCode);
		}
		SaleOutDepotModel queryOutDepotModel = new SaleOutDepotModel();
		queryOutDepotModel.setSaleDeclareOrderCode(orderCode);
		List<SaleOutDepotModel> queryOutDepotList = saleOutDepotDao.list(queryOutDepotModel);
		if(queryOutDepotList != null && queryOutDepotList.size() > 0) {
			logger.error("销售预申报单：" + orderCode + "出库单已存在  保存失败");
			throw new RuntimeException("销售预申报单：" + orderCode + "出库单已存在  保存失败");
		}

		SaleDeclareOrderItemModel item = new SaleDeclareOrderItemModel();
		item.setDeclareOrderId(saleDeclare.getId());
		List<SaleDeclareOrderItemModel> itemList = saleDeclareOrderItemDao.list(item);
		/**检查预申报单表体申报单销售表体id是否为空*/
		for(SaleDeclareOrderItemModel ditem : itemList){
			if(ditem.getSaleItemId()==null||StringUtils.isBlank(ditem.getSaleOrderCode())){
				logger.info("预申报表体销售单表体id|销售单号为空"+ditem.getId());
				throw new RuntimeException("预申报表体销售单表体id|销售单号为空"+ditem.getId());
			}
		}

		//2.检查理货单位与预申报表头理货单位是否一致
		String tallyingUnit = saleDeclare.getTallyingUnit();
		for(SaleLoadingDeliveriesMQGoodsListJson goods : goodsList){
			String jsTallyingUnit = goods.getTallyingUnit();
			if(StringUtils.isBlank(jsTallyingUnit)) jsTallyingUnit = "";//默认为空字符方便比较
			if(StringUtils.isBlank(tallyingUnit)) tallyingUnit = "";//默认为空字符方便比较
			if(!jsTallyingUnit.equals(tallyingUnit)){
				logger.error("报文理货单位与销售预申报理货单位不一致,货号:"+goods.getGoodsNo()+",报文单位:"+jsTallyingUnit+",预申报单单位:"+tallyingUnit);
				throw new RuntimeException("报文理货单位与销售预申报理货单位不一致货号:"+goods.getGoodsNo()+",报文单位:"+jsTallyingUnit+",预申报单单位:"+tallyingUnit);
			}
		}
		//3.合并报文相同商品数量、及总量
		Integer jsTotal = 0;//报文总量
		Map<String,Integer> jsGoodsSumMap= new HashMap<>();//key=goodsid+tallyingUnit value=数量
		for(SaleLoadingDeliveriesMQGoodsListJson goods : goodsList){
			String key = goods.getGoodsId()+"_"+goods.getTallyingUnit();
			Integer num = jsGoodsSumMap.get(key);
			if(num==null) num = 0;
			jsGoodsSumMap.put(key,num + goods.getNum());
			jsTotal += goods.getNum();
		}
		//合并预申报表体相同商品数量、及总量
		Integer total = 0;//预申报总量
		Map<String,Integer> goodsSumMap= new HashMap<>();//key=goodsid+tallyingUnit value=数量
		for(SaleDeclareOrderItemModel sditem : itemList){
			Long goodsId = sditem.getGoodsId();
			String key = goodsId+"_"+tallyingUnit;
			Integer num = goodsSumMap.get(key);
			if(num==null) num = 0;
			goodsSumMap.put(key,num + sditem.getNum());
			total += sditem.getNum();
		}
		//4.比较报文商品数量和申报数量是否一致，不一致则报错
		if(jsTotal.intValue()!=total.intValue()){
			logger.error("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
			throw new RuntimeException("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
		}
		for(String key : jsGoodsSumMap.keySet()){
			Integer jsnum = jsGoodsSumMap.get(key);//报文商品数量
			Integer num = goodsSumMap.get(key);//预申报商品数量
			if(jsnum==null) jsnum = 0;
			if(num==null) num = 0;
			if(jsnum.intValue()!=num.intValue()){
				logger.error("报文商品数量与预申报数量不一致，goodsId:" + key.substring(0,key.indexOf("_"))+"，报文数量："+jsnum+",预申报数量:"+num);
				throw new RuntimeException("报文商品数量与预申报数量不一致，goodsId:" + key.substring(0,key.indexOf("_"))+"，报文数量："+jsnum+",预申报数量:"+num);
			}
		}

		//5.按单号拆分报文
		Map<String,List<SaleLoadingDeliveriesMQGoodsListJson>> orderGoodsListMap = new HashMap<>();//存储按单号拆分好的报文 key=销售单号  value=goodsList
		splitJsonByOrder(goodsList, itemList, orderGoodsListMap);

		//6.遍历按单号拆分好的报文逐个生成出库
		List<InventoryFreezeRootJson> freezeList = new ArrayList<>();
		List<InvetAddOrSubtractRootJson> subtractList = new ArrayList<>();
		for(String saleOrderCode : orderGoodsListMap.keySet()){
			List<SaleLoadingDeliveriesMQGoodsListJson> jsGoodList = orderGoodsListMap.get(saleOrderCode);
			rootJson.setOrderCode(saleOrderCode);
			rootJson.setGoodsList(jsGoodList);
			Map<String,Object> retMap = saveSaleOut_new(saleDeclare,rootJson);
			InventoryFreezeRootJson freezeRootJson = (InventoryFreezeRootJson) retMap.get("freezeRootJson");
			InvetAddOrSubtractRootJson subtractRootJson = (InvetAddOrSubtractRootJson) retMap.get("subtractRootJson");
			freezeList.add(freezeRootJson);
			subtractList.add(subtractRootJson);
		}
		//解冻
		if(freezeList.size()>0){
			for(InventoryFreezeRootJson freezeJson : freezeList){
				SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(freezeJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			    logger.info(sendResult.toString());
				System.out.println("freezeJson = " + JSONObject.fromObject(freezeJson).toString());
			}
		}
		//扣减库存
		if(subtractList.size()>0){
			for(InvetAddOrSubtractRootJson subtractJson : subtractList){
				SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(subtractJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				logger.info("subtractJson = " + JSONObject.fromObject(subtractJson).toString());
				logger.info(sendResult.toString());

			}
		}
		//修改预申报单状态为已出库
		saleDeclare.setStatus(DERP_ORDER.SALEDECLARE_STATUS_008);//出库中
		saleDeclareOrderDao.modify(saleDeclare);
		return true;
	}

	/**分摊报文数量按申报单拆分-分摊到申报单商品上
	 * */
	private void splitJsonByOrder(List<SaleLoadingDeliveriesMQGoodsListJson> goodsList, List<SaleDeclareOrderItemModel> itemList,
								  Map<String,List<SaleLoadingDeliveriesMQGoodsListJson>> orderGoodsListMap){

		for(SaleLoadingDeliveriesMQGoodsListJson goods : goodsList){
			//把报文数量分配到预申报商品上
			for(SaleDeclareOrderItemModel sditem : itemList){
				if(sditem.getGoodsId().equals(goods.getGoodsId())){
					int goodsNum = goods.getNum();//报文商品剩余未分配量
					int igoodsNum = sditem.getNum();//申报商品剩余未分配量

					if(goodsNum<=0||igoodsNum<=0) continue;
					int benNum = 0;//本次分配数量
					//报文商品数量<=申报数量,本次分配数量=报文商品数量
					if(goodsNum<=igoodsNum){
						benNum = goodsNum;
					}else if(goodsNum>igoodsNum){
						//报文商品数量>申报数量,本次分配数量=申报数量
						benNum = igoodsNum;
					}
					//报文商品减去本次分配量
					goods.setNum(goods.getNum()-benNum);
					//申报商品减去本次分配量
					sditem.setNum(sditem.getNum()-benNum);

					SaleLoadingDeliveriesMQGoodsListJson goodsTemp = new SaleLoadingDeliveriesMQGoodsListJson();
					BeanUtils.copyProperties(goods,goodsTemp);//复制对象
					goodsTemp.setNum(benNum);
					goodsTemp.setOrderItemId(sditem.getSaleItemId());

					List<SaleLoadingDeliveriesMQGoodsListJson> orderGoodList  = orderGoodsListMap.get(sditem.getSaleOrderCode());
					if(orderGoodList==null) orderGoodList = new ArrayList<>();
					orderGoodList.add(goodsTemp);
					orderGoodsListMap.put(sditem.getSaleOrderCode(),orderGoodList);
				}
			}
		}
	}

	public Map<String,Object> saveSaleOut_new(SaleDeclareOrderModel saleDeclare,SaleLoadingDeliveriesMQRootJson rootJson) throws Exception {

		String orderCode = rootJson.getOrderCode();// 订单号
		String type = rootJson.getType();// 服务类型 业务类型10：B2B 20：B2B2C 必填
		String wayBillNo = rootJson.getWayBillNo();// 运单号非必填
		String logisticsName = rootJson.getLogisticsName();// 物流企业名称非必填
		String blNo = rootJson.getBlNo();// 提单号非必填
		String deliver = rootJson.getDeliverDate();// 发货时间 非必填
		String topidealCode = rootJson.getTopidealCode();

		// 销售
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setMerchantId(rootJson.getMerchantId());
		saleOrderModel.setCode(orderCode);
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if (saleOrderModel == null) {
			logger.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}
		if (DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderModel.getState())) {
			logger.error("订单状态为“待审核”,订单编号" + orderCode);
			throw new RuntimeException("订单状态为“待审核”,订单编号" + orderCode);
		}
		if(null == saleOrderModel.getBuId()){
			logger.error("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
		}
		// 根据仓库id查询 仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo==null ) {
			logger.error("根据销售订单的仓库id没有查询到仓库,订单号:" + orderCode);
			throw new RuntimeException("根据销售订单的仓库id没有查询到仓库,订单号:" + orderCode);
		}

		String saleOrderTallyingUnit = saleOrderModel.getTallyingUnit();// 销售订单中的理货单位
		for(SaleLoadingDeliveriesMQGoodsListJson goodsJson : rootJson.getGoodsList()) {
			//批次效期强校验：1-是 0-否
			if("1".equals(mongo.getBatchValidation())) {
				String batchNo = goodsJson.getBatchNo();
				String productionDate = goodsJson.getProductionDate();
				String overdueDate = goodsJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					logger.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
				}
			}
			//香港仓-检查销售订单中的理货单位跟接口回推的单位是否一致 理货单位(00-托盘，01-箱，02-件)
			String tallyingUnit = goodsJson.getTallyingUnit();
			if("c".equals(mongo.getType())) {
				if (!saleOrderTallyingUnit.equals(tallyingUnit)) {
					logger.error("香港仓 销售订单中的理货单位和接口回推的理货单位不同,订单号:" + orderCode);
					throw new RuntimeException("香港仓 销售订单中的理货单位和接口回推的理货单位不同,订单号:" + orderCode);
				}
			}
		}

		// 查询销售单商品
		SaleOrderItemModel tempsaleOrderItemModel = new SaleOrderItemModel();
		tempsaleOrderItemModel.setOrderId(saleOrderModel.getId());
		List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(tempsaleOrderItemModel);
		Map<Long, SaleOrderItemModel> kwGoodsMap = new HashMap<>();//库位商品 key=库位商品id value=单据商品
		for(SaleOrderItemModel model:saleOrderItemList) {
			kwGoodsMap.put(model.getGoodsId(),model);
		}

		Timestamp deliverDate = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate = Timestamp.valueOf(deliver + " 00:00:00"); // 发货时间
			} else {
				deliverDate = Timestamp.valueOf(deliver);// 发货时间
			}
		}

		// 新增销售出库单表头
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setSaleOrderId(saleOrderModel.getId());// 销售订单id
		sOutDepotModel.setMerchantId(saleOrderModel.getMerchantId());// 商家ID
		sOutDepotModel.setPoNo(saleOrderModel.getPoNo());// PO号
		sOutDepotModel.setOutDepotId(saleOrderModel.getOutDepotId());// 调出仓库id
		sOutDepotModel.setOutDepotName(saleOrderModel.getOutDepotName()); // 调出仓库名称
		sOutDepotModel.setCustomerId(saleOrderModel.getCustomerId());// 客户id(供应商)
		sOutDepotModel.setCustomerName(saleOrderModel.getCustomerName());// 客户名称
		sOutDepotModel.setSaleType(saleOrderModel.getBusinessModel());// 销售类型  1购销  2代销'
		sOutDepotModel.setDeliverDate(deliverDate);// 发货时间
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);// '状态 017,待出库 ,018,已出库,027:出库中
//		sOutDepotModel.setCode(CodeGeneratorUtil.getNo("XSCK"));// 出库清单编号自生成
		sOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));// 出库清单编号自生成
		sOutDepotModel.setMerchantName(saleOrderModel.getMerchantName());// 商家名称
		sOutDepotModel.setSaleOrderCode(saleOrderModel.getCode());// 销售订单编号
		sOutDepotModel.setLogisticsName(logisticsName);// 物流企业名称
		sOutDepotModel.setLbxNo(saleOrderModel.getLbxNo());// LBX单号
		sOutDepotModel.setBlNo(blNo);// 提单号物流企业名称
		sOutDepotModel.setWayBillNo(wayBillNo);// 运单号
		sOutDepotModel.setImportMode(type);// 进口模式 10：BBC;20：BC 30：保留; 40：CC
		sOutDepotModel.setBuId(saleOrderModel.getBuId());// 事业部
		sOutDepotModel.setBuName(saleOrderModel.getBuName());
		sOutDepotModel.setSaleDeclareOrderId(saleDeclare.getId());//预申报id
		sOutDepotModel.setSaleDeclareOrderCode(saleDeclare.getCode());//预申报单号
		// 新增
		saleOutDepotDao.save(sOutDepotModel);

		Map<Long, Integer> outGoodsNumMap = new HashMap<>();
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (SaleLoadingDeliveriesMQGoodsListJson goodsListJson : rootJson.getGoodsList()) {
			// 获取销售订单商品信息
			SaleOrderItemModel orderItemModel = kwGoodsMap.get(goodsListJson.getGoodsId());
			String tallyingUnit = goodsListJson.getTallyingUnit();

			// 新增销售出库表体
			SaleOutDepotItemModel itemModel = new SaleOutDepotItemModel();
			itemModel.setSaleOutDepotId(sOutDepotModel.getId());// 销售出库ID
			itemModel.setGoodsId(goodsListJson.getGoodsId());// 商品id
			itemModel.setGoodsCode(goodsListJson.getGoodsCode());//商品编码
			itemModel.setGoodsName(goodsListJson.getGoodsName());//商品名称
			itemModel.setGoodsNo(goodsListJson.getGoodsNo());//商品货号
			itemModel.setTransferNum(goodsListJson.getNum());//数量
			itemModel.setTransferBatchNo(goodsListJson.getBatchNo());//批次
			itemModel.setBarcode(goodsListJson.getBarcode());//货品条形码
			itemModel.setCommbarcode(goodsListJson.getCommbarcode());//标准条码
			itemModel.setSaleNum(orderItemModel.getNum());
			itemModel.setSaleItemId(goodsListJson.getOrderItemId());//销售单表体id
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				itemModel.setTallyingUnit(saleOrderTallyingUnit);//销售订单理货单位
			}
			String productionDate = goodsListJson.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {
					itemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
				}else {
					itemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
				}
			}
			String overdueDate = goodsListJson.getOverdueDate();//生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
				}else {
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
				}
			}
			//新增销售出库表体
			saleOutDepotItemDao.save(itemModel);

			//库存加减接口商品实体
			InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			subtractGoodsListJson.setGoodsId(String.valueOf(itemModel.getGoodsId()));
			subtractGoodsListJson.setGoodsName(itemModel.getGoodsName());
			subtractGoodsListJson.setGoodsNo(itemModel.getGoodsNo());
			subtractGoodsListJson.setBarcode(itemModel.getBarcode());
			subtractGoodsListJson.setType("0");
			subtractGoodsListJson.setNum(itemModel.getTransferNum());//出库数量
			subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
			subtractGoodsListJson.setBatchNo(itemModel.getTransferBatchNo());//批次
			subtractGoodsListJson.setStockLocationTypeId(String.valueOf(saleOrderModel.getStockLocationTypeId()));
			subtractGoodsListJson.setStockLocationTypeName(saleOrderModel.getStockLocationTypeName());
			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(itemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				String addeUnit=null;
				if ("00".equals(saleOrderTallyingUnit)) {
					addeUnit="0";
				}else if("01".equals(saleOrderTallyingUnit)){
					addeUnit="1";
				}else if("02".equals(saleOrderTallyingUnit)){
					addeUnit="2";
				}
				subtractGoodsListJson.setUnit(addeUnit);// 销售订单 单位	字符串 0 托盘 1箱  2 件
			}
			subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
			subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
			subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
			subtractGoodsListJsonList.add(subtractGoodsListJson);

			Integer outNum = outGoodsNumMap.get(itemModel.getGoodsId());
			if(outNum == null){
				outNum = itemModel.getTransferNum();
			}else{
				outNum = outNum + itemModel.getTransferNum();
			}
			outGoodsNumMap.put(itemModel.getGoodsId(),outNum);
		}

		//拼装解冻商品数据
		List<InventoryFreezeGoodsListJson> freezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (SaleOrderItemModel item : saleOrderItemList) {
			Integer outNum  = outGoodsNumMap.get(item.getGoodsId());
			InventoryFreezeGoodsListJson freezeGoodsJson = new InventoryFreezeGoodsListJson();
			freezeGoodsJson.setGoodsId(String.valueOf(item.getGoodsId()));
			freezeGoodsJson.setGoodsNo(item.getGoodsNo());
			freezeGoodsJson.setGoodsName(item.getGoodsName());
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				String freezeUnit=null;
				if ("00".equals(saleOrderTallyingUnit)) {
					freezeUnit="0";
				}else if("01".equals(saleOrderTallyingUnit)){
					freezeUnit="1";
				}else if("02".equals(saleOrderTallyingUnit)){
					freezeUnit="2";
				}
				freezeGoodsJson.setUnit(freezeUnit);// 销售订单 单位	字符串 0 托盘 1箱  2 件
			}
			String deliverDate1 = null;// 发货时间
			if (StringUtils.isNotBlank(deliver)) {
				if (deliver.length() == 10) {
					deliverDate1 = deliver + " 00:00:00"; // 发货时间
				} else {
					deliverDate1 = deliver;// 发货时间
				}
			}
			freezeGoodsJson.setDivergenceDate(deliverDate1);
//			freezeGoodsJson.setNum(item.getNum());
			freezeGoodsJson.setNum(outNum);
			freezeGoodsListJsonList.add(freezeGoodsJson);
		}
		//释放并减少冻结量
		InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
		freezeRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		freezeRootJson.setMerchantName(saleOrderModel.getMerchantName());
		freezeRootJson.setDepotId(saleOrderModel.getOutDepotId().toString());
		freezeRootJson.setDepotName(saleOrderModel.getOutDepotName());
		freezeRootJson.setOrderNo(sOutDepotModel.getCode());
		freezeRootJson.setBusinessNo(saleOrderModel.getCode());
		freezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		freezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		freezeRootJson.setSourceDate(TimeUtils.formatFullTime());
		freezeRootJson.setOperateType("1");
		freezeRootJson.setGoodsList(freezeGoodsListJsonList);

		//扣减销售出库库存量
		InvetAddOrSubtractRootJson subtractRootJson = new InvetAddOrSubtractRootJson();
		subtractRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		subtractRootJson.setMerchantName(saleOrderModel.getMerchantName());
		subtractRootJson.setTopidealCode(saleOrderModel.getTopidealCode());// 销售订单的卓志编码
		// 事业部
		subtractRootJson.setBuId(String.valueOf(saleOrderModel.getBuId()));
		subtractRootJson.setBuName(saleOrderModel.getBuName());
		subtractRootJson.setDepotId(mongo.getDepotId().toString());
		subtractRootJson.setDepotName(mongo.getName());
		subtractRootJson.setDepotCode(mongo.getCode());
		subtractRootJson.setDepotType(mongo.getType());
		subtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		subtractRootJson.setOrderNo(sOutDepotModel.getCode());
		subtractRootJson.setBusinessNo(saleOrderModel.getCode());
		subtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		subtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		subtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		String deliverDate1 = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate1 = deliver + " 00:00:00"; // 发货时间
			} else {
				deliverDate1 = deliver;// 发货时间
			}
		}
		subtractRootJson.setDivergenceDate(deliverDate1);//发货时间
		subtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));//归属月份
		subtractRootJson.setGoodsList(subtractGoodsListJsonList);
		subtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());//回调标签
		subtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());//回调主题
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		customParam.put("code", sOutDepotModel.getCode());// 销售订单内部单号
		subtractRootJson.setCustomParam(customParam);////自定义回调参数

		// 修改销售出库单
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setState(DERP_ORDER.SALEORDER_STATE_027);
		sModel.setId(saleOrderModel.getId());
		saleOrderDao.modify(sModel);

		Map<String,Object> retMap = new HashMap<>();
		retMap.put("freezeRootJson",freezeRootJson);
		retMap.put("subtractRootJson",subtractRootJson);
		return retMap;
	}
	/**装载交运业务处理方法-新end**预申报****************************************************************************/
}
