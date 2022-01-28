package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.entity.dto.PurchaseWarehouseDTO;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;
import com.topideal.mapper.order.PurchaseWarehouseMapper;

/**
 * 采购入库单
 * @author zhanghx
 */
@Repository
public class PurchaseWarehouseDaoImpl implements PurchaseWarehouseDao {

	@Autowired
	private PurchaseWarehouseMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<PurchaseWarehouseModel> list(PurchaseWarehouseModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param PurchaseWarehouseModel
	 */
	@Override
	public Long save(PurchaseWarehouseModel model) throws SQLException {
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
	 * 
	 * @param List
	 */
	@Override
	public int modify(PurchaseWarehouseModel model) throws SQLException {
		
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseWarehouseModel
	 */
	@Override
	public PurchaseWarehouseModel searchByPage(PurchaseWarehouseModel model) throws SQLException {
		PageDataModel<PurchaseWarehouseModel> pageModel = mapper.listByPage(model);
		return (PurchaseWarehouseModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public PurchaseWarehouseModel searchById(Long id) throws SQLException {
		PurchaseWarehouseModel model = new PurchaseWarehouseModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public PurchaseWarehouseModel searchByModel(PurchaseWarehouseModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询采购入库单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询采购入库单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime);
	}
	
	/**
	 * 根据采购订单id 获取poNo、币种、供应商名称、供应商编码
	 * @param id
	 * @return
	 */
	public Map<String, Object> getOrderInfo(Long orderId){
		return mapper.getOrderInfo(orderId);
	}
	
	@Override
	public List<PurchaseWarehouseDTO> getByTimeOrder(PurchaseWarehouseDTO purchaseWarehouseDTO) {
		return mapper.getByTimeOrder(purchaseWarehouseDTO);
	}

	@Override
	public String getPurchaseOrderIdByWarehouse(Map<String, Object> queryWarehouseMap) {
		return mapper.getPurchaseOrderIdByWarehouse(queryWarehouseMap);
	}

	@Override
	public List<PurchaseWarehouseModel> getUnCorrelateList() {
		return mapper.getUnCorrelateList();
	}

	@Override
	public List<Map<String, Object>> getInWarehouseDaysDetail(Map<String, Object> queryMap) {
		return mapper.getInWarehouseDaysDetail(queryMap) ;
	}
	

}
