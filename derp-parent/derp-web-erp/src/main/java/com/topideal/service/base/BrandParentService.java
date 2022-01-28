package com.topideal.service.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.BrandParentDTO;
import com.topideal.entity.vo.base.BrandParentModel;

/**
 * 品牌父类
 * @author zhanghx
 */
public interface BrandParentService {
	
	List<BrandParentDTO> exportbrandParentList(BrandParentDTO dto)throws SQLException;

	/**
	 * 查询品牌父类表下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(BrandParentModel model) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	BrandParentDTO listByPage(BrandParentDTO dto) throws SQLException;
	
	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean save(BrandParentModel model) throws SQLException,RuntimeException;
	
	/**
	 * 编辑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modify(BrandParentModel model, String oldName) throws SQLException,RuntimeException;
	
	/**
	 * 删除
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean delete(List<Long> ids) throws SQLException,RuntimeException;

	/**
	 * 获取商家列表
	 * @param user
	 * @return
	 */
	//List<SelectBean> getMerchantList(User user);

	/**
	 * 查询列表
	 * @param queryModel
	 * @return
	 * @throws SQLException 
	 */
	List<BrandParentModel> list(BrandParentModel queryModel) throws SQLException;

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	BrandParentModel detail(BrandParentModel model) throws SQLException;

	/**
	 * 手动导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	Map importBrandParent(Map<Integer, List<List<Object>>> data, User user) throws SQLException;

	List<BrandParentModel> getBrandParentByFuzzyQuery(String brandParent) throws SQLException;

	/**
	 * 根据标准条码获取标准品牌详情
	 * @param commbarcode
	 * @return
	 * @throws SQLException
	 */
	BrandParentModel getByCommbarcode(String commbarcode) throws SQLException;
}
