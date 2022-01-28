package com.topideal.mapper.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.mapper.BaseMapper;

/**
 * 理货单商品批次详情 mapper
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface TallyingItemBatchMapper extends BaseMapper<TallyingItemBatchModel> {

	/**
	 * 根据理货单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<TallyingItemBatchDTO> getGoodsAndBatch(@Param("id") Long id) throws SQLException;

}

