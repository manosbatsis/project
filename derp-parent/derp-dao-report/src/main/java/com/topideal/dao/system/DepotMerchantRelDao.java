package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.DepotMerchantRelModel;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * 仓库商家关联表 dao
 * @author zhanghx
 */
public interface DepotMerchantRelDao extends BaseDao<DepotMerchantRelModel> {
	/**
	 * 根据商家id查询仓库信息
	 * @param id
	 * @return
	 */
	List<DepotInfoModel> getDepotByMerchantId(@Param("merchantId") Long merchantId) throws SQLException;

	/**
	 * 根据仓库ID和商家ID删除信息
	 * @param delMap
	 * @return
	 */
	Integer deleteByMap(Map<String, Object> delMap);
}

