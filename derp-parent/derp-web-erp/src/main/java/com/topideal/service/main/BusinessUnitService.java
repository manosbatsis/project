package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.user.UserBuRelModel;

public interface BusinessUnitService {

	/**
	 * 查询列表信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	BusinessUnitDTO listBusinessUnits(BusinessUnitDTO dto) throws SQLException;

	/**
	 * 保存信息
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	boolean save(BusinessUnitModel model) throws SQLException;

	/**
	 * 修改信息
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modify(BusinessUnitModel model) throws SQLException;

	/**
	 * 查询详情
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	BusinessUnitModel query(BusinessUnitModel model) throws SQLException;

	/**
	 * 查询列表
	 * @param queryModel
	 * @return
	 * @throws SQLException 
	 */
	List<BusinessUnitModel> list(BusinessUnitModel queryModel) throws SQLException;

	/**
	 * 删除
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	boolean delBusinessUnits(BusinessUnitModel model) throws SQLException;

	/**
	 * 判断是否被引用
	 * @param model
	 * @return
	 */
	boolean judgeQuote(BusinessUnitModel model);

	/**
	 * 获取所有列表
	 * @return
	 * @throws SQLException 
	 */
	List<BusinessUnitModel> getAllList() throws SQLException;
	/**查询编码/名称是否已存在
	 *  */
	public List<BusinessUnitDTO> getcheckCodeName(BusinessUnitDTO model);
	/**
	 * 根据用户关联事业部获取部门下拉
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getDepartSelectBeanByUserId(List<Long> buIds) throws SQLException;

	/**
	 * 获取数据
	 * @return
	 * @throws SQLException
	 */
	List<BusinessUnitDTO> listDTO(BusinessUnitDTO dto, User user) throws Exception;
}
