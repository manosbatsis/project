package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotItemModel;

/**
 * 销售退货出库表体 dao
 * @author lian_
 *
 */
public interface SaleReturnOdepotItemDao extends BaseDao<SaleReturnOdepotItemModel>{

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
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
    List<SaleReturnOdepotItemModel> getSaleReturnOdepotItemList(Long sreturnOdepotId, Long outGoodsId);
}

