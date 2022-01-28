package com.topideal.dao.base;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;

/**
 * 品牌父级表 dao
 * @author lian_
 */
public interface BrandParentDao extends BaseDao<BrandParentModel> {
	/**
	 * 导出
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<BrandParentDTO> exportbrandParentList(BrandParentDTO dto)throws SQLException;
		
	/**
	 * 获取下拉
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBean(BrandParentModel model) throws SQLException;

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	BrandParentDTO getListByPage(BrandParentDTO dto) throws SQLException;

	List<BrandParentModel> getBrandParentByFuzzyQuery(String brandParent) throws SQLException;
	
}

