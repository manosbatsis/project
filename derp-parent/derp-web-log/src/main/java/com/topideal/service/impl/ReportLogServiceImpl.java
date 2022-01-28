package com.topideal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.ReportLogService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.ParseDaterangepicker;

import net.sf.json.JSONObject;

/**
 * 报表service
 * @author zhanghx
 */
@Service
public class ReportLogServiceImpl implements ReportLogService {

	// 报表dao
	@Autowired
	JSONMongoDao jsonMongoDao;
	
	/**
	 * 分页
	 * @param pageMongo
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 */
	@Override
	public PageMongo searchLog(PageMongo pageMongo, String state, String point, String endDateStr) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		pageMongo = jsonMongoDao.findAll(params, sort, pageMongo,CollectionEnum.REPORT_LOG.getCollectionName());
		return pageMongo;
	}

	@Override
	public Long count(String state, String point) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}

		return  jsonMongoDao.count(params, CollectionEnum.REPORT_LOG.getCollectionName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List searchListLog( String state, String point)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		
		List<JSONObject> list = jsonMongoDao.findAll(params, sort, CollectionEnum.REPORT_LOG.getCollectionName());
		
		for (JSONObject jsonObject : list) {
			String stateLabel = jsonObject.getString("state");
			stateLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_statusList, stateLabel) ;
			
			jsonObject.put("state", stateLabel) ;
		}
		
		return list;
	}

}
