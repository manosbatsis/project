package com.topideal.dao.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.dao.common.UpdateGoodsInfoDao;
import com.topideal.mapper.common.UpdateGoodsInfoMapper;

/**
 * 更新业务库的商品信息
 * @author lchenxing
 */
@Repository
public class UpdateGoodsInfoDaoImpl implements UpdateGoodsInfoDao {

    @Autowired
    private UpdateGoodsInfoMapper mapper;

	@Override
	public void updateGoodsInfo(String updateSql) {
		mapper.updateGoodsInfo(updateSql);
	}
}
