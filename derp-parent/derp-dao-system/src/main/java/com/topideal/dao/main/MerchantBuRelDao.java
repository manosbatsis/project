package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;


public interface MerchantBuRelDao extends BaseDao<MerchantBuRelModel> {

	/**
	 * 获取下拉框
	 * @param merchantBuRelModel
	 * @return
	 */
	List<SelectBean> getSelectBean(MerchantBuRelModel merchantBuRelModel);

	MerchantBuRelDTO getListByPage(MerchantBuRelDTO dto);

	List<MerchantBuRelDTO> getExportList(MerchantBuRelDTO dto);
	/**
	 * 获取商家事业部关系表数据
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	MerchantBuRelDTO getMerchantBuRel(MerchantBuRelDTO dto) throws SQLException;	










}
