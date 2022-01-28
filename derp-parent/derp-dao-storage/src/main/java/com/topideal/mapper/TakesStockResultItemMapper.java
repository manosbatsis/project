package com.topideal.mapper;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.TakesStockResultItemModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 盘点结果详情表
 * @author lian_
 *
 */
@MyBatisRepository
public interface TakesStockResultItemMapper extends BaseMapper<TakesStockResultItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);

	List<TakesStockResultItemDTO> listDto(TakesStockResultItemDTO dto) throws SQLException;
	
	Long countNoExistBu(@Param("id") Long id) throws SQLException;

}

