package com.topideal.dao.base;


import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 爬虫配置表 dao
 * @author lian_
 */
public interface ReptileConfigDao extends BaseDao<ReptileConfigModel> {
		

	/**
	  * 分页查询
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	ReptileConfigDTO getListByPage(ReptileConfigDTO dto) throws SQLException;
	
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ReptileConfigModel getDetails(ReptileConfigModel model)throws SQLException;


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
	public List<MerchantInfoModel> getSelectMerchant() throws SQLException;

	ReptileConfigDTO searchDTOById(Long id);

}

