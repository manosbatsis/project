package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleReturnIdepotModel;

/**
 * 销售退货入库 mapper
 * @author lian_
 */
@MyBatisRepository
public interface SaleReturnIdepotMapper extends BaseMapper<SaleReturnIdepotModel> {

	/**
	 * 报表api 根据 商家开始时间结束时间分页查询销售退货入库
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize)throws SQLException;
	/**
	 * 报表api 根据 商家开始时间结束时间查询销售退货入库总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
