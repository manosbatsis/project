package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 爬虫配置表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface ReptileConfigMapper extends BaseMapper<ReptileConfigModel> {

	/**
	  * 分页
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	 PageDataModel<ReptileConfigDTO> getListByPage(ReptileConfigDTO dto) throws SQLException;

	 /**
	  * 获取详情
	  */
	 ReptileConfigModel getDetails(ReptileConfigModel model) throws SQLException;
	 
	 /**
	  * 查询客户下拉列表
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	 List<SelectBean> getSelectBean(@Param("merchantId") Long merchantId) throws SQLException;
	 /**
	  * 查询商家下拉列表
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	 List<MerchantInfoModel> getSelectMerchant() throws SQLException;

	ReptileConfigDTO searchDTOById(Long id);
	  
}

