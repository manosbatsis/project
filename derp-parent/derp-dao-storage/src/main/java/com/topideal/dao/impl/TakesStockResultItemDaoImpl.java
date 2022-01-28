package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.TakesStockResultItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.mapper.TakesStockResultItemMapper;

/**
 * 盘点结果详情表
 * 
 * @author lchenxing
 */
@Repository
public class TakesStockResultItemDaoImpl implements TakesStockResultItemDao {

	@Autowired
	private TakesStockResultItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TakesStockResultItemModel> list(TakesStockResultItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(TakesStockResultItemModel model) throws SQLException {
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
	public int modify(TakesStockResultItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultItemModel searchByPage(TakesStockResultItemModel model) throws SQLException {
		PageDataModel<TakesStockResultItemModel> pageModel = mapper.listByPage(model);
		return (TakesStockResultItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public TakesStockResultItemModel searchById(Long id) throws SQLException {
		TakesStockResultItemModel model = new TakesStockResultItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public TakesStockResultItemModel searchByModel(TakesStockResultItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

	@Override
	public List<TakesStockResultItemDTO> listDto(TakesStockResultItemDTO dto) throws SQLException {
		return mapper.listDto(dto);
	}

	@Override
	public Long countNoExistBu(Long id) throws SQLException {
		return mapper.countNoExistBu(id);
	}

}
