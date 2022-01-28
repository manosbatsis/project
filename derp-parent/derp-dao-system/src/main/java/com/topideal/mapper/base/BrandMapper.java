package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 品牌表
 * @author lchenxing
 *
 */
@MyBatisRepository
public interface BrandMapper extends BaseMapper<BrandModel> {

	/**
	 * 查询品牌表下拉列表
	 * */
	List<SelectBean> getSelectBean() throws SQLException;
    
	/**
	 * 查询品牌表下拉列表
	 * */
	List<SelectBean> getSelectBeanByMerchant(Map<String, Object> paramMap) throws SQLException;
	
	/**
	 * 根据父类id获取品牌集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	//List<BrandModel> getListByParentId(@Param("parentId") Long parentId) throws SQLException;
	
	/**
	 * 查询下拉列表，去除已经匹配的
	 * @return
	 * @throws SQLException
	 */
	//List<SelectBean> getSelectBeanByNoMatching() throws SQLException;
	
	/**
	 * 弹窗 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	//PageDataModel<BrandModel> getBrandByPage(BrandModel model) throws SQLException;
	
	/**
	 * 根据ids获取信息
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<BrandModel> getListByIds(List list) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<BrandDTO> getListByPage(BrandDTO dto) throws SQLException;

    int count(BrandModel model);

    List<BrandModel> listByLike(BrandModel brandModel);

    /**
	 * 解绑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	//int updateUnMatching(BrandModel model) throws SQLException;
}

