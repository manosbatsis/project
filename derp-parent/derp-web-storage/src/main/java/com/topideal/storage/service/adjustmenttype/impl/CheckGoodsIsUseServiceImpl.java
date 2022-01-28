package com.topideal.storage.service.adjustmenttype.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.AdjustmentInventoryItemDao;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.dao.TakesStockItemDao;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.storage.service.adjustmenttype.CheckGoodsIsUseService;

/**
 * 校验商品是否被使用
 * @author zhanghx
 */
@Service
public class CheckGoodsIsUseServiceImpl implements CheckGoodsIsUseService {

	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 盘点指令
	@Autowired
	private TakesStockItemDao takesStockItemDao;
	// 盘点结果
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;
	// 类型调整
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;
	// 库存调整
	@Autowired
	private AdjustmentInventoryItemDao adjustmentInventoryItemDao;

	/**
	 * 校验商品是否被使用
	 * @return
	 */
	@Override
	public boolean checkGoodsIsUse(List<Long> ids) throws SQLException{
		int flag = 0;
		Long id = null;
		for (Long goodsId : ids) {
			// 盘点指令
			Integer takesStockItemCount = takesStockItemDao.checkGoodsIsUse(goodsId);
			if (takesStockItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 盘点结果
			Integer takesStockResultItemCount = takesStockResultItemDao.checkGoodsIsUse(goodsId);
			if (takesStockResultItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 类型调整
			Integer adjustmentTypeItemCount = adjustmentTypeItemDao.checkGoodsIsUse(goodsId);
			if (adjustmentTypeItemCount > 0) {
				flag = 1;
				id = goodsId;
				break;
			}
			// 库存调整
			Integer adjustmentInventoryItemCount = adjustmentInventoryItemDao.checkGoodsIsUse(goodsId);
			if (adjustmentInventoryItemCount > 0) {
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
			if (mogo != null) {
				goodsNo = mogo.getGoodsNo();
			}
			throw new RuntimeException(goodsNo + "商品已被使用，不能删除");
		}
		return true;
	}

}
