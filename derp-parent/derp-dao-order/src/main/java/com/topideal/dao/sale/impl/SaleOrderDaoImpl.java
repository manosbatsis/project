package com.topideal.dao.sale.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.entity.dto.sale.PendingCheckOrderVo;
import com.topideal.entity.dto.sale.PendingShelfSaleOrderVo;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.mapper.sale.SaleOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售订单 impl
 *
 * @author lchenxing
 */
@Repository
public class SaleOrderDaoImpl implements SaleOrderDao {

	@Autowired
	private SaleOrderMapper mapper;// 销售订单

	/**
	 * 根据预收单id查询销售订单明细
	 */
    @Override
    public List<Map<String, Object>>getSaleOrderByAdvanceId(List<Long> ids) {
        return mapper.getSaleOrderByAdvanceId(ids);
    }
	/**
	 * 列表查询
	 *
	 * @param model
	 */
	@Override
	public List<SaleOrderModel> list(SaleOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 */
	@Override
	public Long save(SaleOrderModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 *
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 */
	@Override
	public int modify(SaleOrderModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 *
	 * @param
	 */
	@Override
	public SaleOrderModel searchByPage(SaleOrderModel model) throws SQLException {
		PageDataModel<SaleOrderModel> pageModel = mapper.listByPage(model);
		return (SaleOrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 */
	@Override
	public SaleOrderModel searchById(Long id) throws SQLException {
		SaleOrderModel model = new SaleOrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 */
	@Override
	public SaleOrderModel searchByModel(SaleOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 逻辑删除
	 *
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delSaleOrder(List ids) throws SQLException {
		return mapper.delSaleOrder(ids);
	}

	@Override
	public SaleOrderModel queryListByPage(SaleOrderModel model) throws SQLException {
		PageDataModel<SaleOrderModel> pageModel = mapper.queryListByPage(model);
		return (SaleOrderModel) pageModel.getModel();
	}

	@Override
	public SaleOrderModel searchByUnfinished(String poNo, Long goodsId, Integer totalSalesQty, Long merchantId,
			Long customerId,String notInIds) {
		return mapper.searchByUnfinished(poNo, goodsId, totalSalesQty, merchantId, customerId,notInIds);
	}

	@Override
	public boolean poNoIsExist(SaleOrderModel saleOrder, Long id) throws SQLException {
		List<SaleOrderModel> list = mapper.list(saleOrder);
		for (SaleOrderModel saleOrderModel : list) {
			if (!DERP.DEL_CODE_006.equals(saleOrderModel.getState())) {
				if (id != null && saleOrderModel.getId().equals(id)) {// 排除自身
					continue;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据条件查询
	 *
	 * @return
	 */
	@Override
	public List<SaleOrderDTO> queryDTOList(SaleOrderDTO dto) throws SQLException {
		return mapper.queryDTOList(dto);
	}

	@Override
	public boolean lbxNoIsExist(String lbxNo, Long id) throws SQLException {
		SaleOrderModel saleOrder = new SaleOrderModel();
		saleOrder.setLbxNo(lbxNo);
		List<SaleOrderModel> list = mapper.list(saleOrder);
		for (SaleOrderModel saleOrderModel : list) {
			if (!DERP.DEL_CODE_006.equals(saleOrderModel.getState())) {
				if (id != null && saleOrderModel.getId().equals(id)) {// 排除自身
					continue;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public List<SaleOrderModel> searchByPoUnfinished(String poNo, Long goodsId, Integer totalSalesQty, Long merchantId,
			Long customerId) {
		return mapper.searchByPoUnfinished(poNo, goodsId, totalSalesQty, merchantId, customerId);
	}

	/**
	 * 修改 销售订单 入库仓仓库id和仓库名称为null
	 */
	@Override
	public int modifyCulomNULL(Long id) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.modifyCulomNULL(id);
	}
	/**
	 * 新版爬虫出库-获取销售单货号的销售数量
	 * */
	public Map<String, Object> getOrderGoodsNum(Map<String, Object> map){
		return mapper.getOrderGoodsNum(map);
	}

	/**
	 * 把归属月份和原销出仓仓库修改为空
	 */
	public int updateMonthAndDepot(Long orderId) {
		return mapper.updateMonthAndDepot(orderId);
	}

	@Override
	public List<SaleOrderModel> searchByPo(String poNo) {
		return mapper.searchByPo(poNo);
	}

	@Override
	public List<PendingShelfSaleOrderVo> getPendingShelfSaleOrders(Map<String, Object> map) throws SQLException {
		return mapper.getPendingShelfSaleOrders(map);
	}

	@Override
	public List<PendingCheckOrderVo> getPendingCheckOrders(Map<String, Object> map) throws SQLException {
		return mapper.getPendingCheckOrders(map);
	}

	@Override
	public Integer countPendingCheckOrders(Map<String, Object> map) throws SQLException {
		return mapper.countPendingCheckOrders(map);
	}

	@Override
	public Integer countPendingShelfOrders(Map<String, Object> map) throws SQLException {
		return mapper.countPendingShelfOrders(map);
	}

	@Override
	public SaleOrderDTO queryDTOListByPage(SaleOrderDTO dto) throws SQLException {
		PageDataModel<SaleOrderDTO> pageModel = mapper.queryDTOListByPage(dto);
		return (SaleOrderDTO) pageModel.getModel();
	}
	/**
	 * 通过id查询实体类信息
	 */
	@Override
	public SaleOrderDTO searchDTOById(Long id) throws SQLException {
		SaleOrderDTO dto = new SaleOrderDTO();
		dto.setId(id);
		return mapper.getSaleOrderDTOById(dto);
	}

	@Override
	public List<SaleOrderModel> listByDepotAndGoodsNo(Map<String, Object> map) throws SQLException {
		return mapper.listByDepotAndGoodsNo(map);
	}

	/**
	 * 云集爬虫出库获取sku对应的货号未核销量>0的销售单
	 */
	@Override
	public List<Map<String, Object>> getDeliveryOrderGoodsWhxNum(Map<String, Object> map) {
		return mapper.getDeliveryOrderGoodsWhxNum(map);
	}

	@Override
	public SaleOrderDTO saleGetListSaleOrderByPage(SaleOrderDTO dto) throws SQLException {
		PageDataModel<SaleOrderDTO> pageModel = mapper.saleGetListSaleOrderByPage(dto);
		return (SaleOrderDTO)pageModel.getModel();
	}

	@Override
	public SaleOrderDTO getDetails(SaleOrderDTO saleOrderDTO) {
		return mapper.getDetails(saleOrderDTO);
	}

	@Override
	public SaleOrderDTO getSaleOrderDTOBySaleOrderCodeAndStatusAndMerchantId(String code, Long merchantId) {
		return mapper.getSaleOrderDTOBySaleOrderCodeAndStatusAndMerchantId(code, merchantId);
	}

	@Override
	public List<Map<String,Object>> getByCodeAndPoAndBarcode(Map<String,Object> map) {
		return mapper.getByCodeAndPoAndBarcode(map);
	}
	/**根据单号、条码查询订单中是否存在条码相同货号不同的商品*/
	@Override
	public List<Map<String,Object>> getItemGroupByCodeBarcode(Map<String,Object> map){
		return mapper.getItemGroupByCodeBarcode(map);
	}

	@Override
	public List<Map<String, Object>> getNoShelfNum(Map<String, Object> map) throws SQLException {
		return mapper.getNoShelfNum(map);
	}

	@Override
	public List<Map<String,Object>> getShelfNum(Map<String, Object> map) throws SQLException {
		return mapper.getShelfNum(map);
	}

	@Override
	public List<Map<String, Object>> getNotShaleSaleByPoNo(Map<String, Object> map) throws SQLException {
		return mapper.getNotShaleSaleByPoNo(map);
	}

	@Override
	public List<SaleOrderModel> getSaleOrderByParams(Map<String, Object> paramMap) {
		return mapper.getSaleOrderByParams(paramMap);
	}
	@Override
	public List<Map<String, Object>> getStatusNum(SaleOrderDTO dto){
		return mapper.getStatusNum(dto);
	}

	@Override
	public int modifyWithNull(SaleOrderModel model) throws SQLException {
		return mapper.modifyWithNull(model);
	}

}
