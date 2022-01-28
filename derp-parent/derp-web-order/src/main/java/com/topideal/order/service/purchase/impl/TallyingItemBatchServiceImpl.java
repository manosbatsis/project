package com.topideal.order.service.purchase.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.purchase.TallyingItemBatchDao;
import com.topideal.order.service.purchase.TallyingItemBatchService;

/**
 * 理货单批次 serviceimpl
 * @author zhanghx
 */
@Service
public class TallyingItemBatchServiceImpl implements TallyingItemBatchService {

	@Autowired
	private TallyingItemBatchDao tallyingItemBatchDao;
	
	/**
	 * 根据理货单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<TallyingItemBatchDTO> getGoodsAndBatch(Long id) throws SQLException {
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		List<TallyingItemBatchDTO> list = tallyingItemBatchDao.getGoodsAndBatch(id);
		if(list!=null && list.size()>0){
			for (TallyingItemBatchDTO batchDTO : list) {
				if (batchDTO.getProductionDate() != null) {
					String productionDate = sft.format(batchDTO.getProductionDate());
					batchDTO.setProductionDateStr(productionDate);
				}
				if (batchDTO.getOverdueDate() != null) {
					String overdueDate = sft.format(batchDTO.getOverdueDate());
					batchDTO.setOverdueDateStr(overdueDate);
				}
			}
		}
		return list;
	}

}
