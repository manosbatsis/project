package com.topideal.service.dev.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.BuInventoryDao;
import com.topideal.entity.vo.BuInventoryModel;
import com.topideal.mongo.dao.BuInventoryMongoDao;
import com.topideal.service.dev.InventorySysDataService;
import com.topideal.service.impl.BuInventoryServiceImpl;

import net.sf.json.JSONObject;

/**
 * 库存同步数到mongdb
 * 杨创  2020-08-25
 */
@Service
public class InventorySysDataServiceImpl implements InventorySysDataService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BuInventoryServiceImpl.class);
	//事务部库存
	@Autowired
	private BuInventoryDao buInventoryDao;
	@Autowired
	private BuInventoryMongoDao buInventoryMongoDao;// 事业部库存mongo	

	/**
	 * 同步数据
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean synsDataToMongo(String json, String keys,String topics,String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		if (jsonData.get("tableName")==null||StringUtils.isBlank(jsonData.getString("tableName"))) {
			throw new RuntimeException("要同步的表名不能为空");
		}	
		String tableName = jsonData.getString("tableName");		
		if ("i_bu_inventory".equals(tableName)) {
			savebuInventory(jsonData);
		}
		
		return true;
	}
	/**
	 * 同步事业部库存到报表库
	 * @param jsonData
	 * @throws SQLException 
	 */
	private void savebuInventory(JSONObject jsonData) throws SQLException {
		String month=(String) jsonData.get("month");
		String merchantId=(String) jsonData.get("merchantId");
		
		BuInventoryModel buInventoryModel= new BuInventoryModel();		
		if (StringUtils.isNotBlank(merchantId))buInventoryModel.setMerchantId(Long.valueOf(merchantId));
		if (StringUtils.isNotBlank(month))buInventoryModel.setMonth(month);
		buInventoryModel.setBegin(0);
		buInventoryModel.setPageSize(10000);
		while (true) {
			BuInventoryModel searchByPage = buInventoryDao.searchByPage(buInventoryModel);
			buInventoryModel.setBegin(buInventoryModel.getBegin()+buInventoryModel.getPageSize());
			List<BuInventoryModel> list = searchByPage.getList();
			if (list==null||list.size()==0) {
				break;
			}
			for (BuInventoryModel model : list) {
				//存储到MONGODB
				JSONObject jsonObject=JSONObject.fromObject(model);
				jsonObject.put("buInventoryId",model.getId());
				jsonObject.remove("id");
				jsonObject.remove("createDate");
				jsonObject.remove("modifyDate");
				buInventoryMongoDao.insertJson(jsonObject.toString());
			}			
			list=null;
		}
		
		

	}

	
	
}









