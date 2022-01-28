package com.topideal.service.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 品牌父类
 * @author zhanghx
 */
public interface BrandSuperiorService {
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	BrandSuperiorDTO listByPage(BrandSuperiorDTO dto) throws SQLException;
	
	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean save(BrandSuperiorModel model,User user) throws SQLException,RuntimeException;

	/**
	 * 根据id获取信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	BrandSuperiorModel findById(Long id) throws SQLException;

	/**
	 * 编辑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modify(BrandSuperiorModel model,User user) throws SQLException,RuntimeException;
	
	/**
	 * 删除
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean delete(List<Long> ids) throws SQLException,RuntimeException;

	/**
	 * 获取下拉
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBean() throws SQLException;
}
