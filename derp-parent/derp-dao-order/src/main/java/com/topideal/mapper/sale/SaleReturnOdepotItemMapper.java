package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货出库表体
 * @author lian_
 */
@MyBatisRepository
public interface SaleReturnOdepotItemMapper extends BaseMapper<SaleReturnOdepotItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	 /**
     * 列表查询
     * @param model
     * @return
     * @throws SQLException
     */
    List<SaleReturnOdepotItemDTO> listSaleReturnOdepotItemDTO(SaleReturnOdepotItemDTO dto)throws SQLException;
    
	/**
	 * 根据出库单id和调出商品id， 根据失效时间降序查询调出商品信息
	 */
	List<SaleReturnOdepotItemModel> getSaleReturnOdepotItemList(@Param("sreturnOdepotId")Long sreturnOdepotId, @Param("outGoodsId")Long outGoodsId);

}

