package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseWarehouseDao;
import com.topideal.entity.dto.purchase.PurchaseWarehouseDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportBean;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.mapper.purchase.PurchaseWarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库单
 * @author lchenxing
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
	 * @param PurchaseWarehouseDTO
	 */
	@Override
	public Long save(PurchaseWarehouseModel model) throws SQLException {
		model.setCreateDate(TimeUtils.getNow());
		model.setModifyDate(TimeUtils.getNow());
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
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseWarehouseDTO
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
	 * 获取采购入库单和采购入库单商品批次
	 */
	@Override
	public Map<String, Object> getPurchaseItemBatch(PurchaseWarehouseModel model) throws SQLException {
		return mapper.getPurchaseItemBatch(model);
	}

	/**
	 * 详情
	 * 
	 * @return model
	 */
	@Override
	public PurchaseWarehouseModel getDetails(PurchaseWarehouseModel model) throws SQLException {
		return mapper.getDetails(model);
	}

	/**
	 * 入库明细导出
	 * @param ids
	 * @return
	 */
	@Override
	public List<PurchaseWarehouseExportDTO> getExportDetails(List ids) throws SQLException {
		return mapper.getExportDetails(ids);
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PurchaseWarehouseDTO getListByPage(PurchaseWarehouseDTO dto) throws SQLException {
		PageDataModel<PurchaseWarehouseDTO> pageModel = mapper.getListByPage(dto);
		return (PurchaseWarehouseDTO) pageModel.getModel();
	}

	/**
	 * 根据预入库单id获取采购订单集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<String> getWarehouseListById(Long id) throws SQLException {
		return mapper.getWarehouseListById(id);
	}

	/**
	 * 根据表头ids获取表体集合
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseWarehouseExportBean> getItemDetails(List list) throws SQLException {
		return mapper.getItemDetails(list);
	}

	/**
	 * 根据ids获取集合
	 * @param list
	 * @return
	 */
	@Override
	public List<PurchaseWarehouseModel> getListByIds(List<Long> list) {
		return mapper.getListByIds(list);
	}

	@Override
	public PurchaseWarehouseDTO getDTODetails(PurchaseWarehouseDTO dto) {
		return mapper.getDTODetails(dto);
	}

	@Override
	public List<PurchaseWarehouseExportDTO> getExportDetailsByDTO(PurchaseWarehouseDTO dto) {
		return mapper.getExportDetailsByDTO(dto);
	}

	@Override
	public List<PurchaseWarehouseDTO> listDTO(PurchaseWarehouseDTO dto) {
		return mapper.listDTO(dto);
	}

	/**统计采购单未入库量
	 * purchaseId 采购单id
	 * */
	public List<Map<String,Object>> getNoInWarehouseNum(Long purchaseId){
		return mapper.getNoInWarehouseNum(purchaseId);
	}
}
