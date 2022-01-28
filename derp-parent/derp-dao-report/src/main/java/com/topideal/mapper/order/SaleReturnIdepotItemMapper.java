package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleReturnIdepotItemModel;

/**
 * 销售退货入库表体  mapper
 * @author lian_
 */
@MyBatisRepository
public interface SaleReturnIdepotItemMapper extends BaseMapper<SaleReturnIdepotItemModel> {
	/**
	 * 根据销售退货入库ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("list") List list)throws SQLException;

	Double getPrice(@Param("orderCode") String orderCode, @Param("goodsNo") String goodsNo) throws SQLException;

	Integer getYJVeriSaleRetunIdepotAccount(Map<String, Object> queryMap);
}
