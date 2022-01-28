package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

/**
 * 仓库商家关联表 dao
 * @author lian_
 *
 */
public interface DepotMerchantRelDao extends BaseDao<DepotMerchantRelModel>{
		
	/**
	 * 根据仓库id查询商家列表
	 * @param id
	 * @return
	 */
	List<DepotMerchantRelDTO> getMerchantByDepotId(Long id) throws SQLException;
	
	/**
	 * 根据仓库id删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delByDepotId(Long id) throws SQLException;
	/**
	 * 根据仓库id和商家id查询该仓库信息
	 * @param id
	 * @return
	 */
	DepotMerchantRelDTO  getByDepotIdAndMerchantId(Long depotId,Long merchantId) throws SQLException;

	/**
	 * 根据商家id查询商家列表
	 * @param id
	 * @return
	 */
	List<DepotMerchantRelDTO> getListByMerchantId(Long id);

	/**
	 * 根据仓库id删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delByMerchantId(Long id);
}

