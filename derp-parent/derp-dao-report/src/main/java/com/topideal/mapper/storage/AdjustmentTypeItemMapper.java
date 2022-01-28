package com.topideal.mapper.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.storage.AdjustmentTypeItemModel;

/**
 * 类型调整详情表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface AdjustmentTypeItemMapper extends BaseMapper<AdjustmentTypeItemModel> {

	/**
	 * 根据类型调整ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("list") List list, @Param("type") String type)throws SQLException;

}
