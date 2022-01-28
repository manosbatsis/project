package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货订单表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnOrderItemMapper extends BaseMapper<SaleReturnOrderItemModel> {
	
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	
    /**
     * 通过订单ID查询信息
     * @param orderId
     * @return
     */
     List<SaleReturnOrderItemModel> searchByOrderId(@Param("orderId")Long orderId);
     
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
 	 * @param orderId
 	 * @return
 	 */
 	List<SaleReturnOrderItemModel> getListByBesidesIds (@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);
	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param orderId
	 */
 	Long delBesidesIds(@Param("itemIds")List<Long> itemIds, @Param("orderId")Long orderId);
	
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
	List<SaleReturnOrderItemModel> statisticsBySaleCode(@Param("saleCodeList") List<String> saleCodeList) throws SQLException;

	/**
	 * 根据po单号统计查询退货单明细
	 * @param poNoList
	 * @throws SQLException
	 */
	List<SaleReturnOrderItemModel> statisticsByPoNos(@Param("poNoList") List<String> poNoList) throws SQLException;
	/**
	 * 购销退货 选择商品
	 * @param itemDto
	 * @return
	 * @throws Exception
	 */
	PageDataModel<SaleReturnOrderItemModel> getSalePopupListByPage(SaleReturnOrderItemDTO itemDto)throws SQLException;

	/**
	 * 根据销售退订单id集合查询销售退订单表体
	 * @param orderIds
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnOrderItemModel> listByOrderIds(@Param("orderIds") List<Long> orderIds) throws SQLException;
}

