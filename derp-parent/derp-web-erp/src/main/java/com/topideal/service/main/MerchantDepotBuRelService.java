package com.topideal.service.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;

import java.sql.SQLException;
import java.util.List;

public interface MerchantDepotBuRelService {


	/**
	 * 根据关联仓库获取公司事业部下拉框
	 * @param relDTO
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBean(MerchantDepotBuRelDTO relDTO) throws SQLException;
	/**
	 * 根据多个关联仓库获取公司事业部下拉框（多个仓库的情况）
	 * @param relDTO
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBean2(MerchantDepotBuRelDTO relDTO) throws SQLException;

	/**
	 * 获取事业部ID
	 * @param model
	 * @return
	 */
	String getSelectInfoByMerchantId(MerchantDepotBuRelModel model);

	/**
	 * 获取事业部名称
	 * @param model
	 * @return
	 */
	String getBuNameByMerchantDepot(MerchantDepotBuRelModel model);
}
