package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnIdepotItemDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.entity.vo.sale.SaleReturnIdepotItemModel;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;

/**
 * 销售退货入库表体 dao
 * @author lian_
 *
 */
public interface SaleReturnIdepotItemDao extends BaseDao<SaleReturnIdepotItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	
    
    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<SaleReturnIdepotItemDTO> listDTO(SaleReturnIdepotItemDTO dto)throws SQLException;
	/**
	 * 根据销售退货入库订单id集合查询销售退货入库订单表体
	 * @param sreturnIdepotIds
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnIdepotItemModel> listBySreturnIdepotIds(List<Long> sreturnIdepotIds) throws SQLException;

	/**
	 *  根据销售退入单单号、po号、商品货号查询采购SD单明细
	 * @param map
	 * @return
	 * @throws
	 */
	List<SaleReturnIdepotItemModel> getDetailsByReceive (Map<String, Object> map);
}

