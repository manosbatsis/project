package com.topideal.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.AdjustmentTypeItemModel;

/**
 * 类型调整详情表
 * @author lian_
 *
 */
@MyBatisRepository
public interface AdjustmentTypeItemMapper extends BaseMapper<AdjustmentTypeItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	
	Long countNoExistBu(@Param("id") Long id) throws SQLException;
}

