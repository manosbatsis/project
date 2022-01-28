package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InitInventoryDao;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.inventory.service.CheckGoodsIsUseService;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * 校验商品是否被使用
 * @author zhanghx
 */
@Service
public class CheckGoodsIsUseServiceImpl implements CheckGoodsIsUseService {

	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 期初库存
	@Autowired
	private InitInventoryDao initInventoryDao;
	// 批次库存
	@Autowired
	private InventoryBatchDao inventoryBatchDao;

	/**
	 * 校验商品是否被使用
	 * 
	 * @return
	 */
	@Override
	public boolean checkGoodsIsUse(List<Long> ids) throws Exception {
		int flag = 0;
		Long id = null;
		for (Long goodsId : ids) {
			// 期初库存
			Integer initInventoryCount = initInventoryDao.checkGoodsIsUse(goodsId);
			if (initInventoryCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 批次库存
			Integer inventoryBatchCount = inventoryBatchDao.checkGoodsIsUse(goodsId);
			if (inventoryBatchCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
		}
		if (flag == 1) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", id);
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
			String goodsNo = "";
			if(mogo!=null){
				goodsNo = mogo.getGoodsNo();
			}
			throw new RuntimeException(goodsNo+"商品已被使用，不能删除");
		}
		return true;
	}
	
	/**
	 * 校验仓库是否已使用
	 */
	@Override
	public boolean checkDepotIsUse(List<Long> ids) throws Exception {
		
		for (Long depotId : ids) {
			Integer checkDepotIsUseNum = inventoryBatchDao.checkDepotIsUse(depotId);
			if (checkDepotIsUseNum > 0) {
				throw new RuntimeException(depotId+"仓库在批次库存已经被使用，不能删除");
			}
		}	
		return true;
	}

}
