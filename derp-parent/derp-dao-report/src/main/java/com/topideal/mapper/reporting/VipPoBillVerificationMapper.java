package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipPoBillVerificationDTO;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface VipPoBillVerificationMapper extends BaseMapper<VipPoBillVerificationModel> {

	/**
	 * 查询账单状态未完结的所有po、标准条码、sku。
	 * @param merchantId 
	 * @return
	 */
	List<VipPoBillVerificationModel> getUnVeriPoBill(VipPoBillVerificationModel model) ;

	/**
	 * 按特定条件获取分页信息
	 */
	List<VipPoBillVerificationDTO> listVipPoBillVeriList(VipPoBillVerificationDTO model);

	/**
	 * 根据ID数组查询
	 * @param ids
	 * @return
	 */
	List<VipPoBillVerificationModel> searchByIds(List ids);

	/**
	 * 获取列表数量
	 * @param model
	 * @return
	 */
	Integer getVipPoBillVeriListCount(VipPoBillVerificationDTO model);

	Integer updateNecessaryValue(VipPoBillVerificationModel vipPoBillVeriModel);

	/**
	 * 按特定条件获取PO列表分页信息
	 */
	List<VipPoBillVerificationDTO> listVipPoBillVeriPoList(VipPoBillVerificationDTO model);

	/**
	 * 按特定条件获取PO列表总数
	 */
	Integer getVipPoBillVeriPoListCount(VipPoBillVerificationDTO model);

	/**
	 * 根据PO获取未结算量
	 * @param model
	 * @return
	 */
	Integer countUnsettledAccount(VipPoBillVerificationModel model);

	/**
	 * 修改未完结状态
	 * @param model
	 * @return
	 */
	Integer modifyStatus(VipPoBillVerificationModel model);

	List<VipPoBillVerificationModel> getListByPo(Map<String, Object> map);

	/**
	 * 获取数据截止时间
	 * @param merchantId
	 * @return
	 */
	Map<String, Object> getDataTime(@Param("merchantId") Long merchantId);



}
