package com.topideal.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.inventory.MonthlyAccountItemDao;
import com.topideal.service.DelMonthlyAccountItemService;

import net.sf.json.JSONObject;

/**
 * 删除月结明细数据
 */
@Service
public class DelMonthlyAccountItemServiceImpl implements DelMonthlyAccountItemService {

	private static final Logger logger = LoggerFactory.getLogger(DelMonthlyAccountItemServiceImpl.class);

	@Autowired
	private MonthlyAccountItemDao  monthlyAccountItemDao;
	
	@Override
	public void delMonthlyAccountItemData(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonObj = JSONObject.fromObject(json);
		if(jsonObj == null || !jsonObj.containsKey("monthlyAccountId")){
			logger.error("json字符串为空或json字符串没有包含key:\"monthlyAccountId\":"+json);
			return;
		}
		if(StringUtils.isBlank(jsonObj.getString("monthlyAccountId"))){
			logger.error("monthlyAccountId的数据为空："+json);
			return;
		}
		Long monthlyAccountId = Long.valueOf(jsonObj.getString("monthlyAccountId"));
		monthlyAccountItemDao.delItemByMonthlyAccountId(monthlyAccountId);
	}
	

}
