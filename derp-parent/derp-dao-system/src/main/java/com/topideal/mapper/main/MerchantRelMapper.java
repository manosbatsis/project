package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 商家关联关系表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface MerchantRelMapper extends BaseMapper<MerchantRelModel> {

	/**
	 * 根据商家id删除
	 * @param id
	 * @return
	 */
	int delByMerchantId(@Param("id") Long id) throws SQLException;
	
	/**
	 * 获取集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<MerchantRelModel> getMerchantById(@Param("id") Long id) throws SQLException;

}

