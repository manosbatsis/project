package com.topideal.service.main;


import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商家店铺 Service实现类
 * @author lian_
 */
public interface MerchantShopService {

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchantShopRelDTO getListByPage(MerchantShopRelDTO dto) throws SQLException;
	/**
	 * 新增
	 * @param model 
	 * @return
	 */
	boolean saveShop(MerchantShopRelDTO model) throws Exception;

	/**
	 * 查询商家下拉列表
	 * @return
	 * @throws SQLException
	 */
	List<MerchantInfoModel> getMerchant() throws SQLException;
	
	/**
	 * 修改
	 * @param model
	 * @return
	 */
	boolean modifyShop(MerchantShopRelDTO model) throws Exception;
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws Exception 
	 */
	boolean delShop(List<Long> list) throws Exception;
	
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	MerchantShopRelDTO searchDetail(Long id) throws SQLException;

	/**
	 * 获取商家店铺信息
	 */
	public List<MerchantShopRelModel> getSelectMerchantShopRelBean(MerchantShopRelModel model) throws SQLException;

	/**
	 * 获取商家店铺信息
	 */
	public List<MerchantShopRelModel> getListByModel(MerchantShopRelModel model) throws SQLException;
	/**
	 * 获取导出商家店铺信息
	 */
	List<Map<String,Object>> getExportListByDTO(MerchantShopRelDTO dto);
}
