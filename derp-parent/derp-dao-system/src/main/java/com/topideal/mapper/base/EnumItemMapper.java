package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.base.EnumItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 枚举列表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface EnumItemMapper extends BaseMapper<EnumItemModel> {

	/**
	 * 根据枚举类型id获取枚举值列表
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<EnumItemModel> getListById(@Param("id") Long id) throws SQLException;

}

