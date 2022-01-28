package com.topideal.dao.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;

/**
 * 理货单商品批次详情 dao
 * @author lianchenxing
 *
 */
public interface TallyingItemBatchDao extends BaseDao<TallyingItemBatchModel>{
		
	/**
	 * 根据理货单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<TallyingItemBatchDTO> getGoodsAndBatch(Long id) throws SQLException;

}

