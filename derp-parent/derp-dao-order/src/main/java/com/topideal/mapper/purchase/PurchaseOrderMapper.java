package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单 mapper
 *
 * @author lianchenxing
 */
@MyBatisRepository
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrderModel> {

	/**
	 * 条件过滤查询，会查询出表体、商品
	 *
	 * @return
	 */
	PurchaseOrderModel getDetails(PurchaseOrderModel model) throws SQLException;

	/**
	 * 条件查询供应商id和仓库id
	 */
	List<PurchaseOrderModel> getSupplierIdAndDepotId(List list) throws SQLException;

	/**
	 * 根据表头id删除表体
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	int delById(@Param("id") Long id) throws SQLException;

	/**
	 * 根据采购订单id获取采购入库的商品数量
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseItemModel> getWarehouseItemById(@Param("id") Long id) throws SQLException;

	/**
	 * 分页
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<PurchaseOrderDTO> getlistByPage(PurchaseOrderDTO dto) throws SQLException;

	/**
	 * 根据预采购订单id获取入库单集合
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<String> getWarehouseCodeById(@Param("id") Long id) throws SQLException;

	/**
	 * 导出查询表头表体
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderExportDTO> getDetailsByExport(PurchaseOrderExportDTO dto) throws SQLException;

	/**
	 * 根据po号查询是否已经存在（已删除的不算）
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PurchaseOrderModel getPurchaseByPoNo(PurchaseOrderModel model) throws SQLException;

	/**
	 * 抓取经分销收到发票没有付款的采购订单
	 *
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderModel> getGrabDerpPurchaseOrder(@Param("merchantId")Long merchantId,@Param("supplierId")Long supplierId) throws SQLException;

	/**
	 * 校验是否存在(已删除的除外)
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderModel> getListByCheck(PurchaseOrderModel model) throws SQLException;

	/**
	 * 根据商家获取待录入的采购订单
	 * @Param
	 * @return
	 */
	List<PurchaseOrderDTO> getPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException;

	/**
	 * 根据商家统计待录入的采购订单数量
	 * @Param
	 * @return
	 */
	Integer countPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException;

	PurchaseOrderDTO getDTODetails(PurchaseOrderDTO dto);

	PageDataModel<PurchaseOrderDTO> getListPurchaseOrderByPage(PurchaseOrderDTO dto);

	Integer getItemNumBypurchaseCode(Map<String, Object> queryMap);

	/**
	 * 根据采购订单ID、货号统计采购数量
	 * @param queryMap
	 * @return
	 */
	Map<String, Object> getPurchaseNumByIdsAndGoodsNo(Map<String, Object> queryMap);
	/**
	 * 选择内部采购单
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderDTO> getOwnPurchaseOrder(PurchaseOrderDTO dto) throws SQLException;

	/**
	 * 获取本月和前月的无入库本位币金额的采购单，有单号根据单号获取
	 * @param paramMap
	 * @return
	 */
	List<PurchaseOrderModel> getNoTgtAmountOrder(Map<String, Object> paramMap);

	/**
	 * 获取发票信息为空的采购订单
	 * @param queryOrderMap
	 */
	List<PurchaseOrderModel> getNoneInvoiceOrderList(Map<String, Object> queryMap);

	/**
	 * 查询需要创建嘉宝、健太、卓烨创建日期为最近3个月的【已审核】采购订单
	 * @return
	 */
	List<PurchaseOrderModel> getCreateSdOrder(Map<String,Object> paramMap);

	/**
	 * 项目额度预警查询采购订单
	 * @param queryOrderMap
	 * @return
	 */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap);

    /**
     * 预付单查询采购订单
     * @param dto
     * @return
     */
	PageDataModel<PurchaseOrderDTO> getAdvancePaymentListByPage(PurchaseOrderDTO dto);

	/**
	 * 应付单查询采购订单
	 * @param dto
	 * @return
	 */
	PageDataModel<PurchaseOrderDTO> getPaymentListByPage(PurchaseOrderDTO dto);

	/***
	 * 更新融资信息
	 * @param updateModel
	 */
	void updateFinanceInfo(PurchaseOrderModel updateModel);


	/**
	 * 查询预计付款日期在当前日期前后7天内
	 * 采购订单号不存在应付单明细中，应付单排除已删除状态
	 * @return
	 */
	List<PurchaseOrderModel> getPurchaseOrderByPayDate();

	/**
	 * 通过预申报id查看采购订单
	 * @param ids
	 * @return
	 */
	List<PurchaseOrderModel> getPurchaseOrderByDeclare(@Param("ids") String ids);

	/**
	 * 宝信事业部、保健品事业部、母婴事业部维度，查找这3个部门下事业部的采购订单
	 * 以及采购订单创建日期在当前日期前60天内
	 * @param queryParam
	 * @return
	 */
	List<PurchaseOrderModel> getPurchaseOrderByParams(Map<String, Object> queryParam);
	/**
	 * 查询未创建应付单据的采购订单 （包括已删除，已作废的应付单据）
	 * @param queryParam
	 * @return
	 */
	List<PurchaseOrderDTO> getNotPaymentBillPurchaseOrder(Map<String, Object> queryParam);

	int modifyWithNull(PurchaseOrderModel  model) throws SQLException;

	/**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getPurchaseOrderTypeNum(PurchaseOrderDTO dto);

	/**
	 * 根据采购订单id集合查询
	 */
	List<PurchaseOrderModel> listByIds(@Param("ids") List<Long> ids) throws SQLException;

    int batchUpdate(@Param("list") List<PurchaseOrderDTO> list);

    List<PurchaseOrderDTO> listByDTO(PurchaseOrderDTO purchaseOrderDTO);
}
