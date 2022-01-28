package com.topideal.mapper;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.TakesStockItemModel;

/**
 * 盘点详情表
 * @author lian_
 *
 */
@MyBatisRepository
public interface TakesStockItemMapper extends BaseMapper<TakesStockItemModel> {

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
	Integer checkGoodsIsUse(@Param("id") Long id);
    
}

