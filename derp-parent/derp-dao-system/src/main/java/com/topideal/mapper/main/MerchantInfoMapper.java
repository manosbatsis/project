package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 商家信息 mapper
 * @author lian_
 */
@MyBatisRepository
public interface MerchantInfoMapper extends BaseMapper<MerchantInfoModel> {
	
	/**
	 * 查询商家信息表下拉列表
	 * */
	List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;
	
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<MerchantInfoDTO> getListByPage(MerchantInfoDTO dto) throws SQLException;
	
	/**
	 * 根据条件查询下拉列表
	 * @param Long
	 * */
	List<SelectBean> getSelectBeanById(MerchantInfoModel model) throws SQLException;

	MerchantInfoDTO searchDTOById(Long id);

	List<MerchantInfoDTO> getMerchantRelInfoAndMerchantInfo(Map<String, Object> map);
	/**
	 * 修改为空的字段
	 * @param depotInfoModel
	 * @return
	 */
	int updateNULL(MerchantInfoModel model);

}

