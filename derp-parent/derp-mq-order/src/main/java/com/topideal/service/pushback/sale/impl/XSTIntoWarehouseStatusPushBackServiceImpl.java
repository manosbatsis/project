package com.topideal.service.pushback.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.dao.common.SdSaleConfigItemDao;
import com.topideal.dao.sale.SaleReturnDeclareOrderDao;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnIdepotItemDao;
import com.topideal.dao.sale.SaleReturnOdepotDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.dao.sale.SaleSdOrderDao;
import com.topideal.dao.sale.SaleSdOrderItemDao;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.entity.vo.sale.SaleReturnDeclareOrderModel;
import com.topideal.entity.vo.sale.SaleReturnIdepotItemModel;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.entity.vo.sale.SaleSdOrderItemModel;
import com.topideal.entity.vo.sale.SaleSdOrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.service.pushback.sale.XSTIntoWarehouseStatusPushBackService;

import net.sf.json.JSONObject;

/**
 * 进仓单状态回推库存加减成功回推 
 * 2019/02/27
 * 杨创
 */
@Service
public class XSTIntoWarehouseStatusPushBackServiceImpl implements XSTIntoWarehouseStatusPushBackService {
	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(XSTIntoWarehouseStatusPushBackServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;// 销售退货表头
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货出入库
	@Autowired
	private SaleReturnOdepotDao saleReturnOdepotDao;// 销售退货出库单
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;//销售退预申报
	@Autowired
	private SdSaleConfigDao sdSaleConfigDao;
	@Autowired
	private SdSaleConfigItemDao sdSaleConfigItemDao;
	@Autowired
	private SaleSdOrderDao saleSdOrderDao;
	@Autowired
	private SaleSdOrderItemDao saleSdOrderItemDao;
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	
	// 进仓单状态回推
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201153600,model=DERP_LOG_POINT.POINT_13201153600_Label,keyword="code")
	public boolean updatexSTIntoWarehouseStatusPushBackInfo(String json, String keys, String topics, String tags)
			throws Exception {
		Thread.sleep(100);
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		String addAndSubTags = (String)customParam.get("tags");// 1 调增 , 0 调减
		// 销售退货模块 以入定出
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setCode(code);// 销售退货订单编码
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		if (saleReturnOrderModel == null) {
			logger.error(DERP.MQ_FAILTYPE_04 + "没有查到销售退货订单,订单编号code:" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查到销售退货订单,订单编号code:" + code);
		}
		SaleReturnDeclareOrderModel declareOrderModel = null;
		//退入
		if(addAndSubTags.equals("1")){
            //查询销售退货入库单
			SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
			saleReturnIdepotModel.setOrderCode(code);
			saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
			if (null == saleReturnIdepotModel) {
				logger.error(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在,销售退货订单号code:" + code);
				throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在,销售退货订单号code" + code);
			}

			//把销售退货入库单状态改为已入仓
			SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();
			sReturnIdepotModel.setId(saleReturnIdepotModel.getId());// 销售退货入库id
			sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);// "011","待入仓" 012","已入仓
			saleReturnIdepotDao.modify(sReturnIdepotModel);
			//修改销售退订单为已入仓
			SaleReturnOrderModel orderModel = new SaleReturnOrderModel();
			orderModel.setId(saleReturnOrderModel.getId());
			orderModel.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_012);
			saleReturnOrderDao.modify(orderModel);

			//查询预申报单
			if(StringUtils.isNotBlank(saleReturnIdepotModel.getReturnDeclareOrderCode())) {
				declareOrderModel = new SaleReturnDeclareOrderModel();
				declareOrderModel.setCode(saleReturnIdepotModel.getReturnDeclareOrderCode());
				declareOrderModel = saleReturnDeclareOrderDao.searchByModel(declareOrderModel);
			}
			
			/**
			 * （1）仅对类型为“退货退款”的销售退货入库单入仓成功后会对应生成销售SD单；
				（2）以“公司+事业部+客户”查询销售SD配置表 在生效日期范围内且审核时间为最新的一条配置信息，是否存在配置记录，若无则结束，若有则进行下一步；			
				（3）若销售SD配置记录中存在单比例配置，则上架单中所有商品均按照对应单比例配置的“SD类型+SD比例”进行生成的销售SD单，各商品销售SD类型金额 = 退货入库数量*退货单价*SD比例（退货单价为负数保存）；
				（4）若销售SD配置记录中存在多比例配置，根据上架单中的商品标准条码查询是否存在对应多比例配置的“SD类型+SD比例”，若有则生成销售SD单，商品销售SD类型金额 = 退货入库数量*退货单价*SD比例（退货单价为负数保存）；
				（5）若查询不存在任何SD类型的比例配置项，则该商品不生成销售SD数据；			
				（6）仅对2021年10月开始的退货入库单生成销售SD单，历史月份不生成。
			 */
			if(DERP_ORDER.SALERETURNORDER_RETURNMODE_1.equals(saleReturnOrderModel.getReturnMode()) ) {
				generateSaleSdOrder(saleReturnIdepotModel,saleReturnOrderModel);
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("orderCodes", saleReturnOrderModel.getCode());
				map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
				rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
			
			}
			
		}

		//退出
		if(addAndSubTags.equals("0")){
			//查询 销售退货出库单
			SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
			saleReturnOdepotModel.setOrderCode(code);
			saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
			if (saleReturnOdepotModel == null) {
				logger.error("(已入定出)销售退货出库单不存在,售退货订单编号code" + code);
				throw new RuntimeException("(已入定出)销售退货出库单不存在,售退货订单编号code" + code);
			}

			//把销售退货出库单状态改为已出仓
			SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
			sReturnOdepotModel.setId(saleReturnOdepotModel.getId());// 销售退货出库id
			sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_016);// DCC("015","待出仓"),YCC("016","已出仓"),
			saleReturnOdepotDao.modify(sReturnOdepotModel);
			//修改销售退订单为已出仓
			SaleReturnOrderModel orderModel = new SaleReturnOrderModel();
			orderModel.setId(saleReturnOrderModel.getId());
			orderModel.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_016);
			saleReturnOrderDao.modify(orderModel);

			//查询预申报单
			if(StringUtils.isNotBlank(saleReturnOdepotModel.getReturnDeclareOrderCode())) {
				declareOrderModel = new SaleReturnDeclareOrderModel();
				declareOrderModel.setCode(saleReturnOdepotModel.getReturnDeclareOrderCode());
				declareOrderModel = saleReturnDeclareOrderDao.searchByModel(declareOrderModel);
			}
		}

		//异步线程池更新销售退订单、预申报单状态为完结
		updateStatus(saleReturnOrderModel, declareOrderModel);	
		
		return true;
	}
	/**
	 * 异步线程池更新销售退订单、预申报单状态为完结
	 * */
	@Async("asyncServiceExecutor")
	public void updateStatus(SaleReturnOrderModel saleReturnOrderModel,SaleReturnDeclareOrderModel declareOrderModel){
		try {
			Thread.sleep(200);

            /**修改销售退订单状态*/
			//查询入库单
			SaleReturnIdepotModel IdepotModel = new SaleReturnIdepotModel();
			IdepotModel.setOrderCode(saleReturnOrderModel.getCode());
			IdepotModel = saleReturnIdepotDao.searchByModel(IdepotModel);
			//查询出库单
			SaleReturnOdepotModel OdepotModel = new SaleReturnOdepotModel();
			OdepotModel.setOrderCode(saleReturnOrderModel.getCode());
			OdepotModel = saleReturnOdepotDao.searchByModel(OdepotModel);

			SaleReturnOrderModel saleReturnOrderTemp = new SaleReturnOrderModel();
			saleReturnOrderTemp.setId(saleReturnOrderModel.getId());
             /**1.销售退货出库仓不为空&&出库单及状态为已出仓=完结
			  * 2.销售退货出库仓为空&&出库订单为空=完结
			  * 3.出库订单不为空&&出库状态为已出仓=完结
			  * */
			if(saleReturnOrderModel.getOutDepotId()!=null&&OdepotModel!=null
					&&OdepotModel.getStatus().equals(DERP_ORDER.SALERETURNODEPOT_STATUS_016)){
				saleReturnOrderTemp.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//完结
			}else if(saleReturnOrderModel.getOutDepotId()==null&&OdepotModel==null){
				saleReturnOrderTemp.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//完结
			}else if(OdepotModel!=null&&OdepotModel.getStatus().equals(DERP_ORDER.SALERETURNODEPOT_STATUS_016)){
				saleReturnOrderTemp.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//完结
			}
			saleReturnOrderDao.modify(saleReturnOrderTemp);

			/**修改预申报单状态*/
			if(declareOrderModel!=null) {
				boolean declareIsEnd = true;
				//2.获取预申报单对应的所有入库单、出库单
				SaleReturnIdepotModel returnIdepotModel = new SaleReturnIdepotModel();
				returnIdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());
				List<SaleReturnIdepotModel> returnIdepotList = saleReturnIdepotDao.list(returnIdepotModel);
				if (returnIdepotList != null && returnIdepotList.size() > 0) {
					for (SaleReturnIdepotModel IdepotModelTemp : returnIdepotList) {
						if (!IdepotModelTemp.getStatus().equals(DERP_ORDER.SALERETURNIDEPOT_STATUS_012)) {
							declareIsEnd = false;
							break;
						}
					}
				}
				SaleReturnOdepotModel returnOdepotModel = new SaleReturnOdepotModel();
				returnOdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());
				List<SaleReturnOdepotModel> returnOdepotList = saleReturnOdepotDao.list(returnOdepotModel);
				if (returnOdepotList != null && returnOdepotList.size() > 0) {
					for (SaleReturnOdepotModel odepotModelTemp : returnOdepotList) {
						if (!odepotModelTemp.getStatus().equals(DERP_ORDER.SALERETURNODEPOT_STATUS_016)) {
							declareIsEnd = false;
							break;
						}
					}
				}
				//退货订单有出库仓&&出库单为空=false
				if(saleReturnOrderModel.getOutDepotId()!=null&&(returnOdepotList==null||returnOdepotList.size()<=0)){
					declareIsEnd = false;
				}

				SaleReturnDeclareOrderModel declareOrderTemp = new SaleReturnDeclareOrderModel();
				declareOrderTemp.setId(declareOrderModel.getId());
				if (declareIsEnd == true) {
					declareOrderTemp.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_005);//已完结
				} else {
					declareOrderTemp.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_004);//已入仓
				}
				saleReturnDeclareOrderDao.modify(declareOrderTemp);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * 生成销售SD单
	 * @param saleReturnIdepotModel
	 * @param saleReturnOrderModel
	 * @throws Exception
	 */
	private void generateSaleSdOrder(SaleReturnIdepotModel saleReturnIdepotModel, SaleReturnOrderModel saleReturnOrderModel) throws Exception {
		Map<String, List<SaleSdOrderItemModel>> resultMap = new HashMap<String, List<SaleSdOrderItemModel>>();
		Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> numMap = new HashMap<String, Integer>();
		
		SaleReturnIdepotItemModel saleReturnIdepotItemModel = new SaleReturnIdepotItemModel();
		saleReturnIdepotItemModel.setSreturnIdepotId(saleReturnIdepotModel.getId());
		List<SaleReturnIdepotItemModel> queryIdepotItemList = saleReturnIdepotItemDao.list(saleReturnIdepotItemModel);
		Map<String ,List<SaleReturnIdepotItemModel>> idepotItemMap = queryIdepotItemList.stream().collect(Collectors.groupingBy(SaleReturnIdepotItemModel::getInGoodsNo));

		//查询销售退单是否存在销售SD单，若存在，先删除，再重新生成
		SaleSdOrderModel querySaleSdOrder = new SaleSdOrderModel();
		querySaleSdOrder.setOrderCode(saleReturnIdepotModel.getCode());
		List<SaleSdOrderModel> querySaleSdOrderList = saleSdOrderDao.list(querySaleSdOrder);
		if(querySaleSdOrderList != null && querySaleSdOrderList.size() > 0){
			for(SaleSdOrderModel delSdModel:querySaleSdOrderList){
				delSdModel.setState(DERP.DEL_CODE_006);
				saleSdOrderDao.modify(delSdModel);
			}
		}
		// 1、以“公司+事业部+客户”查询销售SD配置表 ,退货入库时间 在生效日期范围内，若无则结束；
		SdSaleConfigDTO config = new SdSaleConfigDTO();
		config.setMerchantId(saleReturnIdepotModel.getMerchantId());
		config.setBuId(saleReturnIdepotModel.getBuId());
		config.setCustomerId(saleReturnIdepotModel.getCustomerId());
		config.setOrderDate(saleReturnIdepotModel.getReturnInDate());
		config.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);
		List<SdSaleConfigDTO> configList = sdSaleConfigDao.listDTO(config);
		if (configList == null || configList.size() <= 0) {
			return;
		}
		// 审核时间为最新的一条配置信息
		configList = configList.stream().sorted(Comparator.comparing(SdSaleConfigDTO::getAuditDate).reversed()).collect(Collectors.toList());
		config = configList.get(0);

		SdSaleConfigItemModel configItemModel = new SdSaleConfigItemModel();
		configItemModel.setSaleConfigId(config.getId());
		List<SdSaleConfigItemModel> configItemList = sdSaleConfigItemDao.list(configItemModel);
		for(SdSaleConfigItemModel configItem : configItemList){
			for(String goodsNo : idepotItemMap.keySet()){
				List<SaleReturnIdepotItemModel> idepotItemList = idepotItemMap.get(goodsNo);
				SaleReturnIdepotItemModel idepotItem = idepotItemList.get(0);
				/**
				 * 2、若销售SD配置记录中存在单比例配置，则销售退中所有商品均按照对应单比例配置的“SD类型+SD比例”进行生成的销售SD单，
				 * 各商品销售SD类型金额 = 销售退货数量*销售退单价*SD比例
				 *
				 * 3、若销售SD配置记录中存在多比例配置，根据销售退单中的商品标准条码查询是否存在对应多比例配置的“SD类型+SD比例”，若有则生成销售SD单，
				 * 商品销售SD类型金额 = 销售退货数量*销售退单价*SD比例；
				 */				
				Integer returnNum = idepotItemList.stream().mapToInt(SaleReturnIdepotItemModel::getReturnNum).sum();
				Integer wornNum = idepotItemList.stream().mapToInt(SaleReturnIdepotItemModel::getWornNum).sum();
				Integer num = returnNum + wornNum;
				if(num.intValue() < 1){
                    continue;
                }
				
				if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(configItem.getSdType())  && !(StringUtils.isNotBlank(configItem.getCommbarcode())
						&& idepotItem.getCommbarcode().equals(configItem.getCommbarcode()))){
					continue;
				}
				SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
				saleReturnOrderItem.setOrderId(saleReturnIdepotModel.getOrderId());
				saleReturnOrderItem.setInGoodsId(idepotItem.getInGoodsId());
				List<SaleReturnOrderItemModel> saleReturnOrderItemList = saleReturnOrderItemDao.list(saleReturnOrderItem);
				saleReturnOrderItem = saleReturnOrderItemList.get(0);

				SaleSdOrderItemModel sdItemModel = new SaleSdOrderItemModel();
				sdItemModel.setGoodsId(idepotItem.getInGoodsId());
				sdItemModel.setGoodsNo(idepotItem.getInGoodsNo());
				sdItemModel.setGoodsName(idepotItem.getInGoodsName());
				sdItemModel.setBarcode(idepotItem.getBarcode());
				sdItemModel.setCommbarcode(idepotItem.getCommbarcode());
				sdItemModel.setNum(num);
				sdItemModel.setPrice(saleReturnOrderItem.getPrice());
				sdItemModel.setAmount(saleReturnOrderItem.getPrice().multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_UP));
				sdItemModel.setSdRatio(configItem.getProportion().doubleValue());

				// SD金额 = 销售退货数量*销售退单价*SD比例
				BigDecimal sdAmount = saleReturnOrderItem.getPrice().multiply(new BigDecimal(num)).multiply(configItem.getProportion()).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal sdPrice = sdAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
				sdItemModel.setSdPrice(sdPrice);
				sdItemModel.setSdAmount(sdAmount);

				String key = configItem.getSdTypeId()+ "_" + configItem.getSdTypeName() + "_" + configItem.getSdSimpleName();
				if (resultMap.containsKey(key)) {
					List<SaleSdOrderItemModel> itemList = resultMap.get(key);
					itemList.add(sdItemModel);
					resultMap.put(key, itemList);

					BigDecimal totalAmount = amountMap.get(key);
					totalAmount = totalAmount.add(sdAmount);
					amountMap.put(key, totalAmount);

					Integer totalNum = numMap.get(key);
					totalNum = totalNum + num ;
					numMap.put(key, totalNum);

				} else {
					List<SaleSdOrderItemModel> itemList = new ArrayList<SaleSdOrderItemModel>();
					itemList.add(sdItemModel);
					resultMap.put(key, itemList);
					amountMap.put(key, sdAmount);
					numMap.put(key, num);
				}
			}
		}

		for (String key : resultMap.keySet()) {
			String sdTypeId = key.split("_")[0];
			String sdType = key.split("_")[1];
			String sdTypeName = key.split("_")[2];

			SaleSdOrderModel sdOrderModel = new SaleSdOrderModel();
			sdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSSD));
			sdOrderModel.setBuId(saleReturnIdepotModel.getBuId());
			sdOrderModel.setBuName(saleReturnIdepotModel.getBuName());
			sdOrderModel.setMerchantId(saleReturnIdepotModel.getMerchantId());
			sdOrderModel.setMerchantName(saleReturnIdepotModel.getMerchantName());
			sdOrderModel.setCustomerId(saleReturnIdepotModel.getCustomerId());
			sdOrderModel.setCustomerName(saleReturnIdepotModel.getCustomerName());
			sdOrderModel.setCurrency(saleReturnOrderModel.getCurrency());
			sdOrderModel.setOrderId(saleReturnIdepotModel.getId());
			sdOrderModel.setOrderCode(saleReturnIdepotModel.getCode());
			sdOrderModel.setOwnDate(saleReturnIdepotModel.getReturnInDate());
			sdOrderModel.setBusinessId(saleReturnOrderModel.getId());
			sdOrderModel.setBusinessCode(saleReturnOrderModel.getCode());
			sdOrderModel.setTotalSdAmount(amountMap.get(key));
			sdOrderModel.setTotalSdNum(numMap.get(key));
			sdOrderModel.setSdTypeId(Long.valueOf(sdTypeId));
			sdOrderModel.setSdType(sdType);
			sdOrderModel.setSdTypeName(sdTypeName);
			sdOrderModel.setPoNo(saleReturnOrderModel.getPoNo());
			sdOrderModel.setCreateName("系统创建");
			sdOrderModel.setCreateDate(TimeUtils.getNow());
			sdOrderModel.setState("001");
            sdOrderModel.setOrderSource(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_1);
            sdOrderModel.setOrderType(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);

			Long num = saleSdOrderDao.save(sdOrderModel);

			List<SaleSdOrderItemModel> itemList = resultMap.get(key);
			for (SaleSdOrderItemModel itemModel : itemList) {
				itemModel.setSaleSdOrderId(num);
				itemModel.setCreateDate(TimeUtils.getNow());
				saleSdOrderItemDao.save(itemModel);

			}
		}
		
        
	}
}
