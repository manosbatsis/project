package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 商家店铺关联表 dao
 * @author lian_   
 *    
 */
public interface MerchantShopRelDao extends BaseDao<MerchantShopRelModel> {
	
	/**
	 * 列表查询
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchantShopRelDTO getListByPage(MerchantShopRelDTO dto) throws SQLException;

	/**
	 * 查询商家下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<MerchantInfoModel> getSelectMerchant() throws SQLException;


	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchantShopRelDTO getDetails(Long id )throws SQLException;


	/**
	 * 查询店铺下拉列表
	 * @Param
	 * @return
	 */
	public List<MerchantShopRelModel> getSelectMerchantShopRel(MerchantShopRelModel model) throws SQLException;

	/**检查店铺编码是否已存在*/
	List<MerchantShopRelModel> getcheckShopCode(MerchantShopRelModel model);

	/**根据查询条件查询导出信息*/
	List<MerchantShopRelDTO> getExportList(MerchantShopRelDTO dto);

	/**
	 * 更新非必填字段
	 * @param saveShopModel
	 */
    int updateWithNull(MerchantShopRelModel saveShopModel);
}
