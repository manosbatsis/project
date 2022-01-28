package com.topideal.dao;

import java.sql.SQLException;

import com.topideal.entity.vo.AdjustmentTypeItemModel;

/**
 * 类型调整详情表
 * @author lian_
 *
 */
public interface AdjustmentTypeItemDao extends BaseDao<AdjustmentTypeItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);
	Long countNoExistBu(Long id) throws SQLException;
}

