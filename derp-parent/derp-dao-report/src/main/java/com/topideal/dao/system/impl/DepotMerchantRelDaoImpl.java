package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.DepotMerchantRelDao;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.DepotMerchantRelModel;
import com.topideal.mapper.system.DepotMerchantRelMapper;

/**
 * 仓库商家关联表 impl
 * @author zhanghx
 */
@Repository
public class DepotMerchantRelDaoImpl implements DepotMerchantRelDao {

	@Autowired
	private DepotMerchantRelMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<DepotMerchantRelModel> list(DepotMerchantRelModel model)
			throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param DepotMerchantRelModel
	 */
	@Override
	public Long save(DepotMerchantRelModel model) throws SQLException {
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
	public int modify(DepotMerchantRelModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param DepotMerchantRelModel
	 */
	@Override
	public DepotMerchantRelModel searchByPage(DepotMerchantRelModel model)
			throws SQLException {
		PageDataModel<DepotMerchantRelModel> pageModel = mapper
				.listByPage(model);
		return (DepotMerchantRelModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public DepotMerchantRelModel searchById(Long id) throws SQLException {
		DepotMerchantRelModel model = new DepotMerchantRelModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 * */
	@Override
	public DepotMerchantRelModel searchByModel(DepotMerchantRelModel model)
			throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public List<DepotInfoModel> getDepotByMerchantId(Long id) throws SQLException {
		return mapper.getDepotByMerchantId(id);
	}

	@Override
	public Integer deleteByMap(Map<String, Object> delMap) {
		return mapper.deleteByMap(delMap);
	}
}
