package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;

import java.sql.SQLException;
import java.util.List;

public interface MerchantBuRelService {

	/**
	 * 获取商家事业部关系表数据
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	String getPurchaseAuditMethod(User user,Long buId,Long supplierId) throws SQLException;
	/**
	 * 获取公司事业部下拉框
	 * @param merchantBuRelModel
	 * @return
	 * @throws SQLException 
	 */
	List<SelectBean> getSelectBean(MerchantBuRelModel merchantBuRelModel) throws SQLException;

	MerchantBuRelDTO listMerchantBuRelPage(MerchantBuRelDTO dto) throws SQLException;

	boolean save(MerchantBuRelModel model) throws SQLException;

	//void changeStaus(MerchantBuRelModel model) throws  Exception;

	List<MerchantBuRelDTO> getExportList(MerchantBuRelDTO dto);

	boolean modifyMerchantBuRel(MerchantBuRelModel model) throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	MerchantBuRelModel searchDetail(Long id) throws SQLException;

	/**
	 * 获取当前公司和用户所关联的事业部下拉列表
	 * @param user
	 * @param model
	 * @return
	 */
    List<SelectBean> getSelectBeanByUser(User user, MerchantBuRelModel model) throws Exception;

	/**
	 * 获取库位管理
	 * @param dto
	 * @return
	 */
	Boolean getLocationManage(MerchantBuRelDTO dto) throws Exception;
}
