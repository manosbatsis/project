package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.AdjustmentInventoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.mapper.AdjustmentInventoryMapper;

/**
 * 库存调整
 * 
 * @author lchenxing
 */
@Repository
public class AdjustmentInventoryDaoImpl implements AdjustmentInventoryDao {

	@Autowired
	private AdjustmentInventoryMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<AdjustmentInventoryModel> list(AdjustmentInventoryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(AdjustmentInventoryModel model) throws SQLException {
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
	public int modify(AdjustmentInventoryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryModel searchByPage(AdjustmentInventoryModel model) throws SQLException {
		PageDataModel<AdjustmentInventoryModel> pageModel = mapper.getListByPage(model);
		return (AdjustmentInventoryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public AdjustmentInventoryModel searchById(Long id) throws SQLException {
		AdjustmentInventoryModel model = new AdjustmentInventoryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentInventoryModel searchByModel(AdjustmentInventoryModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AdjustmentInventoryDTO getDetails(AdjustmentInventoryDTO dto) throws SQLException {
		return mapper.getDetails(dto);
	}

	@Override
	public AdjustmentInventoryDTO searchByDTOPage(AdjustmentInventoryDTO dto) throws SQLException {
		PageDataModel<AdjustmentInventoryDTO> pageModel = mapper.getDTOListByPage(dto);
		return (AdjustmentInventoryDTO) pageModel.getModel();
	}

	@Override
	public List<Map<String, Object>> listForExport(AdjustmentInventoryDTO dto) {
		return mapper.listForExport(dto);
	}

	@Override
	public List<Map<String, Object>> listForExportItem(AdjustmentInventoryDTO dto) {
		return mapper.listForExportItem(dto);
	}

}
