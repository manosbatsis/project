package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PendingCheckOrderVo;
import com.topideal.entity.dto.sale.PendingShelfSaleOrderVo;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.SaleOrderModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售订单 dao
 * @author lian_
 *
 */
public interface SaleOrderDao extends BaseDao<SaleOrderModel>{

    /**
     * 根据预收单id查询销售订单明细
     */
	List<Map<String, Object>> getSaleOrderByAdvanceId(List<Long> ids);
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	int delSaleOrder(List ids) throws SQLException;

	/**
     * 根据条件查询（分页）
     * @return
     */
    SaleOrderModel queryListByPage(SaleOrderModel model)throws SQLException;

    /**
     * 通过po号、商品、商家id、客户id查找销售订单(状态为未完结,且未被核销数量大于出库数量的销售订单。多个结果取日期最早的订单。)
     * @param poNo
     * @param goodsId
     * @param totalSalesQty
     * @param merchantId
     * @param customerId
     * @param notInIds  需要剔除的订单id 以“,”隔开
     * @return
     */
	SaleOrderModel searchByUnfinished(String poNo, Long goodsId, Integer totalSalesQty, Long merchantId, Long customerId,String notInIds);
	/**
	 * 通过po号、商品、商家id查找销售订单(状态为未完结,且未被核销数量大于出库数量的销售订单。按日期排序。)
	 * @param poNo
	 * @param goodsId
	 * @param totalSalesQty
	 * @param merchantId
	 * @param customerId
	 * @return
	 */
	List<SaleOrderModel> searchByPoUnfinished(String poNo, Long goodsId, Integer totalSalesQty, Long merchantId, Long customerId);

	/**
	 * 查询po单号是否存在
	 * @param saleOrder
	 * @param id 修改时，要排除自身的id
	 * @return
	 * @throws SQLException
	 */
	boolean poNoIsExist(SaleOrderModel saleOrder, Long id) throws SQLException;
	/**
     * 根据条件查询
     * @return
     */
	List<SaleOrderDTO> queryDTOList(SaleOrderDTO dto)throws SQLException;
	/**
	 * 校验lbxNo是否存在
	 * @param lbxNo
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	boolean lbxNoIsExist(String lbxNo, Long id) throws SQLException;

	/**
	 * 修改 销售订单 的 入库仓 仓库id和仓库名称为null
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int modifyCulomNULL(Long id)throws SQLException;
	/**
	 * 云集爬虫出库获取sku对应的货号未核销量>0的销售单数量
	 * @param map
	 * @return
	 */
	List<Map<String, Object>>getDeliveryOrderGoodsWhxNum(Map<String, Object> map);
	/**
	 * 新版爬虫出库-获取销售单货号的销售数量
	 * */
	Map<String, Object> getOrderGoodsNum(Map<String, Object> map);
	/**
	 * 把归属月份和原销出仓仓库修改为空
	 * @param orderId
	 * @return
	 */
	int updateMonthAndDepot(Long orderId);
	/**
	 *  根据PO号查询没有删除的销售单
	 * @param poNo
	 * @return
	 */
	List<SaleOrderModel> searchByPo(String poNo);
	/**
	 * 获取待上架的订单（销售订单和销售出库单）
	 * @Param
	 * @return
	 */
	List<PendingShelfSaleOrderVo> getPendingShelfSaleOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 获取待审核的订单（采购、销售、调拨、销售退订单）
	 * @Param
	 * @return
	 */
	List<PendingCheckOrderVo> getPendingCheckOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 统计待审核的订单数量（采购、销售、调拨、销售退订单）
	 * @Param
	 * @return
	 */
	Integer countPendingCheckOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 统计待上架的订单数量(销售订单和销售出库单）
	 * @Param
	 * @return
	 */
	Integer countPendingShelfOrders(Map<String, Object> map) throws SQLException;
	/**
     * 根据条件查询（分页）
     * @return
     */
    SaleOrderDTO queryDTOListByPage(SaleOrderDTO dto)throws SQLException;
    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
    SaleOrderDTO searchDTOById(Long id)throws SQLException;

	/**
     * 销售新增代销获取分页数据
     * @param dto
     * @return
     */
    SaleOrderDTO saleGetListSaleOrderByPage(SaleOrderDTO dto)throws SQLException;
    /**
     * 条件过滤查询，会查询出表体、商品
     * @param saleOrderModel
     * @return
     */
    SaleOrderDTO getDetails(SaleOrderDTO saleOrderDTO);


	/**
	 * 根据商家、仓库id、商品货号查询已审核的代销销售订单
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<SaleOrderModel> listByDepotAndGoodsNo(Map<String, Object> map) throws SQLException;

	/**
	 * 根据销售订单编号、状态、商家id查询销售订单对象
	 * @param code
	 * @return
	 */
    SaleOrderDTO getSaleOrderDTOBySaleOrderCodeAndStatusAndMerchantId(String code, Long merchantId);

	/**
	 * 根据销售订单编号、po、条码查询销售订单对象
	 */
	List<Map<String,Object>> getByCodeAndPoAndBarcode(Map<String,Object> map);
	/**根据单号、条码查询订单中是否存在条码相同货号不同的商品*/
	List<Map<String,Object>> getItemGroupByCodeBarcode(Map<String,Object> map);
	/**
	 * 获取销售在途数量
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getNoShelfNum(Map<String,Object> map) throws SQLException;
	/**
	 * 获取当前 事业部+客户 销售上架总金额
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getShelfNum(Map<String,Object> map) throws SQLException;

	/**
	 * 根据平台采购单 poNo+商家查询  已经生成销售订单 但是销售订单未上架的数据
	 */
	List<Map<String,Object>>  getNotShaleSaleByPoNo(Map<String,Object> map)throws SQLException;

	/**
	 * 查询销售订单创建日期在当前日期前60天内以及销售订单的客户内部公司为是
	 * @param paramMap
	 * @return
	 */
	List<SaleOrderModel> getSaleOrderByParams(Map<String,Object> paramMap);

	/**
	 * 获取各状态数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getStatusNum(SaleOrderDTO dto);

	int modifyWithNull(SaleOrderModel model) throws SQLException;

}

