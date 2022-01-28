package com.topideal.service.api.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.api.v1_2.PurchaseStorageDeclareBatchListJson;
import com.topideal.json.api.v1_2.PurchaseStorageDeclareGoodsListJson;
import com.topideal.json.api.v1_2.PurchaseStorageDeclareRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.purchase.PurchaseStorageDeclareService;
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
 * 入库申报接口实现类
 *
 * @author zhanghx 2018/7/16
 */
@Service
public class PurchaseStorageDeclareServiceImpl implements PurchaseStorageDeclareService {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseStorageDeclareServiceImpl.class);

	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private DeclareOrderDao declareOrderDao;// 预申报单
	@Autowired
	private TallyingOrderDao tallyingOrderDao;// 理货单
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;// 采购入库单
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;// 采购入库商品表
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;// 采购入库商品批次表
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;// 采购入库单关联采购订单表
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;// 采购订单
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private DeclareOrderItemDao declareOrderItemDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203110100, model = DERP_LOG_POINT.POINT_12203110100_Label, keyword = "entInboundId")
	public boolean saveStorageDeclareInfo(String json, String keys, String topics, String tags) throws Exception {
		logger.info("入库申报消费端开始json" + json);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList", PurchaseStorageDeclareGoodsListJson.class);
		classMap.put("receiveList", PurchaseStorageDeclareBatchListJson.class);
		// JSON对象转实体
		PurchaseStorageDeclareRootJson rootJson = (PurchaseStorageDeclareRootJson) JSONObject.toBean(jsonData, PurchaseStorageDeclareRootJson.class, classMap);
		List<PurchaseStorageDeclareGoodsListJson> goodsList = rootJson.getGoodsList();
		String orderCode = rootJson.getEntInboundId();
		 /**1.保存唯一单号表*/
		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(7);// 订单来源1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			logger.info("单号已经存在保存失败");
			throw new RuntimeException("单号已经存在保存失败");
		}
        /**2.判断预申报单是否存在*/
		DeclareOrderModel declareModel = new DeclareOrderModel();
		declareModel.setMerchantId(rootJson.getMerchantId());
		declareModel.setCode(orderCode);// 入库申报单号
		declareModel = declareOrderDao.searchByModel(declareModel);
        if(declareModel==null){
			logger.info("采购预申报单不存在");
			throw new RuntimeException("采购预申报单不存在");
		}
        if(!DERP_ORDER.DECLAREORDER_STATUS_004.equals(declareModel.getStatus())){
			logger.info("采购预申报单非待入仓状态");
			throw new RuntimeException("采购预申报单非待入仓状态");
		}
        PurchaseWarehouseModel queryWarehouseModel = new PurchaseWarehouseModel();
		queryWarehouseModel.setDeclareOrderCode(orderCode);
		List<PurchaseWarehouseModel> queryWarehouseList = purchaseWarehouseDao.list(queryWarehouseModel);
		if(queryWarehouseList != null && queryWarehouseList.size() > 0) {
			logger.error("预申报单：" + orderCode + "出库单已存在  保存失败");
			throw new RuntimeException("预申报单：" + orderCode + "出库单已存在  保存失败");

		}

		DeclareOrderItemModel itemModel = new DeclareOrderItemModel();
		itemModel.setDeclareOrderId(declareModel.getId());
		List<DeclareOrderItemModel> declareItemList = declareOrderItemDao.listOrderByPrice(itemModel);
        /**检查预申报单表体采购单id是否为空*/
        for(DeclareOrderItemModel item : declareItemList){
        	if(item.getPurchaseItemId()==null||StringUtils.isBlank(item.getPurchase())){
				logger.info("预申报表体采购单表体id|采购单号为空"+item.getId());
				throw new RuntimeException("预申报表体采购单表体id|采购单号为空"+item.getId());
			}
		}

        /**3.合并报文相同商品数量 及总量*/
		Integer jsTotal = 0;//报文总量
		Map<String,Integer> jsGoodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(PurchaseStorageDeclareGoodsListJson goods : goodsList){
			String key = goods.getGoodsId().toString();
			for(PurchaseStorageDeclareBatchListJson batch : goods.getReceiveList()){
				Integer num = 0;
				if(batch.getNormalNum()!=null) num += batch.getNormalNum();
				if(batch.getExpireNum()!=null) num += batch.getExpireNum();
				if(batch.getWornNum()!=null) num += batch.getWornNum();
				Integer goodsNum = jsGoodsSumMap.get(key);
				if(goodsNum==null) goodsNum = 0;
				jsGoodsSumMap.put(key,num + goodsNum);
				jsTotal += num;
			}
		}
		//4.合并预申报表体相同商品数量、及总量
		Integer total = 0;//预申报总量
		Map<String,Integer> goodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(DeclareOrderItemModel dcitem : declareItemList){
			String key = dcitem.getGoodsId().toString();
			Integer num = goodsSumMap.get(key);
			if(num==null) num = 0;
			goodsSumMap.put(key,num + dcitem.getNum());
			total += dcitem.getNum();
		}
		//5.比较报文商品数量和申报数量是否一致，不一致则报错
		if(jsTotal.intValue()!=total.intValue()){
			logger.info("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
			throw new RuntimeException("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
		}
		for(String key : jsGoodsSumMap.keySet()){
			Integer jsnum = jsGoodsSumMap.get(key);//报文商品数量
			Integer num = goodsSumMap.get(key);//预申报商品数量
			if(jsnum==null) jsnum = 0;
			if(num==null) num = 0;
			if(jsnum.intValue()!=num.intValue()){
				logger.info("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
				throw new RuntimeException("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
			}
		}

        /**4.按单号拆分报文*/
		Map<String,List<PurchaseStorageDeclareGoodsListJson>> orderGoodsListMap = new HashMap<>();//存储按单号拆分好的报文 key=采购单号  value=goodsList
		//type E-过期数量 W-坏品数量 N-正常数量
		//先分摊过期品
		splitJsonByOrder("E",goodsList,declareItemList,orderGoodsListMap);
		//再分摊坏品
		splitJsonByOrder("W",goodsList,declareItemList,orderGoodsListMap);
		//再分摊正常品
		splitJsonByOrder("N",goodsList,declareItemList,orderGoodsListMap);

		//5.遍历按单号拆分好的报文生成入库单
		for(String purchaseCode : orderGoodsListMap.keySet()){
			List<PurchaseStorageDeclareGoodsListJson> jsGoodList = orderGoodsListMap.get(purchaseCode);
			rootJson.setEntInboundId(purchaseCode);//采购单号
			rootJson.setGoodsList(jsGoodList);
			saveStorageDeclareInfo_new(declareModel,rootJson);
		}

		return true;
	}

	/**分摊报文数量按采购单拆分-分摊到申报单商品上
	 * type N-正常数量 E-过期数量 W-坏品数量
	 * */
	private void splitJsonByOrder(String type,List<PurchaseStorageDeclareGoodsListJson> goodsList,List<DeclareOrderItemModel> declareItemList,
	                         Map<String,List<PurchaseStorageDeclareGoodsListJson>> orderGoodsListMap){
		//把报文数量分配到预申报商品上
		for(PurchaseStorageDeclareGoodsListJson goods : goodsList) {//start---------001
			for(PurchaseStorageDeclareBatchListJson batch : goods.getReceiveList()) {//start------002
				for(DeclareOrderItemModel sditem : declareItemList) {//start-----------------003

					if (sditem.getGoodsId().equals(goods.getGoodsId())) {

						int goodsNum = 0;//报文商品数量
						if (type.equals("N")) {
							goodsNum = batch.getNormalNum() != null ? batch.getNormalNum() : 0;
						} else if (type.equals("E")) {
							goodsNum = batch.getExpireNum() != null ? batch.getExpireNum() : 0;
						} else if (type.equals("W")) {
							goodsNum = batch.getWornNum() != null ? batch.getWornNum() : 0;
						}
						int igoodsNum = sditem.getNum();//申报商品剩余未分配量
						if (goodsNum <= 0 || igoodsNum <= 0) continue;

						int benNum = 0;//本次分配数量
						//报文商品数量<=申报数量,本次分配数量=报文商品数量
						if (goodsNum <= igoodsNum) {
							benNum = goodsNum;
						} else if (goodsNum > igoodsNum) {
							//报文商品数量>申报数量,本次分配数量=申报数量
							benNum = igoodsNum;
						}

						PurchaseStorageDeclareBatchListJson batchTemp = new PurchaseStorageDeclareBatchListJson();
						BeanUtils.copyProperties(batch, batchTemp);//复制对象
						//报文商品减去本次分配量
						if (type.equals("N")) {
							batch.setNormalNum(batch.getNormalNum() - benNum);//报文
							batchTemp.setNormalNum(benNum);//本次分配
							batchTemp.setExpireNum(0);
							batchTemp.setWornNum(0);
						} else if (type.equals("E")) {
							batch.setExpireNum(batch.getExpireNum() - benNum);//报文
							batchTemp.setNormalNum(0);//本次分配
							batchTemp.setExpireNum(benNum);
							batchTemp.setWornNum(0);
						} else if (type.equals("W")) {
							batch.setWornNum(batch.getWornNum() - benNum);//报文
							batchTemp.setNormalNum(0);//本次分配
							batchTemp.setExpireNum(0);
							batchTemp.setWornNum(benNum);
						}
						//申报商品减去本次分配量
						sditem.setNum(sditem.getNum() - benNum);

						List<PurchaseStorageDeclareBatchListJson> batchList = new ArrayList<>();
						batchList.add(batchTemp);

						PurchaseStorageDeclareGoodsListJson goodsTemp = new PurchaseStorageDeclareGoodsListJson();
						BeanUtils.copyProperties(goods, goodsTemp);//复制对象
						goodsTemp.setPurchaseItemId(sditem.getPurchaseItemId());//采购单表体id
						goodsTemp.setReceiveList(batchList);

						List<PurchaseStorageDeclareGoodsListJson> orderGoodList = orderGoodsListMap.get(sditem.getPurchase());
						if (orderGoodList == null) orderGoodList = new ArrayList<>();
						orderGoodList.add(goodsTemp);
						orderGoodsListMap.put(sditem.getPurchase(), orderGoodList);
					}

				}//end------003

			}//end------002

		}//end---------001
	}

	public boolean saveStorageDeclareInfo_new(DeclareOrderModel declareModel,PurchaseStorageDeclareRootJson rootJson) throws Exception {

		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setCode(rootJson.getEntInboundId());// 采购订单号
		purchaseOrderModel.setMerchantId(rootJson.getMerchantId());
		purchaseOrderModel = purchaseOrderDao.searchByModel(purchaseOrderModel);
		if(purchaseOrderModel == null) {
			logger.info("采购订单不存在:订单号" + rootJson.getEntInboundId());
			throw new RuntimeException("采购订单不存在:订单号" + rootJson.getEntInboundId());
		}
		if(!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())
		   ||DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrderModel.getStatus()))) {
			logger.info("订单状态非已审核:订单号" + rootJson.getEntInboundId());
			throw new RuntimeException("订单状态非已审核:订单号" + rootJson.getEntInboundId());
		}
		// 根据理货单号查询理货单信息
		TallyingOrderModel tModel = new TallyingOrderModel();
		tModel.setOrderCode(declareModel.getCode());
		tModel.setState(DERP_ORDER.TALLYINGORDER_STATE_010);// 009:待确认 010:已确认 004:已驳回'
		tModel.setMerchantId(rootJson.getMerchantId());
		tModel = tallyingOrderDao.searchByModel(tModel);
		// 如果理货单是null 采购入库单中理货单存空值就可以了
		if (null == tModel)  tModel = new TallyingOrderModel();

		//获取仓库
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", declareModel.getDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(params);
		if(depot==null){
			logger.info("仓库不存在");
			throw new RuntimeException("仓库不存在");
		}
		/**仓库类型是海外仓，校验报文理货单位和预申报单|采购单理货单位是否一致*/
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
			for (PurchaseStorageDeclareGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				if (StringUtils.isBlank(goodsListJson.getTallyingUnit())) {
					logger.info("报文理货单位为空");
					throw new RuntimeException("报文理货单位为空");
				}
				if (!goodsListJson.getTallyingUnit().equals(declareModel.getTallyingUnit())) {
					logger.info("报文的理货单位和申报单不一致");
					throw new RuntimeException("报文的理货单位和申报单不一致");
				}
			}
		}
		/**校验仓库批次强校验 1-强校验 2-入库强校验*/
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())
				|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())) {
			for (PurchaseStorageDeclareGoodsListJson goodsListJson : rootJson.getGoodsList()) {// 商品
				for (PurchaseStorageDeclareBatchListJson receive : goodsListJson.getReceiveList()) {// 批次
					if (StringUtils.isBlank(receive.getBatchNo()) || StringUtils.isBlank(receive.getProductionDate())
							|| StringUtils.isBlank(receive.getOverdueDate())) {
						logger.info(depot.getName() + "设置了入库批次效期强校验批次和效期不能为空");
						throw new RuntimeException(depot.getName() + ",设置了入库批次效期强校验批次和效期不能为空");
					}
				}
			}
		}

		/**保存入库单表头*/
		PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
		purchaseWarehouseModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 采购入库编号
		purchaseWarehouseModel.setTallyingOrderId(tModel.getId());// 理货单id
		purchaseWarehouseModel.setTallyingOrderCode(tModel.getCode());// 理货单号
		purchaseWarehouseModel.setExtraCode(rootJson.getTallyingOrderCode()); // 外部单号
		purchaseWarehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
		purchaseWarehouseModel.setConfirmDate(tModel.getConfirmDate());// 确认理货时间
		purchaseWarehouseModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_011);// 011:待入仓,012:已入仓
		purchaseWarehouseModel.setPalletNum(rootJson.getPalletNum());// 托板数量
		purchaseWarehouseModel.setCrossStatus(DERP_ORDER.PURCHASEWAREHOUSE_CROSSSTATUS_0);// 采购勾稽状态(1-是,0-否)'
		purchaseWarehouseModel.setTallyingUnit(declareModel.getTallyingUnit());// 理货单位
		purchaseWarehouseModel.setDeclareOrderId(declareModel.getId());// 预申报单id
		purchaseWarehouseModel.setDeclareOrderCode(declareModel.getCode());// 预申报单编号
		purchaseWarehouseModel.setDepotId(declareModel.getDepotId());// 仓库id
		purchaseWarehouseModel.setDepotName(declareModel.getDepotName());// 仓库名称
		purchaseWarehouseModel.setMerchantId(declareModel.getMerchantId());// 商家ID
		purchaseWarehouseModel.setMerchantName(declareModel.getMerchantName());// 商家名称
		purchaseWarehouseModel.setContractNo(declareModel.getContractNo());// 合同号
		purchaseWarehouseModel.setBuId(declareModel.getBuId());
		purchaseWarehouseModel.setBuName(declareModel.getBuName());
		purchaseWarehouseModel.setBusinessModel(purchaseOrderModel.getBusinessModel());
		purchaseWarehouseModel.setCurrency(purchaseOrderModel.getCurrency());
		String tallyingDate = rootJson.getTallyingDate();
		if (tallyingDate.length() == 10) {
			purchaseWarehouseModel.setTallyingDate(Timestamp.valueOf(tallyingDate + " 00:00:00"));// 理货时间
		} else {
			purchaseWarehouseModel.setTallyingDate(Timestamp.valueOf(tallyingDate));// 理货时间
		}
		//保存入库单
		purchaseWarehouseDao.save(purchaseWarehouseModel);

		//存储入库单跟采购单关系表
		WarehouseOrderRelModel wRelModel = new WarehouseOrderRelModel();
		wRelModel.setWarehouseId(purchaseWarehouseModel.getId());// 采购入库单id
		wRelModel.setPurchaseOrderId(purchaseOrderModel.getId());// 采购订单ID
		warehouseOrderRelDao.save(wRelModel);

		//统计每个商品的采购量key=goosNo_采购表体id
		DeclareOrderItemModel queryItem = new DeclareOrderItemModel();
		queryItem.setDeclareOrderId(declareModel.getId());
		queryItem.setPurchaseId(purchaseOrderModel.getId());
		List<DeclareOrderItemModel> dcItemList = declareOrderItemDao.list(queryItem);
		Map<String,Integer> purchaseNumMap = new HashMap<>();//订单商品采购量 key=goosNo_采购表体id
		for(DeclareOrderItemModel dcItem : dcItemList){
			String key = dcItem.getGoodsNo()+"_"+dcItem.getPurchaseItemId();
			Integer num = purchaseNumMap.get(key);
			if(num==null){
				purchaseNumMap.put(key,dcItem.getNum());
			}else{
				purchaseNumMap.put(key,num+dcItem.getNum());
			}
		}

		//合并相同商品key=货号_采购表体id 合并正常量、理货量
		Map<String,PurchaseStorageDeclareGoodsListJson> jsongoodsMergMap = new HashMap<>();
		for (PurchaseStorageDeclareGoodsListJson goodsJson : rootJson.getGoodsList()) {
             String key = goodsJson.getGoodsNo()+"_"+goodsJson.getPurchaseItemId();
			 PurchaseStorageDeclareGoodsListJson goodsJsonTemp = jsongoodsMergMap.get(key);
            //累计正常量、理货量
            Integer normalNum = 0 ;
            Integer tallyingNum = 0 ;
			 if(goodsJsonTemp==null){
                 for(PurchaseStorageDeclareBatchListJson batch : goodsJson.getReceiveList()){
                     normalNum += batch.getNormalNum() + batch.getExpireNum() ;
                     tallyingNum += batch.getNormalNum() + batch.getExpireNum() + batch.getWornNum() ;
                 }
                 goodsJson.setTotoalNormalNum(normalNum);
                 goodsJson.setTallyingNum(tallyingNum);
                 jsongoodsMergMap.put(goodsJson.getGoodsNo()+"_"+goodsJson.getPurchaseItemId(),goodsJson);
			 }else{
                 List<PurchaseStorageDeclareBatchListJson> receiveList = goodsJsonTemp.getReceiveList();
                 for(PurchaseStorageDeclareBatchListJson batch : goodsJson.getReceiveList()){
                     normalNum += batch.getNormalNum() + batch.getExpireNum() ;
                     tallyingNum += batch.getNormalNum() + batch.getExpireNum() + batch.getWornNum() ;
                     receiveList.add(batch);
                 }
                 goodsJsonTemp.setTotoalNormalNum(goodsJsonTemp.getTotoalNormalNum()+normalNum);
                 goodsJsonTemp.setTallyingNum(goodsJsonTemp.getTallyingNum()+tallyingNum);
                 goodsJsonTemp.setReceiveList(receiveList);
             }

		}

        /**保存入库单表体、批次表*/
		for (PurchaseStorageDeclareGoodsListJson goodsJson : jsongoodsMergMap.values()) {
		    //货号_采购表体id的采购量
            Integer purchaseNum = purchaseNumMap.get(goodsJson.getGoodsNo()+"_"+goodsJson.getPurchaseItemId());
            if(purchaseNum==null) purchaseNum = 0;
			// 新增采购入库单商品详情
			PurchaseWarehouseItemModel warehouseItemModel = new PurchaseWarehouseItemModel();
			warehouseItemModel.setWarehouseId(purchaseWarehouseModel.getId());// 采购入库单id
			warehouseItemModel.setBarcode(goodsJson.getBarcode());
			warehouseItemModel.setGoodsName(goodsJson.getGoodsName());
			warehouseItemModel.setGoodsId(goodsJson.getGoodsId());// 商品ID
			warehouseItemModel.setGoodsNo(goodsJson.getGoodsNo());
			warehouseItemModel.setGrossWeight(goodsJson.getGrossWeight());// 货品毛重
			warehouseItemModel.setNetWeight(goodsJson.getNetWeight());// 货品净重
			warehouseItemModel.setVolume(goodsJson.getVolume());// 货品体积
			warehouseItemModel.setLength(goodsJson.getLength());// 货品长
			warehouseItemModel.setWidth(goodsJson.getWidth());// 货品宽
			warehouseItemModel.setHeight(goodsJson.getHeight());// 货品高
			warehouseItemModel.setTallyingUnit(declareModel.getTallyingUnit());// 理货单位
			warehouseItemModel.setPurchaseItemId(goodsJson.getPurchaseItemId());
            warehouseItemModel.setPurchaseNum(purchaseNum);//采购量
            warehouseItemModel.setNormalNum(goodsJson.getTotoalNormalNum());// 正常数量
            warehouseItemModel.setTallyingNum(goodsJson.getTallyingNum());// 理货数量
            if(warehouseItemModel.getTallyingNum() > warehouseItemModel.getPurchaseNum()) {
                warehouseItemModel.setMultiNum(warehouseItemModel.getTallyingNum() - warehouseItemModel.getPurchaseNum());// 多货数量
            }else if(warehouseItemModel.getTallyingNum() < warehouseItemModel.getPurchaseNum()) {
                warehouseItemModel.setLackNum(warehouseItemModel.getPurchaseNum() - warehouseItemModel.getTallyingNum());// 缺少数量
            }

			purchaseWarehouseItemDao.save(warehouseItemModel);

			for (PurchaseStorageDeclareBatchListJson batchJson : goodsJson.getReceiveList()) {
				//新增采购入库单
				PurchaseWarehouseBatchModel warehouseBatchModel = new PurchaseWarehouseBatchModel();
				warehouseBatchModel.setItemId(warehouseItemModel.getId());// 采购入库商品列表id
				warehouseBatchModel.setGoodsId(warehouseItemModel.getGoodsId());// 商品ID
				warehouseBatchModel.setGoodsNo(warehouseItemModel.getGoodsNo());// 商品ID
				warehouseBatchModel.setBatchNo(batchJson.getBatchNo());// 批次号
				warehouseBatchModel.setWornNum(batchJson.getWornNum());// 坏货数量
				warehouseBatchModel.setExpireNum(batchJson.getExpireNum());// 过期数量
				warehouseBatchModel.setNormalNum(batchJson.getNormalNum());// 正常数量
				warehouseBatchModel.setProductionDate(TimeUtils.parseDay(batchJson.getProductionDate()));// 生产日期
				warehouseBatchModel.setOverdueDate(TimeUtils.parseDay(batchJson.getOverdueDate()));// 失效时间
                //保存
                purchaseWarehouseBatchDao.save(warehouseBatchModel);
			}

		}

		logger.info("入库申报消费端结束");
		return true;
	}

}
