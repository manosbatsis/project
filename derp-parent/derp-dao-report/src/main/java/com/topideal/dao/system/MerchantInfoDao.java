package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.MerchantInfoModel;

/**
 * 商家表 dao
 * @author zhanghx
 */
public interface MerchantInfoDao extends BaseDao<MerchantInfoModel> {
	/**
	 * 查询商家表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;

	List<MerchantInfoModel> listDstp(MerchantInfoModel model);
}
