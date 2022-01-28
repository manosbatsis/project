package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.AdjustmentTypeDTO;
import com.topideal.entity.dto.AdjustmentTypeItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.mapper.AdjustmentTypeMapper;

/**
 * 类型调整
 * 
 * @author lchenxing
 */
@Repository
public class AdjustmentTypeDaoImpl implements AdjustmentTypeDao {

	@Autowired
	private AdjustmentTypeMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<AdjustmentTypeModel> list(AdjustmentTypeModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(AdjustmentTypeModel model) throws SQLException {
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
	public int modify(AdjustmentTypeModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentTypeModel searchByPage(AdjustmentTypeModel model) throws SQLException {
		PageDataModel<AdjustmentTypeModel> pageModel = mapper.listByPage(model);
		return (AdjustmentTypeModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public AdjustmentTypeModel searchById(Long id) throws SQLException {
		AdjustmentTypeModel model = new AdjustmentTypeModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentTypeModel searchByModel(AdjustmentTypeModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 分页
	 * 
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentTypeDTO getListByPage(AdjustmentTypeDTO dto) throws SQLException {
		PageDataModel<AdjustmentTypeDTO> pageModel = mapper.getListByPage(dto);
		return (AdjustmentTypeDTO) pageModel.getModel();
	}

	/**
	 * 详情
	 * 
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentTypeDTO getDetails(AdjustmentTypeDTO dto) throws SQLException {
		return mapper.getDetails(dto);
	}

	@Override
	public AdjustmentTypeDTO getAdjustDetails(Long id) throws SQLException {
		return mapper.getAdjuestDetails(id);
	}

	@Override
	public List<Map<String, Object>> listForExport(AdjustmentTypeDTO dto) {
		return mapper.listForExport(dto);
	}

	@Override
	public List<Map<String, Object>> listForExportItem(AdjustmentTypeDTO dto) {
		return mapper.listForExportItem(dto);
	}


}
