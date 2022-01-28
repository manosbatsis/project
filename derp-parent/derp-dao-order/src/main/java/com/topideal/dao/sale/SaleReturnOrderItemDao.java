package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;

/**
 * 销售退货订单表体 dao
 * @author lian_
 *
 */
public interface SaleReturnOrderItemDao extends BaseDao<SaleReturnOrderItemModel>{

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	
    /**
     * 通过订单ID查询信息
     * @param orderId
     * @return
     */
     List<SaleReturnOrderItemModel> searchByOrderId(Long orderId);
     
     /**
      * 根据PO单号+商品货号+订单类型查询退货记录
      * @param saleReturnOrderItemModel
      * @return
      */
     List<SaleReturnOrderItemModel> searchByInfo(SaleReturnOrderItemModel saleReturnOrderItemModel);
     /**
      * 列表查询
      * @param dto
      * @return
      * @throws SQLException
      */
     List<SaleReturnOrderItemDTO> listSaleReturnOrderItemDTO(SaleReturnOrderItemDTO dto)throws SQLException;
 	/**
 	 * 根据表头Id查询的数据（除itemIds之外的数据） 要删除销售退货商品id
 	 * @param itemIds
 	 * @param id
 	 * @return
 	 */
 	List<SaleReturnOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id);
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param id 表头id
	 */
	Long delBesidesIds(List<Long> itemIds, Long id);
	
	/**
	 * 获取表体详情
	 * @param itemDto
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnOrderItemDTO> getSaleReturnOrderItem(SaleReturnOrderItemDTO itemDto)throws Exception;

	/**
	 * 根据销售订单号统计查询退货单明细
	 * @param saleCodeList
	 * @throws SQLException
	 */
	List<SaleReturnOrderItemModel> statisticsBySaleCode(List<String> saleCodeList) throws SQLException;

	/**
	 * 根据po单号统计查询退货单明细
	 * @param poNoList
	 * @throws SQLException
	 */
	List<SaleReturnOrderItemModel> statisticsByPoNos(List<String> poNoList) throws SQLException;
	/**
	 * 购销退货 选择商品
	 * @param itemDto
	 * @return
	 * @throws Exception
	 */
	SaleReturnOrderItemDTO getSalePopupListByPage(SaleReturnOrderItemDTO itemDto)throws SQLException;

	/**
	 * 根据销售退订单id集合查询销售退订单表体
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnOrderItemModel> listByOrderIds(List<Long> orderIds) throws SQLException;

}

