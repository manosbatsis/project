package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.PurchaseSdOrderDao;
import com.topideal.dao.purchase.PurchaseSdOrderSditemDao;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.SynPurchaseSdJBToBXService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
/**
  * 嘉宝的采购SD单复制到宝信
 * @author qiancheng.chen
 * @date 2020-12-23
 *
 */
@Service
public class SynPurchaseSdJBToBXServiceImpl implements SynPurchaseSdJBToBXService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SynPurchaseSdJBToBXServiceImpl.class);

	@Autowired
	private PurchaseSdOrderDao purchaseSdOrderDao ;
	@Autowired
	private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao ;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMongoDao ;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private RMQLogProducer rMQLogProducer;// 日志MQ

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201110010,model=DERP_LOG_POINT.POINT_13201110010_Label)
	public void synsPurchaseSdJBToBX(String json, String keys, String topics, String tags) throws Exception {

		//1、检查嘉宝公司（公司编码：ERP16114000043）下是否存在采购SD单，且单据的“是否已同步宝信”为否，若不存在则终止
		Map<String,Object> jParams = new HashMap<String,Object>();
		jParams.put("code", "ERP16114000043");
		MerchantInfoMongo jMerchantMongo = merchantInfoMongoDao.findOne(jParams);

		PurchaseSdOrderModel jSdModel = new PurchaseSdOrderModel();
		jSdModel.setMerchantId(jMerchantMongo.getMerchantId());
		jSdModel.setIsSyn("0");// “是否已同步宝信”为否
		List<PurchaseSdOrderModel> jList = purchaseSdOrderDao.list(jSdModel);

		if(jList != null && jList.size() >0) {
			Map<String,List<PurchaseSdOrderSditemModel>> synNewSdItemMap =  new HashMap<String, List<PurchaseSdOrderSditemModel>>();//保存要同步的表体
			List<PurchaseSdOrderModel> synNewSdList =  new ArrayList<PurchaseSdOrderModel>();//保存要同步的表头
			Map<String,List<PurchaseSdOrderSditemModel>> synEditSdItemMap =  new HashMap<String, List<PurchaseSdOrderSditemModel>>();//保存要同步的表体
			List<PurchaseSdOrderModel> synEditSdList =  new ArrayList<PurchaseSdOrderModel>();//保存要同步的表头

			Map<String,Object> bParams = new HashMap<String,Object>();
			bParams.put("code", "ERP31194100022");
			MerchantInfoMongo bMerchantMongo = merchantInfoMongoDao.findOne(bParams);

			for(PurchaseSdOrderModel jModel :jList) {
				if(jModel.getInboundDate() != null) {
					// 获取最大的关账日/月结日期
					FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
					closeAccountsMongo.setMerchantId(bMerchantMongo.getMerchantId());
					closeAccountsMongo.setBuId(jModel.getBuId());
					String maxdate = "";
					if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
						maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
					}
					String maxCloseAccountsMonth="";
					if (StringUtils.isNotBlank(maxdate)) {
						// 获取该月份下月时间
						String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
						maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
					}

					// 关账日期和入仓日期比较
					if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
						if (jModel.getInboundDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
							// 关账下个月日期大于 入库日期
							JSONObject jsonError= new  JSONObject();
							jsonError.put("expMsg",jModel.getCode()+"SD单同步宝信失败，所在月份已关账");
							collectionError(jsonError,jsonError,null);
							LOGGER.error("expMsg",jModel.getCode()+"SD单同步宝信失败，所在月份已关账");
							continue;
						}
					}
				}

				//2、检查SD单在宝信下是否已存在，检查条件：（宝信SD单-单据类型，PO号，数量，SD类型）与嘉宝SD单相同
				PurchaseSdOrderModel bModel = new PurchaseSdOrderModel();
				bModel.setMerchantId(bMerchantMongo.getMerchantId());
				bModel.setType(jModel.getType());//单据类型
				bModel.setPoNo(jModel.getPoNo());
				bModel.setSdTypeName(jModel.getSdTypeName());//SD类型
				List<PurchaseSdOrderModel> bSdOrderList = purchaseSdOrderDao.getBXListDTO(bModel,jModel.getId());

				//商品sd明细
				PurchaseSdOrderSditemModel jSdItemModel = new PurchaseSdOrderSditemModel();
				jSdItemModel.setOrderId(jModel.getId());
				List<PurchaseSdOrderSditemModel> jSdItemList = purchaseSdOrderSditemDao.list(jSdItemModel);

				if(bSdOrderList != null && bSdOrderList.size() > 0) {//3、若宝信下存在，则根据商品货号更新对应宝信SD单的SD金额、SD单价、SD本币金额、SD本币单价
					for(PurchaseSdOrderModel bSdOrder : bSdOrderList) {
						bSdOrder.setTgtCurrency(jModel.getTgtCurrency());
						bSdOrder.setRate(jModel.getRate());
						bSdOrder.setInboundDate(jModel.getInboundDate());
						bSdOrder.setModifyDate(TimeUtils.getNow());
//						purchaseSdOrderDao.modify(bSdOrder);
						if(jSdItemList != null && jSdItemList.size() > 0) {
							List<PurchaseSdOrderSditemModel> editItemList = new ArrayList<PurchaseSdOrderSditemModel>();
							for(PurchaseSdOrderSditemModel jItem:jSdItemList) {
								PurchaseSdOrderSditemModel bItem = new PurchaseSdOrderSditemModel();
								bItem.setOrderId(bSdOrder.getId());
								bItem.setGoodsNo(jItem.getGoodsNo());
								bItem.setSdTypeName(jItem.getSdTypeName());
								bItem = purchaseSdOrderSditemDao.searchByModel(bItem);

								if(bItem == null) {
									continue ;
								}

								bItem.setSdPrice(jItem.getSdPrice());
								bItem.setSdAmount(jItem.getSdAmount());
								bItem.setSdTgtPrice(jItem.getSdTgtPrice());
								bItem.setSdTgtAmount(jItem.getSdTgtAmount());
								bItem.setModifyDate(TimeUtils.getNow());

//								purchaseSdOrderSditemDao.modify(bItem);
								editItemList.add(bItem);
							}
							if(editItemList.size() > 0) {
								synEditSdList.add(bSdOrder);
								synEditSdItemMap.put(bSdOrder.getCode(),editItemList);
							}
						}
					}

				}else {//4、若宝信下不存在对应SD单，则创建宝信公司下的SD单
					PurchaseSdOrderModel newModel = new PurchaseSdOrderModel();
					BeanUtils.copyProperties(jModel, newModel);
					newModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGSD));//流水号
					newModel.setMerchantId(bMerchantMongo.getMerchantId());
					newModel.setMerchantName(bMerchantMongo.getName());
					newModel.setSupplierId(jMerchantMongo.getMerchantId());//4.1 供应商：固定嘉宝公司
					newModel.setSupplierName(jMerchantMongo.getName());
					newModel.setCreateName("系统创建");
					newModel.setCreateDate(TimeUtils.getNow());
					newModel.setCreater(null);
					newModel.setModifyDate(null);
					newModel.setId(null);
					//4.2 根据PO号查询宝信下对应的采购单，若查到，则取最新的那条，找不到，则为空
					PurchaseOrderDTO dto = new PurchaseOrderDTO();
					dto.setPoNo(jModel.getPoNo());
					dto.setMerchantId(bMerchantMongo.getMerchantId());
					dto = purchaseOrderDao.getlistByPage(dto);
					String code = "";
					Long purchaseOrderId = null;
					if(dto != null && dto.getList().size() > 0) {
						PurchaseOrderDTO codeDto = (PurchaseOrderDTO) dto.getList().get(0);
						code = codeDto.getCode();
						purchaseOrderId = codeDto.getId();
					}
					newModel.setPurchaseCode(code);
					newModel.setIsSyn("0");
//					Long id = purchaseSdOrderDao.save(newModel);
					synNewSdList.add(newModel);

					//4.3 根据嘉宝公司的货号，查询该货号在宝信公司下的商品id，添加商品sd明细
					if(jSdItemList != null && jSdItemList.size() > 0) {
						List<PurchaseSdOrderSditemModel> newItemList = new ArrayList<PurchaseSdOrderSditemModel>();
						for(PurchaseSdOrderSditemModel jSdItem:jSdItemList) {
							bParams.clear();
							bParams.put("goodsNo", jSdItem.getGoodsNo());
							bParams.put("merchantId", bMerchantMongo.getMerchantId());
							MerchandiseInfoMogo bmongo = merchandiseInfoMongoDao.findOne(bParams);
							if(bmongo != null)
								jSdItem.setGoodsId(bmongo.getMerchandiseId());
//							jSdItem.setOrderId(id);
							jSdItem.setCreateDate(TimeUtils.getNow());
							jSdItem.setModifyDate(null);
							jSdItem.setId(null);

							if(purchaseOrderId != null){
								PurchaseOrderItemModel purchaseItemModel = new PurchaseOrderItemModel();
								purchaseItemModel.setPurchaseOrderId(purchaseOrderId);
								purchaseItemModel.setGoodsId(bmongo.getMerchandiseId());
								List<PurchaseOrderItemModel> tempPurchaseItemList = purchaseOrderItemDao.list(purchaseItemModel);
								if(tempPurchaseItemList != null && tempPurchaseItemList.size() > 0){
									jSdItem.setPurchaseItemId(tempPurchaseItemList.get(0).getId());
								}
							}

//							purchaseSdOrderSditemDao.save(jSdItem);
							newItemList.add(jSdItem);
						}
						synNewSdItemMap.put(newModel.getCode(),newItemList);
					}
				}
				//5、更新嘉宝SD单的“是否已同步宝信”字段为是
				jModel.setIsSyn("1");
				jModel.setModifyDate(TimeUtils.getNow());
				purchaseSdOrderDao.modify(jModel);

			}
			//注入数据 新增
			if(synNewSdList.size() > 0) {
				for(PurchaseSdOrderModel sdModel:synNewSdList) {
					Long id = purchaseSdOrderDao.save(sdModel);
					if(synNewSdItemMap.containsKey(sdModel.getCode())) {
						List<PurchaseSdOrderSditemModel> sdItemList = synNewSdItemMap.get(sdModel.getCode());
						for(PurchaseSdOrderSditemModel sdItem : sdItemList) {
							sdItem.setOrderId(id);
							purchaseSdOrderSditemDao.save(sdItem);

						}
					}
				}
			}
			//注入数据 更新
			if(synEditSdList.size() > 0) {
				for(PurchaseSdOrderModel sdModel:synEditSdList) {
					purchaseSdOrderDao.modify(sdModel);
					if(synEditSdItemMap.containsKey(sdModel.getCode())) {
						List<PurchaseSdOrderSditemModel> sdItemList = synEditSdItemMap.get(sdModel.getCode());
						for(PurchaseSdOrderSditemModel sdItem : sdItemList) {
							purchaseSdOrderSditemDao.modify(sdItem);
						}
					}
				}
			}
		}
	}

	/**
	 * 异常 保存到mongodb
	 * @param jsonError
	 */
	private void collectionError(JSONObject jsonError,JSONObject json,String orderNo){
		Date date=new Date();
		//String format = sdf1.format(date);// keyword 用当前时间来存
		Long startDate = date.getTime();
		Long endDate = startDate;
		MQLog mqLog=new MQLog();
		mqLog.setModel("嘉宝的采购SD单复制到宝信");
		mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13201110010));
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("orderNo");
		mqLog.setMethod("com.topideal.service.timer.impl.SynPurchaseSdJBToBXServiceImpl.synsPurchaseSdJBToBX()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.TIMER_SYN_PURCHASE_SD_JB_TO_BX.getTopic());
		mqLog.setTags(MQOrderEnum.TIMER_SYN_PURCHASE_SD_JB_TO_BX.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("嘉宝的采购SD单复制到宝信---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}
}
