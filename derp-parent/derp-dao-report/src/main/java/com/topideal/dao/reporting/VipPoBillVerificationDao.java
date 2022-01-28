package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipPoBillVerificationDTO;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;


public interface VipPoBillVerificationDao extends BaseDao<VipPoBillVerificationModel> {

	/**
	 * 查询账单总量-上架总量不等于0的所有po、标准条码、sku。
	 * @param merchantId 
	 * @return
	 */
	List<VipPoBillVerificationModel> getUnVeriPoBill(VipPoBillVerificationModel model);

	/**
	 * 获取分页信息
	 * @param model
	 * @return
	 */
	List<VipPoBillVerificationDTO> listVipPoBillVeriList(VipPoBillVerificationDTO model);

	/**
	 * 根据ID数组查询
	 * @param ids
	 * @return
	 */
	List<VipPoBillVerificationModel> searchByIds(List ids);

	/**
	 * 获取列表总数
	 * @param model
	 * @return
	 */
	Integer getVipPoBillVeriListCount(VipPoBillVerificationDTO model);

	Integer modifyNecessaryValue(VipPoBillVerificationModel vipPoBillVeriModel);

	/**
	 * 获取PO列表分页信息
	 * @param model
	 * @return
	 */
	List<VipPoBillVerificationDTO> listVipPoBillVeriPoList(VipPoBillVerificationDTO model);

	/**
	 * 获取PO列表分页总数
	 * @param model
	 * @return
	 */
	Integer getVipPoBillVeriPoListCount(VipPoBillVerificationDTO model);

	/**
	 * 获取未结算数据
	 * @param model
	 * @return
	 */
	Integer countUnsettledAccount(VipPoBillVerificationModel model);

	/**
	 * 修改完结状态
	 * @param model
	 * @return
	 */
	Integer modifyStatus(VipPoBillVerificationModel model);

	/**
	 * 根据PO号获取
	 * @param map
	 * @return
	 */
	List<VipPoBillVerificationModel> getListByPo(Map<String, Object> map);

	/**
	 * 获取数据截止时间
	 * @return
	 */
	Map<String, Object> getDataTime(Long merchantId);
		










}
