package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.dao.sale.OrderReturnIdepotDao;
import com.topideal.dao.sale.OrderReturnIdepotItemDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderReturnIdepotItemModel;
import com.topideal.entity.vo.sale.OrderReturnIdepotModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.service.timer.CrawlerOrderReturnIdepotService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 爬虫生成电商售后退款单
 * @author qiancheng.chen
 *
 */
@Service
public class CrawlerOrderReturnIdepotServiceImpl implements CrawlerOrderReturnIdepotService{
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerOrderReturnIdepotServiceImpl.class);
	@Autowired
	OrderReturnIdepotDao orderReturnIdepotDao;
	@Autowired
	OrderReturnIdepotItemDao orderReturnIdepotItemDao;
	@Autowired
	OrderDao orderDao;
	@Autowired
	OrderItemDao orderItemDao;
	@Autowired
	CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private RMQLogProducer rMQLogProducer;// 日志MQ
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;// 商家和店铺中间表
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_20100000002,model=DERP_LOG_POINT.POINT_20100000002_Label)
	public void saveOrderReturnIdepot(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		String externalCode = (String) jsonData.get("orderNo");//可根据外部单号抓取
		String returnCode = (String) jsonData.get("refundNo");//可根据平台售后退款单抓取
		String queryShopCode = (String) jsonData.get("shopCode");//可根据店铺抓取
		String startDate = (String) jsonData.get("startDate");//退款完成开始时间
		String endDate = (String) jsonData.get("endDate");//退款完成结束时间

		//开始时间 结束时间必须都不为空，否则不执行
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			endDate = TimeUtils.formatDay();
			startDate = TimeUtils.formatDay(TimeUtils.addDay(TimeUtils.parseDay(endDate), -60));
		}

		Map<Long,MerchantInfoMongo> merchantInfoMap = new HashMap<Long,MerchantInfoMongo>();
		Map<String,MerchantShopRelMongo> shopMap = new HashMap<String,MerchantShopRelMongo>();
		Map<String,String> maxDateMap = new HashMap<String,String>();
		Map<Long,DepotInfoMongo> depotInfoMap = new HashMap<Long,DepotInfoMongo>();

		long startIndex = 0;
		long pageSize = 1000;
		ResultSet rs = null;
		//获取链接
		Class.forName(ApolloUtilDB.jdbforName);
		Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);
		// 关闭事务自动提交
		conn.setAutoCommit(false);

		String queryConditionStr = "";
		if(StringUtils.isNotBlank(externalCode)) {
			queryConditionStr += " AND orderNo = '"+externalCode+"' ";
		}
		if(StringUtils.isNotBlank(returnCode)) {
			queryConditionStr += " AND refundNo = '"+returnCode+"' ";
		}
		if(StringUtils.isNotBlank(queryShopCode)) {
			queryConditionStr += " AND shopCode = '"+queryShopCode+"' ";
		}
		//分页查询订单
		String queryTotalStr = "select COUNT(*) as totalNum from platform_refund_info where DATE_FORMAT(refundEndTime,'%Y-%m-%d') BETWEEN '"+startDate+"' and '"+endDate +"'"+ queryConditionStr ;
		// 获取分页查询的订单数据
		String queryStr = "select * from platform_refund_info where DATE_FORMAT(refundEndTime,'%Y-%m-%d') BETWEEN '"+startDate+"' and '"+ endDate +"'"+ queryConditionStr+" limit ";
		// 修改为已经接收
//		String updateStr = "update platform_refund_info set hasReceived = '1' where shopCode = ? and refundNo = ? ";
		Statement pst = conn.createStatement(); // 创建 查询总量
		Statement countPst = conn.createStatement();// 创建 总查询
		//总量
		Integer count = 0;
		ResultSet countRs = pst.executeQuery(queryTotalStr);
		if(countRs.next()) count = countRs.getInt("totalNum");
		//更新爬虫订单状态
//		PreparedStatement updatePst = conn.prepareStatement(updateStr)  ;
		try{
			while(count > 0 && startIndex < count) {
				Map<String,List<OrderReturnIdepotItemModel>> orderRerturnItemMap = new HashMap<String,List<OrderReturnIdepotItemModel>>();
				List<OrderReturnIdepotModel> orderRerturnList = new ArrayList<OrderReturnIdepotModel>();
				List<OrderReturnIdepotItemModel> orderRerturnItemList = new ArrayList<OrderReturnIdepotItemModel>();
				List<String> externalCodeList = new ArrayList<String>();
				List<String> returnCodeList = new ArrayList<String>();
				Map<String, OrderDTO> orderMap = new HashMap<String, OrderDTO>();
				List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();

				String querySql = queryStr + startIndex +","+pageSize;
				rs = pst.executeQuery(querySql);

				while(rs.next()) {
					String shopCode = rs.getString("shopCode");//店铺编码
					String refundNo = rs.getString("refundNo");//退款编号 平台售后单号
					String orderNo = rs.getString("orderNo");//订单编号  来源交易订单号
					String refundAmount = rs.getString("refundAmount");//退款金额
					String refundEndTime = rs.getString("refundEndTime");//退款成功时间 退款完结时间
					String refundReason = rs.getString("refundReason");//退款原因
					String buyers = rs.getString("buyers");//买家
					String refundType = rs.getString("refundType");//退款类型

					if(!externalCodeList.contains(orderNo)) {
						externalCodeList.add(orderNo);
					}
					if(!returnCodeList.contains(refundNo)) {
						returnCodeList.add(refundNo);
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("shopCode", shopCode);
					map.put("refundNo", refundNo);
					map.put("orderNo", orderNo);
					map.put("refundAmount", refundAmount);
					map.put("refundEndTime", refundEndTime);
					map.put("refundReason", refundReason);
					map.put("buyers", buyers);
					map.put("refundType", refundType);
					resultList.add(map);
				}
				//查询已发货电商订单
				OrderDTO queryOrderDTO = new OrderDTO();
				queryOrderDTO.setExternalCodeList(externalCodeList);
				queryOrderDTO.setStatus(DERP_ORDER.ORDER_STATUS_4);
				List<OrderDTO> orderList = orderDao.listDTO(queryOrderDTO);
				for(OrderDTO order : orderList) {
					String key = order.getExternalCode() + "_"+order.getMerchantId();
					if(!orderMap.containsKey(key)) {
						orderMap.put(key, order);
					}
				}
				//根据平台售后单号查询是否存在
				OrderReturnIdepotDTO queryOrderReturn = new OrderReturnIdepotDTO();
				queryOrderReturn.setReturnCodeList(returnCodeList);
				List<OrderReturnIdepotDTO> queryOrderReturnList = orderReturnIdepotDao.listDTO(queryOrderReturn);
				Map<String, List<OrderReturnIdepotDTO>> orderRerturnMap = queryOrderReturnList.stream().collect(Collectors.groupingBy(OrderReturnIdepotDTO::getReturnCode));

				a:for(Map<String,String> map: resultList) {
					String shopCode = map.get("shopCode");//店铺编码
					String refundNo = map.get("refundNo");//退款编号 平台售后单号
					String orderNo = map.get("orderNo");//订单编号  来源交易订单号
					String refundAmount = map.get("refundAmount");//退款金额
					String refundEndTime = map.get("refundEndTime");//退款成功时间 退款完结时间
					String refundReason = map.get("refundReason");//退款原因
					String buyers = map.get("buyers");//买家
					String refundType = map.get("refundType");//退款类型

					MerchantShopRelMongo merchantShopRelMongo = shopMap.get(shopCode);
					if(merchantShopRelMongo == null) {
						Map<String, Object> merchantShopRelMap = new HashMap<>();
						merchantShopRelMap.put("shopCode",shopCode);
						merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
						merchantShopRelMongo = merchantShopRelMongoDao.findOne(merchantShopRelMap);

						if(merchantShopRelMongo != null) shopMap.put(shopCode, merchantShopRelMongo);
					}

					if (merchantShopRelMongo == null) {
						JSONObject jsonError= new  JSONObject();
						jsonError.put("expMsg","店铺编码："+shopCode+"，没有查询到店铺信息数据");
						collectionError(jsonError,jsonError,orderNo);
						LOGGER.info("店铺编码："+shopCode+"，没有查询到店铺信息数据");
						continue;
					}
					if (!DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())) {
						JSONObject jsonError= new  JSONObject();
						jsonError.put("expMsg","店铺编码："+shopCode+"，店铺类型不为单主店");
						collectionError(jsonError,jsonError,orderNo);
						LOGGER.info("店铺编码："+shopCode+"，店铺类型不为单主店");
						continue;
					}

					//1、订单号在电商订单表存在，并且是订单状态为已发货
					OrderDTO orderDTO = orderMap.get(orderNo+"_"+merchantShopRelMongo.getMerchantId());
					if(orderDTO == null) {
						JSONObject jsonError= new  JSONObject();
						jsonError.put("expMsg","来源交易订单号："+ orderNo +"，没有找到对应已发货的电商订单");
						collectionError(jsonError,jsonError,orderNo);
						LOGGER.info("来源交易订单号："+ orderNo +"，没有找到对应已发货的电商订单");
						continue;
					}
					//2、退款单号在电商退款单表不存在，或者退款单的状态为已删除
					List<OrderReturnIdepotDTO> orderReturnList = orderRerturnMap.get(refundNo);
					if(orderReturnList != null && orderReturnList.size() > 0) {
//						JSONObject jsonError= new  JSONObject();
//						jsonError.put("expMsg","平台售后单号："+ refundNo +"，在售后退款单列表已存在");
//						collectionError(jsonError,jsonError,orderNo);
//						LOGGER.info("平台售后单号："+ refundNo +"，在售后退款单列表已存在");
						continue;
					}
					// 查询商家
					MerchantInfoMongo merchant = merchantInfoMap.get(orderDTO.getMerchantId());
					if(merchant == null) {
						Map<String, Object> merchantMap = new HashMap<String, Object>();
						merchantMap.put("merchantId", orderDTO.getMerchantId());
						merchant = merchantMongoDao.findOne(merchantMap);

						if(merchant != null) merchantInfoMap.put(orderDTO.getMerchantId(), merchant);
					}
					boolean isTradeMechant = "1".equals(merchant.getRegisteredType()) ? true : false;//是否是内贸主体
					/**从电商订单取仓库信息*/
					DepotInfoMongo depot = depotInfoMap.get(orderDTO.getDepotId());
					if(depot == null) {
						Map<String, Object> depotParmas = new HashMap<>();
						depotParmas.put("depotId", orderDTO.getDepotId());
						depot = depotInfoMongoDao.findOne(depotParmas);
					}
					/**构造退货商品表头 start*/
					// 表头 封装数据
				 	OrderReturnIdepotModel orderReturnIdepotModelSave = new OrderReturnIdepotModel();
					orderReturnIdepotModelSave.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDDTH));
					orderReturnIdepotModelSave.setExternalCode(orderNo);
					orderReturnIdepotModelSave.setReturnCode(refundNo);
					orderReturnIdepotModelSave.setOriginalDeliveryCode(orderNo);
					orderReturnIdepotModelSave.setRefundEndDate(TimeUtils.parse(refundEndTime, "yyyy-MM-dd"));//退款完结时间
					orderReturnIdepotModelSave.setAmount(new BigDecimal(refundAmount));
					orderReturnIdepotModelSave.setCurrency(DERP.CURRENCYCODE_CNY);
					orderReturnIdepotModelSave.setStorePlatformCode(orderDTO.getStorePlatformName());
					orderReturnIdepotModelSave.setShopCode(shopCode);
					orderReturnIdepotModelSave.setShopName(orderDTO.getShopName());
					orderReturnIdepotModelSave.setShopTypeCode(orderDTO.getShopTypeCode()); //运营类型编码
					orderReturnIdepotModelSave.setCustomerId(orderDTO.getCustomerId());
					orderReturnIdepotModelSave.setCustomerName(orderDTO.getCustomerName());
					orderReturnIdepotModelSave.setOrderReturnSource(2);// 接口生成
					orderReturnIdepotModelSave.setMerchantId(orderDTO.getMerchantId());
					orderReturnIdepotModelSave.setMerchantName(orderDTO.getMerchantName());
					orderReturnIdepotModelSave.setStatus(DERP_ORDER.ORDER_RETURN_STATUS_013); // 已退款
					orderReturnIdepotModelSave.setCreateName("system");
					orderReturnIdepotModelSave.setCreateDate(TimeUtils.getNow());
					orderReturnIdepotModelSave.setAfterSaleType(refundType);//售后类型
					orderReturnIdepotModelSave.setReturnOrderType(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0);//退款信息，是否为退货单为 0
					orderReturnIdepotModelSave.setTotalRefundAmount(new BigDecimal(refundAmount));//退款金额
					orderReturnIdepotModelSave.setRefundRemark(refundReason);
					orderReturnIdepotModelSave.setOrderId(orderDTO.getId());
					orderReturnIdepotModelSave.setOrderCode(orderDTO.getCode());
					orderReturnIdepotModelSave.setBuyerName(buyers);
					orderReturnIdepotModelSave.setIsGenerate("0");
					orderReturnIdepotModelSave.setAuditName("system");
					orderReturnIdepotModelSave.setAuditDate(TimeUtils.getNow());
					if(depot != null) {
						orderReturnIdepotModelSave.setReturnInDepotId(depot.getDepotId());
						orderReturnIdepotModelSave.setReturnInDepotCode(depot.getCode());
						orderReturnIdepotModelSave.setReturnInDepotName(depot.getName());
					}

					if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸税额 、内贸退款金额（不含税）
						/**
						 * 内贸退款金额（不含税）= 订单退款金额/（1+13%），得出的金额保留2位小数，做四舍五入
						 * 内贸税额 = 订单退款金额 - 内贸退款金额（不含税），得出金额保留2位小数，做四舍五入
						 */
						BigDecimal tradeRefundAmount = orderReturnIdepotModelSave.getTotalRefundAmount().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);
						BigDecimal tradeRefundTax = orderReturnIdepotModelSave.getTotalRefundAmount().subtract(tradeRefundAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
						orderReturnIdepotModelSave.setTradeRefundAmount(tradeRefundAmount);
						orderReturnIdepotModelSave.setTradeRefundTax(tradeRefundTax);
					}
					/**构造退货商品表头 end*/

		            /**构造退货商品表体 start*/
					List<OrderReturnIdepotItemModel> itemList =  new ArrayList<OrderReturnIdepotItemModel>();
					//获取电商订单商品信息
					OrderItemModel itemModel = new OrderItemModel();
					itemModel.setOrderId(orderDTO.getId());
					List<OrderItemModel> orderItemsList = orderItemDao.list(itemModel);
					BigDecimal itemTotalAmount = orderItemsList.stream().map(OrderItemModel::getDecTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal calRefund = BigDecimal.ZERO;
					//3、根据退款电商订单事业部，查询最新的关账月份。判断退款完结事业是否大于关账月份，如果小于最新的关账月份，过滤掉这个退款单，并将爬虫库的【是否已使用】标识更新为2
					for(int i = 0 ; i < orderItemsList.size() ; i++) {
						OrderItemModel orderItemModel = orderItemsList.get(i);
						if(orderItemModel.getBuId() == null) {
							JSONObject jsonError= new  JSONObject();
							jsonError.put("expMsg","电商订单："+ orderDTO.getCode() +" 商品货号："+ orderItemModel.getGoodsNo()+" 事业部为空");
							collectionError(jsonError,jsonError,orderNo);
							LOGGER.info("电商订单："+ orderDTO.getCode() +" 商品货号："+ orderItemModel.getGoodsNo()+" 事业部为空");
							continue a;
						}

						String maxdate = maxDateMap.get(orderDTO.getMerchantId()+"_"+orderItemModel.getBuId());
						if(StringUtils.isBlank(maxdate)) {
							FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
							closeAccountsMongo.setMerchantId(orderDTO.getMerchantId());
							closeAccountsMongo.setBuId(orderItemModel.getBuId());
							maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);

							if(StringUtils.isNotBlank(maxdate)) maxDateMap.put(orderDTO.getMerchantId()+"_"+orderItemModel.getBuId(), maxdate);
						}
						String maxCloseAccountsMonth="";
						if (StringUtils.isNotBlank(maxdate)) {
							// 获取该月份下月时间
							String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
							maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
						}
						if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
							// 关账下个月日期大于 完结日期
							if (Timestamp.valueOf(refundEndTime).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
								JSONObject jsonError= new  JSONObject();
								jsonError.put("expMsg","平台售后单号："+ refundNo +" 的单据退款完结时间不能小于关账日期");
								collectionError(jsonError,jsonError,orderNo);
								LOGGER.info("平台售后单号："+ refundNo +" 的单据退款完结时间不能小于关账日期");
								continue a;
							}
						}
						 //表体 封装数据
			            OrderReturnIdepotItemModel returnItemModel = new OrderReturnIdepotItemModel();
						returnItemModel.setInGoodsCode(orderItemModel.getGoodsCode());
						returnItemModel.setInGoodsId(orderItemModel.getGoodsId());
						returnItemModel.setInGoodsNo(orderItemModel.getGoodsNo());
						returnItemModel.setInGoodsName(orderItemModel.getGoodsName());
						returnItemModel.setBarcode(orderItemModel.getBarcode());
						returnItemModel.setCommbarcode(orderItemModel.getCommbarcode());
						returnItemModel.setBuId(orderItemModel.getBuId());
						returnItemModel.setBuName(orderItemModel.getBuName());
						returnItemModel.setPrice(orderItemModel.getPrice());
						returnItemModel.setDecTotal(orderItemModel.getDecTotal());
						returnItemModel.setBadGoodsNum(Integer.valueOf(0));
						returnItemModel.setReturnNum(Integer.valueOf(0));
						returnItemModel.setCreateDate(TimeUtils.getNow());
						returnItemModel.setStockLocationTypeId(orderItemModel.getStockLocationTypeId());
						returnItemModel.setStockLocationTypeName(orderItemModel.getStockLocationTypeName());
						BigDecimal tempRefund = BigDecimal.ZERO;
						/**
						 * 退款金额摊分到各个商品上
						 * 摊分公式逻辑                     *
						 *      （1）前N-1个商品退款金额 =（商品结算金额/订单所有商品结算总金额）*订单退款金额，得出保留2位小数；                     *
						 *      （2）第N个商品退款金额 = 订单退款金额-前N-1个商品退款金额总和，保留2位小数
						 */
						if(i == orderItemsList.size()-1) {
							tempRefund = orderReturnIdepotModelSave.getTotalRefundAmount().subtract(calRefund).setScale(2, BigDecimal.ROUND_HALF_UP);
						}else{
							BigDecimal refundRatio = orderItemModel.getDecTotal().divide(itemTotalAmount,8,BigDecimal.ROUND_HALF_UP);
							tempRefund = refundRatio.multiply(orderReturnIdepotModelSave.getTotalRefundAmount()).setScale(2, BigDecimal.ROUND_DOWN);
						}
						returnItemModel.setRefundAmount(tempRefund);
						calRefund = calRefund.add(tempRefund);//累计已分摊退款金额
						if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸税额 、内贸退款金额（不含税）
							 /**
	                         * 内贸商品退款金额（不含税）= 商品退款金额/（1+13%），得出的金额保留2位小数，做四舍五入
	                         * 内贸商品退款税额 = 商品退款金额 - 内贸商品退款金额（不含税），得出金额保留2位小数，做四舍五入
	                         */
	                        BigDecimal tradeRefundAmount = returnItemModel.getRefundAmount().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);
	                        BigDecimal tradeRefundTax = returnItemModel.getRefundAmount().subtract(tradeRefundAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
	                        returnItemModel.setTradeRefundAmount(tradeRefundAmount);
	                        returnItemModel.setTradeRefundTax(tradeRefundTax);
						}
						orderRerturnItemList.add(returnItemModel);
						itemList.add(returnItemModel);
					}
					/**构造退货商品表体 end*/
					orderRerturnList.add(orderReturnIdepotModelSave);
					orderRerturnItemMap.put(orderReturnIdepotModelSave.getCode(), itemList);

					//更新爬虫订单状态
//					updatePst.setString(1, shopCode);
//					updatePst.setString(2, refundNo);
//					updatePst.addBatch();
				}

				if(orderRerturnList.size() > 0) {
					orderReturnIdepotDao.batchSave(orderRerturnList);
					orderReturnIdepotItemDao.batchSave(orderRerturnItemList);

					//更新表体关联表头id
					List<OrderReturnIdepotItemModel> updateItemList = new ArrayList<OrderReturnIdepotItemModel>();
					Map<String,Long> codeOrderIdMap = orderRerturnList.stream().collect(Collectors.toMap(OrderReturnIdepotModel::getCode, OrderReturnIdepotModel::getId));
					for (String orderReturncode : codeOrderIdMap.keySet()) {
						List<OrderReturnIdepotItemModel> itemList = orderRerturnItemMap.get(orderReturncode);
						for (OrderReturnIdepotItemModel orderRerturnItemModel : itemList) {
							OrderReturnIdepotItemModel tempItemModel = new OrderReturnIdepotItemModel();
							tempItemModel.setOreturnIdepotId(codeOrderIdMap.get(orderReturncode));
							tempItemModel.setId(orderRerturnItemModel.getId());
							updateItemList.add(tempItemModel);
						}
					}
					//表体分批更新
					orderReturnIdepotItemDao.batchUpdate(updateItemList);
				}

				startIndex +=pageSize;//下一页
			}

			//更新爬虫订单状态
//			updatePst.executeBatch() ;
//			conn.commit();

        }catch (Exception e){
			conn.rollback();
        	e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
				rs = null;
			}
//			if(updatePst!=null) {
//				updatePst.close();
//				updatePst = null;
//			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
			if(countRs!=null) {
				countRs.close();
				countRs = null;
			}
			if(countPst!=null) {
				countPst.close();
				countPst = null;
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
		mqLog.setModel("爬虫生成电商售后退款单");
		mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_20100000002));
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("orderNo");
		mqLog.setMethod("com.topideal.service.timer.impl.CrawlerOrderReturnIdepotServiceImpl.saveOrderReturnIdepot()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.TIMER_CRAWLER_ORDER_RETURN_IDEPOT.getTopic());
		mqLog.setTags(MQOrderEnum.TIMER_CRAWLER_ORDER_RETURN_IDEPOT.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("爬虫生成电商售后退款单---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}
}
