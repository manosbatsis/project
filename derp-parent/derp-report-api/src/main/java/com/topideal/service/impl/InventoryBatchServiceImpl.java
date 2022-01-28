package com.topideal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.inventory.InventoryBatchDao;
import com.topideal.entity.dto.ResponseRoot;
import com.topideal.service.InventoryBatchService;

import net.sf.json.JSONObject;
/**
 * 库存查询接口
 * @author qiancheng.chen
 *
 */
@Service
public class InventoryBatchServiceImpl implements InventoryBatchService{

	@Autowired
	private InventoryBatchDao inventoryBatchDao;
	
	@Override
	public ResponseRoot<Map<String, Object>> queryInventoryBatch(JSONObject jsonData) throws Exception {
		String customerCode = (String) jsonData.get("customerCode");
		String customerName = (String) jsonData.get("customerName");
		String warehouseId = (String) jsonData.get("warehouseId");
		String goodsId = (String) jsonData.get("goodsId");
		String dataSource = (String) jsonData.get("dataSource");
		String pageNoStr = "1";
		String pageSizeStr = "100";
		if (jsonData.get("pageNo")!=null&&StringUtils.isNotBlank(jsonData.getString("pageNo"))) {
			pageNoStr=jsonData.getString("pageNo");
			if(!NumberUtils.isNumber(pageNoStr)) {
				pageNoStr = "1";
			}
		}
		if (jsonData.get("pageSize")!=null&&StringUtils.isNotBlank(jsonData.getString("pageSize"))) {
			pageSizeStr=jsonData.getString("pageSize");
			if(!NumberUtils.isNumber(pageSizeStr)) {
				pageSizeStr = "100";
			}
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("customerCode", customerCode);
		paramMap.put("customerName", customerName);
		paramMap.put("warehouseId", warehouseId);
		if(StringUtils.isNotBlank(goodsId) && !"*".equals(goodsId)) {			
			paramMap.put("goodsIdList",goodsId.split(","));
		}
		Integer pageSize = Integer.valueOf(pageSizeStr);
		if(pageSize <= 0) {
			pageSize = 100;
		}
		Integer pageNo = (Integer.valueOf(pageNoStr) - 1)*pageSize;
		if(pageNo < 0) {
			pageNo = 0;
		}
		paramMap.put("pageNo", pageNo);
		paramMap.put("pageSize", pageSize);
		//查询总数
		Integer totalCount = inventoryBatchDao.getRepotApiCount(paramMap);
		//分页查询数据
		List<Map<String, Object>> inventoryMapList = inventoryBatchDao.getRepotApiListPage(paramMap);
		if (totalCount == null || totalCount == 0) {			
			ResponseRoot<Map<String, Object>> responseRoot = new ResponseRoot<Map<String, Object>>();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(totalCount);
			responseRoot.setDataList(inventoryMapList);			
			return responseRoot;
		}		
		
		ResponseRoot<Map<String, Object>> responseRoot = new ResponseRoot<Map<String, Object>>();
		responseRoot.setTotalCount(totalCount);
		responseRoot.setDataList(inventoryMapList);
		
		return responseRoot;
	}

}
