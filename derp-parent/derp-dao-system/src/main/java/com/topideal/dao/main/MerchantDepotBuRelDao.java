package com.topideal.dao.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;

import java.sql.SQLException;
import java.util.List;


public interface MerchantDepotBuRelDao extends BaseDao<MerchantDepotBuRelModel> {

    /**
     * 根据关联仓库获取公司事业部下拉框
     * @param model
     * @return
     * @throws SQLException
     */
    List<SelectBean> getSelectBean(MerchantDepotBuRelModel model) throws SQLException;

	String getSelectInfoByMerchantId(MerchantDepotBuRelModel model);

	/**
	 * 根据实例删除
	 * @param delModel
	 * @return
	 * @throws SQLException 
	 */
	int delByModel(MerchantDepotBuRelModel delModel) throws SQLException;

	String getBuNameByMerchantDepot(MerchantDepotBuRelModel model);




}
