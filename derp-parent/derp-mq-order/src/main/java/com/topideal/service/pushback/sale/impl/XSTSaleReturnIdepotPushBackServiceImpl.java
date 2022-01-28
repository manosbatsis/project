package com.topideal.service.pushback.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
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
import com.topideal.service.pushback.sale.XSTSaleReturnIdepotPushBackService;

import net.sf.json.JSONObject;
/**
 * 销售退上架入库库存加减成功回推
 * 2020/10/25
 * 杨创
 */
@Service
public class XSTSaleReturnIdepotPushBackServiceImpl implements XSTSaleReturnIdepotPushBackService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTSaleReturnIdepotPushBackServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;//销售退货订单
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库单
	@Autowired 
	private SaleReturnOdepotDao saleReturnOdepotDao;//销售退货出库
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
	
	// 销售退入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201159500,model=DERP_LOG_POINT.POINT_13201159500_Label,keyword="code")
	public boolean updateXSTSaleReturnIdepotPushBack(String json,String keys,String topics,String tags) throws Exception {
		// 不论是否是菜鸟仓传的code都是销售退货订单的code
		// 将字符串转成json 
		Thread.sleep(50);
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		// 销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();		
		saleReturnOrderModel.setCode(code);
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);
		
		if (saleReturnOrderModel == null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "没有查询到对应的销售退货订单,订单号:code" + code);
		}		
		// 根据销售退货入库单号 和 销售退货编号 查询销售退货入库表
		SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();		
		saleReturnIdepotModel.setOrderId(saleReturnOrderModel.getId());
		saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);						
		if (saleReturnIdepotModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
			throw new RuntimeException(DERP.MQ_FAILTYPE_04 + "销售退货入库单不存在code:"+code);
		}
		
		// 修改销售退货入库
    	SaleReturnIdepotModel sReturnIdepotModel = new SaleReturnIdepotModel();   	
    	sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);//'状态   DRC("011","待入仓"),YRC("012","已入仓"),  
    	sReturnIdepotModel.setId(saleReturnIdepotModel.getId());
    	saleReturnIdepotDao.modify(sReturnIdepotModel);    
    	
    	//修改销售退货订单状态
    	SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();    	
    	
    	//根据销售退货订单id  查询销售退货出库单
    	SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();		
		saleReturnOdepotModel.setOrderId(saleReturnOrderModel.getId());// 销售退货的订单号 订单号		
		saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
    	
    	//判断是否已经出仓 	
		if(saleReturnOdepotModel != null && DERP_ORDER.SALERETURNODEPOT_STATUS_016.equals(saleReturnOdepotModel.getStatus())){
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_007);//已完结
		}else{
			saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_012);//已入仓
		}
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderDao.modify(saleReturnOrderModel1);	
		
		//若存在预申报单，是否全部都有出入库单，修改预申报单状态
		if(DERP_ORDER.SALEDECLARE_ISGENERATE_1.equals(saleReturnOrderModel.getIsGenerateDeclare()) ) {				
	    	SaleReturnDeclareOrderDTO declareDTO = new SaleReturnDeclareOrderDTO();
	    	declareDTO.setSaleReturnOrderIds(saleReturnOrderModel.getId().toString());
	    	declareDTO = saleReturnDeclareOrderDao.searchDetail(declareDTO);
	    	if(declareDTO != null) {
	    		if(declareDTO != null) {
	    			SaleReturnDeclareOrderModel declareModel = new SaleReturnDeclareOrderModel();
		    		declareModel.setId(declareDTO.getId());
		    		boolean flag = true;
		    		//根据销售退预申报id  查询所有销售退货出库单		    		
		    		for(String returnId : declareDTO.getSaleReturnOrderIds().split(",")) {		    			
		    			//遍历预申报关联的销售退对应的销售退入库单
		    			SaleReturnIdepotModel querySaleReturnIdepotModel = new SaleReturnIdepotModel();		
		    			querySaleReturnIdepotModel.setOrderId(Long.valueOf(returnId));// 销售退货的订单号 订单号	
		    			querySaleReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_012);
		    			querySaleReturnIdepotModel = saleReturnIdepotDao.searchByModel(querySaleReturnIdepotModel);
		    			if(querySaleReturnIdepotModel == null) {
		    				flag = false;
		    				break;
		    			}
		    			
		    			//遍历预申报关联的销售退对应的销售退出库单
		    			SaleReturnOdepotModel querySaleReturnOdepotModel = new SaleReturnOdepotModel();		
		    			querySaleReturnOdepotModel.setOrderId(Long.valueOf(returnId));// 销售退货的订单号 订单号	
		    			querySaleReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_016);
		    			querySaleReturnOdepotModel = saleReturnOdepotDao.searchByModel(querySaleReturnOdepotModel);
		    			if(querySaleReturnOdepotModel == null) {
		    				flag = false;
		    				break;
		    			}
		    		}
		    		if(flag) {
		    			declareModel.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_005);//已完结;
		    		}else {
		    			declareModel.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_004);//已入仓;
		    		}
		    		declareModel.setModifyDate(TimeUtils.getNow());
		    		saleReturnDeclareOrderDao.modify(declareModel);
		    	}
	    	}
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
		return true;
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
