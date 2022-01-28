package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.vo.order.SaleOutDepotModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 销售出库
 * @author zhanghx
 */
@MyBatisRepository
public interface SaleOutDepotMapper extends BaseMapper<SaleOutDepotModel> {

	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap);
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap);
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes);
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap);

	/**
	 * @Description 统计指定业务模式下的销售出库单的所有客户的销售数量
	 */
	public List<BrandSaleDTO> countSaleOutOrderByMerchantIdAndSaleType(Map<String, Object> paramMap);

	/**
	 * @Description 统计指定业务模式下的销售出库单的所有客户销售top 10 的品牌
	 */
	public List<BrandSaleDTO> getSaleOutOrderTop10ByBrand(Map<String, Object> paramMap);
}

