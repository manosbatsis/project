package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v5_8.ESOutInventoryGoodsListJson;
import com.topideal.json.api.v5_8.ESOutInventoryRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.XSOutInventoryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库明细回推实现类 生成出库单
 */
@Service
public class XSOutInventoryServiceImpl implements XSOutInventoryService {
	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(XSOutInventoryServiceImpl.class);
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
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203127000, model = DERP_LOG_POINT.POINT_12203127000_Label,keyword="orderCode")
	public boolean saveOutDepotInfo(String json,String keys,String topics,String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList", ESOutInventoryGoodsListJson.class);
		// JSON对象转实体
		ESOutInventoryRootJson rootJson = (ESOutInventoryRootJson) JSONObject.toBean(jsonData, ESOutInventoryRootJson.class, classMap);
		List<ESOutInventoryGoodsListJson> goodsList = rootJson.getGoodsList();
		String orderCode = rootJson.getOrderCode();// 订单号

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(3);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			logger.error("外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
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
		List<SaleDeclareOrderItemModel> itemList = saleDeclareOrderItemDao.listOrderByPrice(item);
		/**检查预申报单表体申报单销售表体id是否为空*/
		for(SaleDeclareOrderItemModel ditem : itemList){
			if(ditem.getSaleItemId()==null||StringUtils.isBlank(ditem.getSaleOrderCode())){
				logger.info("预申报表体销售单表体id|销售单号为空"+ditem.getId());
				throw new RuntimeException("预申报表体销售单表体id|销售单号为空"+ditem.getId());
			}
		}

		//2.合并报文相同商品数量、及总量
		Integer jsTotal = 0;//报文总量
		Map<String,Integer> jsGoodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(ESOutInventoryGoodsListJson goods : goodsList){
			String key = goods.getGoodsId().toString();
			Integer num = jsGoodsSumMap.get(key);
			if(num==null) num = 0;
			jsGoodsSumMap.put(key,num + goods.getNum());
			jsTotal += goods.getNum();
		}
		//合并预申报表体相同商品数量、及总量
		Integer total = 0;//预申报总量
		Map<String,Integer> goodsSumMap= new HashMap<>();//key=goodsid value=数量
		for(SaleDeclareOrderItemModel sditem : itemList){
			Long goodsId = sditem.getGoodsId();
			String key = goodsId+"";
			Integer num = goodsSumMap.get(key);
			if(num==null) num = 0;
			goodsSumMap.put(key,num + sditem.getNum());
			total += sditem.getNum();
		}
		//3.比较报文商品数量和申报数量是否一致，不一致则报错
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
				logger.error("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
				throw new RuntimeException("报文商品数量与预申报数量不一致，goodsId:" + key+"，报文数量："+jsnum+",预申报数量:"+num);
			}
		}

		//4.按单号拆分报文
		Map<String,List<ESOutInventoryGoodsListJson>> orderGoodsListMap = new HashMap<>();//存储按单号拆分好的报文 key=销售单号  value=goodsList
		splitJsonByOrder(goodsList,itemList,orderGoodsListMap);

		//6.遍历按单号拆分好的报文逐个生成出库
		for(String saleOrderCode : orderGoodsListMap.keySet()){
			List<ESOutInventoryGoodsListJson> jsGoodList = orderGoodsListMap.get(saleOrderCode);
			rootJson.setOrderCode(saleOrderCode);
			rootJson.setGoodsList(jsGoodList);
			saveSaleOut_new(saleDeclare,rootJson);
		}
		//修改预申报单状态为待出库
		saleDeclare.setStatus(DERP_ORDER.SALEDECLARE_STATUS_009);//待出库
		saleDeclareOrderDao.modify(saleDeclare);
		return true;
	}
	/**分摊报文数量按申报单拆分-分摊到申报单商品上
	 * */
	private void splitJsonByOrder(List<ESOutInventoryGoodsListJson> goodsList, List<SaleDeclareOrderItemModel> itemList,
								  Map<String,List<ESOutInventoryGoodsListJson>> orderGoodsListMap){

		//先分坏品到价格低的商品上 stockType  0-好品，1-坏品
		goodsList = goodsList.stream().sorted(Comparator.comparing(ESOutInventoryGoodsListJson::getStockType).reversed()).collect(Collectors.toList());
		for(ESOutInventoryGoodsListJson goods : goodsList){
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

					ESOutInventoryGoodsListJson goodsTemp = new ESOutInventoryGoodsListJson();
					BeanUtils.copyProperties(goods,goodsTemp);//复制对象
					goodsTemp.setNum(benNum);
					goodsTemp.setOrderItemId(sditem.getSaleItemId());

					List<ESOutInventoryGoodsListJson> orderGoodList  = orderGoodsListMap.get(sditem.getSaleOrderCode());
					if(orderGoodList==null) orderGoodList = new ArrayList<>();
					orderGoodList.add(goodsTemp);
					orderGoodsListMap.put(sditem.getSaleOrderCode(),orderGoodList);
				}
			}
		}

	}

	public void saveSaleOut_new(SaleDeclareOrderModel saleDeclare,ESOutInventoryRootJson rootJson) throws Exception {

		String orderCode = rootJson.getOrderCode();// 订单号
		Long merchantId = rootJson.getMerchantId();// 商家ID

		//销售
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setMerchantId(merchantId);
		saleOrderModel.setCode(orderCode);
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if(saleOrderModel == null) {
			logger.info("订单不存在,订单编号" + orderCode);
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
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
			logger.error("出库仓为海外仓不走该流程" + orderCode);
			throw new RuntimeException("出库仓为海外仓不走该流程" + orderCode);
		}
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (ESOutInventoryGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					logger.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
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

		// 新增向销售出库
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setSaleOrderId(saleOrderModel.getId());// 销售订单id
		sOutDepotModel.setMerchantId(saleOrderModel.getMerchantId());// 商家ID
		sOutDepotModel.setPoNo(saleOrderModel.getPoNo());// PO号
		sOutDepotModel.setOutDepotId(saleOrderModel.getOutDepotId());// 调出仓库id
		sOutDepotModel.setOutDepotName(saleOrderModel.getOutDepotName()); // 调出仓库名称
		sOutDepotModel.setCustomerId(saleOrderModel.getCustomerId());// 客户id(供应商)
		sOutDepotModel.setCustomerName(saleOrderModel.getCustomerName());// 客户名称
		sOutDepotModel.setSaleType(saleOrderModel.getBusinessModel());// 销售类型  1购销  2代销'
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_017);// '状态 017,待出库 ,018,已出库,027:出库中
//		sOutDepotModel.setCode(CodeGeneratorUtil.getNo("XSCK"));// 出库清单编号自生成
		sOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));// 出库清单编号自生成
		sOutDepotModel.setMerchantName(saleOrderModel.getMerchantName());// 商家名称
		sOutDepotModel.setSaleOrderCode(saleOrderModel.getCode());// 销售订单编号
		sOutDepotModel.setLbxNo(saleOrderModel.getLbxNo());// LBX单号
		sOutDepotModel.setOutExternalCode(rootJson.getExternalCode());// 外部单号
		sOutDepotModel.setCurrency(saleOrderModel.getCurrency());//币种
		sOutDepotModel.setOrderSource(DERP_ORDER.SALEORDER_ORDERSOURCE_2);
		sOutDepotModel.setBuId(saleOrderModel.getBuId());// 事业部
		sOutDepotModel.setBuName(saleOrderModel.getBuName());
		sOutDepotModel.setSaleDeclareOrderId(saleDeclare.getId());//预申报id
		sOutDepotModel.setSaleDeclareOrderCode(saleDeclare.getCode());//预申报单号
		// 新增
		saleOutDepotDao.save(sOutDepotModel);

		for(ESOutInventoryGoodsListJson goods : rootJson.getGoodsList()) {
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = kwGoodsMap.get(goods.getGoodsId());

			// 新增销售出库表体
			SaleOutDepotItemModel itemModel = new SaleOutDepotItemModel();
			itemModel.setSaleOutDepotId(sOutDepotModel.getId());// 销售出库ID
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
			itemModel.setSaleItemId(goods.getOrderItemId());//销售单表体id
			if (saleOrderItemModel != null) {
				itemModel.setSaleNum(saleOrderItemModel.getNum());
			}
			String productionDate = goods.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(TimeUtils.StringToDate(productionDate));//生产日期
			}
			String overdueDate = goods.getOverdueDate();//生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(TimeUtils.StringToDate(overdueDate));//失效日期
			}
			// 新增销售出库表体
			saleOutDepotItemDao.save(itemModel);
		}

		// 不需要修改销售出库单
//		SaleOrderModel sModel = new SaleOrderModel();
//		sModel.setState(DERP_ORDER.SALEORDER_STATE_017);
//		sModel.setId(saleOrderModel.getId());
//		saleOrderDao.modify(sModel);
	}
	/**出库明细业务处理方法-新end**************************************************/
}
