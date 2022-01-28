package com.topideal.mapper.sale;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.SaleReturnOrderRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售订单退货商品信息 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnOrderRelMapper extends BaseMapper<SaleReturnOrderRelModel> {
	/**
	 * 通过销售订单id获取商品已经退货的数据（过滤已删除的销退订单）
	 * @param saleId
	 * @param merchandiseId
	 * @return
	 */
	List<SaleReturnOrderRelModel> listSaleReturnOrderRel(@Param("saleId")Long saleId, @Param("goodsId")Long merchandiseId);



}

