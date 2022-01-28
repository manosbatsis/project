package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.entity.vo.InitInventoryModel;

/**
 * 期初库存 dao
 * @author lian_
 *
 */
public interface InitInventoryDao extends BaseDao<InitInventoryModel>{
		
	/**
	 * 统计库存最新期初
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<InitInventoryModel> countInitInventory(InitInventoryModel model)throws SQLException;

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);
	
	/**
     * 分页查询返回DTO实体
     * @param model
     * @return
     */
	InitInventoryDTO  searchDTOByPage(InitInventoryDTO model)throws SQLException;
	

	/**
	  * 1 获取事业期初  
	  * 按商家, 仓库, 事业部,调增调减类型, 商品 ,好坏品 ,效期
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	List<Map<String, Object>> getBuInitInventory(Map<String, Object> map) throws SQLException;

}

