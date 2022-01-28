package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.webapi.form.MerchantInfoAddDepotForm;

/**
 * 公司管理service实现类
 */
public interface MerchantInfoService {

	/**
	 * 公司列表
	 * 
	 * @param model
	 * @return
	 */
	List<MerchantInfoModel> listMerchantInfo(MerchantInfoModel model) throws SQLException;

	/**
	 * 查询公司下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;

	/**
	 * 根据id获取商品详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public MerchantInfoDTO searchDetail(Long id) throws SQLException;

	/**
	 * 修改公司信息
	 * 
	 * @param model
	 * @param depotBuRels
	 * @param isOutDependIns
	 * @param isInDependOuts
	 * @param productRestrictions
	 * @param isInvertoryFallingPrices
	 * @param isInOutInstructions
	 * @param depotids
	 * @return
	 * @throws Exception
	 */
	boolean modifyMerchantInfo(MerchantInfoModel model, List<Long> merchantIdList,List<MerchantInfoAddDepotForm> merchantDepotList) throws SQLException, Exception;

	/**
	 * 新增公司信息
	 * 
	 * @param model
	 * @param depotBuRels
	 * @param isOutDependIns
	 * @param isInDependOuts
	 * @param productRestrictions
	 * @param isInvertoryFallingPrices
	 * @param isInOutInstructions
	 * @param depotBuRels2
	 * @return
	 * @throws Exception
	 */
	boolean saveMerchantInfo(MerchantInfoModel model, List<Long> merchantIdList,List<MerchantInfoAddDepotForm> merchantDepotList)
			throws  Exception;

	/**
	 * 列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	MerchantInfoDTO listMerchantInfoPage(MerchantInfoDTO model) throws SQLException;

	/**
	 * 根据id删除公司(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	boolean isOrNotEnable(Long id,String status) throws SQLException;

	public List<SelectBean> getSelectBeanById(MerchantInfoModel model) throws SQLException;

	MerchantInfoModel searchDetailModel(Long merchantId) throws SQLException;

	/**
	 * 查询用户绑定的公司下拉列表
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getUserSelectBean(UserMerchantRelModel model) throws SQLException;

	/**
	 * 查询用户绑定的公司信息
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<MerchantInfoModel> getUserMerchantList(UserMerchantRelModel model) throws SQLException;
	
	/**
	 * 获取商家
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<MerchantInfoModel> getMerchantList(List<Long> list ) throws SQLException;
}
