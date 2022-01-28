package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v2_1.SaleStorageDeclareBatchListIson;
import com.topideal.json.api.v2_1.SaleStorageDeclareGoodsListJson;
import com.topideal.json.api.v2_1.SaleStorageDeclareRootIson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.api.sale.XSTStorageDeclareService;
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
 */
@Service
public class XSTStorageDeclareServiceImpl implements XSTStorageDeclareService{
	private static final Logger logger = LoggerFactory.getLogger(XSTStorageDeclareServiceImpl.class);
	@Autowired 
	private SaleReturnOdepotDao saleReturnOdepotDao;//销售退货出库		
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;// 销售退货出库商品	
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;//销售退货出入库		
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;//销售退货出入库商品
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;// 销售退货表头
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;// 销售退货表体
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	//销售退预申报
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;
	//销售退预申报表体
	@Autowired
	private SaleReturnDeclareOrderItemDao saleReturnDeclareOrderItemDao;
	@Autowired 
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息
	//仓库商家关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	//保存入库申报信息
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203120100,model=DERP_LOG_POINT.POINT_12203120100_Label,keyword="entInboundId")
	public boolean saveStorageDeclareInfo(String json,String keys,String topics,String tags) throws Exception {
		// 将字符串转成json
		JSONObject jsonData =JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleStorageDeclareGoodsListJson.class);
		classMap.put("receiveList",SaleStorageDeclareBatchListIson.class);
		// JSON对象转实体
		SaleStorageDeclareRootIson rootJson = (SaleStorageDeclareRootIson) JSONObject.toBean(jsonData, SaleStorageDeclareRootIson.class, classMap);
		//单号
		String entInboundId = rootJson.getEntInboundId();
		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(entInboundId);
			orderExternalCodeModel.setOrderSource(8);	// 1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			logger.error("外部单号来源表已经存在 单号：" + entInboundId + "  保存失败");
			throw new RuntimeException("外部单号来源表已经存在 单号：" + entInboundId + "  保存失败");
		}

		saveStorageDeclareInfo_new(rootJson);

		return true;
	}


	//保存入库申报信息_新
	public void saveStorageDeclareInfo_new(SaleStorageDeclareRootIson rootJson) throws Exception {
		//单号
		String entInboundId = rootJson.getEntInboundId();
		List<SaleStorageDeclareGoodsListJson> goodsList = rootJson.getGoodsList();

		//1.查询预申报单 是否存在
		SaleReturnDeclareOrderModel declareOrderModel = new SaleReturnDeclareOrderModel();
		declareOrderModel.setCode(entInboundId);
		declareOrderModel = saleReturnDeclareOrderDao.searchByModel(declareOrderModel);
		if(declareOrderModel==null){
			logger.error("销售退预申报单不存在,单号：" + entInboundId );
			throw new RuntimeException("销售退预申报单不存在,单号：" + entInboundId);
		}
		SaleReturnDeclareOrderItemModel itemModel = new SaleReturnDeclareOrderItemModel();
		itemModel.setReturnDeclareOrderId(declareOrderModel.getId());
		List<SaleReturnDeclareOrderItemModel> itemList = saleReturnDeclareOrderItemDao.list(itemModel);

		//2.合并报文相同商品数量、及总量
		Integer jsTotal = 0;//报文总量
		Map<String,Integer> jsGoodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(SaleStorageDeclareGoodsListJson goods : goodsList){
			String key = goods.getGoodsId().toString();
			for(SaleStorageDeclareBatchListIson bacth : goods.getReceiveList()){
				Integer normalNum = bacth.getNormalNum()==null?0:bacth.getNormalNum();//正常数量
				Integer expireNum = bacth.getExpireNum()==null?0:bacth.getExpireNum();//过期数量
				Integer wornNum = bacth.getWornNum()==null?0:bacth.getWornNum();//坏品数量
				Integer num = jsGoodsSumMap.get(key)==null?0:jsGoodsSumMap.get(key);//累计
				num += normalNum + expireNum + wornNum;
				jsTotal += normalNum + expireNum + wornNum;
				jsGoodsSumMap.put(key,num);
			}

		}
		//合并预申报表体相同商品数量、及总量
		Integer total = 0;//预申报总量
		Map<String,Integer> goodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(SaleReturnDeclareOrderItemModel sditem : itemList){
			Long goodsId = sditem.getInGoodsId();
			String key = goodsId+"";
			Integer num = goodsSumMap.get(key)==null?0:goodsSumMap.get(key);
			goodsSumMap.put(key,num + sditem.getNum());
			total += sditem.getNum();
		}
		//3.比较报文商品数量和申报数量是否一致，不一致则报错
		if(jsTotal.intValue()!=total.intValue()){
			logger.error("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
			throw new RuntimeException("报文商品总量与预申报总量不一致报文总量："+jsTotal+",预申报总量:"+total);
		}
		for(String key : jsGoodsSumMap.keySet()){
			Integer jsnum = jsGoodsSumMap.get(key)==null?0:jsGoodsSumMap.get(key);//报文商品数量
			Integer num = goodsSumMap.get(key)==null?0:goodsSumMap.get(key);//预申报商品数量
			if(jsnum.intValue()!=num.intValue()){
				logger.error("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
				throw new RuntimeException("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
			}
		}
		//4.按单号拆分报文
		Map<String,List<SaleStorageDeclareGoodsListJson>> orderGoodsListMap = new HashMap<>();//存储按单号拆分好的报文 key=销售单号  value=goodsList
		for(SaleStorageDeclareGoodsListJson goods : goodsList){
			for(SaleStorageDeclareBatchListIson batch : goods.getReceiveList()){
				//为空则设置为0，避免后面判断空指针
				if(batch.getNormalNum()==null) batch.setNormalNum(0);//正常数量
				if(batch.getExpireNum()==null) batch.setExpireNum(0);//过期数量
				if(batch.getWornNum()==null) batch.setWornNum(0);//坏品数量
				if(batch.getNormalNum().intValue()<=0 && batch.getExpireNum().intValue()<=0 && batch.getWornNum().intValue()<=0) continue;
				//把报文数量分配到预申报商品上
				for(SaleReturnDeclareOrderItemModel sditem : itemList){
					if(sditem.getInGoodsId().equals(goods.getGoodsId())){

						if(sditem.getNum().intValue()<=0) continue;//申报商品剩余未分配量

						/****分配好品****/
						Integer benNormalNum = 0;//本次分配数量
						//报文商品数量<=申报数量,本次分配数量=报文商品数量
						if(batch.getNormalNum().intValue()<=sditem.getNum().intValue()){
							benNormalNum = batch.getNormalNum();
						}else{
							//报文商品数量>申报数量,本次分配数量=申报数量
							benNormalNum = sditem.getNum();
						}
						//报文商品减去本次分配量
					    batch.setNormalNum(batch.getNormalNum() - benNormalNum);
						//申报商品减去本次分配量
						sditem.setNum(sditem.getNum() - benNormalNum);

					    /****分配过期品****/
						Integer benExpireNum = 0;//本次分配数量
						//报文商品数量<=申报数量,本次分配数量=报文商品数量
						if(batch.getExpireNum()<=sditem.getNum()){
							benExpireNum = batch.getExpireNum();
						}else{
							//报文商品数量>申报数量,本次分配数量=申报数量
							benExpireNum = sditem.getNum();
						}
						//报文商品减去本次分配量
						batch.setExpireNum(batch.getExpireNum() - benExpireNum);
						//申报商品减去本次分配量
						sditem.setNum(sditem.getNum() - benExpireNum);

					    /****分配坏品****/
						Integer benWornNum = 0;//本次分配数量
						//报文商品数量<=申报数量,本次分配数量=报文商品数量
						if(batch.getWornNum()<=sditem.getNum()){
							benWornNum = batch.getWornNum();
						}else{
							//报文商品数量>申报数量,本次分配数量=申报数量
							benWornNum = sditem.getNum();
						}
						//报文商品减去本次分配量
						batch.setWornNum(batch.getWornNum() - benWornNum);
						//申报商品减去本次分配量
						sditem.setNum(sditem.getNum() - benWornNum);

						SaleStorageDeclareBatchListIson batchTemp = new SaleStorageDeclareBatchListIson();
						BeanUtils.copyProperties(batch,batchTemp);//复制对象
						batchTemp.setNormalNum(benNormalNum);
						batchTemp.setExpireNum(benExpireNum);
						batchTemp.setWornNum(benWornNum);
						batchTemp.setPoNo(sditem.getPoNo());
						List<SaleStorageDeclareBatchListIson> receiveListTemp = new ArrayList<>();
						receiveListTemp.add(batchTemp);

						SaleStorageDeclareGoodsListJson goodsTemp = new SaleStorageDeclareGoodsListJson();
						BeanUtils.copyProperties(goods,goodsTemp);//复制对象
						goodsTemp.setReceiveList(receiveListTemp);

						List<SaleStorageDeclareGoodsListJson> orderGoodList  = orderGoodsListMap.get(sditem.getSaleReturnOrderCode());
						if(orderGoodList==null) orderGoodList = new ArrayList<>();
						orderGoodList.add(goodsTemp);
						orderGoodsListMap.put(sditem.getSaleReturnOrderCode(),orderGoodList);
					}
				}
			}

		}

		//6.遍历按单号拆分好的报文逐个生成出库
		for(String saleReturnOrderCode : orderGoodsListMap.keySet()){
			List<SaleStorageDeclareGoodsListJson> jsGoodList = orderGoodsListMap.get(saleReturnOrderCode);
			rootJson.setEntInboundId(saleReturnOrderCode);//销售退货订单号
			rootJson.setGoodsList(jsGoodList);
			saveSaleReturnIn_new(declareOrderModel,rootJson);
		}

		//7.修改预申报单状态为待入仓
		declareOrderModel.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_003);//待入仓
		saleReturnDeclareOrderDao.modify(declareOrderModel);
	}
	//保存入库申报信息
	public void saveSaleReturnIn_new(SaleReturnDeclareOrderModel declareOrderModel,SaleStorageDeclareRootIson rootJson) throws Exception {
		//销售退货单号
		String entInboundId = rootJson.getEntInboundId();

		// 销售退货模块 以入定出
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setMerchantId(rootJson.getMerchantId());
		saleReturnOrderModel.setCode(entInboundId);// 销售退货模块
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		if (saleReturnOrderModel == null) {
			logger.error("没有查到对应的订单:订单号"+entInboundId);
			throw new RuntimeException("没有查到对应的订单:订单号"+entInboundId);
		}
		if (DERP_ORDER.SALERETURNORDER_STATUS_001.equals(saleReturnOrderModel.getStatus())) {
			logger.error("订单状态为“待审核”,订单编号" + saleReturnOrderModel.getCode());
			throw new RuntimeException("订单状态为“待审核”,订单编号" + saleReturnOrderModel.getCode());
		}
		if(null == declareOrderModel.getBuId()){
			logger.error("销售退货预申报单,订单编号" + declareOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售退货预申报单,订单编号" + declareOrderModel.getCode()+"事业部的值为空");
		}
		// 获取该事业部信息
		Map<String, Object> buMap = new HashMap<String, Object>();
		buMap.put("merchantId", declareOrderModel.getMerchantId());
		buMap.put("buId", declareOrderModel.getBuId());
		buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
		if(merchantBuRelMongo == null){
			logger.error("订单编号"+declareOrderModel.getCode()+"事业部ID为："+declareOrderModel.getBuId()+",未查到该商家下事业部信息");
			throw new RuntimeException("订单编号"+declareOrderModel.getCode()+"事业部ID为："+declareOrderModel.getBuId()+",未查到该商家下事业部信息");
		}

		DepotInfoMongo outDepotInfoMongo = null;
		if(declareOrderModel.getOutDepotId()!=null){
			// 根据仓库id到mongoDB中查询 仓库信息
			Map<String, Object> outDepotInfoMap= new HashMap<>();
			outDepotInfoMap.put("depotId",saleReturnOrderModel.getOutDepotId());//调出仓库id	getOutDepotId
			outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);//调出仓库信息
			if (outDepotInfoMongo ==null) {
				logger.error("销售退订单的出库仓库mongodb未查到仓库信息,订单编号"+entInboundId);
				throw new RuntimeException("销售退订单的出库仓库mongodb未查到仓库信息,订单编号"+entInboundId);
			}
			if (outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
				logger.error("销售退订单的出库仓为海外仓，不走本接口" + entInboundId);
				throw new RuntimeException("销售退订单的出库仓为海外仓，不走本接口" + entInboundId);
			}
		}

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> inDepotInfoMap= new HashMap<>();
		inDepotInfoMap.put("depotId", declareOrderModel.getInDepotId());//调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);
		if (inDepotInfoMongo ==null) {
			logger.error("销售退订单的入仓库mongodb未查到仓库信息,订单编号"+entInboundId);
			throw new RuntimeException("销售退订单的入仓库mongodb未查到仓库信息,订单编号"+entInboundId);
		}
		if (inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
			logger.error("销售退订单的入库仓为海外仓，不走本接口" + entInboundId);
			throw new RuntimeException("销售退订单的入库仓为海外仓，不走本接口" + entInboundId);
		}

		// 销售退货入库单
		SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
		saleReturnIdepotModel.setOrderCode(entInboundId);// 销售退货编号
		saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
		// --------以入定出 销售退货货出库单 t_sale_return_odepot--------

		if (saleReturnIdepotModel !=null) {
			logger.error("销售退货入库单已经存在,订单编号"+entInboundId);
			throw new RuntimeException("销售退货入库单已经存在,订单编号"+entInboundId);
		}

		// 查询销售退预申报表体当前销售退订单的商品
		SaleReturnDeclareOrderItemModel returnDeclareOrderItemModel = new SaleReturnDeclareOrderItemModel();
		returnDeclareOrderItemModel.setReturnDeclareOrderId(declareOrderModel.getId());
		returnDeclareOrderItemModel.setSaleReturnOrderId(saleReturnOrderModel.getId());
		List<SaleReturnDeclareOrderItemModel> itemList = saleReturnDeclareOrderItemDao.list(returnDeclareOrderItemModel);
		Map<Long, SaleReturnDeclareOrderItemModel> goodsMap = new HashMap<>();//原商品 key=原商品id value=单据商品
		Map<String, SaleReturnDeclareOrderItemModel> kwGoodsMap = new HashMap<>();//库位商品 key=库位商品id_poNo value=单据商品 以入定出用
		for(SaleReturnDeclareOrderItemModel model:itemList) {
			goodsMap.put(model.getInGoodsId(), model);
			kwGoodsMap.put(model.getInGoodsId()+"_"+model.getPoNo(), model);
		}
		//检查报文里的商品在订单中是否存在 校验批次效期
		for(SaleStorageDeclareGoodsListJson goods : rootJson.getGoodsList()){
			SaleReturnDeclareOrderItemModel orderItem = goodsMap.get(goods.getGoodsId());
			if(orderItem == null) {
				logger.error("销售退货预申报中未找到商品,订单编号:" + entInboundId+",原货号:"+goods.getGoodsNo());
				throw new RuntimeException("销售退货单中未找到商品,订单编号:" + entInboundId+",原货号:"+goods.getGoodsNo());
			}

			// 批次效期强校验：1-是 0-否
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
					|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
				for (SaleStorageDeclareBatchListIson receive : goods.getReceiveList()) {// 批次
					String batchNo = receive.getBatchNo();
					String productionDate = receive.getProductionDate();
					String overdueDate = receive.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						logger.error("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
						throw new RuntimeException("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
					}
				}

			}
		}

		// 新增 销售退货入库
		SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();
		sReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());//销售退货ID
		sReturnIdepotModel.setMerchantId(declareOrderModel.getMerchantId());//商家ID
		sReturnIdepotModel.setMerchantName(declareOrderModel.getMerchantName());//商家名称
		sReturnIdepotModel.setContractNo(declareOrderModel.getContractNo());//合同号
		sReturnIdepotModel.setInDepotId(declareOrderModel.getInDepotId());//退入仓库id
		sReturnIdepotModel.setInDepotName(declareOrderModel.getInDepotName());//退入仓库名称
		sReturnIdepotModel.setOutDepotId(declareOrderModel.getOutDepotId());//退出仓库id
		sReturnIdepotModel.setOutDepotName(declareOrderModel.getOutDepotName());//退出仓库名称
		String tallyingDate = rootJson.getTallyingDate();//退货入库时间
		if (tallyingDate.length()==10) {
			sReturnIdepotModel.setReturnInDate(Timestamp.valueOf(tallyingDate+" 00:00:00"));//退货入库时间
		}else {
			sReturnIdepotModel.setReturnInDate(Timestamp.valueOf(tallyingDate));//退货入库时间
		}
		sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_011);//'状态   DRC("011","待入仓"),YRC("012","已入仓"),
//    	sReturnIdepotModel.setCode(CodeGeneratorUtil.getNo("XSTR"));//销售退货入库单号 自生成
		sReturnIdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTR));//销售退货入库单号 自生成
		sReturnIdepotModel.setOrderCode(entInboundId);//销售退货编号(既是ent_inbound_id)
		sReturnIdepotModel.setMerchantReturnNo(saleReturnOrderModel.getMerchantReturnNo());//企业退运单号
		sReturnIdepotModel.setInspectNo(saleReturnOrderModel.getInspectNo());//申报地国检编码
		sReturnIdepotModel.setCustomsNo(saleReturnOrderModel.getCustomsNo());//申报地海关编码
		sReturnIdepotModel.setModel(saleReturnOrderModel.getModel());//业务场景 账册内货权转移/账册内货权转移调仓
		sReturnIdepotModel.setServeTypes(saleReturnOrderModel.getServeTypes());//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
		sReturnIdepotModel.setCustomerId(declareOrderModel.getCustomerId());//客户id
		sReturnIdepotModel.setCustomerName(declareOrderModel.getCustomerName());//客户名称
		sReturnIdepotModel.setLbxNo(declareOrderModel.getLbxNo());
		sReturnIdepotModel.setBuId(declareOrderModel.getBuId()); // 事业部
		sReturnIdepotModel.setBuName(declareOrderModel.getBuName());
		sReturnIdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());//预申报单号
		sReturnIdepotModel.setReturnDeclareOrderCode(declareOrderModel.getCode());


		List<SaleReturnIdepotItemModel> sriItemList = new ArrayList<SaleReturnIdepotItemModel>();//存储拼装好的表体
		Integer inTotalNum = 0;
		for(SaleStorageDeclareGoodsListJson goodsJson : rootJson.getGoodsList()) {
			for (SaleStorageDeclareBatchListIson batchListJson : goodsJson.getReceiveList()) {
				//新增销售入库表体
				SaleReturnIdepotItemModel sReturnIdepotItemModel =new SaleReturnIdepotItemModel();
				sReturnIdepotItemModel.setInGoodsId(goodsJson.getGoodsId());//退入商品id
				sReturnIdepotItemModel.setInGoodsCode(goodsJson.getGoodsCode());//退入商品编码
				sReturnIdepotItemModel.setInGoodsName(goodsJson.getGoodsName());//退入商品名称
				sReturnIdepotItemModel.setInGoodsNo(goodsJson.getGoodsNo());//退入商品货号
				sReturnIdepotItemModel.setBarcode(goodsJson.getBarcode());	// 条码
				sReturnIdepotItemModel.setCommbarcode(goodsJson.getCommbarcode());	// 标准条码
				sReturnIdepotItemModel.setReturnNum(batchListJson.getNormalNum());//退货正品品数量
				sReturnIdepotItemModel.setExpireNum(batchListJson.getExpireNum());// 过期品
				sReturnIdepotItemModel.setWornNum(batchListJson.getWornNum());// 坏品
				sReturnIdepotItemModel.setReturnBatchNo(batchListJson.getBatchNo());//退货批次
				sReturnIdepotItemModel.setPoNo(batchListJson.getPoNo());
				String productionDate = batchListJson.getProductionDate();//生产日期
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length()==10) {
						sReturnIdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
					}else {
						sReturnIdepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
					}
				}
				String overdueDate = batchListJson.getOverdueDate();//到期日期
				if (StringUtils.isNotBlank(overdueDate)) {
					if (overdueDate.length()==10) {
						sReturnIdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//到期日期
					}else {
						sReturnIdepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//到期日期
					}
				}
				Integer normalNum = batchListJson.getNormalNum()==null?0:batchListJson.getNormalNum();
				Integer expireNum = batchListJson.getExpireNum()==null?0:batchListJson.getExpireNum();
				Integer wornNum = batchListJson.getWornNum()==null?0:batchListJson.getWornNum();
				inTotalNum +=normalNum+expireNum+wornNum;
				// 新增销售退货入库单表体
				sriItemList.add(sReturnIdepotItemModel);
			}
		}
		// 数量和
		sReturnIdepotModel.setReturnInNum(inTotalNum);
		// 新增销售退货入库单
		saleReturnIdepotDao.save(sReturnIdepotModel);
		for(SaleReturnIdepotItemModel sReturnIdepotItemModel:sriItemList){
			sReturnIdepotItemModel.setSreturnIdepotId(sReturnIdepotModel.getId());//销售退货入库ID
			saleReturnIdepotItemDao.save(sReturnIdepotItemModel);
		}
		//-----------------------------------判断是否走已入定出(销售退货出库表 )-----------------------
		DepotMerchantRelMongo outdepotMerchantRelInfo =null;
		if(declareOrderModel.getOutDepotId()!=null){
			// 查询某商家某仓库的相关信息
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", saleReturnOrderModel.getMerchantId());
			depotMerchantRelParam.put("depotId", saleReturnOrderModel.getOutDepotId());	// 退出仓库
			outdepotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if (outdepotMerchantRelInfo == null || "".equals(outdepotMerchantRelInfo)) {
				logger.error("未查到该商家下退出仓库信息,销售退货订单编号" + entInboundId);
				throw new RuntimeException("未查到该商家下退出仓库信息,销售退货订单编号" + entInboundId);
			}
		}
		/**
		 * 销售退货类型为代销、购销均适用逻辑：
		 *	1、如果入库仓库为香港仓时，不走以入定出；
		 *	2、出库仓库在对应商家仓库管理下的“以入定出”标识为“是”的,则正常已入定出生成退货出库单，扣减退出仓的出库量；
		 *	3、有出库仓要考虑走以入定出，无出库仓就不用以入定出
		 */
		if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleReturnOrderModel.getReturnType()) ||
				DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleReturnOrderModel.getReturnType())){
			if(!DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType()) &&
					outdepotMerchantRelInfo!=null && DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(outdepotMerchantRelInfo.getIsInDependOut())){

				if (outDepotInfoMongo ==null) {
					logger.error("订单的出库仓库mongodb未查到仓库信息,订单编号,订单编号"+entInboundId);
					throw new RuntimeException("订单的出库仓库mongodb未查到仓库信息,订单编号,订单编号"+entInboundId);
				}
				SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
				saleReturnOdepotModel.setOrderCode(entInboundId);//销售退货编号
				saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
				if (saleReturnOdepotModel!=null) {
					logger.error("销售退货出库单已经存在,订单编号"+entInboundId);
					throw new RuntimeException("销售退货出库单已经存在,订单编号"+entInboundId);
				}
				// 批次效期强校验：1-是 0-否
				if ("1".equals(outDepotInfoMongo.getBatchValidation())||"1".equals(inDepotInfoMongo.getBatchValidation()) || "2".equals(inDepotInfoMongo.getBatchValidation())) {
					for (SaleStorageDeclareGoodsListJson goodsListJson : rootJson.getGoodsList()) {// 商品
						for (SaleStorageDeclareBatchListIson receive : goodsListJson.getReceiveList()) {// 批次
							String batchNo = receive.getBatchNo();
							String productionDate = receive.getProductionDate();
							String overdueDate = receive.getOverdueDate();
							if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
								logger.error("已入定出,出库仓"+outDepotInfoMongo.getName()+",或者入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
								throw new RuntimeException("已入定出,出库仓"+outDepotInfoMongo.getName()+",或者入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
							}
						}
					}
				}
				//----------------------------------已入定出(销售退货出库表 ) - 已入定出 出的商家怎么选定----------------------
				// 新增销售退货出库表
				SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
				sReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());//销售退货ID
				sReturnOdepotModel.setMerchantId(declareOrderModel.getMerchantId());//商家ID
				sReturnOdepotModel.setMerchantName(declareOrderModel.getMerchantName());//商家名称
				sReturnOdepotModel.setContractNo(declareOrderModel.getContractNo());//合同号
				sReturnOdepotModel.setInDepotId(declareOrderModel.getInDepotId());//退入仓库id
				sReturnOdepotModel.setInDepotName(declareOrderModel.getInDepotName());//退入仓库名称
				sReturnOdepotModel.setOutDepotId(declareOrderModel.getOutDepotId());//退出仓库id
				sReturnOdepotModel.setOutDepotName(declareOrderModel.getOutDepotName());//退出仓库名称
				// 入的时间就是出的时间
				if (tallyingDate.length()==10) {
					sReturnOdepotModel.setReturnOutDate(Timestamp.valueOf(tallyingDate+" 00:00:00"));//退货出库时间
				}else {
					sReturnOdepotModel.setReturnOutDate(Timestamp.valueOf(tallyingDate));//退货出库时间
				}
				sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_015);//状态015:待出仓,016已出仓
				//sReturnOdepotModel.setCode(CodeGeneratorUtil.getNo("XSTC"));//销售退货出库单号(自生成)
				sReturnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTC));//销售退货出库单号(自生成)
				sReturnOdepotModel.setOrderCode(entInboundId);//销售退货编号
				sReturnOdepotModel.setMerchantReturnNo(saleReturnOrderModel.getMerchantReturnNo());//企业退运单号
				sReturnOdepotModel.setInspectNo(saleReturnOrderModel.getInspectNo());//申报地国检编码
				sReturnOdepotModel.setCustomsNo(saleReturnOrderModel.getCustomsNo());//申报地海关编码
				sReturnOdepotModel.setModel(saleReturnOrderModel.getModel());//业务场景 账册内货权转移/账册内货权转移调仓
				sReturnOdepotModel.setServeTypes(saleReturnOrderModel.getServeTypes());;//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务',
				sReturnOdepotModel.setCustomerId(declareOrderModel.getCustomerId());//客户id
				sReturnOdepotModel.setCustomerName(declareOrderModel.getCustomerName());//客户名称
				sReturnOdepotModel.setLbxNo(declareOrderModel.getLbxNo());//LBX单号
				sReturnOdepotModel.setBuId(declareOrderModel.getBuId()); // 事业部
				sReturnOdepotModel.setBuName(declareOrderModel.getBuName());
				sReturnOdepotModel.setReturnOutNum(sReturnIdepotModel.getReturnInNum());
                sReturnOdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());//预申报单号
                sReturnOdepotModel.setReturnDeclareOrderCode(declareOrderModel.getCode());
				//新增销售出库单
				saleReturnOdepotDao.save(sReturnOdepotModel);

				//查询复制退货入库单表体生成退货出库单表体
				SaleReturnIdepotItemModel inItemModel = new SaleReturnIdepotItemModel();
				inItemModel.setSreturnIdepotId(sReturnIdepotModel.getId());
				List<SaleReturnIdepotItemModel> inItemList = saleReturnIdepotItemDao.list(inItemModel);
				for(SaleReturnIdepotItemModel indepotItem : inItemList){
					//退货入库单商品获取退货订单商品
					SaleReturnDeclareOrderItemModel orderItemModel = kwGoodsMap.get(indepotItem.getInGoodsId()+"_"+indepotItem.getPoNo());
					//获取退货出库位商品
					Map<String, Object> outMap = new HashMap<>();
					outMap.put("merchandiseId",orderItemModel.getOutGoodsId());
					MerchandiseInfoMogo outMerchandiseInfo = merchandiseInfoMogoDao.findOne(outMap);

					//拼装退货出库单表体
					SaleReturnOdepotItemModel  sReturnOdepotItemModel = new SaleReturnOdepotItemModel();
					sReturnOdepotItemModel.setOutGoodsId(orderItemModel.getOutGoodsId());//退出库位商品
					sReturnOdepotItemModel.setOutGoodsNo(orderItemModel.getOutGoodsNo());
					sReturnOdepotItemModel.setOutGoodsCode(orderItemModel.getOutGoodsCode());
					sReturnOdepotItemModel.setOutGoodsName(orderItemModel.getOutGoodsName());
					sReturnOdepotItemModel.setBarcode(outMerchandiseInfo.getBarcode());
					sReturnOdepotItemModel.setCommbarcode(outMerchandiseInfo.getCommbarcode());	// 标准条码
					sReturnOdepotItemModel.setReturnNum(indepotItem.getReturnNum());
					sReturnOdepotItemModel.setExpireNum(indepotItem.getExpireNum());//过期品
					sReturnOdepotItemModel.setWornNum(indepotItem.getWornNum());//坏品
					sReturnOdepotItemModel.setReturnBatchNo(indepotItem.getReturnBatchNo());
					sReturnOdepotItemModel.setPoNo(orderItemModel.getPoNo());// PO单号
					sReturnOdepotItemModel.setProductionDate(indepotItem.getProductionDate());//生产日期
					sReturnOdepotItemModel.setOverdueDate(indepotItem.getOverdueDate());//到期日期
					sReturnOdepotItemModel.setSreturnOdepotId(sReturnOdepotModel.getId());
					// 新增销售出库单表体
					saleReturnOdepotItemDao.save(sReturnOdepotItemModel);
				}
			}
		}
	}
}
