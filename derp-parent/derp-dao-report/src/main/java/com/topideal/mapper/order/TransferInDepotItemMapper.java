package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.TransferInDepotItemModel;

/**
 * 调拨入库表体 mapper
 * @author lian_
 */
@MyBatisRepository
public interface TransferInDepotItemMapper extends BaseMapper<TransferInDepotItemModel> {
	/**
	 * 根据调拨ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("list") List list)throws SQLException;

	/**
	 * 唯品ByPO获取调拨入库量
	 * @param queryMap
	 * @return
	 */
	Integer getVIPInDepotAccount(Map<String, Object> queryMap);

	/**
	 * 唯品ByPO获取调拨入库明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVIPInDepotDetails(Map<String, Object> queryMap);

}
