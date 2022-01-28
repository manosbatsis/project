package com.topideal.order.service.purchase.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.purchase.PurchaseWarehouseBatchDao;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.order.service.purchase.PurchaseWarehouseBatchService;

/**
 * 采购入库商品批次
 * @author zhanghx
 */
@Service
public class PurchaseWarehouseBatchServiceImpl implements PurchaseWarehouseBatchService {

	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
	
	/**
	 * 根据采购入库单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseBatchModel> getGoodsAndBatch(Long id) throws SQLException {
		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(id);
		for (PurchaseWarehouseBatchModel batchModel : batchList) {
			if(batchModel.getProductionDate()!=null){
				batchModel.setProductionDateStr(sft.format(batchModel.getProductionDate()));
			}
			if(batchModel.getOverdueDate()!=null){
				batchModel.setOverdueDateStr(sft.format(batchModel.getOverdueDate()));
			}
		}
		return batchList;
	}

}
