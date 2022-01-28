package com.topideal.mq;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.WlbItem;
import com.taobao.api.request.WlbItemQueryRequest;
import com.taobao.api.request.WlbWmsSkuGetRequest;
import com.taobao.api.response.WlbItemQueryResponse;
import com.taobao.api.response.WlbWmsSkuGetResponse;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SynsCLGoodsInfoService;

import net.sf.json.JSONObject;


/**
 * 同步菜鸟-定时器
 * @author 
 */
@Component
public class SyncCLGoodsInfoMQ extends ConsumerMQAbstract {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(SyncCLGoodsInfoMQ.class);

	private static final Long TAOBAO_PAGE_SIZE = 50L ;
	
	@Autowired
	private SynsCLGoodsInfoService synsCLGoodsInfoService;
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	
	@Autowired
	private RMQProducer rocketMQProducer;

	public SyncCLGoodsInfoMQ() {
		super.setTags(MQErpEnum.SYNC_CL_GOODS.getTags());
		super.setTopics(MQErpEnum.SYNC_CL_GOODS.getTopic());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============同步菜鸟商品开始json: --==========="+json);
		try {
			List<MerchantShopRelModel> merchantShopRelList = synsCLGoodsInfoService.getSycnMerchantShop() ;
			
			synchronized (this) {
				for (MerchantShopRelModel merchantShopRelModel : merchantShopRelList) {
					String sessionKey = merchantShopRelModel.getSessionKey() ;
					String appKey = merchantShopRelModel.getAppKey() ;
					String appSecret = merchantShopRelModel.getAppSecret() ;
					Long total = 0L ;
					
					/**
					 * 创建淘宝api客户端，线程安全，可复用
					 */
					TaobaoClient client = new DefaultTaobaoClient(ApolloUtil.taobaoApi, appKey, appSecret);
					
					/**
					 * 创建请求实例
					 */
					WlbItemQueryRequest req = new WlbItemQueryRequest();
					req.setStatus("ITEM_STATUS_VALID");
					req.setItemType("NORMAL");
					req.setPageNo(1L);
					req.setPageSize(TAOBAO_PAGE_SIZE);
					
					WlbItemQueryResponse rsp = client.execute(req, sessionKey);
					
					String errorCode = rsp.getErrorCode();
					
					//请求阿里异常，记录日志
					if(StringUtils.isNotBlank(errorCode)) {
						synsCLGoodsInfoService.synsCLGoodsCollectionError(rsp.getSubMsg(), merchantShopRelModel.getAppKey(), JSONObject.fromObject(rsp)) ;
						continue ;
					}
					
					total = rsp.getTotalCount() ;
					
					long pageNum = total / TAOBAO_PAGE_SIZE ;
					
					if(total % TAOBAO_PAGE_SIZE != 0) {
						pageNum += 1L;
					}
					
					for(long pageNo = 1L; pageNo <= pageNum ; pageNo ++) {
						
						
						req.setPageNo(pageNo);
						
						WlbItemQueryResponse pageRsp = client.execute(req, sessionKey);
						
						WlbWmsSkuGetRequest itemReq = new WlbWmsSkuGetRequest();
						
						List<WlbItem> itemList = pageRsp.getItemList();
						for(int i = 0 ; i < itemList.size(); i ++) {
							
							
							WlbItem item = itemList.get(i);
							
							//查询商品详情所需货主ID
							Long userId = item.getUserId();
							//查询商品详情所需商品ID,对应经分销货号
							Long id = item.getId();
							
							/**
							 * 如果存在则跳过
							 */
							boolean isExsit = synsCLGoodsInfoService.checkMerchandiseExsit(merchantShopRelModel.getMerchantId(), id.toString()) ;
							
							if(isExsit) {
								continue ;
							}
							
							itemReq.setItemId(id.toString());
							itemReq.setOwnerUserId(userId.toString());
							
							WlbWmsSkuGetResponse itemRsp = client.execute(itemReq, sessionKey);
							
							try {
								MerchantInfoModel merchantInfoModel= merchantInfoDao.searchById(merchantShopRelModel.getMerchantId());
								 Map<String, Object> requestMap = synsCLGoodsInfoService.synsCLGoodsInfo(itemRsp, merchantShopRelModel,merchantInfoModel,null);							
								if(requestMap == null||requestMap.get("syncGoodsRequest")==null) {
									continue ;
								}
								SyncGoodsRequest request=(SyncGoodsRequest) requestMap.get("syncGoodsRequest");
								Long  sourceGoodsId = (Long) requestMap.get("sourceGoodsId");
								
								// 推送
								JSONObject jsonObject = JSONObject.fromObject(request);
								jsonObject.put("order_id", request.getGoodsBusiCode()) ;
								jsonObject.put("method", EpassAPIMethodEnum.EPASS_E07_METHOD.getMethod()) ;
								
					            LOGGER.info("推送外部API MQ 开始");
					            LOGGER.info("请求报文: "+ jsonObject.toString());
					            SendResult result = rocketMQProducer.send(jsonObject.toString(),
					            		MQPushApiEnum.PUSH_MAINDATA.getTopic(),
					            		MQPushApiEnum.PUSH_MAINDATA.getTags());
					            LOGGER.info("返回结果:"+result.toString());
								// 新增嘉宝
					            /*if ("0000138".equals(merchantInfoModel.getTopidealCode())) {
					            	merchantInfoModel=new MerchantInfoModel();
					            	merchantInfoModel.setTopidealCode("1000000594");
					            	merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
					            	MerchandiseInfoModel merchandiseInfo=new MerchandiseInfoModel();
					            	merchandiseInfo.setMerchantId(merchantInfoModel.getId());
					            	merchandiseInfo.setGoodsNo(id.toString());					            	
					            	merchandiseInfo= merchandiseInfoDao.searchByModel(merchandiseInfo);
					            	SyncGoodsRequest jiabaoRequest=null;
					            	if (merchandiseInfo==null) {
					            		Map<String, Object> jiabaoRequestMap = synsCLGoodsInfoService.synsCLGoodsInfo(itemRsp, merchantShopRelModel,merchantInfoModel,sourceGoodsId);
										if (jiabaoRequestMap!=null) jiabaoRequest=(SyncGoodsRequest) jiabaoRequestMap.get("syncGoodsRequest");
					            	}
					            	if (jiabaoRequest!=null) {
					            		jsonObject = net.sf.json.JSONObject.fromObject(jiabaoRequest);
										jsonObject.put("order_id", jiabaoRequest.getGoodsBusiCode()) ;
										jsonObject.put("method", EpassAPIMethodEnum.EPASS_E07_METHOD.getMethod()) ;
										
							            LOGGER.info("推送外部API MQ 开始");
							            LOGGER.info("请求报文: "+ jsonObject.toString());
							            result = rocketMQProducer.send(jsonObject.toString(),
							            		MQPushApiEnum.PUSH_MAINDATA.getTopic(),
							            		MQPushApiEnum.PUSH_MAINDATA.getTags());
							            LOGGER.info("返回结果:"+result.toString());
									}
					            	
								}*/
					            // 新增卓普信
					            /*merchantInfoModel=new MerchantInfoModel();
				            	merchantInfoModel.setTopidealCode("1000000544");
				            	merchantInfoModel= merchantInfoDao.searchByModel(merchantInfoModel);
				            	// 查询商品
				            	MerchandiseInfoModel merchandiseInfo=new MerchandiseInfoModel();
				            	merchandiseInfo.setMerchantId(merchantInfoModel.getId());
				            	merchandiseInfo.setGoodsNo(id.toString());
				            	merchandiseInfo= merchandiseInfoDao.searchByModel(merchandiseInfo);
				            	SyncGoodsRequest zhuopuxinRequest=null;				           	
				            	if (merchandiseInfo==null) {
									Map<String, Object> zhuopuxinRequestMap = synsCLGoodsInfoService.synsCLGoodsInfo(itemRsp, merchantShopRelModel,merchantInfoModel,sourceGoodsId);
									if (zhuopuxinRequestMap!=null) zhuopuxinRequest=(SyncGoodsRequest) zhuopuxinRequestMap.get("syncGoodsRequest");
								}
				            	// 推送外部
				            	if (zhuopuxinRequest!=null) {
				            		jsonObject = net.sf.json.JSONObject.fromObject(zhuopuxinRequest);
									jsonObject.put("order_id", zhuopuxinRequest.getGoodsBusiCode()) ;
									jsonObject.put("method", EpassAPIMethodEnum.EPASS_E07_METHOD.getMethod()) ;
									
						            LOGGER.info("推送外部API MQ 开始");
						            LOGGER.info("请求报文: "+ jsonObject.toString());
						            result = rocketMQProducer.send(jsonObject.toString(),
						            		MQPushApiEnum.PUSH_MAINDATA.getTopic(),
						            		MQPushApiEnum.PUSH_MAINDATA.getTags());
						            LOGGER.info("返回结果:"+result.toString());
								}*/

					            
					            synsCLGoodsInfoService.synsCLGoodsCollectionSucc(itemRsp) ;
					            
							} catch (Exception e) {
								synsCLGoodsInfoService.synsCLGoodsCollectionError(itemRsp.getItemId(), e.getMessage(), JSONObject.fromObject(itemRsp)) ;
								
								LOGGER.error("同步菜鸟商品同步异常，菜鸟ID" + itemRsp.getItemId() + e.getMessage(), e.getMessage());
								continue ;
							}
							
							
						}
						
					}
				}
			}
			
			LOGGER.info("=============同步菜鸟商品结束: --===========");
		} catch (Exception e) {
			LOGGER.error("同步菜鸟商品异常:{}", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
	/**
	 * 推送主数据
	 */
	private void sendMaindata() {
		// TODO Auto-generated method stub

	}

}
