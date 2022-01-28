package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.CrawlerBillLogService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.ParseDaterangepicker;

import net.sf.json.JSONObject;

/**
 * 爬虫日志
 */
@Service
public class CrawlerBillLogServiceImpl implements CrawlerBillLogService {
	//爬虫日志
	@Autowired
	JSONMongoDao jsonMongoDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	
	@Override
	public PageMongo searchLog(PageMongo pageMongo, String platformType, String saleOutDepotCode, String status,
			String billCode, String goodsNo, String errorMsg, String createDateStr,Long depotId,Long merchantId,String poNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(platformType)) {
			params.put("platformType", platformType);
		}
		if (StringUtils.isNotBlank(saleOutDepotCode)) {
			params.put("saleOutDepotCode", saleOutDepotCode);
		}
		if (StringUtils.isNotBlank(status)) {
			params.put("status", Integer.parseInt(status));
		}
		if (StringUtils.isNotBlank(billCode)) {
			params.put("billCode", billCode);
		}
		if (StringUtils.isNotBlank(goodsNo)) {
			params.put("goodsNo", goodsNo);
		}
		if (StringUtils.isNotBlank(errorMsg)) {
			params.put("errorMsg", errorMsg);
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			params.put("createDate", ParseDaterangepicker.parse(createDateStr));
		}
		if (merchantId != null) {
			params.put("merchantId", merchantId);
		}
		if (depotId != null) {
			params.put("depotId", depotId);
		}
		if (poNo != null) {
			params.put("poNo", poNo);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "createDate");
		pageMongo = jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.CRAWLER_BILL_LOG.getCollectionName());
		return pageMongo;
	}

	@Override
	public boolean sendMQ(String ids) throws Exception {
		/*if (StringUtils.isNotBlank(ids)) {
			JSONObject josnObject = new JSONObject();
			josnObject.put("ids", ids);
			josnObject.put("desc", "勾选触发生成出库清单");
			rocketMQProducer.send(josnObject.toString(),MQOrderEnum.TIMER_SALE_OUT_DEPOT.getTopic(),MQOrderEnum.TIMER_SALE_OUT_DEPOT.getTags());
			return true;
		}*/
		return false;
	}
	
	@Override
	public Integer count(String platformType, String saleOutDepotCode, String status, String billCode, String goodsNo,
			String errorMsg, String createDateStr,Long depotId,Long merchantId,String poNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(platformType)) {
			params.put("platformType", platformType);
		}
		if (StringUtils.isNotBlank(saleOutDepotCode)) {
			params.put("saleOutDepotCode", saleOutDepotCode);
		}
		if (StringUtils.isNotBlank(status)) {
			params.put("status", Integer.parseInt(status));
		}
		if (StringUtils.isNotBlank(billCode)) {
			params.put("billCode", billCode);
		}
		if (StringUtils.isNotBlank(goodsNo)) {
			params.put("goodsNo", goodsNo);
		}
		if (StringUtils.isNotBlank(errorMsg)) {
			params.put("errorMsg", errorMsg);
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			params.put("createDate", ParseDaterangepicker.parse(createDateStr));
		}
		if (merchantId != null) {
			params.put("merchantId", merchantId);
		}
		if (depotId != null) {
			params.put("depotId", depotId);
		}
		if (poNo != null) {
			params.put("poNo", poNo);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "createDate");
		List list = jsonMongoDao.findAll(params, sort, CollectionEnum.CRAWLER_BILL_LOG.getCollectionName());
		return list.size();
	}
	
	@Override
	public List getExportList(String platformType, String saleOutDepotCode, String status,
			String billCode, String goodsNo, String errorMsg, String createDateStr, Integer count,Long depotId,Long merchantId,String poNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(platformType)) {
			params.put("platformType", platformType);
		}
		if (StringUtils.isNotBlank(saleOutDepotCode)) {
			params.put("saleOutDepotCode", saleOutDepotCode);
		}
		if (StringUtils.isNotBlank(status)) {
			params.put("status", Integer.parseInt(status));
		}
		if (StringUtils.isNotBlank(billCode)) {
			params.put("billCode", billCode);
		}
		if (StringUtils.isNotBlank(goodsNo)) {
			params.put("goodsNo", goodsNo);
		}
		if (StringUtils.isNotBlank(errorMsg)) {
			params.put("errorMsg", errorMsg);
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			params.put("createDate", ParseDaterangepicker.parse(createDateStr));
		}
		if (merchantId != null) {
			params.put("merchantId", merchantId);
		}
		if (depotId != null) {
			params.put("depotId", depotId);
		}
		if(poNo != null) {
			params.put("poNo", poNo);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "createDate");
		
		List<JSONObject> crawlerBillList = jsonMongoDao.findAll(params, sort, CollectionEnum.CRAWLER_BILL_LOG.getCollectionName());
		for (JSONObject jsonObject : crawlerBillList) {
			String stateLabel = jsonObject.getString("status");
			stateLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_statusList, stateLabel) ;
			
			jsonObject.put("status", stateLabel) ;
			
			String platformTypeLabel = jsonObject.getString("platformType");
			platformTypeLabel = DERP_LOG.getLabelByKey(DERP_LOG.logCrawlerPlatformtypeList, platformTypeLabel) ;
			jsonObject.put("platformType", platformTypeLabel) ;
		}
		
		return crawlerBillList;
	}

}
