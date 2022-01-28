package com.topideal.mapper.inventory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.inventory.InitInventoryModel;
import com.topideal.mapper.BaseMapper;

/**
 * 期初库存 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InitInventoryMapper extends BaseMapper<InitInventoryModel> {
    

	
	/**
	 * 商家、仓库、货号的期初库存量
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getGoodsNoInitSum(Map<String, Object> paramMap) throws SQLException;

}

