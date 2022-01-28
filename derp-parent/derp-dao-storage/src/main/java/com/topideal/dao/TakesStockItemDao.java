package com.topideal.dao;

import com.topideal.entity.vo.TakesStockItemModel;

/**
 * 盘点详情表
 * @author lian_
 *
 */
public interface TakesStockItemDao extends BaseDao<TakesStockItemModel>{
	
	/**
	 * 根据盘点申请id删除详情
	 * */
    public void delByTakesStockId(Long takesStockId);

    /**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);

}

