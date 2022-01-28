package com.topideal.dao;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import com.topideal.entity.vo.TakesStockResultItemModel;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 盘点结果详情表
 * @author lian_
 *
 */
public interface TakesStockResultItemDao extends BaseDao<TakesStockResultItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);

	List<TakesStockResultItemDTO> listDto(TakesStockResultItemDTO dto) throws SQLException;

	Long countNoExistBu(@Param("id") Long id) throws SQLException;
}

