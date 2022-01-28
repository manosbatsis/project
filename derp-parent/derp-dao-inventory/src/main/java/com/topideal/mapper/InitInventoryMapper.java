package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.entity.vo.InitInventoryModel;

/**
 * 期初库存 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InitInventoryMapper extends BaseMapper<InitInventoryModel> {

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
	Integer checkGoodsIsUse(@Param("id") Long id);
	
	/**
     * 分页查询返回DTO实体
     * @param model
     * @return
     */
	PageDataModel<InitInventoryDTO>  listDTOByPage(InitInventoryDTO model)throws SQLException;
	
	/**
	  * 1 获取事业期初  
	  * 按商家, 仓库, 事业部,调增调减类型, 商品 ,好坏品 ,效期
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	List<Map<String, Object>> getBuInitInventory(Map<String, Object> map) throws SQLException;
	
}

