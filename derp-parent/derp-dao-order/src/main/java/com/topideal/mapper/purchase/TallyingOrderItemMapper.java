package com.topideal.mapper.purchase;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 理货单商品详情
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface TallyingOrderItemMapper extends BaseMapper<TallyingOrderItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	
}

