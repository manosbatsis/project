package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;
import com.topideal.mapper.purchase.PurchaseOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单 impl
 * @author lianchenxing
 */
@Repository
public class PurchaseOrderDaoImpl implements PurchaseOrderDao {

	//采购订单mapper
    @Autowired
    private PurchaseOrderMapper mapper;

	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseOrderModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }

	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }

	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(PurchaseOrderModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseOrderModel  searchByPage(PurchaseOrderModel  model) throws SQLException{
        PageDataModel<PurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (PurchaseOrderModel) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseOrderModel  searchById(Long id)throws SQLException {
        PurchaseOrderModel  model=new PurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     * @param model
     * */
	@Override
	public PurchaseOrderModel searchByModel(PurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseOrderModel> list(PurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
     * 条件过滤查询，会查询出表体、商品
     * @return
     */
	@Override
	public PurchaseOrderModel getDetails(PurchaseOrderModel model) throws SQLException{
		return mapper.getDetails(model);
	}

	/**
	 * 条件查询供应商id和仓库id
	 */
	@Override
	public List<PurchaseOrderModel> getSupplierIdAndDepotId(List list) throws SQLException {
		return mapper.getSupplierIdAndDepotId(list);
	}

	/**
	 * 根据表头id删除表体
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delById(Long id) throws SQLException {
		return mapper.delById(id);
	}

	/**
	 * 根据采购订单id获取采购入库的商品数量
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseItemModel> getWarehouseItemById(Long id) throws SQLException {
		return mapper.getWarehouseItemById(id);
	}

	/**
	 * 根据预采购订单id获取入库单集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<String> getWarehouseCodeById(Long id) throws SQLException {
		return mapper.getWarehouseCodeById(id);
	}

	/**
	 * 分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PurchaseOrderDTO getlistByPage(PurchaseOrderDTO dto) throws SQLException {
		PageDataModel<PurchaseOrderDTO> pageModel=mapper.getlistByPage(dto);
        return (PurchaseOrderDTO ) pageModel.getModel();
	}

	/**
	 * 导出查询表头表体
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseOrderExportDTO> getDetailsByExport(PurchaseOrderExportDTO dto) throws SQLException {
		return mapper.getDetailsByExport(dto);
	}

	/**
	 * 根据po号查询是否已经存在（已删除的不算）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PurchaseOrderModel getPurchaseByPoNo(PurchaseOrderModel model) throws SQLException {
		return mapper.getPurchaseByPoNo(model);
	}
	/**
	 * 抓取经分销收到发票没有付款的采购订单
	 */
	@Override
	public List<PurchaseOrderModel> getGrabDerpPurchaseOrder(Long merchantId,Long supplierId) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getGrabDerpPurchaseOrder(merchantId,supplierId);
	}

	/**
	 * 校验是否存在(已删除的除外)
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseOrderModel> getListByCheck(PurchaseOrderModel model) throws SQLException {
		return mapper.getListByCheck(model);
	}

	@Override
	public List<PurchaseOrderDTO> getPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException {
		return mapper.getPendingRecordOrders(dto);
	}

	@Override
	public Integer countPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException {
		return mapper.countPendingRecordOrders(dto);
	}

	@Override
	public PurchaseOrderDTO getDTODetails(PurchaseOrderDTO dto) {
		return mapper.getDTODetails(dto);
	}

	@Override
	public PurchaseOrderDTO getListPurchaseOrderByPage(PurchaseOrderDTO dto) {
		PageDataModel<PurchaseOrderDTO> pageModel =  mapper.getListPurchaseOrderByPage(dto);
		return (PurchaseOrderDTO)pageModel.getModel();
	}

	@Override
	public Integer getItemNumBypurchaseCode(Map<String, Object> queryMap) {
		return mapper.getItemNumBypurchaseCode(queryMap);
	}

	@Override
	public Map<String, Object> getPurchaseNumByIdsAndGoodsNo(Map<String, Object> queryMap) {
		return mapper.getPurchaseNumByIdsAndGoodsNo(queryMap);
	}

	@Override
	public List<PurchaseOrderDTO> getOwnPurchaseOrder(PurchaseOrderDTO dto) throws SQLException {
		return mapper.getOwnPurchaseOrder(dto);
	}

	@Override
	public List<PurchaseOrderModel> getNoTgtAmountOrder(Map<String, Object> paramMap) throws SQLException {
		return mapper.getNoTgtAmountOrder(paramMap);
	}

	@Override
	public List<PurchaseOrderModel> getNoneInvoiceOrderList(Map<String, Object> queryMap) {
		return mapper.getNoneInvoiceOrderList(queryMap);
	}

	@Override
	public List<PurchaseOrderModel> getCreateSdOrder(Map<String,Object> paramMap) {
		return mapper.getCreateSdOrder(paramMap) ;
	}

	@Override
	public List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap) {
		return mapper.getProjectWarnList(queryOrderMap);
	}

	@Override
	public PurchaseOrderDTO getAdvancePaymentListByPage(PurchaseOrderDTO dto) {
		PageDataModel<PurchaseOrderDTO> pageModel = mapper.getAdvancePaymentListByPage(dto);
        return (PurchaseOrderDTO ) pageModel.getModel();
	}

	@Override
	public PurchaseOrderDTO getPaymentListByPage(PurchaseOrderDTO dto) {
		PageDataModel<PurchaseOrderDTO> pageModel = mapper.getPaymentListByPage(dto);
        return (PurchaseOrderDTO ) pageModel.getModel();
	}

	@Override
	public void updateFinanceInfo(PurchaseOrderModel updateModel) {
		mapper.updateFinanceInfo(updateModel) ;
	}

	@Override
	public List<PurchaseOrderModel> getPurchaseOrderByPayDate() {
		return mapper.getPurchaseOrderByPayDate();
	}

	@Override
	public List<PurchaseOrderModel> getPurchaseOrderByDeclare(String ids) {
		return mapper.getPurchaseOrderByDeclare(ids);
	}

	@Override
	public List<PurchaseOrderModel> getPurchaseOrderByParams(Map<String, Object> queryParam) {
		return mapper.getPurchaseOrderByParams(queryParam);
	}

	@Override
	public List<PurchaseOrderDTO> getNotPaymentBillPurchaseOrder(Map<String, Object> queryParam){
		return mapper.getNotPaymentBillPurchaseOrder(queryParam);
	}

	@Override
	public int modifyWithNull(PurchaseOrderModel model) throws SQLException {
		return mapper.modifyWithNull(model);
	}

	@Override
	public List<Map<String, Object>> getPurchaseOrderTypeNum(PurchaseOrderDTO dto) {
		return mapper.getPurchaseOrderTypeNum(dto);
	}

	@Override
	public List<PurchaseOrderModel> listByIds(List<Long> ids) throws SQLException {
		return mapper.listByIds(ids);
	}

	@Override
	public int batchUpdate(List<PurchaseOrderDTO> updateList) {
		return mapper.batchUpdate(updateList);
	}

	@Override
	public List<PurchaseOrderDTO> listByDTO(PurchaseOrderDTO purchaseOrderDTO) {
		return mapper.listByDTO(purchaseOrderDTO);
	}

}
