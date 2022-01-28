package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.MerchantRelModel;

/**
 * 商家关联关系表 dao
 * @author lian_
 */
public interface MerchantRelDao extends BaseDao<MerchantRelModel> {

	/**
	 * 根据商家id删除
	 * @param id
	 * @return
	 */
	int delByMerchantId(Long id) throws SQLException;
	
	/**
	 * 获取集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<MerchantRelModel> getMerchantById(Long id) throws SQLException;

}

