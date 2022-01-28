package com.topideal.inventory.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.BuInventoryDao;
import com.topideal.dao.BuInventoryItemDao;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.dto.BuInventoryItemDTO;
import com.topideal.entity.vo.BuInventoryModel;
import com.topideal.inventory.service.BuInventoryService;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;

/**
 * 库存管理-批次库存明细-service实现类
 */
@Service
public class BuInventoryServiceImpl implements BuInventoryService {

    @Autowired
    private BuInventoryDao buInventoryDao;	//事业部库存表头
    @Autowired
    private BuInventoryItemDao buInventoryItemDao;//事业部库存表体
    //商品
  	@Autowired
  	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
  	/**
  	 * 获取事业部库存分页
  	 */
	@Override
	public BuInventoryDTO listBuInventory(User user,BuInventoryDTO dto) throws SQLException {
		
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		
		
		return buInventoryDao.getListBuInventoryByPage(dto);
	}
	/**
	 * 导出事业部库存
	 */
	@Override
	public List<Map<String, Object>>  exportBuInventory(User user,BuInventoryDTO dto) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}

		return buInventoryDao.exportBuInventory(dto);
	}
	/**
	 * 导出事业部库存表体
	 */
	@Override
	public List<Map<String, Object>> exportBuInventoryItem(User user,BuInventoryDTO dto) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
		return buInventoryDao.exportBuInventoryItem(dto);
	}
	@Override
	public List<Map<String, Object>> countByMonthAndMerchant(BuInventoryDTO model) throws SQLException {
		return buInventoryDao.countByMonthAndMerchant(model);
	}

	@Override
	public List<Map<String, Object>> countBuInventoryRate(BuInventoryDTO model) throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();
		//指定事业部的各仓库库存量
		List<Map<String, Object>> buMap = buInventoryDao.countBuInventoryByBuId(model);
		//各仓库的仓库库存量
		List<Map<String, Object>> depotMap = buInventoryDao.countBuInventoryByDepotId(model);
		for (Map<String, Object> bMap : buMap) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", bMap.get("depotName"));
			map.put("num", bMap.get("surplusNum"));
			BigDecimal num = (BigDecimal) bMap.get("surplusNum");
			for (Map<String, Object> dMap : depotMap) {
				if (dMap.get("depotId").equals(bMap.get("depotId"))) {
					map.put("totalNum", dMap.get("surplusNum"));
					BigDecimal total = (BigDecimal) dMap.get("surplusNum");
					BigDecimal rate = num.divide(total, 2, BigDecimal.ROUND_HALF_UP);
					map.put("rate", rate);
				}
			}
			result.add(map);
		}

		return result;
	}
	@Override
	public List<BuInventoryItemDTO> getBuInventoryItem(BuInventoryItemDTO dto) throws SQLException {
		// TODO Auto-generated method stub
		return buInventoryItemDao.getBuInventoryItem(dto);
	}



}
