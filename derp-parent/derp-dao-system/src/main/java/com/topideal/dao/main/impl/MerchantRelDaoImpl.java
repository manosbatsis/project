package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchantRelDao;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.mapper.main.MerchantRelMapper;

/**
 * 商家关联关系表 impl
 * 
 * @author lchenxing
 */
@Repository
public class MerchantRelDaoImpl implements MerchantRelDao {

	@Autowired
	private MerchantRelMapper mapper; // 商家关联关系表

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<MerchantRelModel> list(MerchantRelModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(MerchantRelModel model) throws SQLException {
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
	public int modify(MerchantRelModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public MerchantRelModel searchByPage(MerchantRelModel model) throws SQLException {
		PageDataModel<MerchantRelModel> pageModel = mapper.listByPage(model);
		return (MerchantRelModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public MerchantRelModel searchById(Long id) throws SQLException {
		MerchantRelModel model = new MerchantRelModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public MerchantRelModel searchByModel(MerchantRelModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据商家id删除
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public int delByMerchantId(Long id) throws SQLException {
		return mapper.delByMerchantId(id);
	}

	/**
	 * 获取集合
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MerchantRelModel> getMerchantById(Long id) throws SQLException {
		return mapper.getMerchantById(id);
	}

}
