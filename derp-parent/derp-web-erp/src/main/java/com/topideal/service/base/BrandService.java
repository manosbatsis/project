package com.topideal.service.base;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.base.BrandDTO;
import com.topideal.entity.vo.base.BrandModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 品牌表 service
 * 
 * @author zhanghx
 */
public interface BrandService {

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBeanByMerchant(Map<String, Object> paramMap) throws SQLException;

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 */
	BrandDTO listByPage(BrandDTO DTO) throws SQLException;
	
	/**
	 * 获取品牌信息以及匹配的品牌信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	BrandModel getDetails(Long id) throws SQLException;

	/**
	 * 编辑
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int modify(BrandModel model,User user) throws SQLException;
	
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
	//BrandModel getBrandByPage(BrandModel model) throws SQLException;
	
	/**
	 * 根据ids获取信息
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<BrandModel> getListByIds(List list) throws SQLException;

	//导出
	List<Map<String, Object>> listForExport(BrandModel model) throws SQLException;

	/**
	 * 新增品牌
	 * @param model
	 */
	ResponseBean save(User user, BrandModel model) throws SQLException;

    /**
	 * 保存匹配
	 * @param model
	 * @param brandIds
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	//boolean saveMatching(BrandModel model, String[] brandIds, Long userId) throws SQLException,RuntimeException;
	
	/**
	 * 保存解绑
	 * @param model
	 * @param brandIds
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	//boolean saveUnMatching(List<Long> ids, Long userId) throws SQLException,RuntimeException;
	
}
