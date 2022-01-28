package com.topideal.dao.purchase;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;

/**
 * 理货单商品详情
 * @author lianchenxing
 *
 */
public interface TallyingOrderItemDao extends BaseDao<TallyingOrderItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);

}

