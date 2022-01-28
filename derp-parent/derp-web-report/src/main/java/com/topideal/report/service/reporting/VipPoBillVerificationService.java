package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.reporting.*;
import com.topideal.mongo.entity.FileTaskMongo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface VipPoBillVerificationService {

	/**
	 * 
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	VipPoBillVerificationDTO listVipPoBillVeriList(VipPoBillVerificationDTO model, User user);

	/**
	 * 获取实体
	 * @param vipPoBillVerificationModel
	 * @return
	 * @throws SQLException 
	 */
	VipPoBillVerificationModel searchByModel(VipPoBillVerificationModel vipPoBillVerificationModel) throws SQLException;

	/**
	 * 获取爬虫账单客户名
	 * @param vipPoBillVerificationModel
	 * @return
	 * @throws SQLException 
	 */
	String getCustomerName(VipPoBillVerificationModel vipPoBillVerificationModel) throws SQLException;

	/**
	 * 获取唯品Id
	 * @return
	 * @throws SQLException 
	 */
	Long getVipDepotId() throws SQLException;

	/**
	 * 获取商品上架明细
	 * @param vipPoBillVerificationModel
	 * @return
	 */
	List<VipShelfDetailsDTO> getShelfDetails(VipPoBillVerificationDTO vipPoBillVerificationModel) throws SQLException;

	/**
	 * 销售出库明细
	 * @param vipPoBillVerificationDTO
	 * @return
	 */
	List<VipOutDepotDetailsDTO> getSaleOutDetails(VipPoBillVerificationDTO vipPoBillVerificationDTO) throws SQLException;

	/**
	 * 销售退货明细
	 * @param vipPoBillVerificationDTO
	 * @return
	 */
	List<VipSaleReturnOdepotDetailsDTO> getSaleReturnOdepotDetails(VipPoBillVerificationDTO vipPoBillVerificationDTO) throws SQLException;

	/**
	 * 获取国检抽样明细
	 * @param vipPoBillVerification
	 * @return
	 * @throws SQLException 
	 */
	List<VipAdjustmentInventoryDetailsDTO> getNationalInspectionDetails(
            VipPoBillVerificationDTO vipPoBillVerification) throws SQLException;

	
	/**
	 * 获取唯品红冲明细
	 * @param vipPoBillVerification
	 * @return
	 * @throws SQLException 
	 */
	List<VipAdjustmentInventoryDetailsDTO> getVipHcDetails(VipPoBillVerificationDTO vipPoBillVerification)
			throws SQLException;

	/**
	 * 根据ID 串搜索
	 * @param ids
	 * @return
	 */
	List<VipPoBillVerificationModel> searchByIds(String ids);

	/**
	 * 根据标准条码获取商品名称
	 * @param commbarcode
	 * @return
	 * @throws SQLException 
	 */
	String getGoodsNameByCommbarcode(String commbarcode, Long merchantId) throws SQLException;

	/**
	 * 
	 * 获取PO列表分页数据
	 * @param model
	 * @return
	 */
	VipPoBillVerificationDTO listVipPoBillVeriPoList(VipPoBillVerificationDTO model);

	/**
	 * 根据PO获取未结算量
	 * @param model
	 * @return
	 */
	Integer countUnsettledAccount(VipPoBillVerificationModel model) throws Exception ;

	Integer modifyStatus(VipPoBillVerificationModel model) throws Exception;

	/**
	 * 根据PO和商家ID获取列表
	 * @param map
	 * @return
	 */
	List<VipPoBillVerificationModel> getListByPo(Map<String, Object> map);

	/**
	 * 获取唯品报废明细
	 * @param vipPoBillVerification
	 * @return
	 * @throws SQLException 
	 */
	List<VipAdjustmentInventoryDetailsDTO> getVipScrapDetails(VipPoBillVerificationDTO vipPoBillVerification) throws SQLException;

	/**
	 * 获取盘点明细
	 * @param vipPoBillVerification
	 * @return
	 * @throws SQLException 
	 */
	List<VipTakesStockResultsDetailsDTO> getVipTakesStockResultsDetails(
            VipPoBillVerificationDTO vipPoBillVerification) throws SQLException;

    /**
     * 明细表核销任务
     *
     * @param task
     * @param basePath
     * @return
     * @throws SQLException
     * @throws Exception
     */
    String createExcel(FileTaskMongo task, String basePath) throws Exception;

	List<VipTransferDepotDetailsDTO> getVipTransferDetails(VipPoBillVerificationDTO vipPoBillVerification) throws SQLException;

	/**
	 * 获取数据截止时间
	 * @param user
	 * @return
	 */
	Map<String, Object> getDataTime(Long merchantId);
	
	
}
