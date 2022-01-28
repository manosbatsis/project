package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.ReceiveBillDao;
import com.topideal.dao.common.QuotaPeriodConfigDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.bill.ReceiveBillModel;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.timer.SaveCutomerQuotaWarningService;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveCutomerQuotaWarningServiceImpl implements SaveCutomerQuotaWarningService {

	private static final Logger LOGGER = Logger.getLogger(SaveCutomerQuotaWarningServiceImpl.class) ;

	@Autowired
	private CustomerQuotaConfigDao customerQuotaConfigDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private CutomerQuotaWarningDao cutomerQuotaWarningDao;
	@Autowired
	private CutomerQuotaWarningItemDao cutomerQuotaWarningItemDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private ReceiveBillDao receiveBillDao;
	@Autowired
	private QuotaPeriodConfigDao quotaPeriodConfigDao;

	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201120005,model=DERP_LOG_POINT.POINT_13201120005_Label)
	@Override
	public void saveCutomerQuotaWarning(String json, String keys, String topics, String tags) throws Exception {
		//客户额度需要过滤内部客户的数据  只要配置表不配置内部客户的配置就可以了 生产数据不做拦截
		JSONObject jsonData = JSONObject.fromObject(json);
		Long buId = null ;
		if(jsonData.containsKey("buId") && jsonData.get("buId") != null) {
			buId = Long.valueOf(jsonData.getString("buId"));
		}
		Long customerId = null ;
		if(jsonData.containsKey("customerId") && jsonData.get("customerId") != null) {
			customerId = Long.valueOf(jsonData.getString("customerId"));
		}
		String currentDate = null ;
		if(jsonData.containsKey("currentDate") && jsonData.get("currentDate") != null) {
			currentDate = jsonData.getString("currentDate");
		}else {
			currentDate = TimeUtils.formatFullTime();//时间为空，获取当前时间
		}

		Map<String,Object> paramMap = new HashedMap<String, Object>();
		paramMap.put("buId", buId);
		paramMap.put("customerId", customerId);
		paramMap.put("currentDate", currentDate);

		List<CustomerQuotaConfigDTO> list = customerQuotaConfigDao.listEffectiveDTO(paramMap);

		if(list != null && list.size() > 0) {
			for(CustomerQuotaConfigDTO configDTO: list) {
				// 客户id或者事业部id为空 继续
			    if (configDTO.getCustomerId()==null||configDTO.getBuId()==null)continue;

				boolean isInnerCustomer = false;
				// 客户是否为内部公司 不用过滤客户是内部公司的数据
				/*Map<String, Object> customerInfoMap = new HashMap<>();
				customerInfoMap.put("customerid", configDTO.getCustomerId());
				customerInfoMap.put("type", DERP_SYS.CUSTOMERINFO_TYPE_1);// 1:内部,2:外部
				customerInfoMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
				customerInfoMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
				CustomerInfoMogo customerInfoMogo = customerInfoMongoDao.findOne(customerInfoMap);
				//过滤客户为内部公司的数据
				if (customerInfoMogo != null) {
					 continue;
				}*/

				QuotaPeriodConfigModel quotaPeriod=new QuotaPeriodConfigModel();
				quotaPeriod.setBuId(configDTO.getBuId());
				quotaPeriod.setConfigObjectId(configDTO.getCustomerId());//客户id
				quotaPeriod.setQuotaType(DERP_ORDER.QUOTACONFIG_quotaType_2);//客户额度
				quotaPeriod.setStatus(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_1);// 已审核
				quotaPeriod = quotaPeriodConfigDao.searchByModel(quotaPeriod);
				if(quotaPeriod==null)quotaPeriod=new QuotaPeriodConfigModel();
				BigDecimal periodQuota = quotaPeriod.getPeriodQuota();
				if (periodQuota==null) periodQuota=new BigDecimal(0);



				List<CutomerQuotaWarningItemModel> itemList = new ArrayList<CutomerQuotaWarningItemModel>();
				Map<String,Object> param = new HashedMap<String, Object>();
				param.put("buId", configDTO.getBuId());
				param.put("customerId", configDTO.getCustomerId());
				param.put("periodDate", TimeUtils.formatFullTime(quotaPeriod.getPeriodDate()));
				//销售在途金额
				Map<String,Object> noshelfMap = getNoShelfMap(configDTO,param);
				BigDecimal totalNoShelfAmount = (BigDecimal) noshelfMap.get("totalNoShelfAmount");//累计在途金额
				List<CutomerQuotaWarningItemModel> noShelfList = (List<CutomerQuotaWarningItemModel>) noshelfMap.get("noShelfList");//在途单据
				if(noShelfList != null && noShelfList.size() > 0){
					itemList.addAll(noShelfList);
				}
				//待确认
				Map<String,Object> noConfirmMap = getNoConfirmMap(configDTO,param);
				BigDecimal totalNoConfirmAmount = (BigDecimal) noConfirmMap.get("totalNoConfirmAmount");//累计待确认金额
				List<CutomerQuotaWarningItemModel> noConfirmList = (List<CutomerQuotaWarningItemModel>) noConfirmMap.get("noConfirmList");//待确认单据
				if(noConfirmList != null && noConfirmList.size() > 0){
					itemList.addAll(noConfirmList);
				}
				//待开票
				Map<String,Object> noInvoiceMap = getNoInvoiceMap(configDTO,param);
				BigDecimal totalNoInvoiceAmount = (BigDecimal) noInvoiceMap.get("totalNoInvoiceAmount");//累计待开票金额
				List<CutomerQuotaWarningItemModel> noInvoiceList = (List<CutomerQuotaWarningItemModel>) noInvoiceMap.get("noInvoiceList");//待开票单据
				if(noInvoiceList != null && noInvoiceList.size() > 0){
					itemList.addAll(noInvoiceList);
				}
				//待回款
				Map<String,Object> noReturnMap = getNoReturnMap(configDTO,param);
				BigDecimal totalNoReturnAmount = (BigDecimal) noReturnMap.get("totalNoReturnAmount");//累计待回款金额
				List<CutomerQuotaWarningItemModel> noReturnList = (List<CutomerQuotaWarningItemModel>) noReturnMap.get("noReturnList");//待回款单据
				if(noReturnList != null && noReturnList.size() > 0){
					itemList.addAll(noReturnList);
				}
				//已上架金额
				BigDecimal shelfAmount = getShelfAmount(configDTO,param);
				//已回款金额//已回款金额取以“事业部+客户”  获取应收账单列表中的对应状态 为 “已核销”的应收账单 的 “应收汇总金额
				BigDecimal returnfAmount = getReturnAmount(configDTO,param);
				/**
				 * 待出账单金额=已上架金额汇总-待确认账单金额-待开票金额-待回款金额-已回款金额
				 * 待出账单金额=已上架金额汇总-待确认账单金额-待开票金额-待回款金额-已回款金额
				 */
				BigDecimal noBillAmount = BigDecimal.ZERO;
				noBillAmount = shelfAmount.subtract(totalNoConfirmAmount).subtract(totalNoInvoiceAmount).subtract(totalNoReturnAmount).subtract(returnfAmount);

				/**
				 * 可用额度=客户总额度- 期初已使用额度 - 销售在途金额 - 待出账单金额 - 待确认账单金额 - 待开票金额 - 待回款金额
				 */
				BigDecimal availableAmount = BigDecimal.ZERO;
				availableAmount = configDTO.getCustomerQuota().subtract(periodQuota).subtract(totalNoShelfAmount).subtract(noBillAmount)
									.subtract(totalNoConfirmAmount).subtract(totalNoInvoiceAmount).subtract(totalNoReturnAmount);


				 //1、删除当前 （事业部+客户） 已存在客户额度预警数据
				CutomerQuotaWarningModel existModel = new CutomerQuotaWarningModel();
				existModel.setBuId(configDTO.getBuId());
				existModel.setCustomerId(configDTO.getCustomerId());
				existModel = cutomerQuotaWarningDao.searchByModel(existModel);
				if(existModel != null){
					/*CutomerQuotaWarningItemModel existItemModel = new CutomerQuotaWarningItemModel();
					existItemModel.setWaringId(existModel.getId());
					List<CutomerQuotaWarningItemModel> existItemList = cutomerQuotaWarningItemDao.list(existItemModel);
					List<Long> itemIds = new ArrayList<Long>();
					if(existItemList != null && existItemList.size() > 0){
						for(CutomerQuotaWarningItemModel eItem: existItemList){
							itemIds.add(eItem.getId());
						}
					}*/
					//删除头数据
					List<Long> ids = new ArrayList<Long>();
					ids.add(existModel.getId());
					cutomerQuotaWarningDao.delete(ids);
					// 删除体数据
					cutomerQuotaWarningItemDao.deleteByWarningId(existModel.getId());
				}

				//2、封装实体 新增
				CutomerQuotaWarningModel model = new CutomerQuotaWarningModel();
				model.setBuId(configDTO.getBuId());
				model.setBuName(configDTO.getBuName());
				model.setCustomerId(configDTO.getCustomerId());
				model.setCustomerName(configDTO.getCustomerName());
				model.setCurrency(configDTO.getCurrency());
				model.setCustomerQuota(configDTO.getCustomerQuota());
				model.setSaleNoshelfAmount(totalNoShelfAmount);
				model.setNobillAmount(noBillAmount);
				model.setNoconfirmAmount(totalNoConfirmAmount);
				model.setNoinvoiceAmount(totalNoInvoiceAmount);
				model.setNoreturnAmount(totalNoReturnAmount);
				model.setAvailableAmount(availableAmount);
				model.setCreateDate(TimeUtils.getNow());
				model.setPeriodQuota(periodQuota);

				Long num = cutomerQuotaWarningDao.save(model);

				if(num != null && itemList.size() > 0){
					for(CutomerQuotaWarningItemModel item: itemList){
						item.setWaringId(num);
						item.setCreateDate(TimeUtils.getNow());
						cutomerQuotaWarningItemDao.save(item);
					}
				}

			}
		}
	}
	/**
	 * 销售在途
	 * @param configDTO
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> getNoShelfMap(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		Map<String,Object> result = new HashedMap<String, Object>();
		List<CutomerQuotaWarningItemModel> noShelfList = new ArrayList<CutomerQuotaWarningItemModel>();
		BigDecimal totalNoShelfAmount = BigDecimal.ZERO;
		/**
		 *  以“事业部+客户”取有效的销售订单累计在途数据，且单据的创建日期需大于对应客户额度记录配置中的“期初开始日期；
		 * 	累计销售在途数量计算公式如下（与财务进销存的累计销售在途计算逻辑一致）
		 * （1）取符合时间范围内的已出库的销售订单（不分销售类型）出库未上架量
		 * （2）计算公式：累计销售在途量=销售订单（不分销售类型）出库总数量-已标记上架量汇总-已标记残损量汇总-已标记少货量汇总；
		 */
		Map<Long,CutomerQuotaWarningItemModel> modelMap = new HashedMap<Long,CutomerQuotaWarningItemModel>();
		List<Map<String,Object>> noShelfAmountList = saleOrderDao.getNoShelfNum(paramMap);
		for(Map<String,Object> map : noShelfAmountList) {
			Long outId = (Long) map.get("id");//销售出库订单id
    		Long goodsId = (Long) map.get("goods_id");//商品id
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		BigDecimal num = (BigDecimal) map.get("num");//在途数量
			Long saleItemId = (Long) map.get("sale_item_id");//销售明细id

    		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(outId);

    		if (saleOutDepotModel==null)continue;
    		Long orderId = saleOutDepotModel.getSaleOrderId();   //销售订单id
    		//箱托转化单位
    		Integer saleNum = changeUnit(tallyingUnit,num.intValue(),goodsId);

    		SaleOrderModel saleModel = saleOrderDao.searchById(orderId);
    		if (saleModel==null)continue;
    		SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(saleItemId);
    		BigDecimal price = saleItemModel.getPrice();
    		/**
    		 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
    		 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
    		 */
    		ExchangeRateMongo exchangeRateMongo = null;
    		if(!saleModel.getCurrency().equals(configDTO.getCurrency())) {
    			//创建日期月底最后一天的平均汇率
    			String month = TimeUtils.formatMonth(saleModel.getCreateDate()) ;
    			String lastDay = TimeUtils.getLastDayOfMonth(month);
    			exchangeRateMongo = getExchangeRate(saleModel.getCurrency(),configDTO.getCurrency(),lastDay);
				price = price.multiply(new BigDecimal(exchangeRateMongo.getAvgRate()));
    		}

    		BigDecimal amount = saleItemModel.getPrice().multiply(new BigDecimal(saleNum)).setScale(2, BigDecimal.ROUND_HALF_UP);//销售金额
    		BigDecimal occupationAmount = price.multiply(new BigDecimal(saleNum)).setScale(2, BigDecimal.ROUND_HALF_UP) ;//占用金额

    		if(modelMap.containsKey(orderId)) {
    			CutomerQuotaWarningItemModel mapItemModel = modelMap.get(orderId);
    			Integer itemNum = mapItemModel.getNum() + saleNum;
    			BigDecimal itemAmount = mapItemModel.getAmount().add(amount);
    			BigDecimal itemOccupationAmount = mapItemModel.getOccupationAmount().add(occupationAmount);
    			mapItemModel.setNum(itemNum);
    			mapItemModel.setAmount(itemAmount);
    			mapItemModel.setOccupationAmount(itemOccupationAmount);
    			modelMap.put(orderId, mapItemModel);
    		}else {
    			CutomerQuotaWarningItemModel itemModel = new CutomerQuotaWarningItemModel();
        		itemModel.setMerchantId(saleModel.getMerchantId());
        		itemModel.setMerchantName(saleModel.getMerchantName());
        		itemModel.setCode(saleModel.getCode());
        		itemModel.setPoNo(saleModel.getPoNo());
        		itemModel.setCurrency(saleModel.getCurrency());
        		itemModel.setType("1");//1-销售在途 2-待确认 3-待开票 4-待回款
        		itemModel.setNum(saleNum);
        		itemModel.setAmount(amount);
        		itemModel.setOccupationAmount(occupationAmount);
        		itemModel.setRate(exchangeRateMongo == null ? 1.0 : exchangeRateMongo.getAvgRate());
        		itemModel.setRateDate(exchangeRateMongo == null ? null : TimeUtils.parseFullTime(exchangeRateMongo.getEffectiveDate()+ " 00:00:00") );
    			modelMap.put(orderId, itemModel);
    		}
    		totalNoShelfAmount = totalNoShelfAmount.add(occupationAmount);
		}
		for(Long key : modelMap.keySet()) {
			CutomerQuotaWarningItemModel itemModel = modelMap.get(key);
			noShelfList.add(itemModel);
		}
		result.put("noShelfList", noShelfList);
		result.put("totalNoShelfAmount", totalNoShelfAmount);

		return result;
	}
	/**
	 * 待确认账单
	 * @param configDTO
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> getNoConfirmMap(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		Map<String,Object> result = new HashedMap<String, Object>();
		List<CutomerQuotaWarningItemModel> noConfirmList = new ArrayList<CutomerQuotaWarningItemModel>();
		BigDecimal totalNoConfirmAmount = BigDecimal.ZERO;
		/**
		 * 1、以“事业部+客户” 获取应收账单列表中的对应状态 为 “待提交”、“待审核”、“待作废” 的应收账单 的 “应收汇总金额” ，需要过滤掉 客户为内部公司的数据；
		 * 2、 “待提交”、“待审核”的账单创建日期需大于对应客户额度记录配置中的“期初开始日期“；
		 * 3、“待作废” 的作废提交日期需大于对应客户额度记录配置中的“期初开始日期，日期取最早的一条；
		 */
		List<Map<String,Object>> noConfirmAmountList = receiveBillDao.getNoConfirmAmount(paramMap);
		for(Map<String,Object> map : noConfirmAmountList) {
			Long billId = (Long) map.get("bill_id");//应收订单id
			BigDecimal num = (BigDecimal) map.get("num");//数量
			BigDecimal amount = (BigDecimal) map.get("amount");//金额

			ReceiveBillModel billModel = receiveBillDao.searchById(billId);
			/**
    		 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
    		 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
    		 */
    		ExchangeRateMongo exchangeRateMongo = null;
    		BigDecimal occupationAmount = BigDecimal.ZERO;
    		if(!billModel.getCurrency().equals(configDTO.getCurrency())) {
    			//创建日期月底最后一天的平均汇率
    			String month = TimeUtils.formatMonth(billModel.getCreateDate()) ;
    			String lastDay = TimeUtils.getLastDayOfMonth(month);
    			exchangeRateMongo = getExchangeRate(billModel.getCurrency(),configDTO.getCurrency(),lastDay);
    			occupationAmount = amount.multiply(new BigDecimal(exchangeRateMongo.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);//占用金额
			}else {
				occupationAmount = amount;
			}

    		CutomerQuotaWarningItemModel itemModel = new CutomerQuotaWarningItemModel();
    		itemModel.setMerchantId(billModel.getMerchantId());
    		itemModel.setMerchantName(billModel.getMerchantName());
    		itemModel.setCode(billModel.getCode());
    		itemModel.setCurrency(billModel.getCurrency());
    		itemModel.setType("2");//1-销售在途 2-待确认 3-待开票 4-待回款
    		itemModel.setNum(num.intValue());
    		itemModel.setReceiveType(billModel.getSettlementType());
    		itemModel.setAmount(amount);
    		itemModel.setOccupationAmount(occupationAmount);
    		itemModel.setRate(exchangeRateMongo == null ? 1.0 : exchangeRateMongo.getAvgRate());
    		itemModel.setRateDate(exchangeRateMongo == null ? null : TimeUtils.parseFullTime(exchangeRateMongo.getEffectiveDate()+ " 00:00:00") );

    		noConfirmList.add(itemModel);
    		totalNoConfirmAmount = totalNoConfirmAmount.add(occupationAmount);

		}
		result.put("noConfirmList", noConfirmList);
		result.put("totalNoConfirmAmount", totalNoConfirmAmount);
		return result;
	}
	/**
	 * 待开票账单
	 * @param configDTO
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> getNoInvoiceMap(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		Map<String,Object> result = new HashedMap<String, Object>();
		List<CutomerQuotaWarningItemModel> noInvoiceList = new ArrayList<CutomerQuotaWarningItemModel>();
		BigDecimal totalNoInvoiceAmount = BigDecimal.ZERO;
		/**
		 * 1、以“事业部+客户” 获取应收账单列表中的对应状态 为 “待核销”、“部分核销”的应收账单 且 发票状态为“待开票” 的 “应收汇总金额” ，需要过滤掉 客户为内部公司的数据；
		 * 2、 “待核销”、“部分核销”的账单创建日期需大于对应客户额度记录配置中的“期初开始日期”；
		 */
		List<Map<String,Object>> noInvoiceAmountList = receiveBillDao.getNoInvoiceAmount(paramMap);
		for(Map<String,Object> map : noInvoiceAmountList) {
			Long billId = (Long) map.get("bill_id");//应收订单id
			BigDecimal num = (BigDecimal) map.get("num");//数量
			BigDecimal amount = (BigDecimal) map.get("amount");//金额

			ReceiveBillModel billModel = receiveBillDao.searchById(billId);
			/**
    		 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
    		 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
    		 */
    		ExchangeRateMongo exchangeRateMongo = null;
    		BigDecimal occupationAmount = BigDecimal.ZERO;
    		if(!billModel.getCurrency().equals(configDTO.getCurrency())) {
    			//创建日期月底最后一天的平均汇率
    			String month = TimeUtils.formatMonth(billModel.getCreateDate()) ;
    			String lastDay = TimeUtils.getLastDayOfMonth(month);
    			exchangeRateMongo = getExchangeRate(billModel.getCurrency(),configDTO.getCurrency(),lastDay);
    			occupationAmount = amount.multiply(new BigDecimal(exchangeRateMongo.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);//占用金额
			} else {
				occupationAmount = amount;
			}

    		CutomerQuotaWarningItemModel itemModel = new CutomerQuotaWarningItemModel();
    		itemModel.setMerchantId(billModel.getMerchantId());
    		itemModel.setMerchantName(billModel.getMerchantName());
    		itemModel.setCode(billModel.getCode());
    		itemModel.setCurrency(billModel.getCurrency());
    		itemModel.setType("3");//1-销售在途 2-待确认 3-待开票 4-待回款
    		itemModel.setNum(num.intValue());
    		itemModel.setReceiveType(billModel.getSettlementType());
    		itemModel.setAmount(amount);
    		itemModel.setOccupationAmount(occupationAmount);
    		itemModel.setRate(exchangeRateMongo == null ? 1.0 : exchangeRateMongo.getAvgRate());
    		itemModel.setRateDate(exchangeRateMongo == null ? null : TimeUtils.parseFullTime(exchangeRateMongo.getEffectiveDate()+ " 00:00:00") );

    		noInvoiceList.add(itemModel);
    		totalNoInvoiceAmount = totalNoInvoiceAmount.add(occupationAmount);

		}
		result.put("noInvoiceList", noInvoiceList);
		result.put("totalNoInvoiceAmount", totalNoInvoiceAmount);
		return result;
	}
	/**
	 * 待回款账单
	 * @param configDTO
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> getNoReturnMap(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		Map<String,Object> result = new HashedMap<String, Object>();
		List<CutomerQuotaWarningItemModel> noReturnList = new ArrayList<CutomerQuotaWarningItemModel>();
		BigDecimal totalNoReturnAmount = BigDecimal.ZERO;
		/**
		 * 1、以“事业部+客户” 获取应收账单列表中的对应状态 为 “待核销”、“部分核销” 的应收账单 且 发票状态为“待签章”、 “已签章”的 “应收汇总金额” ，需要过滤掉 客户为内部公司的数据；
		 * 2、“待核销”、“部分核销” 的应收账单 且 发票状态为“待签章”、 “已签章”的账单创建日期需大于对应客户额度记录配置中的“期初开始日期”；
		 */
		List<Map<String,Object>> noReturnAmountList = receiveBillDao.getNoReturnAmount(paramMap);
		for(Map<String,Object> map : noReturnAmountList) {
			Long billId = (Long) map.get("bill_id");//应收订单id
			BigDecimal num = (BigDecimal) map.get("num");//数量
			BigDecimal amount = (BigDecimal) map.get("amount");//金额

			ReceiveBillModel billModel = receiveBillDao.searchById(billId);
			/**
    		 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
    		 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
    		 */
    		ExchangeRateMongo exchangeRateMongo = null;
    		BigDecimal occupationAmount = BigDecimal.ZERO;
    		if(!billModel.getCurrency().equals(configDTO.getCurrency())) {
    			//创建日期月底最后一天的平均汇率
    			String month = TimeUtils.formatMonth(billModel.getCreateDate()) ;
    			String lastDay = TimeUtils.getLastDayOfMonth(month);
    			exchangeRateMongo = getExchangeRate(billModel.getCurrency(),configDTO.getCurrency(),lastDay);
    			occupationAmount = amount.multiply(new BigDecimal(exchangeRateMongo.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);//占用金额
			} else {
				occupationAmount = amount;
			}
    		CutomerQuotaWarningItemModel itemModel = new CutomerQuotaWarningItemModel();
    		itemModel.setMerchantId(billModel.getMerchantId());
    		itemModel.setMerchantName(billModel.getMerchantName());
    		itemModel.setCode(billModel.getCode());
    		itemModel.setCurrency(billModel.getCurrency());
    		itemModel.setType("4");//1-销售在途 2-待确认 3-待开票 4-待回款
    		itemModel.setNum(num.intValue());
    		itemModel.setReceiveType(billModel.getSettlementType());
    		itemModel.setAmount(amount);
    		itemModel.setOccupationAmount(occupationAmount);
    		itemModel.setRate(exchangeRateMongo == null ? 1.0 : exchangeRateMongo.getAvgRate());
    		itemModel.setRateDate(exchangeRateMongo == null ? null : TimeUtils.parseFullTime(exchangeRateMongo.getEffectiveDate()+ " 00:00:00") );

    		noReturnList.add(itemModel);
    		totalNoReturnAmount = totalNoReturnAmount.add(occupationAmount);

		}
		result.put("noReturnList", noReturnList);
		result.put("totalNoReturnAmount", totalNoReturnAmount);
		return result;
	}

	/**
	 * 已上架金额
	 * @param configDTO
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private BigDecimal getShelfAmount(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		BigDecimal totalShelfAmount = BigDecimal.ZERO;
		/**
		 * 以“事业部+客户”取有效的销售订单累计上架数据，且上架日期需大于对应客户额度记录配置中的“期初开始日期；
		 * 已上架金额=上架好品数量*销售单价；
		 */
		List<Map<String,Object>> shelfNumtList = saleOrderDao.getShelfNum(paramMap);
		for(Map<String,Object> map : shelfNumtList) {
			Long orderId = (Long) map.get("id");//销售订单id
			Long goodsId = (Long) map.get("goods_id");//商品id
			String tallyingUnit = (String) map.get("tallying_unit");//理货单位
			BigDecimal num = (BigDecimal) map.get("num");//已上架数量
			Long saleItemId = (Long) map.get("sale_item_id");//销售明细id

			//箱托转化单位
			Integer saleNum = changeUnit(tallyingUnit,num.intValue(),goodsId);

			SaleOrderModel saleModel = saleOrderDao.searchById(orderId);
			SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(saleItemId);
			BigDecimal price = saleItemModel.getPrice();
			/**
			 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
			 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
			 */
			ExchangeRateMongo exchangeRateMongo = null;
			if(!saleModel.getCurrency().equals(configDTO.getCurrency())) {
				//创建日期月底最后一天的平均汇率
    			String month = TimeUtils.formatMonth(saleModel.getCreateDate()) ;
    			String lastDay = TimeUtils.getLastDayOfMonth(month);
				exchangeRateMongo = getExchangeRate(saleModel.getCurrency(),configDTO.getCurrency(),lastDay);
				price = price.multiply(new BigDecimal(exchangeRateMongo.getAvgRate()));
			}
			BigDecimal occupationAmount = price.multiply(new BigDecimal(saleNum)).setScale(2, BigDecimal.ROUND_HALF_UP);//占用金额
			totalShelfAmount = totalShelfAmount.add(occupationAmount);
		}

		return totalShelfAmount;
	}
	/**
	 * 已回款金额
	 * @param configDTO
	 * @return
	 * @throws Exception
	 */
	private BigDecimal getReturnAmount(CustomerQuotaConfigDTO configDTO,Map<String,Object> paramMap) throws Exception{
		BigDecimal totalReturnAmount = BigDecimal.ZERO;
		/**
		 * 已回款金额取以“事业部+客户” 获取应收账单列表中的对应状态 为 “已核销”的应收账单 的 “应收汇总金额” ，需要过滤掉 客户为内部公司的数据；
		 * 且已回款日期需大于对应额度记录配置中的“期初开始日期（已回款日期若有多个时取最早的一个）；
		 */
		List<Map<String,Object>> returnList = receiveBillDao.getReturnAmount(paramMap);
		for(Map<String,Object> map : returnList) {
			Long billId = (Long) map.get("bill_id");//应收订单id
			BigDecimal amount = (BigDecimal) map.get("amount");//金额

			ReceiveBillModel billModel = receiveBillDao.searchById(billId);
			/**
			 * 若公司核算币种与客户额度记录配置中的币种不一致时，需要进行汇率换算，
			 * 以客户额度记录配置中的币种做为本位币进行换算销售在途金额，转换的汇率取销售订单创建月份的平均汇率，若无则往前月份取；
			 */
			ExchangeRateMongo exchangeRateMongo = null;
			BigDecimal occupationAmount = BigDecimal.ZERO;
			if(!billModel.getCurrency().equals(configDTO.getCurrency())) {
				//创建日期月底最后一天的平均汇率
				String month = TimeUtils.formatMonth(billModel.getCreateDate()) ;
				String lastDay = TimeUtils.getLastDayOfMonth(month);
				exchangeRateMongo = getExchangeRate(billModel.getCurrency(),configDTO.getCurrency(),lastDay);
				occupationAmount = amount.multiply(new BigDecimal(exchangeRateMongo.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);//占用金额
			}else {
				occupationAmount = amount;
			}


			totalReturnAmount = totalReturnAmount.add(occupationAmount);

		}
		return totalReturnAmount;
	}

	/**理货单位转换
	 *箱/托盘转换为件
	 *boxToUnit 一箱多少件
	 *torrToUnit 一托多少件
	 *unit 理货单位 00-托盘 01-箱 02/空-件
	 * @return
	 */
    private Integer changeUnit(String unit,Integer num,Long goodId) throws RuntimeException{
    	Integer numInt=0;
    	if(num==null) return numInt;
    	numInt = num;

    	HashMap<String, Object> merchandiseMap = new HashMap<>();
		merchandiseMap.put("merchandiseId", goodId);
		merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
		MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseMap);
		if (DERP.ORDER_TALLYINGUNIT_01.equals(unit) ) {
			if(merchandiseInfo.getBoxToUnit() > 0) {
				numInt = num * merchandiseInfo.getBoxToUnit();
			}else {
				throw new RuntimeException("商品货号：" + merchandiseInfo.getGoodsNo() + " 箱转件数据为空，结束执行");
			}
		}

		if (DERP.ORDER_TALLYINGUNIT_00.equals(unit)) {
			if(merchandiseInfo.getTorrToUnit() > 0) {
				numInt = num * merchandiseInfo.getTorrToUnit();
			} else {
				throw new RuntimeException("商品货号：" + merchandiseInfo.getGoodsNo() + " 托转件数据为空，结束执行");
			}
		}

    	return numInt;
    }

    private ExchangeRateMongo getExchangeRate(String origCurrencyCode,String tgtCurrencyCode,String effectiveDate) throws Exception {

    	Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
		exchangeRateParams.put("origCurrencyCode", origCurrencyCode);
		exchangeRateParams.put("tgtCurrencyCode", tgtCurrencyCode);
		exchangeRateParams.put("effectiveDate", effectiveDate);
		exchangeRateParams.put("status", "1");
		ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
		if (exchangeRateMongo == null) {
			//当月月底最后一天的平均汇率 当月份小于2020-01月，结束查询
	    	String month = TimeUtils.getLastMonth(TimeUtils.parseDay(effectiveDate)) ;
	    	if(Timestamp.valueOf(month + "-01 00:00:00").getTime() < Timestamp.valueOf("2020-01-01 00:00:00").getTime()) {
	    		throw new RuntimeException("币种：" + origCurrencyCode +"兑 " +tgtCurrencyCode+" 平均汇率不存在 ,结束执行");
	    	}
			String lastDay = TimeUtils.getLastDayOfMonth(month);
			exchangeRateMongo = getExchangeRate(origCurrencyCode, tgtCurrencyCode, lastDay);
		}
		return exchangeRateMongo;
    }
}
