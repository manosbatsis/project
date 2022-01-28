package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

/**
 * 商家表 dao
 * 
 * @author lian_
 *
 */
public interface MerchantInfoDao extends BaseDao<MerchantInfoModel> {
	

	/**
	 * 查询商家表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MerchantInfoDTO getListByPage(MerchantInfoDTO dto) throws SQLException;
	
	public List<SelectBean> getSelectBeanById(MerchantInfoModel model) throws SQLException;

	public MerchantInfoDTO searchDTOById(Long id);

    List<MerchantInfoDTO> getMerchantRelInfoAndMerchantInfo(Map<String, Object> map);
	/**
	 * 修改非必填的字段
	 * @param depotInfoModel
	 * @return
	 */
	int updateNULL(MerchantInfoModel model)throws SQLException;
}
