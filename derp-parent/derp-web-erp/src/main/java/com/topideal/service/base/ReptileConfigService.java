package com.topideal.service.base;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;

/**
 * 爬虫配置 service
 * @author lian_
 */
public interface ReptileConfigService {

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ReptileConfigDTO getListByPage(ReptileConfigDTO dto) throws SQLException;
	
	/**
	 * 修改
	 * @param model
	 * @return
	 */
	Map<String,Object> modifyReptile(ReptileConfigModel model) throws SQLException;
	
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	ReptileConfigDTO searchDetail(Long id) throws SQLException;
	
	/**
	 * 新增
	 * @param model 
	 * @return
	 */
	Map<String,Object> saveReptile(ReptileConfigModel model) throws SQLException;
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean delReptile(List<Long> list) throws SQLException;
	
	/**
	 * 查询客户下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(Long merchantId) throws SQLException;
	/**
	 * 查询商家下拉列表
	 * @return
	 * @throws SQLException
	 */
	List<MerchantInfoModel> getMerchant() throws SQLException;

	/**
	 * 查询店铺下拉列表
	 * @param customerId 
	 * @param merchantId 
	 * @return
	 * @throws SQLException
	 */
	List<MerchantShopRelModel> getShopList(Long merchantId) throws SQLException;
	
	
	

	
}
