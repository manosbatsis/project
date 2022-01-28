package com.topideal.mapper.common;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;
import com.topideal.entity.vo.common.SdSaleVerifyConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SdSaleVerifyConfigMapper extends BaseMapper<SdSaleVerifyConfigModel> {

	PageDataModel<SdSaleVerifyConfigDTO> listDTOByPage(SdSaleVerifyConfigDTO dto) throws SQLException ;
	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SdSaleVerifyConfigDTO searchDetail(Long id) throws SQLException ;

}
