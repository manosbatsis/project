package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.DeclareOrderDao;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.json.pushapi.epass.e01.PurchaseOrderRootJson;
import com.topideal.mapper.purchase.DeclareOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 预申报单 impl
 *
 * @author lchenxing
 */
@Repository
public class DeclareOrderDaoImpl implements DeclareOrderDao {

	// 预申报单mapper
	@Autowired
	private DeclareOrderMapper mapper;

	/**
	 * 列表查询
	 *
	 * @param model
	 */
	@Override
	public List<DeclareOrderModel> list(DeclareOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 *
	 * @param model
	 */
	@Override
	public Long save(DeclareOrderModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 *
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 *
	 * @param model
	 */
	@Override
	public int modify(DeclareOrderModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 *
	 * @param model
	 */
	@Override
	public DeclareOrderModel searchByPage(DeclareOrderModel model) throws SQLException {
		PageDataModel<DeclareOrderModel> pageModel = mapper.listByPage(model);
		return (DeclareOrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 *
	 * @param
	 */
	@Override
	public DeclareOrderModel searchById(Long id) throws SQLException {
		DeclareOrderModel model = new DeclareOrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 *
	 * @param model
	 */
	@Override
	public DeclareOrderModel searchByModel(DeclareOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取详情
	 */
	@Override
	public DeclareOrderModel getDetails(Long id) throws SQLException {
		DeclareOrderModel model = new DeclareOrderModel();
		model.setId(id);
		return mapper.getDetails(model);
	}

	/**
	 * 条件过滤查询，会查询出表体、商品
	 *
	 * @return
	 */
	@Override
	public List<DeclareOrderItemModel> getAll(Long id) throws SQLException {
		return mapper.getAll(id);
	}

	@Override
	public List<DeclareOrderItemModel> getItem(Long id) throws SQLException {
		return mapper.getItem(id);
	}

	/**
	 * 根据表头id删除表体
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int delById(Long id) throws SQLException {
		return mapper.delById(id);
	}

	/**
	 * 将采购实体类映射成Vo实体类
	 *
	 * @param id
	 * @return
	 */
	@Override
	public PurchaseOrderRootJson searchVoById(Long id) {
		return mapper.getVo(id);
	}

	/**
	 * 根据预申报单id获取采购订单集合
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<PurchaseOrderModel> getPurchaseListById(Long id) throws SQLException {
		return mapper.getPurchaseListById(id);
	}

	/**
	 * 分页
	 *
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public DeclareOrderDTO getListByPage(DeclareOrderDTO dto) throws SQLException {
		PageDataModel<DeclareOrderDTO> pageModel = mapper.getListByPage(dto);
		return (DeclareOrderDTO) pageModel.getModel();
	}


	@Override
	public List<DeclareOrderDTO> getDeclareOrderByExport(DeclareOrderDTO dto) throws SQLException {
		return mapper.getDeclareOrderByExport(dto);
	}

	@Override
	public DeclareOrderDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}

	@Override
	public List<Map<String, Object>> getDeclareTypeNum(DeclareOrderDTO dto) {
		return mapper.getDeclareTypeNum(dto);
	}

	@Override
	public List<DeclareOrderDTO> listDTO(DeclareOrderDTO dto) {
		return mapper.listDTO(dto);
	}
}
