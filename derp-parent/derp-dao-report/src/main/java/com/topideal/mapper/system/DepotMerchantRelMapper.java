package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.DepotMerchantRelModel;

/**
 * 仓库商家关联表
 * @author zhanghx
 */
@MyBatisRepository
public interface DepotMerchantRelMapper extends BaseMapper<DepotMerchantRelModel> {
	/**
	 * 根据商家id查询仓库信息
	 * @param id
	 * @return
	 */
	List<DepotInfoModel> getDepotByMerchantId(@Param("merchantId") Long id) throws SQLException;

	Integer deleteByMap(Map<String, Object> delMap);
}

