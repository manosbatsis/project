package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 仓库商家关联表
 * @author lian_
 *
 */
@MyBatisRepository
public interface DepotMerchantRelMapper extends BaseMapper<DepotMerchantRelModel> {

	/**
	 * 根据仓库id查询商家列表
	 * @param id
	 * @return
	 */
	List<DepotMerchantRelDTO> getMerchantByDepotId(@Param("id") Long id) throws SQLException;
	
	/**
	 * 根据仓库id删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delByDepotId(@Param("id") Long id) throws SQLException;
	/**
	 * 根据仓库id和商家id查询该仓库信息
	 * @param id
	 * @return
	 */
	DepotMerchantRelDTO  getByDepotIdAndMerchantId(@Param("depotId")Long depotId,@Param("merchantId")Long merchantId) throws SQLException;

	/**
	 * 根据商家id查询商家列表
	 * @param id
	 * @return
	 */
	List<DepotMerchantRelDTO> getListByMerchantId(@Param("merchantId") Long id);

	/**
	 * 根据商家id删除
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delByMerchantId(@Param("id")Long id);
}

