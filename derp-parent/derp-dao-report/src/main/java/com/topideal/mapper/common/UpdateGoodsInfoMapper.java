package com.topideal.mapper.common;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;

/**
 * 更新业务库的商品信息
 * @author lian_
 *
 */
@MyBatisRepository
public interface UpdateGoodsInfoMapper {
	void updateGoodsInfo(@Param("updateSql") String updateSql);
}

