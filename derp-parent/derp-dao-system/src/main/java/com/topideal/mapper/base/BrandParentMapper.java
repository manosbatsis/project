package com.topideal.mapper.base;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.mapper.BaseMapper;

/**
 *  品牌父级表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface BrandParentMapper extends BaseMapper<BrandParentModel> {
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
	PageDataModel<BrandParentDTO> getListByPage(BrandParentDTO dto) throws SQLException;

	List<BrandParentModel> getBrandParentByFuzzyQuery(@Param("brandParent")String brandParent);

}

