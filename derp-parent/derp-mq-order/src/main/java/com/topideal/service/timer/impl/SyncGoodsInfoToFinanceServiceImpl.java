package com.topideal.service.timer.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.SyncGoodsInfoToFinanceService;

import net.sf.json.JSONObject;

@Service
public class SyncGoodsInfoToFinanceServiceImpl implements SyncGoodsInfoToFinanceService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncGoodsInfoToFinanceServiceImpl.class);
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ

	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201120008,model=DERP_LOG_POINT.POINT_13201120008_Label)
	public void syncGoodsInfoToFinance(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		String dateStr = (String) jsonData.get("date");// 日期		
        if (StringUtils.isBlank(dateStr)) {
        	dateStr = TimeUtils.formatDay(new Date());        	
        }
        Date date = TimeUtils.parseDay(dateStr);
		
        Map<Long, MerchantInfoMongo> merchantInfoMap = new HashMap<Long, MerchantInfoMongo>();
		List<Long> merchantIds = new ArrayList<Long>();
		// 查询最近创建的销售订单，条件：公司=卓烨、健太、润佰，事业部=采销事业部（编码：I020000），新增或修改时间为最近30天内
		Map<String,Object> merchantParams = new HashMap<String,Object>();
		merchantParams.put("code", "ERP31194100049");//健太
		MerchantInfoMongo merchantMongo = merchantInfoMongoDao.findOne(merchantParams);
		merchantIds.add(merchantMongo.getMerchantId());
		merchantInfoMap.put(merchantMongo.getMerchantId(), merchantMongo);
		
		merchantParams.clear();
		merchantParams.put("code", "ERP31194200007");//润佰
		merchantMongo = merchantInfoMongoDao.findOne(merchantParams);
		merchantIds.add(merchantMongo.getMerchantId());
		merchantInfoMap.put(merchantMongo.getMerchantId(), merchantMongo);
		
		merchantParams.clear();
		merchantParams.put("code", "ERP26143500022");//卓烨
		merchantMongo = merchantInfoMongoDao.findOne(merchantParams);
		merchantIds.add(merchantMongo.getMerchantId());
		merchantInfoMap.put(merchantMongo.getMerchantId(), merchantMongo);
		
		Map<String,Object> buParams = new HashMap<String,Object>();
		buParams.put("code", "I020000");//采销事业部
		BusinessUnitMongo businessMongo = businessUnitMongoDao.findOne(buParams);		
		
		SaleOrderDTO  saleOrderDTO = new SaleOrderDTO();
		saleOrderDTO.setMerchantIds(merchantIds);
		saleOrderDTO.setBuId(businessMongo.getBusinessUnitId());
		saleOrderDTO.setOrderStartDate(TimeUtils.formatDay(TimeUtils.addDay(date, -30)));
		saleOrderDTO.setOrderEndDate(dateStr);
		//若查询到订单信息，则执行数据同步
		List<SaleOrderDTO> list = saleOrderDao.queryDTOList(saleOrderDTO);
		for(SaleOrderDTO dto : list) {			
			MerchantInfoMongo merchantInfo = merchantInfoMap.get(dto.getMerchantId());
			
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(dto.getId());
			List<SaleOrderItemModel> itemList = saleOrderItemDao.list(saleOrderItemModel);
			for(SaleOrderItemModel itemModel : itemList) {
				JSONObject jsonObject = new JSONObject();// 推送内容
				jsonObject.put("merchantId", dto.getMerchantId());
				jsonObject.put("customerId", dto.getCustomerId());
				jsonObject.put("goodsId", itemModel.getGoodsId());
				jsonObject.put("goodsNo", itemModel.getGoodsNo());
				rocketMQProducer.send(jsonObject.toString(), MQErpEnum.SEND_FINANCE_GOODS.getTopic(),MQErpEnum.SEND_FINANCE_GOODS.getTags());

			}
		}		
	}	

}
